package org.ingcar_boot_war.controller;


import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dao.*;
import org.ingcar_boot_war.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class TransLogCarController {

    @Autowired
    private TransLogCarDAO transLogCarDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private TransLogDAO transLogDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private CarDAO carDAO;

    @Autowired
    private CartCarDAO cartCarDAO;

    @Autowired
    private TransLogReviewCarDAO transLogReviewCarDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


}