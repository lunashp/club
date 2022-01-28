package kr.co.luna.club.service;

import kr.co.luna.club.dto.ClubAuthMember;
import kr.co.luna.club.entity.ClubMember;
import kr.co.luna.club.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {
    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
        log.info("loadByUserName:" + username);
        //데이터베이스에서 username 에 해당하는 데이터를 찾아오기
        ClubMember clubMember = clubMemberRepository.findByEmail(username, false).get();
        //인증을 위한 클래스의 인스턴스를 생성
        ClubAuthMember clubAuthMember = new ClubAuthMember(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.getRoleSet().stream()
                        .map(role->new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toSet())
        );
        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubAuthMember.isFromSocial());
        log.info(clubMember);
        log.info(clubAuthMember);
        return clubAuthMember;
    }
}
