//package org.ingcar_boot_war.controller;
//
//import org.ingcar_boot_war.dto.TransLogCarDTO;
//import org.ingcar_boot_war.service.AdminProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminProductController {
//
//    @Autowired
//    private AdminProductService adminProductService;
//
//    @GetMapping("/product")
//    public String product(@RequestParam(defaultValue = "1") int page, Model model) {
//
//        int totalPages= adminProductService.product_totalPages();
//
//        int offset= adminProductService.offset(page);
//
//        List<TransLogCarDTO> resultList = adminProductService.product_getAllCar(offset);
//
//        model.addAttribute("resultlist", resultList);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", totalPages);
//
//        return "admin_car_list";
//    }
//
//    @GetMapping("/product/detail")
//    public String detail(@RequestParam Map<String, String> params, Model model) {
//
//        model.addAttribute("resultlist", params);
//
//        return "admin_car_detail";
//    }
//
//    @GetMapping("/producting")
//    public String producting(@RequestParam(defaultValue = "1") int page, Model model) {
//
//        int totalPages= adminProductService.producting_totalPages();
//
//        int offset= adminProductService.offset(page);
//
//        List<TransLogCarDTO> resultList = adminProductService.producting_getAllCar(offset);
//
//        model.addAttribute("resultlist", resultList);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", totalPages);
//
//        return "admin_ingcar_list";
//    }
//}
