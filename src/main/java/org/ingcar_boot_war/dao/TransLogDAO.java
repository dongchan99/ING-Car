package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.TransLogDTO;

import java.util.List;


@Mapper
public interface TransLogDAO {

    @Select("SELECT COUNT(*) FROM transLog WHERE car_registration_id = #{car_registration_id} AND user_id = #{user_id}")
    int countTransLog(@Param("car_registration_id") int car_registration_id, @Param("user_id") String user_id);


    //예약하기
    @Insert("INSERT INTO transLog (car_registration_id, user_id, translog_price) " +
            "VALUES (#{car_registration_id}, #{user_id}, #{translog_price})")
    void insertTransLog(@Param("car_registration_id") int car_registration_id, @Param("user_id") String user_id, @Param("translog_price") String translog_price);

    // "취소하기" (구매중)
    @Delete("DELETE FROM TransLog WHERE log_id=#{log_id}")
    void deleteFromTable(int log_id);

    // "확정하기" (판매중)
    @Update("UPDATE TransLog SET current_translog=1, log_date=SYSDATE WHERE log_id=#{log_id} and car_registration_id=#{car_registration_id}")
    void mypage_selling_confirm(int log_id, int car_registration_id);

    // "확정 후 나머지 삭제" (판매중)
    @Delete("DELETE FROM TransLog WHERE log_id!=#{log_id} and car_registration_id=#{car_registration_id} and current_translog=0")
    void mypage_selling_confirm_kill(int log_id, int car_registration_id);

    // "예약하기"
    @Insert("INSERT INTO TransLog (log_id, car_registration_id, user_id, current_translog, log_date) VALUES (log_id_seq.NEXTVAL, #{car_registration_id}, #{user_id}, 0, SYSDATE)")
    void mypage_translog(int car_registration_id, String user_id);

    // car_registration_id 찾기 (장바구니)
    @Select("SELECT car_registration_id FROM TransLog WHERE log_id=#{log_id}")
    int find_car_registration_id(int log_id);

    // car_single에서 "예약하기" 띄울지 말지 확인
    @Select("SELECT *FROM TransLog WHERE user_id=#{user_id} and car_registration_id=#{car_registration_id}")
    List<TransLogDTO> checkExist(String user_id, int car_registration_id);


    // 메인페이지 -> review_main (이미지 출력)
    @Select("SELECT car_registration_id FROM TransLog WHERE current_translog=1 and log_id=#{log_id}")
    int find_heart_car_registration(int log_id);

    // update car에서 비교를 위해 reg 전부 출력
    @Select("SELECT car_registration_id FROM TransLog WHERE current_translog=1")
    List<Integer> find_all_reg();

    // delete car에서 비교를 위해 0인 reg 전부 출력
    @Select("SELECT car_registration_id FROM TransLog WHERE current_translog=1")
    List<Integer> find_zero_reg();

    // 차량 정보 수정에의한 translog_status 변경
    @Update("UPDATE TransLog SET translog_status='N' WHERE car_registration_id=#{car_registration_id}")
    void change_translog_status(int car_registraion_id);

    @Select("SELECT COUNT(*) FROM transLog where car_registration_id=#{car_registration_id} AND current_translog=1")
    int check_transLog(int car_registration_id);

    @Update("UPDATE TransLog SET current_translog=2 WHERE log_id=#{log_id}")
    void change_current_translog(int log_id);

    @Select("SELECT * FROM Translog WHERE car_registration_id=#{car_registration_id} and current_translog=1")
    List<TransLogDTO> check_current_translog(int car_registration_id);

    @Update(("UPDATE TransLog SET translog_status='N' WHERE car_registration_id=#{car_registration_id}"))
    void update_translog_status(int car_registration_id);

    @Update("UPDATE TransLog SET translog_status='Y' WHERE user_id=#{user_id}")
    void dismiss_all_translog_status(String user_id);

    @Delete("DELETE FROM TransLog WHERE user_id=#{user_id} and car_registration_id=#{car_registration_id}")
    void dismiss_all_tcar_existed(String user_id, int car_registration_id);

    @Update("UPDATE TransLog SET translog_status='Y' WHERE log_id=#{log_id}")
    void tdismiss(int log_id);

    @Delete("DELETE FROM TransLog WHERE log_id=#{log_id}")
    void tddismiss(int log_id);

    @Delete("DELETE FROM TransLog WHERE user_id=#{user_id} AND car_registration_id=#{car_registration_id} AND current_translog=2")
    void uddismiss(String user_id, int car_registration_id);

    @Delete("DELETE FROM TransLog WHERE user_id=#{user_id} AND current_translog=2")
    void deleteTranslog(String user_id);

    @Select("SELECT car_registration_id FROM TransLog WHERE log_id=#{log_id}")
    int reg_id2(int log_id);

    @Select("SELECT * FROM TransLog WHERE car_registration_id=#{car_registration_id} AND current_translog=2")
    List<TransLogDTO> find2(int car_registration_id);

}




