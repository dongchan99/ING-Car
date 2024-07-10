package org.ingcar_boot_war.controller;

import org.ingcar_boot_war.dto.CarListDTO;
import org.ingcar_boot_war.service.CarListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CarListController {

    @Autowired
    private CarListService carListService;

    @GetMapping("/car_list")
    public String carList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "carModel", required = false) String carModel,
            @RequestParam(value = "brands", required = false) List<String> brands,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "minMileage", required = false) Integer minMileage,
            @RequestParam(value = "maxMileage", required = false) Integer maxMileage,
            @RequestParam(value = "fuels", required = false) List<String> fuels,
            @RequestParam(value = "accident", required = false, defaultValue = "전체") String accident,
            @RequestParam(value = "colors", required = false) List<String> colors,
            Model model) {

        List<CarListDTO> cars = carListService.getAllCars(page, size, carModel, brands, minPrice, maxPrice, minMileage, maxMileage, fuels, accident, colors);
        int totalPages = carListService.getTotalPages(size);

        model.addAttribute("cars", cars);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // 검색 조건을 모델에 추가하여 뷰에서 사용 가능하게 합니다.
        model.addAttribute("carModel", carModel);
        model.addAttribute("brands", brands);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minMileage", minMileage);
        model.addAttribute("maxMileage", maxMileage);
        model.addAttribute("fuels", fuels);
        model.addAttribute("accident", accident);
        model.addAttribute("colors", colors);

        return "car_list";
    }
}