package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ingcar_boot_war.dto.InformDTO;
import org.ingcar_boot_war.dto.TransLogCarDTO;
import org.ingcar_boot_war.dto.UserDTO;


import java.util.List;

@Mapper
public interface ApiUserDAO {


    @Select("SELECT COUNT(*) FROM users")
    int user_getAllUser_count();

    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * from users order by user_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<UserDTO> getAllUsers(int offset, int pageSize);

    @Update("UPDATE users SET use_YN = #{use_YN} WHERE user_id = #{user_id}")
    int updateUserStatus(@Param("user_id") String user_id, @Param("use_YN") String use_YN);

    @Select("SELECT * from users where user_id=#{user_id}")
    UserDTO userInfo(@Param("user_id") String user_id);

    @Update("UPDATE users SET user_password = #{user_password}, user_name = #{user_name}, user_email = #{user_email}, user_phone = #{user_phone}, user_gender = #{user_gender}, user_area = #{user_area} WHERE user_id = #{user_id}")
    int updateUser(UserDTO user);

    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * from users WHERE user_id LIKE '%' || #{keyword} || '%' order by user_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<UserDTO> searchByUser_id(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * from users WHERE user_name LIKE '%' || #{keyword} || '%' order by user_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<UserDTO> searchByUser_name(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

}
