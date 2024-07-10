package org.ingcar_boot_war.service;


import org.ingcar_boot_war.dao.InquiryDAO;
import org.ingcar_boot_war.dto.InquiryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryService {

    @Autowired
    private InquiryDAO inquiryDAO;

    // 문의 글 등록 시
    public void insertInquiry(InquiryDTO inquiryDTO) {
        inquiryDAO.insertInquiry(inquiryDTO);
    }


    public int getTotalInquiries(String user_id) {
        return inquiryDAO.countAll(user_id);
    }


    public List<InquiryDTO> getPagedInquiries(String user_id, int offset, int pageSize) {
        return inquiryDAO.selectPaged(user_id, offset, pageSize);
    }

    public InquiryDTO getInquiryById(int inquiry_id) {
        return inquiryDAO.findById(inquiry_id);
    }

    // 문의 수정
    public void updateInquiry(int inquiry_id, String inquiry_title, String inquiry_content) {
        inquiryDAO.updateInquiry(inquiry_id, inquiry_title, inquiry_content);
    }

    // 문의 삭제
    public void deleteInquiry(int inquiry_id) {
        inquiryDAO.deleteInquiryById(inquiry_id);
    }

}

