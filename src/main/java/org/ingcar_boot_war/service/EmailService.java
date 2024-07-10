package org.ingcar_boot_war.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public boolean sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ING CAR <ydcbucket@naver.com>"); // 발신자 이메일
            message.setTo(to);                   // 수신자 이메일
            message.setSubject(subject);         // 이메일 제목
            message.setText(text);               // 이메일 본문
            emailSender.send(message);           // 이메일 전송
            return true;                         // 전송 성공
        } catch (Exception e) {
            // 이메일 전송 실패 (네트워크 오류, 인증 오류 등)
            e.printStackTrace();
            return false;                        // 전송 실패 반환
        }
    }
}
