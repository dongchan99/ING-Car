package org.ingcar_boot_war.dao;

import org.ingcar_boot_war.dto.CarListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CarListDAO {

    @Select("<script>" +
            "WITH LatestTransLog AS (" +
            "    SELECT t.*, ROW_NUMBER() OVER (PARTITION BY t.car_registration_id ORDER BY t.log_date DESC) AS rn " +
            "    FROM transLog t" +
            ")" +
            "SELECT * FROM (" +
            "    SELECT a.*, ROWNUM rnum FROM (" +
            "        SELECT c.car_registration_id, c.car_id, c.car_year, c.car_mileage, c.car_brand, c.car_model, c.car_color," +
            "               c.car_fuel_type, c.car_price, c.car_accident_history, c.car_description, c.car_registration_date, c.car_price2, c.car_existed" +
            "        FROM Car c" +
            "        LEFT JOIN LatestTransLog t ON c.car_registration_id = t.car_registration_id AND t.rn = 1" +
            "        WHERE c.car_existed = 'Y'" +
            "        AND (t.current_translog != 1 OR t.current_translog IS NULL)" +
            "        <if test='carModel != null and carModel != \"\"'> AND LOWER(c.car_model) LIKE '%' || LOWER(#{carModel}) || '%'</if>" +
            "        <if test='brands != null and !brands.isEmpty()'> AND c.car_brand IN" +
            "            <foreach item='brand' index='index' collection='brands' open='(' separator=',' close=')'>" +
            "                #{brand}" +
            "            </foreach>" +
            "        </if>" +
            "        <if test='minPrice != null'> AND c.car_price &gt;= #{minPrice}</if>" +
            "        <if test='maxPrice != null'> AND c.car_price &lt;= #{maxPrice}</if>" +
            "        <if test='minMileage != null'> AND c.car_mileage &gt;= #{minMileage}</if>" +
            "        <if test='maxMileage != null'> AND c.car_mileage &lt;= #{maxMileage}</if>" +
            "        <if test='fuels != null and !fuels.isEmpty()'> AND c.car_fuel_type IN" +
            "            <foreach item='fuel' index='index' collection='fuels' open='(' separator=',' close=')'>" +
            "                #{fuel}" +
            "            </foreach>" +
            "        </if>" +
            "        <if test='accident != null and accident != \"\" and accident != \"전체\"'> AND c.car_accident_history = #{accident}</if>" +
            "        <if test='colors != null and !colors.isEmpty()'> AND c.car_color IN" +
            "            <foreach item='color' index='index' collection='colors' open='(' separator=',' close=')'>" +
            "                #{color}" +
            "            </foreach>" +
            "        </if>" +
            "        ORDER BY c.car_registration_date DESC" +
            "    ) a WHERE ROWNUM &lt;= #{offset} + #{limit}" +
            ") WHERE rnum &gt; #{offset}" +
            "</script>")
    List<CarListDTO> findAllCars(@Param("limit") int limit, @Param("offset") int offset,
                                 @Param("carModel") String carModel, @Param("brands") List<String> brands,
                                 @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice,
                                 @Param("minMileage") Integer minMileage, @Param("maxMileage") Integer maxMileage,
                                 @Param("fuels") List<String> fuels, @Param("accident") String accident,
                                 @Param("colors") List<String> colors);



    @Select("SELECT COUNT(*) FROM Car c LEFT JOIN transLog t ON c.car_registration_id = t.car_registration_id WHERE t.current_translog != 1 OR t.current_translog IS NULL")
    int countAllCars();
}