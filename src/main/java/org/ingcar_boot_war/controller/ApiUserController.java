package org.ingcar_boot_war.controller;

import org.ingcar_boot_war.dto.UserDTO;
import org.ingcar_boot_war.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private ApiUserService apiUserService;

//    @GetMapping("/users")
//    public ResponseEntity<List<UserDTO>> getAllUsers() {
//        return ResponseEntity.ok(apiUserService.getAllUsers());
//    }

    @PatchMapping("/users/{userId}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable String userId, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("use_YN");
        if ("N".equals(newStatus)) {
            boolean success = apiUserService.toggleUserStatus(userId, newStatus);
            if (success) {
                return ResponseEntity.ok(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("success", false));
            }
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
        }
    }
}



