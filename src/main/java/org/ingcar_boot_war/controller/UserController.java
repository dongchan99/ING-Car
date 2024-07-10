package org.ingcar_boot_war.controller;


import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dto.UserDTO;
import org.ingcar_boot_war.service.EmailService;
import org.ingcar_boot_war.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }




/*    @PostConstruct  // 동일한 BCryptPasswordEncoder 인스턴스가 사용되는지 확인하기 위해
    public void init() {
        System.out.println("UserController PasswordEncoder HashCode: " + passwordEncoder.hashCode());
    }*/




    //ID 중복체크
    @GetMapping("/check/checkUserId")
    public @ResponseBody Map<String, Boolean> checkUserId(@RequestParam("user_id") String user_id) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = userService.isUserIdExists(user_id);
        response.put("exists", exists);
        return response;
    }

    //회원가입 동찬님 버전
/*    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO user) {
        userService.registerUser(user);
        // 회원가입 성공 시 해당 요청을 다시 처리
        return "redirect:/register-success";
    }*/

    //회원가입 암복호화

    @PostMapping("/register")
    public String handleRegister(@RequestParam String user_id, @RequestParam String user_password,
                                 @RequestParam String user_name, @RequestParam String user_birth,
                                 @RequestParam String user_email, @RequestParam String user_gender,
                                 @RequestParam String user_phone, @RequestParam String user_area) {
        UserDTO newUser = new UserDTO();
        newUser.setUser_id(user_id);
        newUser.setUser_password(passwordEncoder.encode(user_password)); // 비밀번호 해싱
        newUser.setUser_name(user_name);
        newUser.setUser_birth(user_birth);
        newUser.setUser_email(user_email);
        newUser.setUser_gender(user_gender);
        newUser.setUser_phone(user_phone);
        newUser.setUser_area(user_area);
        userService.registerUser(newUser); // 해싱된 비밀번호 저장
        return "join-success";
    }

    //로그인 동찬님 버전
/*    @PostMapping("/login")
    public String loginUser(@ModelAttribute UserDTO user, Model model, HttpSession session) {
        UserDTO loggedInUser = userService.loginUser(user);
        if (loggedInUser != null) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            session.setAttribute("user_session", loggedInUser);

            return "redirect:/";
        } else {
            model.addAttribute("loginError", "아이디나 비밀번호가 틀렸습니다, 다시 로그인 해주세요.");
            return "login";
        }
    }*/


    //로그인 복호화
    @PostMapping("/login")
    public String loginUser(@RequestParam String user_id, @RequestParam String user_password, Model model, HttpSession session) {

        String checkUnsign = userService.checkUnsginUser(user_id); //SELECT use_YN FROM users WHERE user_id = #{user_id}

        if (checkUnsign == null) {
            model.addAttribute("loginError", "아이디나 비밀번호가 틀렸습니다, 다시 로그인 해주세요.");
            return "login";
        }

        if(checkUnsign.equals("N")){
            model.addAttribute("loginError", "탈퇴 처리된 아이디입니다, 다시 확인 해주세요.");
            return "login";
        }

        UserDTO user = new UserDTO();
        user.setUser_id(user_id);
        user.setUser_password(user_password); // 해싱되지 않은 비밀번호 설정
        UserDTO loggedInUser = userService.loginUser(user);
        System.out.println("loggedInUser = " + loggedInUser);

        if (loggedInUser != null) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            session.setAttribute("user_session", loggedInUser);
            if ("1".equals(loggedInUser.getUser_role())) {
                return "redirect:/admin";
            }

            return "redirect:/";
        } else {
            model.addAttribute("loginError", "아이디나 비밀번호가 틀렸습니다, 다시 로그인 해주세요.");
            return "login";
        }
    }




    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/";
    }


    /*@GetMapping("/check/findPw")
    public @ResponseBody Map<String, Boolean> findPassword(@RequestParam("userId") String userId, @RequestParam("userEmail") String userEmail) {
        Map<String, Boolean> response = new HashMap<>();
        boolean match = userService.checkUserIdAndEmail(userId, userEmail);
        response.put("match", match);
        return response;
    }

    @PostMapping("/check/resetPassword")
    public @ResponseBody Map<String, String> resetPassword(@RequestParam("userId") String userId, @RequestParam("newPassword") String newPassword) {
        Map<String, String> response = new HashMap<>();
        userService.updatePassword(userId, newPassword);
        response.put("message", "Password updated successfully");
        return response;
    }*/

    // 복호화
    @PostMapping("/check/findPw")
    public ResponseEntity<?> findPassword(@RequestParam("user_id") String user_id, @RequestParam("user_email") String user_email) {
        // 사용자 ID와 이메일을 확인
        if (!userService.checkUserIdAndEmail(user_id, user_email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching user found.");
        }

        // 임시 비밀번호 생성
        String temporaryPassword = generateTemporaryPassword();

        // 이메일로 임시 비밀번호 전송
        boolean emailSent = emailService.sendSimpleMessage(user_email, "임시 비밀번호 발급", "임시 비밀번호: " + temporaryPassword);
        if (!emailSent) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
        }

        // 데이터베이스에 비밀번호 업데이트
        boolean updateSuccess = userService.updatePassword(user_id, temporaryPassword);
        if (!updateSuccess) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password.");
        }

        // 성공적으로 처리되었을 때 응답 반환
        return ResponseEntity.ok("Temporary password sent successfully.");
    }

    private String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(62);
            char ch = (char) (number < 10 ? number + '0' : (number < 36 ? number + 55 : number + 61));
            sb.append(ch);
        }
        return sb.toString();
    }

    @PostMapping("/delete_user")
    public @ResponseBody Map<String, String> resetPassword(@RequestParam("user_password") String user_password, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        String user_id=user_session.getUser_id();
        String storedPassword = userService.getUserPassword(user_id);
        if (passwordEncoder.matches(user_password, storedPassword)) {
            userService.deleteUser(user_id);
            userService.change_ucar_existed(user_id);
            userService.deleteTranslog(user_id);
            response.put("message", "User has been deleted");
            return response;
        }
        else{
            response.put("message", "Password is incorrect");
            return response;
        }
    }

}
