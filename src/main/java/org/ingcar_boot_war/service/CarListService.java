package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.CarListDAO;
import org.ingcar_boot_war.dto.CarListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarListService {

    @Autowired
    private CarListDAO carListDAO;

    public List<CarListDTO> getAllCars(int page, int size, String carModel, List<String> brands, Integer minPrice, Integer maxPrice, Integer minMileage, Integer maxMileage, List<String> fuels, String accident, List<String> colors) {
        int offset = page * size;
        return carListDAO.findAllCars(size, offset, carModel, brands, minPrice, maxPrice, minMileage, maxMileage, fuels, accident, colors);
    }

    public int getTotalPages(int size) {
        int totalCars = carListDAO.countAllCars();
        return (int) Math.ceil((double) totalCars / size);
    }
}