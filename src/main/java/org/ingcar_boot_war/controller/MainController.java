package org.ingcar_boot_war.controller;

import org.ingcar_boot_war.dao.CommonDAO;
import org.ingcar_boot_war.dto.DeptDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
@Controller
public class MainController {

 //   @Autowired
    //private DeptService service; // 서비스 클래스 di

   // @RequestMapping("/list") // list 로 요청시
/*    public String list(Model model) {
        // System.out.println("여기");
        // getAll 메서드 실행 후 내용 담아서 list 파일로 전달
        // getAll 은 DB의 내용을 가져오는 메서드
        model.addAttribute("list", service.getAll());

//        return "list";
       // return service.getAll();*/

    @Autowired
    private CommonDAO commonDAO;

    @GetMapping("/list")
    public String getAllObjects(Model model) {
        List<DeptDTO> resultList = commonDAO.selectAll();
        model.addAttribute("resultdata", resultList);
        System.out.println("이야이야호 " + resultList.toString());
        return "list"; // list.jsp 또는 list.html 뷰를 반환
    }
}

    // search 로 요청이 왓을때 처리
    // requestParam 은 요청 파라미터를 의미하며, defaultvalue 는 기본 요청 파라미터, 즉 요청 파라미터가 null 값일때 대신해줄 값
/*    @PostMapping("/search")
    public String search(@RequestParam(value = "word", defaultValue = "all") String word, Model model) {
        // 요청 파라미터의 기본값은 all
        if(word.equals("all")) {
            model.addAttribute("list", commonDAO.getAll());
        }else {
            // 검색 단어가 들어왔을 땐 해당 단어로 DB 에서 like sql 문 사용후
            // 내용을 담아서 전달
            //System.out.println(service.getOne(word));
            model.addAttribute("list", commonDAO.getOne(word));
        }
        return "list";
            }
        */




