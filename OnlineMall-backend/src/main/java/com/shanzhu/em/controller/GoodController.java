package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Good;
import com.shanzhu.em.entity.Standard;
import com.shanzhu.em.entity.vo.GoodVo;
import com.shanzhu.em.service.GoodService;
import com.shanzhu.em.service.StandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/good")
@RequiredArgsConstructor
public class GoodController {

    private final GoodService goodService;

    private final StandardService standardService;

    @GetMapping("/{id}")
    public R<Good> findGood(@PathVariable Long id) {
        return R.success(goodService.getGoodById(id));
    }


    @GetMapping("/standard/{id}")
    public R<String> getStandard(@PathVariable Integer id) {
        return R.success(goodService.getStandard(id));
    }


    @GetMapping("/all")
    public R<GoodVo> findFrontGoods(@RequestParam(required = false, value = "userId") Long userId) {
        return R.success(goodService.findFrontGoods(userId));
    }


    @GetMapping("/rank")
    public R<List<Good>> getSaleRank(@RequestParam int num) {
        return R.success(goodService.getSaleRank(num));
    }


    @PostMapping
    public R<Long> save(@RequestBody Good good) {
        return R.success(goodService.saveOrUpdateGood(good));
    }


    @PutMapping
    public R<Void> update(@RequestBody Good good) {
        goodService.update(good);
        return R.success();
    }


    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        goodService.loginDeleteGood(id);
        return R.success();
    }


    @PostMapping("/standard")
    public R<Void> saveStandard(
            @RequestBody List<Standard> standards,
            @RequestParam int goodId
    ) {
        standardService.deleteAll(goodId);

        for (Standard standard : standards) {
            standard.setGoodId(goodId);
            if (!standardService.save(standard)) {
                return R.error(Status.CODE_500, "Product id: " + goodId + " ,Failed to save specification");
            }
        }

        return R.success();
    }

    @DeleteMapping("/standard")
    public R<Void> delStandard(@RequestBody Standard standard) {
        if (BooleanUtil.isTrue(standardService.delete(standard))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "Deletion failure");
        }
    }

    @GetMapping("/recommend")
    public R<Void> setRecommend(
            @RequestParam Long id,
            @RequestParam Boolean isRecommend
    ) {
        return R.success(goodService.setRecommend(id, isRecommend));
    }


    @GetMapping("/page")
    public R<IPage<GoodVo>> findGoodPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(required = false) Integer categoryId
    ) {
        return R.success(
                goodService.findPage(pageNum, pageSize, searchText, categoryId)
        );
    }


    @GetMapping("/fullPage")
    public R<IPage<GoodVo>> findGoodFullPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(required = false) Integer categoryId) {

        return R.success(goodService.findFullPage(pageNum, pageSize, searchText, categoryId));
    }

}
