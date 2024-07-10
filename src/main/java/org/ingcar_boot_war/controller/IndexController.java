package org.ingcar_boot_war.controller;

import org.ingcar_boot_war.dao.IndexDAO;
import org.ingcar_boot_war.dto.CarDTO;
import org.ingcar_boot_war.dto.ReviewDTO;
import org.ingcar_boot_war.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDAO indexDAO;

    @GetMapping("/")
    public String getIndexPage(Model model) {

        // TOP 3 차량 정보 조회
        List<CarDTO> top3Cars = indexDAO.getIndexCarList();
        // TOP 3 리뷰 정보 조회
        List<ReviewDTO> top3Reviews = indexDAO.getIndexReviewList();

        // TOP 3 차량 정보 저장
        model.addAttribute("top3Cars", top3Cars);

        // TOP 3 리뷰 정보 저장
        model.addAttribute("top3Reviews",top3Reviews);

        return "index"; // Thymeleaf가 참조할 HTML 파일의 이름
    }
}
