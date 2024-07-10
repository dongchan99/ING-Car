package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.CarDTO;
import org.ingcar_boot_war.dto.ReviewCountDTO;

import java.util.List;

@Mapper
public interface CarDAO {
    @Insert("INSERT INTO Car (car_registration_id, car_id, car_year, car_mileage, car_brand, car_model," +
            " car_color, car_fuel_type, car_price, car_accident_history, car_description,car_registration_date,user_id, car_price2) " +
            "VALUES (car_registration_seq.NEXTVAL, #{car_id}, #{car_year}, #{car_mileage}, #{car_brand}," +
            " #{car_model}, #{car_color}, #{car_fuel_type}, #{car_price}, #{car_accident_history}, #{car_description}, SYSDATE, #{user_id}, #{car_price2})")
    void insertCar(CarDTO car);


    @Select("SELECT user_id, car_registration_id, car_registration_date, car_brand, car_model, car_price2, car_mileage, car_views, car_accident_history, car_fuel_type, car_description, car_existed FROM car WHERE car_registration_id = #{car_registration_id}")
    CarDTO getCarDetails(int car_registration_id);

    //모델 가져오기
    @Select("SELECT * FROM car WHERE car_registration_id = #{car_registration_id}")
    CarDTO getCarModel(int car_registration_id);

    //가져온 모델과 동일한 차량 정보 가져오기
    @Select("SELECT car_brand, car_model, car_price2, user_id, car_registration_id FROM car WHERE car_model = #{car_model} AND car_registration_id != #{car_registration_id} AND car_existed='Y' ORDER BY car_registration_date DESC")
    List<CarDTO> getCarDetailsList(String car_model, Integer car_registration_id);

    // 조회수 업데이트 메서드 추가
    @Update("UPDATE car SET car_views = car_views + 1 WHERE car_registration_id = #{car_registration_id}")
    void increaseCarViews(int car_registration_id);


    //별점
    @Select("SELECT rb.board_star, COUNT(*) AS review_count " +
            "FROM reviewboard rb " +
            "JOIN translog tl ON rb.log_id = tl.log_id " +
            "JOIN car c ON tl.car_registration_id = c.car_registration_id " +
            "JOIN users u ON c.user_id = u.user_id " +
            "WHERE u.user_id = #{user_id} " +
            "GROUP BY rb.board_star " +
            "ORDER BY rb.board_star DESC")
    List<ReviewCountDTO> getReviewCountsByUserId(@Param("user_id") String user_id);

    // 박태경 //

    // car_list -> car_single (car_model만 가져가기)
    @Select("SELECT car_model FROM Car WHERE car_registration_id=#{car_registration_id}")
    String car_single_search(int car_registration_id);

    // car_list -> car_single (all 가져가기)
    @Select("SELECT * FROM Car WHERE car_registration_id=#{car_registration_id}")
    List<CarDTO> car_single_searchAll(int car_registration_id);

    // 메인페이지 -> review_main (이미지)
    @Select("SELECT car_model FROM Car WHERE car_registration_id=#{car_registration_id}")
    String find_car_model(int car_registration_id);

    // 유저가 등록한 모든 차량에 대한 car_registration_id 가져오기
    @Select("SELECT car_registration_id FROM Car WHERE user_id=#{user_id} and car_existed='Y'")
    List<Integer> find_all_reg(String user_id);

    // update_car, delete_car에 사용 가능한 reg dto로 가져오기
    @Select("SELECT * FROM Car WHERE car_registration_id=#{car_registration_id}")
    CarDTO car(int car_registration_id);

    // 가격 수정
    @Update("UPDATE Car SET car_price=#{car_price}, car_price2=#{car_price2} WHERE car_registration_id=#{car_registration_id}")
    void update_price(int car_price, String car_price2, int car_registration_id);

    // 차량 정보 삭제에 의한 car_existed 변경
    @Update("UPDATE Car SET car_existed='N' WHERE car_registration_id=#{car_registration_id}")
    void change_car_existed(int car_registration_id);

    //수정
    @Update("UPDATE car SET car_price = #{car_price}, car_price2 = #{car_price2} WHERE car_registration_id = #{car_registration_id}")
    void updateCarPrice(@Param("car_registration_id") int car_registration_id, @Param("car_price") int car_price, @Param("car_price2") String car_price2);

    //삭제
    @Update("Update car SET car_existed='N' WHERE car_registration_id = #{car_registration_id}")
    void deleteCar(@Param("car_registration_id") int car_registration_id);

    // 회원탈퇴 시 해당 차량의 car_existed='N'으로 변경
    @Update("UPDATE Car SET car_existed='N' WHERE user_id=#{user_id}")
    void change_ucar_existed(String user_id);

    ///////////////////////////////////////////////////////////////////////////////////

    @Select("SELECT * FROM Car WHERE car_registration_id=#{car_registration_id}")
    CarDTO product_one(int car_registration_id);

    @Update("UPDATE Car SET user_id=#{user_id}, car_model=#{car_model}, car_brand=#{car_brand}, car_price=#{car_price}, car_price2=#{car_price2} WHERE car_registration_id=#{car_registration_id}")
    void update(String user_id, String car_brand, String car_model, int car_price, String car_price2, int car_registration_id);

    @Update("UPDATE Car SET car_existed=#{car_existed} WHERE car_registration_id=#{car_registration_id}")
    void delete(String car_existed, int car_registration_id);
}