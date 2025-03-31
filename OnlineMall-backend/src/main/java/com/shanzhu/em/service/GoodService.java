package com.shanzhu.em.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.constants.RedisConstants;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Good;
import com.shanzhu.em.entity.GoodStandard;
import com.shanzhu.em.entity.vo.GoodVo;
import com.shanzhu.em.exception.BizException;
import com.shanzhu.em.mapper.GoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class GoodService extends ServiceImpl<GoodMapper, Good> {

    @Value("${useRecommend}")
    private Boolean userRecommend;

    @Resource
    private RecommendService recommendService;

    @Resource
    private GoodMapper goodMapper;

    @Resource
    private RedisTemplate<String, Good> redisTemplate;


    public Boolean existGood(Long goodId) {
        Good good = this.getById(goodId);
        return BooleanUtil.isTrue(good != null);
    }


    public Good getGoodById(Long id) {
        String redisKey = RedisConstants.GOOD_ID_KEY + id;

        Good redisGood = redisTemplate.opsForValue().get(redisKey);
        if (redisGood != null) {
            redisTemplate.expire(redisKey, RedisConstants.GOOD_ID_TTL, TimeUnit.MINUTES);
            return redisGood;
        }

        Good dbGood = lambdaQuery().eq(Good::getIsDelete, Boolean.FALSE).eq(Good::getId, id).one();

        if (dbGood != null) {
            redisTemplate.opsForValue().set(redisKey, dbGood, RedisConstants.GOOD_ID_TTL, TimeUnit.MINUTES);
            return dbGood;
        }

        throw new BizException(Status.NO_RESULT, "No result");

    }


    public String getStandard(Integer id) {
        List<GoodStandard> standards = goodMapper.getStandardById(id);
        if (standards.size() == 0) {
            throw new BizException(Status.NO_RESULT, "No result");
        }
        return JSON.toJSONString(standards);
    }

    public BigDecimal getMinPrice(Long id) {
        return goodMapper.getMinPrice(id);
    }



    public List<GoodVo> findFrontGoods(Long userId) {
        if (userId != null && userId != 0 && BooleanUtil.isTrue(userRecommend)) {
            List<GoodVo> goodVoList = recommendService.recommendGoods(userId).stream().map(
                    good -> {
                        GoodVo goodVo = new GoodVo();
                        BeanUtil.copyProperties(good, goodVo);
                        return goodVo;
                    }
            ).collect(Collectors.toList());

            if (CollUtil.isEmpty(goodVoList)) {
                return goodMapper.findFrontGoods();
            } else {
                return goodVoList;
            }

        } else {
            return goodMapper.findFrontGoods();
        }
    }


    public Long saveOrUpdateGood(Good good) {
        if (good.getId() == null) {
            goodMapper.insertGood(good);
        } else {
            this.updateById(good);
            redisTemplate.delete(RedisConstants.GOOD_ID_KEY + good.getId());
        }
        return good.getId();
    }


    public void loginDeleteGood(Long id) {
        redisTemplate.delete(RedisConstants.GOOD_ID_KEY + id);
        this.update(Wrappers.<Good>update().set("is_delete", Boolean.TRUE).eq("id", id));
    }


    public boolean setRecommend(Long id, Boolean isRecommend) {
        LambdaUpdateWrapper<Good> goodsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        goodsLambdaUpdateWrapper.eq(Good::getId, id).set(Good::getRecommend, isRecommend);
        return update(goodsLambdaUpdateWrapper);
    }


    public List<Good> getSaleRank(int num) {
        return goodMapper.getSaleRank(num);
    }


    public void update(Good good) {
        this.updateById(good);
        redisTemplate.delete(RedisConstants.GOOD_ID_KEY + good.getId());
    }


    public IPage<GoodVo> findPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Good> query = Wrappers.<Good>lambdaQuery()
                .like(StrUtil.isNotBlank(searchText), Good::getName, searchText).or()
                .like(StrUtil.isNotBlank(searchText), Good::getDescription, searchText).or()
                .eq(StrUtil.isNotBlank(searchText), Good::getId, searchText)
                .eq(categoryId != null, Good::getCategoryId, categoryId)
                .eq(Good::getIsDelete, Boolean.FALSE)
                .orderByDesc(Good::getId);

        IPage<Good> page = this.page(new Page<>(pageNum, pageSize), query);
        IPage<GoodVo> goodVoPage = page.convert(good -> {
            GoodVo goodVo = new GoodVo();
            BeanUtil.copyProperties(good, goodVo);
            return goodVo;
        });

        for (GoodVo good : goodVoPage.getRecords()) {
            good.setPrice(getMinPrice(good.getId()));
        }

        return goodVoPage;
    }

    public IPage<Good> findFullPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Good> query = Wrappers.<Good>lambdaQuery()
                .like(StrUtil.isNotBlank(searchText), Good::getName, searchText).or()
                .like(StrUtil.isNotBlank(searchText), Good::getDescription, searchText).or()
                .eq(StrUtil.isNotBlank(searchText), Good::getId, searchText)
                .eq(categoryId != null, Good::getCategoryId, categoryId)
                .eq(Good::getIsDelete, Boolean.FALSE);

        IPage<Good> page = this.page(new Page<>(pageNum, pageSize), query);
        for (Good good : page.getRecords()) {
            good.setPrice(getMinPrice(good.getId()));
        }

        return page;
    }
}
