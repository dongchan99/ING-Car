package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.CommonDAO;
import org.ingcar_boot_war.dto.DeptDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {

    // DAO 를 DI
    // 다만 CommonDAO 는 이름에서 느낌이 가듯이 interface 이다.
    // 왜 인터페이스인지는 해당 코드를 보면 알 수 있을 듯

    @Autowired
    CommonDAO dao;

    // dao 의 selectAll 메서드 실행
    public List<DeptDTO> getAll() {

        return dao.selectAll();
    }

    // 넘겨받은 dname 파라미터로 selectOne 메서드 실행
    public List<DeptDTO> getOne(String dname) {
        return dao.selectOne(dname);
    }
}
