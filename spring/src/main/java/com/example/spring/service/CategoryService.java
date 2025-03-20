package com.example.spring.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.Category;
import com.example.spring.entity.IconCategory;
import com.example.spring.mapper.CategoryMapper;
import com.example.spring.mapper.IconCategoryMapper;
import com.example.spring.utils.InterfaceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    private final IconCategoryMapper iconCategoryMapper;


    public void add(Category category) {
        save(category);

        IconCategory iconCategory = new IconCategory();
        iconCategory.setCategoryId(category.getId());
        iconCategory.setIconId(category.getIconId());
        iconCategoryMapper.insert(iconCategory);
    }


    public Map<String, Object> delete(Long id) {
        iconCategoryMapper.delete(
                Wrappers.<IconCategory>lambdaQuery().eq(IconCategory::getCategoryId, id)
        );

        removeById(id);
        return InterfaceWrapper.success();
    }
}
