package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.InformDAO;
import org.ingcar_boot_war.dto.InformDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InformService {

    @Autowired
    private final InformDAO informDAO;



    public InformService(InformDAO informDAO) {
        this.informDAO = informDAO;
    }

    public List<InformDTO> findAllPaged(int page, int pageSize) {
        return informDAO.findAllPaged(page, pageSize);
    }

    public int getTotalCount() {
        return informDAO.getTotalCount();
    }

    public List<InformDTO> searchByPostTitle(String keyword) {
        return informDAO.searchByPostTitle(keyword);
    }

    public List<InformDTO> searchByPostContent(String keyword) {
        return informDAO.searchByPostContent(keyword);
    }

    public List<InformDTO> searchByTitleOrContent(String titleKeyword, String contentKeyword) {
        return informDAO.searchByTitleOrContent(titleKeyword, contentKeyword);
    }
}