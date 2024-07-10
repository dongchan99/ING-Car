package org.ingcar_boot_war.controller;

import org.ingcar_boot_war.dto.CarDTO;
import org.ingcar_boot_war.service.ApiProductService;
import org.ingcar_boot_war.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiProductController {

    @Autowired
    private ApiProductService apiProductService;

    @Autowired
    private MypageService mypageService;

    @GetMapping("/product")
    public ResponseEntity<Map<String, String>> search(@RequestParam String key){

        int car_registration_id = Integer.parseInt(key);

        CarDTO car = apiProductService.product_one(car_registration_id);

        Date carRegistrationDate = new Date();

        // 날짜 형식 지정 (월 일 년 순서)
        SimpleDateFormat formatter = new SimpleDateFormat("MM dd yyyy");

        // Date 객체를 지정된 형식의 문자열로 변환
        String formattedDate = formatter.format(carRegistrationDate);

        Map<String, String> response = new HashMap<>();

        response.put("car_registration_id", String.valueOf(car.getCar_registration_id()));
        response.put("user_id", String.valueOf(car.getUser_id()));
        response.put("car_registration_date", formattedDate);
        response.put("car_brand", car.getCar_brand());
        response.put("car_model", car.getCar_model());
        response.put("car_price", String.valueOf(car.getCar_price()));
//        response.put("car_price2", String.valueOf(car.getCar_price2()));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody CarDTO carDTO){

        apiProductService.update(carDTO);

        int reg_id = carDTO.getCar_registration_id();

        mypageService.change_cart_status(reg_id);

        mypageService.change_translog_status(reg_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody CarDTO carDTO){

        apiProductService.delete(carDTO);

        System.out.println(carDTO);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
