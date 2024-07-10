package org.ingcar_boot_war.service;

import org.apache.ibatis.annotations.Select;
import org.ingcar_boot_war.dao.CarDAO;
import org.ingcar_boot_war.dao.CartDAO;
import org.ingcar_boot_war.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class ApiProductService {

    @Autowired
    private CarDAO carDAO;

    public CarDTO product_one(int car_registration_id){

        return carDAO.product_one(car_registration_id);
    }

    public void update(CarDTO carDTO){

        System.out.println("=================================");
        System.out.println(carDTO);

        String user_id=carDTO.getUser_id();
        String car_model=carDTO.getCar_model();
        String car_brand=carDTO.getCar_brand();
        int car_price=carDTO.getCar_price();
        int car_registration_id=carDTO.getCar_registration_id();
        String car_price2=carDTO.getCar_price2();

        carDAO.update(user_id, car_brand, car_model, car_price, car_price2, car_registration_id);
    }

    public void delete(CarDTO carDTO){

        int car_registration_id=carDTO.getCar_registration_id();

        String car_existed=carDTO.getCar_existed();

        carDAO.delete(car_existed, car_registration_id);
    }
}
