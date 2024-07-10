package org.ingcar_boot_war.controller;

import org.ingcar_boot_war.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;


    @PostMapping("/sendCode")
    public ResponseEntity<?> sendCode(@RequestParam("to") String to, @RequestParam("subject") String subject, @RequestParam("text") String text) {
        if(emailService.sendSimpleMessage(to, subject, text)) {
            return ResponseEntity.ok("verifyCode sended successfully.");
        }
        else{ //위의 조건에 실패한다면
            return ResponseEntity.badRequest().body("verifyCode sended fail.");

        }
    }

}