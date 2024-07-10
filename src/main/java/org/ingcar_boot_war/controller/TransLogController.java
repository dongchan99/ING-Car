package org.ingcar_boot_war.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dto.UserDTO;
import org.ingcar_boot_war.service.TransLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransLogController {

    private final TransLogService transLogService;

    @Autowired
    public TransLogController(TransLogService transLogService) {
        this.transLogService = transLogService;
    }

    @PostMapping("/reserveCar")
    public ResponseEntity<String> reserveCar(@RequestParam("car_registration_id") int car_registration_id, @RequestParam(name="translog_price") String translog_price, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user_session");
        String user_id = userDTO.getUser_id();

        String result = transLogService.reserveCar(car_registration_id, user_id, translog_price);
        return ResponseEntity.ok(result);
    }


}
