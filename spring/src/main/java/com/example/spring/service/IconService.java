package com.example.spring.service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.Icon;
import com.example.spring.entity.IconCategory;
import com.example.spring.mapper.IconCategoryMapper;
import com.example.spring.mapper.IconMapper;
import com.example.spring.utils.InterfaceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class IconService extends ServiceImpl<IconMapper, Icon> {

    private final IconCategoryMapper iconCategoryMapper;

    private final IconMapper iconMapper;


    public List<Icon> getIconCategoryMapList() {
        return iconMapper.getIconCategoryMapList();
    }


    public Map<String, Object> deleteById(Long id) {
        Long count = iconCategoryMapper.selectCount(
                Wrappers.<IconCategory>lambdaQuery()
                        .eq(IconCategory::getIconId, id));

        if (count > 0) {
            return InterfaceWrapper.error("This superior category has subordinate categories, please delete all subordinate categories and try to delete");
        }

        this.removeById(id);

        return InterfaceWrapper.success();
    }

}