package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.ingcar_boot_war.dto.CartCarDTO;
import org.ingcar_boot_war.dto.CartDTO;

import java.util.List;

@Mapper
public interface CartCarDAO {

    // 전체 조회 (cart)
    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT A.list_id, B.car_registration_date, B.user_id, B.car_model, B.car_price2, A.cart_status FROM Cart A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} ORDER BY A.list_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<CartCarDTO> mypage_cart_search(String user_id, int offset, int pageSize);

    // 전체 DATA 개수 (장바구니)
    @Select("SELECT COUNT(*) FROM Cart A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id}")
    int mypage_cart_count(String user_id);

    @Select("SELECT B.car_model, B.user_id, A.list_id FROM Cart A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} AND B.car_existed='N'")
    List<CartCarDTO> find_ccar_existed(String user_id);

    @Select("SELECT A.car_registration_id FROM Cart A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} AND B.car_existed='N'")
    List<Integer> dismiss_all_reg_id(String user_id);

    @Select("SELECT B.car_model, B.user_id, A.list_id FROM Cart A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} AND B.car_existed!='N' AND A.cart_status='N'")
    List<CartCarDTO> find_cart_status(String user_id);

}
