package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.ApiUserDAO;
import org.ingcar_boot_war.dto.InformDTO;
import org.ingcar_boot_war.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiUserService {

    @Autowired
    private ApiUserDAO apiDAO;

    // 공용
    private final int pageSize = 10;  // 페이지당 게시물 수

    // 공용
    public int offset(int page){

        return (page - 1) * pageSize;
    }

    public int user_totalPages(){

        int totalReviews = apiDAO.user_getAllUser_count(); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<UserDTO> getAllUsers(int offset) {
        return apiDAO.getAllUsers(offset, pageSize);
    }

    public boolean toggleUserStatus(String userId, String newStatus) {
        int updatedRows = apiDAO.updateUserStatus(userId, newStatus);
        return updatedRows > 0;
    }

    public UserDTO userInfo(String user_id) {
        return apiDAO.userInfo(user_id);
    }

    public int updateUser(UserDTO user){
        return apiDAO.updateUser(user);
    }

    // 공지 내용 검색
    public List<UserDTO> searchByUser_id(String keyword, int offset) {
        return apiDAO.searchByUser_id(keyword, offset, pageSize);
    }

    // 공지 내용 검색
    public List<UserDTO> searchByUser_name(String keyword, int offset) {
        return apiDAO.searchByUser_name(keyword, offset, pageSize);
    }


}