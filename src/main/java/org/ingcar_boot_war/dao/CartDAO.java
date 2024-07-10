package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.CartDTO;

import java.util.List;

@Mapper
public interface CartDAO {

//    @Select("SELECT COUNT(*) FROM cart WHERE reg_id = #{reg_id} AND cart_user = #{cart_user}")
//    int countCartItem(@Param("reg_id") int reg_id, @Param("cart_user") String cart_user);
//
//    @Insert("INSERT INTO cart (reg_id, cart_user) VALUES (#{reg_id}, #{cart_user})")
//    void insertCartItem(@Param("reg_id") int reg_id, @Param("cart_user") String cart_user);
//
//    @Delete("DELETE FROM cart WHERE reg_id = #{reg_id} AND cart_user = #{cart_user}")
//    void deleteCartItem(@Param("reg_id") int reg_id, @Param("cart_user") String cart_user);

    // 박태경 //

    // mypage_cart_input 테이블에 이미존재하는지 확인
    @Select("SELECT * FROM Cart WHERE user_id=#{user_id} and car_registration_id=#{car_registration_id}")
    List<CartDTO> mypage_cart_input(String user_id, int car_registration_id);

    // mypage_cart_input 테이블에 값 대입
    @Insert("INSERT INTO Cart (list_id, car_registration_id, user_id) VALUES (list_id_seq.NEXTVAL, #{car_registration_id}, #{user_id})")
    void mypage_cart_insert(String user_id, int car_registration_id);

    // mypage_cart_input 테이블에서 삭제
    @Delete("DELETE FROM Cart WHERE user_id=#{user_id} and car_registration_id=#{car_registration_id}")
    void mypage_cart_delete(String user_id, int car_registration_id);

    // mypage 안에서 직접 "취소"
    @Delete("DELETE FROM Cart WHERE list_id=#{list_id}")
    void mypage_cart_cancel(int list_id);

    // mypage_selling_confirm_cart_kill
    @Delete("DELETE FROM Cart WHERE car_registration_id=#{car_registration_id}")
    void mypage_selling_confirm_cart_kill(int car_registration_id);

    // 차량 가격 수정에의한 cart_status 변경
    @Update(("UPDATE Cart SET cart_status='N' WHERE car_registration_id=#{car_registration_id}"))
    void change_cart_status(int car_registration_id);

    @Update("UPDATE Cart set cart_status='N' where car_registration_id=#{car_registration_id} ")
    void update_cart_status(int car_registration_id);

    @Update("UPDATE Cart SET cart_status='Y' WHERE user_id=#{user_id}")
    void dismiss_all_cart_translog_status(String user_id);

    @Delete("DELETE FROM Cart WHERE user_id=#{user_id} AND car_registration_id=#{car_registration_id}")
    void dismiss_all_ccar_existed(String user_id, int car_registration_id);

    @Update("UPDATE Cart SET cart_status WHERE list_id=#{list_id}")
    void cdismiss(int list_id);

    @Delete("DELETE FROM Cart WHERE list_id=#{list_id}")
    void cddismiss(int list_id);
}
