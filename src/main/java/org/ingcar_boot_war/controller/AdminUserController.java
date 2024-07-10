//package org.ingcar_boot_war.controller;
//import org.ingcar_boot_war.dto.TransLogCarDTO;
//import org.ingcar_boot_war.dto.UserDTO;
//import org.ingcar_boot_war.service.ApiUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminUserController {
//
//    @Autowired
//    ApiUserService apiService;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//    //추가
//    @GetMapping("")
//    public String admin() {
//
//        return "admin";
//    }
//
//    @GetMapping("/users")
//    public String adminUsers(@RequestParam(defaultValue = "1") int page, Model model){
//
//        int totalPages= apiService.user_totalPages();
//
//        int offset= apiService.offset(page);
//
//        List<UserDTO> user = apiService.getAllUsers(offset);
//
//        model.addAttribute("users", user);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", totalPages);
//        return "admin_user";
//    }
//
//    @GetMapping("/users/details")
//    public String adminUsersDetails(@RequestParam String user_id, Model model){
//
//        UserDTO user=apiService.userInfo(user_id);
//        model.addAttribute("user",user);
//        return "admin_user_details";
//    }
//
//    // 사용자 정보 수정 폼 페이지를 보여주는 메서드
//    @GetMapping("/user_update")
//    public String showUpdateForm(@RequestParam("user_id") String user_id, Model model) {
//        UserDTO user=apiService.userInfo(user_id);
//        model.addAttribute("user",user);
//        return "admin_user_update"; // 사용자 정보 수정을 위한 뷰 페이지 반환
//    }
//
//    // 사용자 정보를 실제로 수정하는 메서드
//    @PostMapping("/user_update")
//    public ResponseEntity<?> updateUser(@ModelAttribute UserDTO user) {
//
//        user.setUser_password(passwordEncoder.encode(user.getUser_password()));
//        int updateStatus = apiService.updateUser(user);
//        if (updateStatus == 1) {
//            return ResponseEntity.ok().body("User updated successfully");
//        } else {
//            return ResponseEntity.badRequest().body("Failed to update user");
//        }
//    }
//
//}
