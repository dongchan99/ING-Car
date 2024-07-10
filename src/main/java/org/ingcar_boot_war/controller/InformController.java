/*
    공지사항 InformController.java
*/

package org.ingcar_boot_war.controller;

import org.ingcar_boot_war.dao.InformDAO;
import org.ingcar_boot_war.dto.InformDTO;
import org.ingcar_boot_war.service.CommentService;
import org.ingcar_boot_war.service.InformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;



@Controller
public class InformController {

    @Autowired
    private InformService informService;


    @GetMapping("/inform")
    public String getAllInform(Model model,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) String searchKeyword,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "pageSize", defaultValue = "6") int pageSize) {

        int maxDisplayPages = 10; // 최대 페이지 수 설정
        int totalInforms = informService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalInforms / pageSize);

        // 페이지 범위 제한.
        page = Math.min(page, totalPages);
        page = Math.max(page, 1);

        List<InformDTO> informs;

        if (type != null && searchKeyword != null) {
            if (type.equals("post_title")) {
                informs = informService.searchByPostTitle(searchKeyword);
            } else if (type.equals("post_content")) {
                informs = informService.searchByPostContent(searchKeyword);
            } else {
                // 사용자가 옵션을 선택하지 않은 경우
                informs = informService.searchByTitleOrContent(searchKeyword, searchKeyword);
            }
        } else {
            informs = informService.findAllPaged(page, pageSize);
        }
        if (informs.isEmpty()) {
            System.out.println("No informs found.");
            model.addAttribute("message", "원하는 내용의 공지사항이 없습니다.");
        }
        model.addAttribute("informs", informs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("maxDisplayPages", maxDisplayPages); // 최대 페이지 수 추가

        return "inform";
    }
}