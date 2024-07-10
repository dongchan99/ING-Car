package org.ingcar_boot_war.controller;

import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dto.*;
import org.ingcar_boot_war.service.InquiryService;
import org.ingcar_boot_war.service.MypageService;
import org.ingcar_boot_war.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private MypageService mypageService;

    @Autowired
    private UserService userService;

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/success")
    public String success(){

        return "mypage_success";
    }

    @GetMapping("/mypage_empty")
    public String empty() {

        return "mypage";
    }

    @GetMapping("/mypage_selling_confirm_alert")
    public String mypage_selling_confirm_alert() {

        return "mypage_buying_cancel";
    }

    @GetMapping("/mypage_buying_cancel_alert")
    public String mypage_buying_cancel_alert() {

        return "mypage";
    }

    @GetMapping("/mypage_update_error")
    public String mypage_update_error() {

        return "mypage_profile";
    }

    @GetMapping("/mypage_cart_cancel_alert")
    public String mypage_cart_cancel_alert() {

        return "redirect:/mypage";
    }

    @GetMapping("")
    public String mypage(HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        model.addAttribute("log", log);

        return "mypage";
    }

    @GetMapping("/buying")
    public String buying(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        model.addAttribute("log", log);

        int totalPages=mypageService.buying_totalPages(user_session.getUser_id());

        int offset=mypageService.offset(page);

        List<TransLogCarDTO> resultList = mypageService.buying_resultlist(user_session.getUser_id(), offset);

        if(resultList.isEmpty()){
            return "mypage_empty";
        }

        model.addAttribute("resultlist", resultList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "mypage_buying";
    }

    @GetMapping("/selling")
    public String selling(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        model.addAttribute("log", log);

        int totalPages=mypageService.selling_totalPages(user_session.getUser_id());

        int offset=mypageService.offset(page);

        System.out.println(totalPages + offset);

        List<TransLogCarDTO> resultList = mypageService.selling_resultlist(user_session.getUser_id(), offset);

        if(resultList.isEmpty()){
            return "mypage_empty";
        }

        model.addAttribute("resultlist", resultList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "mypage_selling";
    }

    @GetMapping("/buyed")
    public String buyed(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        model.addAttribute("log", log);

        int totalPages=mypageService.buyed_totalPages(user_session.getUser_id());

        int offset=mypageService.offset(page);

        List<TransLogReviewCarDTO> resultList = mypageService.buyed_resultlist(user_session.getUser_id(), offset);

        if(resultList.isEmpty()){
            return "mypage_empty";
        }

        model.addAttribute("resultlist", resultList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "mypage_buyed";
    }

    @GetMapping("/selled")
    public String selled(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        model.addAttribute("log", log);

        int totalPages=mypageService.selled_totalPages(user_session.getUser_id());

        int offset=mypageService.offset(page);

        List<TransLogCarDTO> resultList = mypageService.selled_resultlist(user_session.getUser_id(), offset);

        if(resultList.isEmpty()){
            return "mypage_empty";
        }

        model.addAttribute("resultlist", resultList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "mypage_selled";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        model.addAttribute("log", log);

        UserDTO user = mypageService.profile(user_session.getUser_id());

        model.addAttribute("user", user);

        return "profile";
    }

    @GetMapping("/cart")
    public String cart(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model) {

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        model.addAttribute("log", log);

        int totalPages=mypageService.cart_totalPages(user_session.getUser_id());

        int offset=mypageService.offset(page);

        List<CartCarDTO> resultList = mypageService.cart_resultlist(user_session.getUser_id(), offset);

        if(resultList.isEmpty()){
            return "mypage_empty";
        }

        model.addAttribute("resultlist", resultList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "mypage_cart";
    }

    @PostMapping("/selling/list")
    public ResponseEntity<Map<String, String>> selling_list(@RequestBody Map<String, Object> request){

        String car_registration_id = request.get("key").toString();

        Map<String, String> response = new HashMap<>();

        response.put("message", car_registration_id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/update/car")
    public String update_car(HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        List<Integer> reg_ids=mypageService.ufind_all_reg(user_session.getUser_id());

        List<CarDTO> resultList = new ArrayList<>();

        for (Integer item1 : reg_ids) {

            CarDTO val = mypageService.car(item1);
            resultList.add(val);
        }

        model.addAttribute("resultlist", resultList);

        if(resultList.isEmpty()){
            return "mypage_empty";
        }

        return "mypage_update_car";
    }

    @GetMapping("/delete/car")
    public String delete_car(HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        List<Integer> reg_ids=mypageService.dfind_all_reg(user_session.getUser_id());

        List<CarDTO> resultList = new ArrayList<>();

        for (Integer item1 : reg_ids) {

            CarDTO val = mypageService.car(item1);
            resultList.add(val);
        }

        System.out.println(resultList);

        model.addAttribute("resultlist", resultList);

        if(resultList.isEmpty()){
            return "mypage_empty";
        }

        return "mypage_delete_car";
    }

    @PostMapping("/list")
    public ModelAndView list(@RequestParam(name="car_registration_id") String car_registration_id, HttpSession session, @RequestParam(defaultValue = "1") int page){

        int offset=mypageService.offset(page);

        int reg_id=Integer.parseInt(car_registration_id);

        int totalPages=mypageService.selling_list_totalPages(reg_id);

        List<TransLogCarDTO> resultList = mypageService.selling_list_resultlist(reg_id, offset);

        if(resultList.isEmpty()){

            return new ModelAndView("mypage_empty");
        }

        ModelAndView mav = new ModelAndView("mypage_selling_list");

        mav.addObject("resultlist", resultList);

        mav.addObject("currentPage", page);

        mav.addObject("totalPages", totalPages);

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String log=mypageService.checkLog(user_session.getUser_id());

        mav.addObject("log", log);

        return mav;
    }

    @GetMapping("/notification")
    public String mypage_notification(HttpSession session, Model model){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        // 구매자 입장

        List<TransLogCarDTO> translog_status = mypageService.find_translog_status(user_session.getUser_id()); // 가격 변화 translog (완료 전까지)

        List<CartCarDTO> cart_status = mypageService.find_cart_status(user_session.getUser_id()); // 가격 변화 cart

        List<TransLogCarDTO> tcar_existed = mypageService.find_tcar_existed(user_session.getUser_id()); // 차량 or 판매자 사라졌을 경우 translog

        List<CartCarDTO> ccar_existed = mypageService.find_ccar_existed(user_session.getUser_id()); // 차량 or 판매자 사라졌을 경우 cart

        // 판매자 입장

        List<String> user_id = mypageService.find_user_id(user_session.getUser_id()); // 구매자가 탈퇴 됬을 경우

        for (String item : user_id) {
            List<UserDTO> use_YN = mypageService.find_use_YN(item);
            model.addAttribute("use_YN", use_YN);
        }

        model.addAttribute("translog_status", translog_status);
        model.addAttribute("cart_status", cart_status);
        model.addAttribute("tcar_existed", tcar_existed);
        model.addAttribute("ccar_existed", ccar_existed);

        if(translog_status.isEmpty() && cart_status.isEmpty() && tcar_existed.isEmpty() && ccar_existed.isEmpty()){

            return "mypage_empty";
        }

        return "mypage_notification";
    }

    @GetMapping("/dismiss_all")
    public void noti_dismiss_all(HttpSession session){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        mypageService.dismiss_all_translog_status(user_session.getUser_id()); // 가격 수정 : translog_status Y로 복귀

        mypageService.dismiss_all_cart_translog_status(user_session.getUser_id()); // 가격 수정 : cart_status Y로 복귀

        List<Integer> tcar_registration_id = mypageService.dismiss_all_treg_id(user_session.getUser_id());

        for(int i=0 ; i<tcar_registration_id.size() ; i++){

            mypageService.dismiss_all_tcar_existed(user_session.getUser_id(), tcar_registration_id.get(i)); // 삭제 : translog 테이블에서 삭제
        }

        List<Integer> ccar_registration_id = mypageService.dismiss_all_creg_id(user_session.getUser_id());

        for(int i=0 ; i<tcar_registration_id.size() ; i++){

            mypageService.dismiss_all_ccar_existed(user_session.getUser_id(), ccar_registration_id.get(i)); // 삭제 : cart 테이블에서 삭제
        }

    }

    @PostMapping("/tdismiss")
    public ResponseEntity<Map<String, String>> tdismiss(@RequestBody Map<String, Object> request){

        int log_id = Integer.parseInt(request.get("key").toString());

        mypageService.tdismiss(log_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cdismiss")
    public ResponseEntity<Map<String, String>> cdismiss(@RequestBody Map<String, Object> request){

        int list_id = Integer.parseInt(request.get("key").toString());

        mypageService.cdismiss(list_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/tddismiss")
    public ResponseEntity<Map<String, String>> tddismiss(@RequestBody Map<String, Object> request){

        int log_id = Integer.parseInt(request.get("key").toString());

        mypageService.tddismiss(log_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cddismiss")
    public ResponseEntity<Map<String, String>> cddismiss(@RequestBody Map<String, Object> request){

        int list_id = Integer.parseInt(request.get("key").toString());

        mypageService.cddismiss(list_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/uddismiss")
    public ResponseEntity<Map<String, String>> uddismiss(@RequestBody Map<String, Object> request, HttpSession session){

        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        String user_id = request.get("key").toString();

        mypageService.uddismiss(user_id, user_session.getUser_id());

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updateUser")
    public String updateProfile(@ModelAttribute("user") UserDTO user, @RequestParam("check_password") String check_password, HttpSession session, RedirectAttributes redirectAttributes) {
        //세션에서 유저 정보 가져오기
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        String User_id = user_session.getUser_id();

        if (!user.getUser_password().equals(check_password)) {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/profile";
        }
        // 비밀번호 인코딩
        user.setUser_password(passwordEncoder.encode(user.getUser_password()));
        // 데이터베이스에서 사용자 정보 업데이트
        boolean updateSuccess = userService.updateUser(user, User_id);
        if (updateSuccess) {
            redirectAttributes.addFlashAttribute("successMessage", "정보가 성공적으로 업데이트 되었습니다.");
            return "redirect:/mypage/profile";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "정보 업데이트에 실패했습니다.");
            return "redirect:/mypage/profile";
        }
    }

    @PostMapping("/buying")
    public ResponseEntity<Map<String, String>> buying_cancel(@RequestBody Map<String, Object> request){

        int log_id = Integer.parseInt(request.get("key").toString());

        mypageService.buying_cancel(log_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/review/write")
    public ResponseEntity<Map<String, String>> review_write(@RequestBody Map<String, Object> request){

        String log_id = request.get("key").toString();

        Map<String, String> response = new HashMap<>();

        response.put("message", log_id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/write")
    public ModelAndView submitUser1(@RequestParam(name="log_id") String log_id) {

        ModelAndView mav = new ModelAndView("review_write");

        mav.addObject("log_id", log_id);

        System.out.println(log_id);

        return mav;
    }

    @PostMapping("/cart")
    public ResponseEntity<Map<String, String>> cart_cancel(@RequestBody Map<String, Object> request){

        int list_id = Integer.parseInt(request.get("key").toString());

        mypageService.cart_cancel(list_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/choose")
    public ResponseEntity<Map<String, String>> choose_cancel(@RequestBody Map<String, Object> request, HttpSession session){

        UserDTO userSession = (UserDTO) session.getAttribute("user_session");

        int log_id = Integer.parseInt(request.get("key").toString());

        int reg_id2=mypageService.reg_id2(log_id); // current_translog=2 인 이 있는지 확인 car_registration_id

        List<TransLogDTO> find2=mypageService.find2(reg_id2);

        // 판매자가 이미 선택한 구매자가 있다면
        if(!find2.isEmpty()){

            Map<String, String> response = new HashMap<>();

            response.put("message", "FAILED");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        mypageService.change_current_translog(log_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, String>> buying_payment(@RequestBody Map<String, Object> request){

        String log_id = request.get("key").toString();

        Map<String, String> response = new HashMap<>();

        response.put("message", log_id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/pay")
    public String buying_pay(@RequestParam(name="log_id") String log_id, Model model){

        int logId = Integer.parseInt(log_id);

        model.addAttribute("log_id", logId);

        return "mypage_payment";
    }

    @PostMapping("/selling")
    public ResponseEntity<Map<String, String>> selling_confirm(@RequestBody Map<String, Object> request){

        int log_id = Integer.parseInt(request.get("key").toString());

        int car_registration_id = mypageService.find_reg_id(log_id);

        mypageService.confirm(log_id, car_registration_id);

        mypageService.selling_kill(log_id, car_registration_id);

        mypageService.cart_kill(car_registration_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/review/detail")
    public ResponseEntity<Map<String, String>> review_detail(@RequestBody Map<String, Object> request){

        String log_id = request.get("key").toString();

        Map<String, String> response = new HashMap<>();

        response.put("message", log_id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/detail")
    public ModelAndView submitUser2(@RequestParam(name="log_id") String log_id, HttpSession session) {

        UserDTO userSession = (UserDTO) session.getAttribute("user_session");

        String userId = userSession.getUser_id();

        int logId = Integer.parseInt(log_id);

        List<ReviewDTO> val = mypageService.find_log_id(logId);

        if(val.isEmpty()){

            ModelAndView mav = new ModelAndView("mypage_empty");
            return mav;
        }

        mypageService.increase(logId);

        List<ReviewDTO> result_reviewdetail = mypageService.select_detail(logId);

        Boolean tf_IsOwner = false;

        if (!result_reviewdetail.isEmpty()) {

            tf_IsOwner = result_reviewdetail.get(0).getBuyer().equals(userId);
        }

        ModelAndView mav = new ModelAndView("review_detail");

        mav.addObject("log_id = " + log_id);
        mav.addObject("result_reviewdetail", result_reviewdetail);
        mav.addObject("tf_IsOwner", tf_IsOwner);
        mav.addObject("userId", userId);
        mav.addObject("user_session", userSession);

        return mav;
    }

    @PostMapping("/update/car")
    public ResponseEntity<Map<String, String>> update_c(@RequestBody Map<String, Object> request){

        int price = Integer.parseInt(request.get("key").toString());

        String price2 = (request.get("key2").toString());

        int reg_id = Integer.parseInt(request.get("key3").toString());

        mypageService.change_cart_status(reg_id);

        mypageService.change_translog_status(reg_id);

        mypageService.update_price(price, price2, reg_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/car")
    public ResponseEntity<Map<String, String>> delete_c(@RequestBody Map<String, Object> request){

        int reg_id = Integer.parseInt(request.get("key").toString());

       mypageService.change_car_existed(reg_id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/mypage_cart_input")
    @ResponseBody
    public ResponseEntity<String> mypage_cart_input(@RequestParam(name = "car_registration_id") int car_registration_id, HttpSession session) throws Exception {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        if (user_session == null) {
            return new ResponseEntity<>("session_empty", HttpStatus.UNAUTHORIZED);
        }
        System.out.println(car_registration_id + "mypage_cart");
        List<CartDTO> checkEmpty = mypageService.checkEmpty(user_session.getUser_id(), car_registration_id);
        if (checkEmpty.isEmpty()) {
            mypageService.cart_insert(user_session.getUser_id(), car_registration_id);
            return new ResponseEntity<>("added", HttpStatus.OK);
        } else {
            mypageService.cart_delete(user_session.getUser_id(), car_registration_id);
            return new ResponseEntity<>("removed", HttpStatus.OK);
        }

    }

    @GetMapping("/inquiry_list")
    public String listUserInquiries(Model model, HttpSession session, @RequestParam(defaultValue = "1") int page) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        if (user_session == null) {
            return "redirect:/login";
        }

        String userId = user_session.getUser_id();
        // 세션 아이디 존재 여부 확인 로그
        System.out.println("listUserInquiries - userId = " + userId);

        model.addAttribute("user_Id", userId);

        // 문의 리스트 페이징 처리
        int pageSize = 6;
        int offset = (page - 1) * pageSize;
        int totalInquiries = inquiryService.getTotalInquiries(userId);

        List<InquiryDTO> resultInquiryList = inquiryService.getPagedInquiries(userId, offset, pageSize);


        int totalPages = (int) Math.ceil((double) totalInquiries / pageSize);

        model.addAttribute("resultInquiryList", resultInquiryList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        if (resultInquiryList.isEmpty()) {
            model.addAttribute("message", "데이터가 없습니다.");
        }

        return "inquiry_list";
    }

    // 문의 글쓰기 화면
    @GetMapping("/inquiry_write")
    public String showInquiryWritePage(Model model, HttpSession session) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        String userId = user_session.getUser_id();

        // 세션 아이디 존재 여부 확인 로그
        System.out.println("listUserInquiries - userId = " + userId);

        model.addAttribute("user_Id", userId);
        return "inquiry_write";
    }

    // 문의글 등록
    @PostMapping("/submit_inquiry")
    public ResponseEntity<String> createInquiry(@RequestParam(name = "inquiry_title", required = false) String inquiry_title,
                                                @RequestParam(name = "inquiry_content") String inquiry_content,
                                                HttpSession session) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        String userId = user_session.getUser_id();

        // 세션 아이디 존재 여부 확인 로그
        System.out.println("createInquiry - userId = " + userId);

        if (userId == null) {
            // 세션에 user_id가 없는 경우 처리
            return new ResponseEntity<>("redirect", HttpStatus.OK); // 예: 로그인 페이지로 리디렉션
        }

        InquiryDTO inquiryDTO = new InquiryDTO();
        inquiryDTO.setUser_id(userId);
        inquiryDTO.setInquiry_title(inquiry_title);
        inquiryDTO.setInquiry_content(inquiry_content);
        inquiryDTO.setInquiry_status("N");
        System.out.println("createInquiry - inquiryDTO = " + inquiryDTO);

        try {

            inquiryService.insertInquiry(inquiryDTO);
            return new ResponseEntity<>("submit", HttpStatus.OK);
        } catch (Exception e) {
            // 로그를 추가하여 에러 원인 파악
            e.printStackTrace();

            // 에러 발생 시 응답
            return new ResponseEntity<>("error", HttpStatus.OK);
        }
    }



    // 문의글 상세화면
    @GetMapping("/inquiry_detail")
    public String getInquiryDetail(@RequestParam("inquiry_id") int inquiry_id, Model model,HttpSession session) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        String userId = user_session.getUser_id();

        // 세션 아이디 존재 여부 확인 로그
        System.out.println("getInquiryDetail - userId = " + userId);

        model.addAttribute("user_session", user_session);

        InquiryDTO inquiry = inquiryService.getInquiryById(inquiry_id);
        model.addAttribute("inquiry", inquiry);
        return "inquiry_detail";
    }


    // 문의글 수정
    @PostMapping("/inquiries/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateInquiry(@RequestBody Map<String, Object> request){


        int inquiry_id = Integer.parseInt(request.get("inquiry_id").toString());
        String inquiry_title = request.get("inquiry_title").toString();
        String inquiry_content = request.get("inquiry_content").toString();

        inquiryService.updateInquiry(inquiry_id, inquiry_title, inquiry_content);

        Map<String, String> response = new HashMap<>();

        response.put("message", "success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 문의 삭제

    @PostMapping("/inquiries/delete")
    public ResponseEntity<String> deleteInquiry(@RequestParam("password") String password,
                                                @RequestParam("inquiry_id") int inquiry_id,
                                                HttpSession session) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");

        System.out.println("deleteInquiry ---- user_session = " + user_session);

        if (user_session != null) {
            String storedPassword = userService.getUserPassword(user_session.getUser_id());
            if (passwordEncoder.matches(password, storedPassword)) {
                System.out.println("비밀번호 일치. 삭제 진행");

                // 삭제
                inquiryService.deleteInquiry(inquiry_id);
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.status(403).body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return ResponseEntity.status(403).body("User is not logged in.");
        }
    }
}
