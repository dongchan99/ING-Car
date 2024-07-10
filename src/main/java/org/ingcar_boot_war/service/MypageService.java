package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.*;
import org.ingcar_boot_war.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MypageService {

    @Autowired
    private TransLogCarDAO transLogCarDAO;

    @Autowired
    private TransLogReviewCarDAO transLogReviewCarDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CartCarDAO cartCarDAO;

    @Autowired
    private TransLogDAO transLogDAO;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private ReviewDAO reviewDAO;

///////////////////////////////////////////////////////////////////////////////////////////////

    private final int pageSize = 4;  // 페이지당 게시물 수
    @Autowired
    private CarDAO carDAO;

///////////////////////////////////////////////////////////////////////////////////////////////

    public int buying_totalPages(String user_id){

        int totalReviews = transLogCarDAO.mypage_buying_count(user_id); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<TransLogCarDTO> buying_resultlist(String user_id, int offset){

        return transLogCarDAO.mypage_buying(user_id, offset, pageSize);
    }

///////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////

    public int selling_totalPages(String user_id){

        int totalReviews = transLogCarDAO.mypage_selling_count(user_id); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<TransLogCarDTO> selling_resultlist(String user_id, int offset){

        return transLogCarDAO.mypage_selling(user_id, offset, pageSize);
    }

///////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////

    public int buyed_totalPages(String user_id){

        int totalReviews = transLogReviewCarDAO.mypage_buyed_count(user_id); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<TransLogReviewCarDTO> buyed_resultlist(String user_id, int offset){

        return transLogReviewCarDAO.mypage_buyed(user_id, offset, pageSize);
    }

///////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////

    public int selled_totalPages(String user_id){

        int totalReviews = transLogCarDAO.mypage_selled_count(user_id); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<TransLogCarDTO> selled_resultlist(String user_id, int offset){

        return transLogCarDAO.mypage_selled(user_id, offset, pageSize);
    }

//////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////

    public List<UserDTO> profile_resultlist(String user_id){

        return userDAO.mypage_profile(user_id);
    }

    public UserDTO profile(String user_id){

        return userDAO.profile(user_id);
    }

//////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////

    public int cart_totalPages(String user_id){

        int totalReviews = cartCarDAO.mypage_cart_count(user_id); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<CartCarDTO> cart_resultlist(String user_id, int offset){

        return cartCarDAO.mypage_cart_search(user_id, offset, pageSize);
    }

///////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////

    public int selling_list_totalPages(int car_registration_id){

        int totalReviews = transLogCarDAO.selling_list_totalPages(car_registration_id);
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<TransLogCarDTO> selling_list_resultlist(int car_registration_id, int offset){

        return transLogCarDAO.mypage_selling_list(car_registration_id, offset, pageSize);
    }

///////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////

    public void update_username(String name, String user_id){

        userDAO.mypage_update_username(name, user_id);
    }

    public void update_useremail(String email, String user_id){

        userDAO.mypage_update_useremail(email, user_id);
    }

    public void update_userpassword(String password1, String user_id){

        userDAO.mypage_update_userpassword(password1, user_id);
    }

    public void update_userarea(String area, String user_id){

        userDAO.mypage_update_userarea(area, user_id);
    }

////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////

    public void buying_cancel(int log_id){

        transLogDAO.deleteFromTable(log_id);
    }

    public void cart_cancel(int list_id){

        cartDAO.mypage_cart_cancel(list_id);
    }

    public void change_current_translog(int log_id){

        transLogDAO.change_current_translog(log_id);
    }

    public int find_reg_id(int log_id){

        return transLogCarDAO.find_reg_id1(log_id);
    }

    public void confirm(int log_id, int car_registration_id){

        transLogDAO.mypage_selling_confirm(log_id, car_registration_id);
    }

    public void selling_kill(int log_id, int car_registration_id){

        transLogDAO.mypage_selling_confirm_kill(log_id, car_registration_id);
    }

    public void cart_kill(int car_registration_id){

        cartDAO.mypage_selling_confirm_cart_kill(car_registration_id);
    }

    public List<ReviewDTO> find_log_id(int log_id){

        return reviewDAO.mypage_review_find_log(log_id);
    }

    public void increase(int log_id){

        reviewDAO.mypageincrementViewCount(log_id);
    }

    public List<ReviewDTO> select_detail(int log_id){

        return reviewDAO.mypageselectReviewWithDetails(log_id);
    }

    public List<Integer> ufind_all_reg(String user_id){

        List<Integer> car_reg = carDAO.find_all_reg(user_id);

        List<Integer> translog_reg = transLogDAO.find_all_reg();

        // 한쪽에만 있는 요소를 담을 리스트
        List<Integer> uniqueElements = new ArrayList<>();

        // car에만 있는 요소를 찾기
        for (Integer item1 : car_reg) {
            if (!translog_reg.contains(item1)) {
                uniqueElements.add(item1);
            }
        }

        return uniqueElements;
    }

    public List<Integer> dfind_all_reg(String user_id){

        List<Integer> car_reg = carDAO.find_all_reg(user_id);

        List<Integer> translog_reg = transLogDAO.find_zero_reg();

        // 한쪽에만 있는 요소를 담을 리스트
        List<Integer> uniqueElements = new ArrayList<>();

        // car에만 있는 요소를 찾기
        for (Integer item1 : car_reg) {
            if (!translog_reg.contains(item1)) {
                uniqueElements.add(item1);
            }
        }

        return uniqueElements;
    }

    public CarDTO car(int car_registration_id){

        return carDAO.car(car_registration_id);
    }

    public void update_price(int car_price, String car_price2, int reg_id){

        carDAO.update_price(car_price, car_price2, reg_id);
    }

    public List<CartDTO> checkEmpty(String user_id, int car_registration_id){

        return cartDAO.mypage_cart_input(user_id, car_registration_id);
    }

    public void cart_insert(String user_id, int car_registration_id){

        cartDAO.mypage_cart_insert(user_id, car_registration_id);
    }

    public void change_cart_status(int car_registration_id){

        cartDAO.change_cart_status(car_registration_id);
    }

    public void change_translog_status(int car_registration_id){

        transLogDAO.change_translog_status(car_registration_id);
    }

    public void cart_delete(String user_id, int car_registration_id){

        cartDAO.mypage_cart_delete(user_id, car_registration_id);
    }

    public void change_car_existed(int car_registration_id){

        carDAO.change_car_existed(car_registration_id);
    }

    public void update_cart_status(int car_registration_id) {

        cartDAO.update_cart_status(car_registration_id);
    }

    public void update_translog_status(int car_registration_id){

        transLogDAO.update_translog_status(car_registration_id);
    }

    public List<TransLogCarDTO> find_translog_status(String user_id){

        return transLogCarDAO.find_translog_status(user_id);
    }

    public List<CartCarDTO> find_cart_status(String user_id){

        return cartCarDAO.find_cart_status(user_id);
    }

    public List<TransLogCarDTO> find_tcar_existed(String user_id){

        return transLogCarDAO.find_tcar_existed(user_id);
    }

    public List<CartCarDTO> find_ccar_existed(String user_id){

        return cartCarDAO.find_ccar_existed(user_id);
    }

    public List<String> find_user_id(String user_id){

        return transLogCarDAO.find_user_id(user_id);
    }

    public List<UserDTO> find_use_YN(String user_id){

        return userDAO.find_use_YN(user_id);
    }

    public void dismiss_all_translog_status(String user_id){

        transLogDAO.dismiss_all_translog_status(user_id);
    }

    public void dismiss_all_cart_translog_status(String user_id){

        cartDAO.dismiss_all_cart_translog_status(user_id);
    }

    public List<Integer> dismiss_all_treg_id(String user_id){

        return transLogCarDAO.dismiss_all_reg_id(user_id);
    }

    public void dismiss_all_tcar_existed(String user_id, int car_registration_id){

        transLogDAO.dismiss_all_tcar_existed(user_id, car_registration_id);
    }

    public List<Integer> dismiss_all_creg_id(String user_id){

        return cartCarDAO.dismiss_all_reg_id(user_id);
    }

    public void dismiss_all_ccar_existed(String user_id, int car_registration_id){

        cartDAO.dismiss_all_ccar_existed(user_id, car_registration_id);
    }

    public void tdismiss(int log_id){

        transLogDAO.tdismiss(log_id);
    }

    public void cdismiss(int list_id){

        cartDAO.cdismiss(list_id);
    }

    public void tddismiss(int log_id){

        transLogDAO.tddismiss(log_id);
    }

    public void cddismiss(int list_id){

        cartDAO.cddismiss(list_id);
    }

    public void uddismiss(String user_id, String seller){

        int car_registration_id = transLogCarDAO.uddismiss(user_id, seller);

        transLogDAO.uddismiss(user_id, car_registration_id);
    }

    public int reg_id2(int log_id){

        return transLogDAO.reg_id2(log_id);
    }

    public List<TransLogDTO> find2(int car_registration_id){

        return transLogDAO.find2(car_registration_id);
    }

////////////////////////////////////////////////////////////////////////////////////////////////

    public int offset(int page){

        return (page - 1) * pageSize;
    }

    public List<UserDTO> checkpassword(String password, String user_id){

        return userDAO.checkpaswword(password, user_id);
    }

    public String checkLog(String user_id){

        if(user_id==null){

            return "out";
        }

        return "in";
    }

}
