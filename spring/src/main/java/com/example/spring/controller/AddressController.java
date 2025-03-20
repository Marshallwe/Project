package com.example.spring.controller;

import com.example.spring.common.R;
import com.example.spring.entity.Address;
import com.example.spring.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @GetMapping("/{userId}")
    public R<Address> findUserAddress(@PathVariable Long userId) {
        return R.success(addressService.selectById(userId));
    }


    @GetMapping
    public R<List<Address>> findAllUserAddress() {
        return R.success(addressService.list());
    }


    @PostMapping
    public R<Void> save(@RequestBody Address address) {
        addressService.save(address);
        return R.success();
    }


    @PutMapping
    public R<Void> update(@RequestBody Address address) {
        addressService.updateById(address);
        return R.success();
    }


    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        addressService.removeById(id);
        return R.success();
    }

}
