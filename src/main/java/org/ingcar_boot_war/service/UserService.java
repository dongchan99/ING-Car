package org.ingcar_boot_war.service;

import jakarta.annotation.PostConstruct;
import org.ingcar_boot_war.dao.CarDAO;
import org.ingcar_boot_war.dao.ReviewDAO;
import org.ingcar_boot_war.dao.TransLogDAO;
import org.ingcar_boot_war.dao.UserDAO;
import org.ingcar_boot_war.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    @Autowired
    UserDAO dao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private CarDAO carDAO;
    @Autowired
    private TransLogDAO transLogDAO;

/*    public void registerUser(UserDTO user) {

        dao.join(user);
    }

    public UserDTO loginUser(UserDTO user) {

        return dao.login(user);
    }

    public boolean checkUserIdAndEmail(String userId, String userEmail) {
        UserDTO user = dao.findUserByUserId(userId);
        return user != null && user.getUser_email().equals(userEmail);
    }

    public void updatePassword(String userId, String newPassword) {
        dao.updateUserPassword(userId, newPassword);
    }

    public boolean isUserIdExists(String userId) {
        int count = dao.countByUserId(userId);
        return count > 0;
    }*/


    // 회원가입 복호화
    public void registerUser(UserDTO user) {

        // 중복된 user_id 체크
        if (dao.findUserByUserId(user.getUser_id()) != null) {
            System.out.println("이미 존재하는 id");
            return;
        }
        // 비밀번호 해싱
        System.out.println("암호화 전 = " + user.getUser_password());
        //String encodedPassword = passwordEncoder.encode(user.getUser_password());
       // System.out.println("암호화 후: " + encodedPassword); // 해싱된 비밀번호 출력
        user.setUser_password(user.getUser_password());
        System.out.println("user = " + user);
        dao.join(user);
    }

    public UserDTO loginUser(UserDTO user) {
        // 데이터베이스에서 입력된 사용자 ID에 해당하는 사용자 정보를 조회
        UserDTO existingUser = dao.findUserByUserId(user.getUser_id());
        System.out.println("loginUser - existingUser: " + existingUser); // 디버그를 위한 출력


        if (existingUser != null) {

            // 디버그를 위한 비밀번호 일치 여부 확인
            System.out.println("user.getUser_password() = " + user.getUser_password());// 입력된 비밀번호 출력
            System.out.println("existingUser.getUser_password() = " + existingUser.getUser_password());// 저장된 해시 비밀번호 출력

            // 해싱된 비밀번호가 같은지 비교
            boolean isMatch = false;

            isMatch = passwordEncoder.matches(user.getUser_password(), existingUser.getUser_password());
           // isMatch = passwordEncoder.matches("dnfltkaqks1", "$2a$10$enXfDH/KG/W0sAy9o0KtEeTuR4x1sKfpHBdcNixcqZSJARpuex.BO");
            System.out.println("Password match: " + isMatch); // 비교 결과 출력

            if (isMatch) {
                // 비밀번호가 일치하면 사용자 정보를 반환
                user.setUser_password(existingUser.getUser_password());
                return existingUser;
            }
        }
        // 비밀번호가 일치하지 않거나 사용자가 존재하지 않으면 null을 반환
        return null;
    }

    public boolean checkUserIdAndEmail(String user_id, String user_email) {
        UserDTO user = dao.findUserByUserId(user_id);
        return user != null && user.getUser_email().equals(user_email);
    }

    public boolean updatePassword(String user_id, String newPassword) {
        // 비밀번호 해싱
        try {
            String encodedPassword = passwordEncoder.encode(newPassword);
            dao.updateUserPassword(user_id, encodedPassword);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserIdExists(String user_id) {
        int count = dao.countByUserId(user_id);
        return count > 0;
    }

    // 김현아 추가 복호화

    public String getUserPassword(String user_id) {
        return dao.findPasswordByUserId(user_id);
    }

    public boolean updateUser(UserDTO user,String user_id) {
        try {
            dao.updateUser(user,user_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteUser(String user_id){
        dao.deleteUser(user_id);
    }

    public void change_ucar_existed(String user_id){

        carDAO.change_ucar_existed(user_id);
    }

    public String checkUnsginUser(String user_id){
        return dao.checkUnsignUser(user_id);
    }

    public void deleteTranslog(String user_id){

        transLogDAO.deleteTranslog(user_id);
    }

}
