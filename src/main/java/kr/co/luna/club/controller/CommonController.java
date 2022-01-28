package kr.co.luna.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class CommonController {
    @GetMapping("/customlogin")
    public void customLogin(){
        log.info("--사용자 로그인 페이지--");
    }
}
