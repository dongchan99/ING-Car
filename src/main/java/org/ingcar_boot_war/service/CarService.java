package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.CarDAO;
import org.ingcar_boot_war.dto.CarDTO;
import org.ingcar_boot_war.dto.ReviewCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarDAO carDAO;

    public void addCar(CarDTO car) {
        System.out.println("Adding car to database: " + car);
        carDAO.insertCar(car);
    }

    public CarDTO getCarDetails(int car_registration_id) {

        return carDAO.getCarDetails(car_registration_id);
    }

    public void increaseCarViews(int car_registration_id){
        carDAO.increaseCarViews(car_registration_id);
    }

    public List<CarDTO> getCarDetailsList(String car_model, Integer car_registration_id) {

        return carDAO.getCarDetailsList(car_model,car_registration_id );
    }

    public CarDTO getCarModel(int car_registration_id) {

        return carDAO.getCarModel(car_registration_id);
    }

    public List<ReviewCountDTO> getReviewCountsByUserId(String user_id) {
        List<ReviewCountDTO> reviewCounts = carDAO.getReviewCountsByUserId(user_id);

        // 기본 별점별 리뷰 개수 초기화
        List<ReviewCountDTO> completeReviewCounts = new ArrayList<>();
        for (int i = 5; i >= 1; i--) {
            final int starCount = i; // 람다 표현식에서 사용될 최종 변수
            ReviewCountDTO reviewCount = reviewCounts.stream()
                    .filter(r -> r.getBoard_star() == starCount)
                    .findFirst()
                    .orElse(new ReviewCountDTO(starCount, 0));
            completeReviewCounts.add(reviewCount);
        }

        return completeReviewCounts;
    }

    //수정
    public void updateCarPrice(int car_registration_id, int car_price, String car_price2) {
        carDAO.updateCarPrice(car_registration_id, car_price, car_price2);
    }

    //삭제
    public void deleteCar(int car_registration_id) {
        carDAO.deleteCar(car_registration_id);
    }

}