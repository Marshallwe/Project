package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Carousel;
import com.shanzhu.em.service.CarouselService;
import com.shanzhu.em.service.GoodService;
import com.shanzhu.em.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/api/carousel")
@RequiredArgsConstructor
public class CarouselController {

    private final HttpServletRequest request;

    private final CarouselService carouselService;

    private final UserService userService;

    private final GoodService goodService;


    @GetMapping("/{id}")
    public R<Carousel> findCarousel(@PathVariable Long id) {
        return R.success(carouselService.getById(id));
    }


    @GetMapping
    public R<List<Carousel>> findAllCarousel() {
        return R.success(carouselService.findAllCarousel());
    }


    @PostMapping
    public R<Void> save(@RequestBody Carousel carousel) {
        if (BooleanUtil.isFalse(goodService.existGood(carousel.getGoodId()))) {
            return R.error("400", "No products found id = " + carousel.getGoodId());
        }

        carouselService.saveOrUpdate(carousel);
        return R.success();
    }


    @PutMapping
    public R<Void> update(@RequestBody Carousel carousel) {
        if (BooleanUtil.isFalse(goodService.existGood(carousel.getGoodId()))) {
            return R.error("400", "No products found id = " + carousel.getGoodId());
        }

        carouselService.updateById(carousel);
        return R.success();
    }


    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        carouselService.removeById(id);
        return R.success();
    }

}
