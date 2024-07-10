package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.DeptDTO;


import java.util.List;

@Mapper
public interface CommonDAO {

    // 전체 조회
    @Select
            ("SELECT * FROM DEPT")
    List<DeptDTO> selectAll();

    // 일부 조회
    // 동적으로 변수를 정해줘야하는 경우 mapper 와 마찬가지로 # 을 사용함
    // 이때 변수는 매개변수로 받아오는 변수가 곧 SQL 문에 들어갈 변수가 됨

    @Select
            ("SELECT * FROM DEPT WHERE dname like '%'||#{dname}||'%'")
    List<DeptDTO> selectOne(@Param("dname") String dname);

    // Insert : 마찬가지로 동적바인딩이 필요한 경우 # 을 사용
    // 여기에 SQL 에서 사용하는 Auto Increment == 오라클은 sequence 도 그대로 사용 가능하다
    // 추가로 만약 AI 값을 확인하고 싶다면
    // @Options 어노테이션을 과 useGeneratedKeys, keyproperty 를 사용하면 int 값을 반환 가능하다
    // useGeneratedKeys : 자동 키생성 여부?
    // keyproperty : PK가(보통 PK니까) 어떤 컬럼인지 컬럼명 작성

    @Insert
            ("INSERT INTO DEPT VALUES(DEPT_DEPTNO_SEQ, ${dname}, ${loc}")
    @Options
            (useGeneratedKeys = true, keyProperty = "deptno")
    int insertOne();

}
