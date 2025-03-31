package com.shanzhu.em.controller;

import cn.hutool.core.date.DateUtil;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Cart;
import com.shanzhu.em.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping("/{id}")
    public R<Cart> findUserCart(@PathVariable Long id) {
        return R.success(cartService.getById(id));
    }

    @GetMapping
    public R<Cart> findAllUserCart() {
        return R.success(cartService.list());
    }


    @GetMapping("/userid/{userId}")
    public R<List<Cart>> selectByUserId(@PathVariable Long userId) {
        return R.success(cartService.selectByUserId(userId));
    }


    @PostMapping
    public R<Void> save(@RequestBody Cart cart) {
        cart.setCreateTime(DateUtil.now());
        cartService.save(cart);
        return R.success();
    }


    @PutMapping
    public R<Void> update(@RequestBody Cart cart) {
        cartService.updateById(cart);
        return R.success();
    }


    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        cartService.removeById(id);
        return R.success();
    }

}
