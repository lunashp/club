package kr.co.luna.club.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMember extends User implements OAuth2User {
    private String email;
    private String name;
    private boolean fromSocial;
    private String password;

    //속성 값을 읽어오기 위한 Map
    private Map<String, Object> attr;

    public ClubAuthMember(String username, String password, Collection<? extends GrantedAuthority> authorities){
        //상위 클래스의 생성자 호출
        super(username, password, authorities);
        this.email = username;
        this.password = password;
    }


    public ClubAuthMember(String username, String password, Collection<?extends GrantedAuthority>authorities, Map<String, Object>attr){
        //상위 클래스의 생성자 호출
        super(username, password, authorities);
        this.attr=attr;
    }
    //모든 속성의 값을 리턴하는 메소드
    @Override
    public Map<String, Object> getAttributes(){
        return this.attr;
    }
}
