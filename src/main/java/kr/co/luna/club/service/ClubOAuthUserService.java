package kr.co.luna.club.service;

import kr.co.luna.club.dto.ClubAuthMember;
import kr.co.luna.club.entity.ClubMember;
import kr.co.luna.club.entity.ClubMemberRole;
import kr.co.luna.club.repository.ClubMemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
//로그 찍기 위함
@Log4j2
//주입 받고자 할 때 사용
@RequiredArgsConstructor
public class ClubOAuthUserService extends DefaultOAuth2UserService {
    //데이터 저장을 위한 Repository
    private final ClubMemberRepository clubMemberRepository;
    //비밀번호 암호화를 위한 인스턴스
    private final PasswordEncoder passwordEncoder;

    //데이터베이스에 저장하는 메소드
    private ClubMember saveSocialMember(String email){
        //email에 해당하는 데이터가 있는지 확인
        Optional<ClubMember> result = clubMemberRepository.findById(email);
        //있으면 데이터를 리턴
        if(result.isPresent()){
            return result.get();
        }
        //데이터베이스에 없으면 추가
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .password(passwordEncoder.encode("1106"))
                .fromSocial(true)
                .build();
        clubMember.addMemberRole(ClubMemberRole.USER);
        clubMemberRepository.save(clubMember);
        return clubMember;
    }
    @Override
    //OAuth를 이용해서 로그인했을 때 호출 되는 메소드
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName:" + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v);
        });

        //구글에서 접속한 경우의 email을 가져오기
        String email = null;
        if(clientName.trim(). toLowerCase(). indexOf("google")>=0){
            email = oAuth2User.getAttribute("email");
        }

        //저장
        ClubMember member = saveSocialMember(email);
        ClubAuthMember clubAuthMember = new ClubAuthMember(
                member.getEmail(),
                member.getPassword(),
                member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        return oAuth2User;
    }
}
