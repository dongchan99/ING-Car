package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.InformDTO;
import org.ingcar_boot_war.dto.ReviewDTO;

import java.util.List;

@Mapper
public interface ReviewDAO {

    // 전체 가져오기
//    @Select("SELECT * FROM REVIEWBOARD")
//    List<ReviewDTO> getReview();
//    @Select("SELECT * FROM (" +
//            "SELECT a.*, ROWNUM rnum FROM (" +
//            "SELECT * FROM REVIEWBOARD ORDER BY board_updatedate DESC" +
//            ") a WHERE ROWNUM <= #{page} * #{pageSize}" +
//            ") WHERE rnum > (#{page} - 1) * #{pageSize}")
//    List<ReviewDTO> findAllPaged(@Param("page") int page, @Param("pageSize") int pageSize);


    @Select("SELECT * FROM (SELECT ROWNUM AS RNUM, A.* FROM (SELECT r.*, c.car_model as model FROM REVIEWBOARD r " +
            "JOIN translog t ON r.log_id = t.log_id " +
            "JOIN car c ON t.car_registration_id = c.car_registration_id " +
            "ORDER BY r.BOARD_ID DESC" +
            ") A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<ReviewDTO> findAllPaged(@Param("page") int page, @Param("pageSize") int pageSize);

    // 팀원 공유 받은 로직
    @Insert("INSERT INTO REVIEWBOARD (BOARD_ID, LOG_ID, BOARD_SUBJECT, BOARD_CONTENT, BOARD_STAR)" +
            "VALUES (BOARD_ID_SEQ.NEXTVAL, #{log_id}, #{board_subject}, #{board_content}, #{board_star})")
    void insertReview(int log_id, String board_star, String board_content, String board_subject);

    @Select("SELECT board_like FROM ReviewBoard WHERE log_id=#{log_id}")
    int find_board_like(int log_id);

    // board_like 1감소
    @Update("UPDATE ReviewBoard SET board_like=#{board_like}-1 WHERE log_id=#{log_id}")
    void minus_board_like(int log_id, int board_like);

    // board_like 1증가
    @Update("UPDATE ReviewBoard SET board_like=#{board_like}+1 WHERE log_id=#{log_id}")
    void plus_board_like(int log_id, int board_like);

    // 팀원 공유 받은 로직 끝


    ///////////////////////////////////////////////////////

    @Select("SELECT * FROM (SELECT ROWNUM AS RNUM, A.* FROM (SELECT r.*, c.car_model as model,c.user_id as seller FROM REVIEWBOARD r " +
            "JOIN translog t ON r.log_id = t.log_id " +
            "JOIN car c ON t.car_registration_id = c.car_registration_id " +
            "ORDER BY r.BOARD_ID DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<ReviewDTO> findAllPagedReview(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 전체 게시물 수
    @Select("SELECT COUNT(*) FROM REVIEWBOARD")
    int countAllReview();

    // review_detail
    @Select("SELECT * FROM REVIEWBOARD WHERE LOG_ID = #{log_id} AND BOARD_SUBJECT = #{board_subject}")
    List<ReviewDTO> selectReview(@Param("log_id") String log_id, @Param("board_subject") String board_subject);

    // 조회수 증가
    @Update("UPDATE REVIEWBOARD SET BOARD_COUNT = BOARD_COUNT + 1 WHERE LOG_ID = #{log_id} AND BOARD_SUBJECT = #{board_subject}")
    void incrementViewCount(@Param("log_id") String log_id, @Param("board_subject") String board_subject);

    // 조회수 가져오기
    @Select("SELECT BOARD_COUNT FROM REVIEWBOARD WHERE LOG_ID = #{log_id} AND BOARD_SUBJECT = #{board_subject}")
    int getViewCount(@Param("log_id") String log_id, @Param("board_subject") String board_subject);

    // 좋아요 증가
    @Update("UPDATE REVIEWBOARD SET BOARD_LIKE = BOARD_LIKE + #{increment} WHERE LOG_ID = #{log_id}")
    int updateLikeCount(@Param("log_id") String logId, @Param("increment") int increment);

    @Select("SELECT BOARD_LIKE FROM REVIEWBOARD WHERE LOG_ID = #{log_id}")
    int getLikeCount(@Param("log_id") String logId);


    @Select("SELECT COUNT(*) FROM reviewlike WHERE user_id = #{user_id} AND review_id = #{review_id}")
    int isLiked(@Param("user_id") String userId, @Param("review_id") int reviewId);



    // 제목으로 검색한 데이터들에 대한 페이징
    @Select("SELECT * FROM " +
            "(SELECT ROWNUM AS RNUM, A.* FROM " +
            "(SELECT r.*, c.car_model as model FROM REVIEWBOARD r " +
            "JOIN TRANSLOG t ON r.log_id = t.log_id " +
            "JOIN CAR c ON t.car_registration_id = c.car_registration_id " +
            "WHERE r.BOARD_SUBJECT LIKE '%' || #{board_subject} || '%' " +
            "ORDER BY r.BOARD_ID DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) " +
            "WHERE RNUM > #{offset}")
    List<ReviewDTO> findByBoardSubjectWithPaging
    (@Param("board_subject") String boardSubject, @Param("offset") int offset,
     @Param("pageSize") int pageSize);


    // 제목으로 검색된 데이터 전체 갯수
    @Select("SELECT COUNT(*) FROM REVIEWBOARD WHERE BOARD_SUBJECT LIKE '%' || #{board_subject} || '%'")
    int countByBoardSubject(@Param("board_subject") String boardSubject);

    //////// 조인한 테이블로 detail 화면에 데이터 뿌리기 ////////
    @Select("SELECT r.*, u.user_id as buyer, c.user_id as seller, c.car_model as model " +
            "FROM reviewboard r " +
            "JOIN translog t ON r.log_id = t.log_id " +
            "JOIN users u ON t.user_id = u.user_id " +
            "JOIN car c ON t.car_registration_id = c.car_registration_id " +
            "WHERE r.log_id = #{log_id} AND r.board_subject = #{board_subject}")

    List<ReviewDTO> selectReviewWithDetails(@Param("log_id") String log_id, @Param("board_subject") String board_subject);


    // 수정한 후기 update
    @Update("UPDATE reviewboard" +
            " SET board_subject = #{board_subject}, " +
            "board_content = #{board_content}," +
            " board_star = #{board_star}," +
            " board_updatedate = SYSDATE WHERE log_id = #{log_id}")
    void updateReviewByLogId(@Param("log_id") int log_id,
                             @Param("board_subject") String board_subject,
                             @Param("board_content") String board_content,
                             @Param("board_star") int board_star);



    //--------------------------------------------ㅍ 모델명으로 검색

    @Select("SELECT COUNT(*) " +
            "FROM reviewboard r " +
            "JOIN translog t ON r.log_id = t.log_id " +
            "JOIN car c ON t.car_registration_id = c.car_registration_id " +
            "WHERE c.car_model LIKE '%' || #{car_model} || '%' ")
    int countByCarModel(@Param("car_model") String carModel);


    // 차 모델명으로 검색 & 페이징 메서드
    @Select("SELECT * FROM " +
            "(SELECT A.*, ROWNUM AS RNUM FROM " +
            "(SELECT r.*, c.car_model as model FROM REVIEWBOARD r " +
            "JOIN TRANSLOG t ON r.log_id = t.log_id " +
            "JOIN CAR c ON t.car_registration_id = c.car_registration_id " +
            "WHERE c.car_model LIKE '%' || #{car_model} || '%' " +
            "ORDER BY r.BOARD_ID DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) " +
            "WHERE RNUM > #{offset}")
    List<ReviewDTO> findByCarModelWithPaging(@Param("car_model") String carModel, @Param("offset") int offset, @Param("pageSize") int pageSize);


    //--------------------------------------------ㅍ 모델명으로 검색


    // -----------------------------------------------------------------------------------------------------------
    // 박태경 //

    // mypage_buyed에서 후기 보기
    @Select("SELECT * FROM REVIEWBOARD WHERE LOG_ID = #{log_id}")
    List<ReviewDTO> mypage_selectReview(@Param("log_id") int log_id);

    // mypage -> review_detail
    @Select("SELECT r.*, u.user_id as buyer, c.user_id as seller, c.car_model as model " +
            "FROM reviewboard r " +
            "JOIN translog t ON r.log_id = t.log_id " +
            "JOIN users u ON t.user_id = u.user_id " +
            "JOIN car c ON t.car_registration_id = c.car_registration_id " +
            "WHERE r.log_id = #{log_id}")
    List<ReviewDTO> mypageselectReviewWithDetails(@Param("log_id") int log_id);

    // 조회수 증가
    @Update("UPDATE REVIEWBOARD SET BOARD_COUNT = BOARD_COUNT + 1 WHERE LOG_ID = #{log_id}")
    void mypageincrementViewCount(@Param("log_id") int log_id);

    // ReviewBoard 테이블에 값이 존재하는지 확인
    @Select("SELECT * FROM ReviewBoard WHERE log_id=#{log_id}")
    List<ReviewDTO> mypage_review_find_log(int log_id);






    ///// 관리자 ----------------------------------------------------------------------------------------------


    // 트랜잭션으로 댓글, 후기 삭제
    @Delete("DELETE FROM reviewboard WHERE log_id = #{log_id}")
    void deleteReviewByLogId(@Param("log_id") int log_id);


    // 리뷰 제목검색
    @Select("SELECT * FROM (SELECT ROWNUM AS RNUM, A.* FROM (SELECT r.*, c.car_model as model,c.user_id as seller FROM REVIEWBOARD r " +
            "JOIN translog t ON r.log_id = t.log_id " +
            "JOIN car c ON t.car_registration_id = c.car_registration_id " +
            "WHERE r.BOARD_SUBJECT LIKE '%' || #{keyword} || '%' " +
            "ORDER BY r.BOARD_ID DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<ReviewDTO> searchByReviewTitle(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);



    // 리뷰 내용 검색
    @Select("SELECT * FROM ( " +
            "SELECT ROWNUM AS rn, a.* FROM ( " +
            "SELECT * FROM reviewboard WHERE board_content LIKE '%' || #{keyword} || '%' ORDER BY board_updatedate DESC " +
            ") a WHERE ROWNUM <= #{offset} + #{pageSize} " +
            ") WHERE rn > #{offset}")
    List<ReviewDTO> searchByReviewContent(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);




}
