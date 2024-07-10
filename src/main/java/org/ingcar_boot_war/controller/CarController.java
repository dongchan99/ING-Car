package org.ingcar_boot_war.controller;

import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dao.CartDAO;
import org.ingcar_boot_war.dto.*;
import org.ingcar_boot_war.service.CarService;
import org.ingcar_boot_war.service.CarUserService;
import org.ingcar_boot_war.service.MypageService;
import org.ingcar_boot_war.service.TransLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarUserService carUserService;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private TransLogService transLogService;

    @Autowired
    private MypageService myPageService;

    @PostMapping("/api/cars/register")
    @ResponseBody
    public ResponseEntity<?> registerCar(@RequestBody CarDTO car, HttpSession session) {
        System.out.println("Received car registration request: " + car);
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        car.setUser_id(user_session.getUser_id());
        System.out.println(user_session.getUser_id());
        carService.addCar(car);
        return ResponseEntity.ok("Car registered successfully");
    }

    @GetMapping("/car_registration")
    public String car_registration() {
        System.out.println("Accessed /car_registration endpoint");
        return "car_registration"; // car_registration.html 뷰를 반환
    }

    @GetMapping("/car_detail")
    public String getCarDetailsPage(@RequestParam("car_registration_id") int car_registration_id, Model model, HttpSession session) {
        CarUserDTO carUser = carUserService.getCarUserDetails(car_registration_id);
        UserDTO loggedInUser = (UserDTO) session.getAttribute("user_session");
        CarDTO carModel = carService.getCarModel(car_registration_id);
        String CarModel = carModel.getCar_model();
        Integer CarRegId = carModel.getCar_registration_id();
        List<CarDTO> carModelList = carService.getCarDetailsList(CarModel, CarRegId);
        model.addAttribute("List", carModelList);

        List<CartDTO> checkEmpty = cartDAO.mypage_cart_input(loggedInUser.getUser_id(), car_registration_id);
        if (checkEmpty.isEmpty()) {
            model.addAttribute("val", "true");
        }

        // 리뷰 데이터 조회 및 모델에 추가
        List<ReviewCountDTO> reviewCounts = carService.getReviewCountsByUserId(carUser.getUser_id());
        model.addAttribute("reviewCounts", reviewCounts);

        List<TransLogDTO> resultList = transLogService.check_current_translog(car_registration_id);

        if(!resultList.isEmpty()){

            return "car_empty";
        }

        if (loggedInUser != null && loggedInUser.getUser_id().equals(carUser.getUser_id())) {
            model.addAttribute("cu", carUser);
            return "car_detail2"; // car_detail2.html 템플릿 반환
        } else {
            carService.increaseCarViews(car_registration_id); // 조회수 증가
            CarUserDTO carUser2 = carUserService.getCarUserDetails(car_registration_id); // 조회수가 증가된 정보 다시 가져오기
            model.addAttribute("cu", carUser2);
            return "car_detail";
        }
    }

    //수정 요청 시 transLog 테이블의 데이터 확인
    @GetMapping("/check_transLog")
    @ResponseBody
    public ResponseEntity<String> checkCarTransaction(@RequestParam("car_registration_id") int car_registration_id) {
        if (transLogService.check_transLog(car_registration_id)) {
            return ResponseEntity.ok("RESERVED");
        }
        return ResponseEntity.ok("OK");
    }

    //차량 수정
    @GetMapping("/car_update")
    public String showUpdateForm(@RequestParam("car_registration_id") int car_registration_id, Model model) {
        CarDTO car = carService.getCarModel(car_registration_id);
        model.addAttribute("car", car);
        return "car_update"; // car_update.html 템플릿 반환
    }

    @PostMapping("/car_update")
    @ResponseBody
    public ResponseEntity<?> updateCar(@RequestBody CarDTO car) {

        if (car.getCar_price() == null || car.getCar_price() <= 0) {
            return ResponseEntity.badRequest().body("Invalid price value.");
        }
        carService.updateCarPrice(car.getCar_registration_id(), car.getCar_price(), car.getCar_price2());
        myPageService.update_cart_status(car.getCar_registration_id());
        myPageService.update_translog_status(car.getCar_registration_id());

        return ResponseEntity.ok("Car price updated successfully.");
    }

    //차량 삭제
    @PostMapping("/car_delete")
    @ResponseBody
    public ResponseEntity<?> deleteCar(@RequestBody CarDTO car) {
        int car_registration_id = car.getCar_registration_id();
        carService.deleteCar(car_registration_id);
        return ResponseEntity.ok("Car deleted successfully.");
    }

}