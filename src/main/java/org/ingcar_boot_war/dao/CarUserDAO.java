package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ingcar_boot_war.dto.CarUserDTO;

@Mapper
public interface CarUserDAO {

    @Select("SELECT c.car_registration_id, c.car_id, c.car_year, c.car_mileage, c.car_brand, c.car_model, c.car_color, " +
            "c.car_fuel_type, c.car_price, c.car_accident_history, c.car_description, c.car_registration_date, " +
            "c.user_id, c.car_price2, c.car_views, u.user_comment, u.user_name " +
            "FROM car c " +
            "JOIN users u ON c.user_id = u.user_id " +
            "WHERE c.car_registration_id = #{car_registration_id}")
    CarUserDTO getCarUserDetails(int car_registration_id);


}
