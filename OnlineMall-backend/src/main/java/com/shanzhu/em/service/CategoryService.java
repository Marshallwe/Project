package com.shanzhu.em.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.entity.Category;
import com.shanzhu.em.entity.IconCategory;
import com.shanzhu.em.mapper.CategoryMapper;
import com.shanzhu.em.mapper.IconCategoryMapper;
import com.shanzhu.em.utils.InterfaceWrapper;
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
