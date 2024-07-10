package org.ingcar_boot_war.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MoveController {

    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register-success")
    public String registerPage() {
        return "join-success";
    }

    @GetMapping("/car_regPage")
    public String car_regPage() {
        return "car_reg";
    }

}






