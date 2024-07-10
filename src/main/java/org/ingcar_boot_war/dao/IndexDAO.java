package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.ingcar_boot_war.dto.CarDTO;
import org.ingcar_boot_war.dto.ReviewDTO;

import java.util.List;

@Mapper
public interface IndexDAO {

    //조회수로 차량 정보를 가져와 List 객체에 저장
    @Select("SELECT car_brand, car_model, car_price2, user_id, car_registration_id FROM car WHERE car_existed='Y' ORDER BY car_views DESC")
    List<CarDTO> getIndexCarList();

    //조회수로 리뷰 정보를 가져와 List 객체에 저장
    @Select("SELECT board_subject, board_regdate, board_content FROM ReviewBoard ORDER BY board_count DESC")
    List<ReviewDTO> getIndexReviewList();
}

