package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.IndexDAO;
import org.ingcar_boot_war.dto.CarDTO;
import org.ingcar_boot_war.dto.ReviewDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IndexService {

    private final IndexDAO indexDAO;

    public IndexService(IndexDAO indexDAO) {
        this.indexDAO = indexDAO;
    }

    public List<CarDTO> getIndexCarList() {
        return indexDAO.getIndexCarList();
    }

    public List<ReviewDTO> getIndexReviewList() {
        return indexDAO.getIndexReviewList();
    }
}
