package kr.co.luna.club;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SpringPasswordTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testEncoding(){
        String password = "1106";
        //암호화
        String enPw = passwordEncoder.encode(password);

        //출력
        System.out.println("인코딩 된 1106:" + enPw);

        //평문 비교
        System.out.println("비교:" + passwordEncoder.matches(password, enPw));

    }
}
