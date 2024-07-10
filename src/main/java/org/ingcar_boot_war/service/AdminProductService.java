package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.TransLogCarDAO;
import org.ingcar_boot_war.dto.TransLogCarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProductService {

    @Autowired
    private TransLogCarDAO transLogCarDAO;

    // 공용
    private final int pageSize = 15;  // 페이지당 게시물 수

    // 공용
    public int offset(int page){

        return (page - 1) * pageSize;
    }


    public int product_totalPages(){

        int totalReviews = transLogCarDAO.product_getAllCar_count(); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<TransLogCarDTO> product_getAllCar(int offset){

        return transLogCarDAO.product_getAllCar(offset, pageSize);
    }

    //검색
    public List<TransLogCarDTO> searchByUser_id(String keyword, int offset){

        return transLogCarDAO.searchByUser_id(keyword, offset, pageSize);
    }

    public List<TransLogCarDTO> searchByCar_brand(String keyword, int offset){

        return transLogCarDAO.searchByCar_brand(keyword, offset, pageSize);
    }

    public List<TransLogCarDTO> searchByCar_model(String keyword, int offset){

        return transLogCarDAO.searchByCar_model(keyword, offset, pageSize);
    }
    //
    public int producting_totalPages(){

        int totalReviews = transLogCarDAO.producting_getAllCar_count(); // 총 레코드 개수
        return (int) Math.ceil((double) totalReviews / pageSize);
    }

    public List<TransLogCarDTO> producting_getAllCar(int offset){

        return transLogCarDAO.producting_getAllCar(offset, pageSize);
    }

    public List<TransLogCarDTO> productingSearchByUser_id(String keyword, int offset){

        return transLogCarDAO.productingSearchByUser_id(keyword, offset, pageSize);
    }

    public List<TransLogCarDTO> productingSearchByCar_brand(String keyword, int offset){

        return transLogCarDAO.productingSearchByCar_brand(keyword, offset, pageSize);
    }

    public List<TransLogCarDTO> productingSearchByCar_model(String keyword, int offset){

        return transLogCarDAO.productingSearchByCar_model(keyword, offset, pageSize);
    }


}
