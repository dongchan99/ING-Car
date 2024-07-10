package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ingcar_boot_war.dto.UserDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface UserDAO {

    /// 복호화 ///
    @Select("SELECT * FROM users WHERE user_id = #{user_id}")
    UserDTO findUserByUserId(String user_id);

    @Insert("INSERT INTO users (user_id, user_password, user_name, user_birth, user_email, user_gender, user_phone, user_area) " +
            "VALUES (#{user_id}, #{user_password}, #{user_name}, #{user_birth}, #{user_email}, #{user_gender}, #{user_phone}, #{user_area})")
    void join(UserDTO user);

    @Update("UPDATE users SET user_password = #{user_password} WHERE user_id = #{user_id}")
    void updateUserPassword(@Param("user_id") String user_id, @Param("user_password") String user_password);

    @Select("SELECT COUNT(*) FROM users WHERE user_id = #{user_id}")
    int countByUserId(String user_id);
    /// 복호화 끝 ///

    //회원가입
   /* @Insert("INSERT INTO USERS (user_id, user_password, user_name, user_birth, user_email, user_gender, user_phone, user_area) " +
            "VALUES (#{user_id}, #{user_password}, #{user_name}, #{user_birth}, #{user_email}, #{user_gender}, #{user_phone}, #{user_area})")
    void join(UserDTO user);*/

    //로그인
    /*@Select("SELECT * FROM USERS WHERE user_id = #{user_id} AND user_password = #{user_password}")
    UserDTO login(UserDTO user);*/

    //비밀번호 재설정 아이디-이메일 체크
   /* @Select("SELECT * FROM users WHERE user_id = #{userId}")
    UserDTO findUserByUserId(String userId);*/

    //비밀번호 재설정
   /* @Update("UPDATE users SET user_password = #{newPassword} WHERE user_id = #{userId}")
    void updateUserPassword(@Param("userId") String userId, @Param("newPassword") String newPassword);*/

    //아이디 중복검사
/*    @Select("SELECT COUNT(*) FROM users WHERE user_id = #{user_id}")
    int countByUserId(String user_id);*/

    // 박태경 //

    // mypage_profile 전체 조회
    @Select("SELECT * FROM Users WHERE user_id=#{user_id}")
    List<UserDTO> mypage_profile(String user_id);

    // checkpassword 프로필 변경할 때 비밀번호 확인
    @Select("SELECT * FROM Users WHERE user_password=#{password} and user_id=#{user_id}")
    List<UserDTO> checkpaswword(String password, String user_id);

    // mypage_update_username 변경
    @Update("UPDATE users SET user_name=#{name} WHERE user_id = #{user_id}")
    void mypage_update_username(String name, String user_id);

    // mypage_update_email 변경
    @Update("UPDATE users SET user_email=#{email} WHERE user_id = #{user_id}")
    void mypage_update_useremail(String email, String user_id);

    // mypage_update_area 변경
    @Update("UPDATE users SET user_area=#{area} WHERE user_id = #{user_id}")
    void mypage_update_userarea(String area, String user_id);

    // mypage_update_password 변경
    @Update("UPDATE users SET user_password=#{password1} WHERE user_id = #{user_id}")
    void mypage_update_userpassword(String password1, String user_id);

    // 김현아 추가
    @Select("SELECT user_password FROM users WHERE user_id = #{user_id}")
    String findPasswordByUserId(@Param("user_id") String user_id);

    @Select("SELECT user_id FROM Users WHERE user_id=#{user_id} AND use_YN='N'")
    List<UserDTO> find_use_YN(String user_id);

    @Select("SELECT * FROM Users WHERE user_id=#{user_id}")
    UserDTO profile(String user_id);

    //회원 정보 수정
    @Update("UPDATE users SET user_name=#{user.user_name}, user_email=#{user.user_email}, user_password=#{user.user_password}, user_comment=#{user.user_comment} WHERE user_id=#{user_id}")
    void updateUser(@Param("user") UserDTO user,@Param("user_id") String user_id);

    //회원 탈퇴
    @Update("UPDATE users SET use_YN='N' WHERE user_id=#{user_id}")
    void deleteUser(@Param("user_id") String user_id);

    //로그인 시 탈퇴 회원 확인
    @Select("SELECT use_YN FROM users WHERE user_id = #{user_id}")
    String checkUnsignUser(@Param("user_id") String user_id);
}
