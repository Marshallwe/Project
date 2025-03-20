package com.example.spring.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring.entity.Good;
import com.example.spring.entity.GoodStandard;
import com.example.spring.entity.vo.GoodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface GoodMapper extends BaseMapper<Good> {

    @Select("select * from good_standard where good_id = #{id}")
    List<GoodStandard> getStandardById(Integer id);

    List<GoodVo> findFrontGoods();

    void insertGood(@Param("good") Good good);

    @Select("SELECT discount * MIN(price) FROM good_standard gs, good WHERE good.id = gs.good_id AND good.id = #{id} ")
    BigDecimal getMinPrice(Long id);

    boolean saleGood(@Param("id")Long goodId,@Param("count") int count,@Param("money") BigDecimal totalPrice);

    @Select("SELECT * FROM `good` ORDER BY sale_money DESC LIMIT 0,#{num}")
    List<Good> getSaleRank(int num);
}
