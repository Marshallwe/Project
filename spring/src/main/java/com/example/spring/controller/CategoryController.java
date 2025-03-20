package com.example.spring.controller;

import com.example.spring.common.R;
import com.example.spring.entity.Category;
import com.example.spring.service.CategoryService;
import com.example.spring.utils.InterfaceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/{id}")
    public R<Category> findCategory(@PathVariable Long id) {
        return R.success(categoryService.getById(id));
    }


    @GetMapping
    public R<List<Category>> findAllCategory() {
        return R.success(categoryService.list());
    }


    @PostMapping
    public R<Void> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success();
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Category category) {
        categoryService.add(category);
        return InterfaceWrapper.success();
    }


    @PutMapping
    public R<Void> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success();
    }

    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Long id) {
        return categoryService.delete(id);
    }

}
