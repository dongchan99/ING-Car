package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.ReviewLikeDTO;

import java.util.List;

@Mapper
public interface ReviewLikeDAO {

    // ReviewLike 테이블에 user_id와 log_id가 모두 만족하는게 있는지 확인 ?
    @Select("SELECT * FROM ReviewLike WHERE user_id=#{user_id} and log_id=#{log_id}")
    List<ReviewLikeDTO> checkHeart(String user_id, int log_id);

    // ReviewLike 테이블에 값이 없으면 insert
    @Insert("INSERT INTO ReviewLike (like_id, log_id, user_id) " +
            "VALUES (like_id_seq.NEXTVAL, #{log_id}, #{user_id})")
    void insertHeart(String user_id, int log_id);

    // ReviewLike 테이블에 값이 있으면 delete
    @Delete("DELETE FROM ReviewLike WHERE log_id=#{log_id} and user_id=#{user_id}")
    void deleteHeart(String user_id, int log_id);

    // 메인페이지 -> review_main
    @Select("SELECT * FROM (SELECT ROWNUM AS RNUM, A.* FROM (SELECT * FROM ReviewLike ORDER BY like_id DESC) A WHERE ROWNUM = 1 + #{page_heart}) WHERE RNUM = 1 + #{page_heart}")
    List<ReviewLikeDTO> one(@Param("page_heart") int page_heart);

    // 김현아 추가 20240626
    // 특정 log_id에 해당하는 좋아요 기록 삭제
    @Delete("DELETE FROM ReviewLike WHERE log_id = #{log_id}")
    void deleteLikesByLogId(@Param("log_id") int log_id);
}