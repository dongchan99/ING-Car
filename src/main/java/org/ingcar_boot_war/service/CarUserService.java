package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.CarUserDAO;
import org.ingcar_boot_war.dto.CarUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarUserService {

    @Autowired
    private CarUserDAO carUserDAO;

    public CarUserDTO getCarUserDetails(int car_registration_id) {
        return carUserDAO.getCarUserDetails(car_registration_id);
    }


}
