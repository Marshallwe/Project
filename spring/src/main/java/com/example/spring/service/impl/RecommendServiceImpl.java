package com.example.spring.service.impl;

import com.google.common.collect.Lists;
import com.example.spring.entity.Good;
import com.example.spring.entity.Message;
import com.example.spring.mapper.MessageMapper;
import com.example.spring.service.GoodService;
import com.example.spring.service.MessageService;
import com.example.spring.service.RecommendService;
import com.example.spring.utils.recommend.CoreMath;
import com.example.spring.utils.recommend.dto.ProductDTO;
import com.example.spring.utils.recommend.dto.RelateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RecommendServiceImpl implements RecommendService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private GoodService goodService;

    @Override
    public List<Good> recommendGoods(Long userId) {
        CoreMath coreMath = new CoreMath();
        List<RelateDTO> relateDTOList = generateRelates();
        List<Long> recommendations = coreMath.recommend(userId, relateDTOList);
        List<ProductDTO> productDTOList = generateProducts().stream()
                .filter(e -> recommendations.contains(e.getProductId())).collect(Collectors.toList());

        List<Good> goods = new ArrayList<>();
        List<Long> productIdList = productDTOList.stream().map(e -> e.getProductId()).collect(Collectors.toList());
        for (Long productId : productIdList) {
            goods.add(goodService.getGoodById(productId));
        }
        return goods;
    }


    private List<RelateDTO> generateRelates() {
        List<Message> messages = messageMapper.findAllMessage();
        List<RelateDTO> relateDTOList = Lists.newArrayList();
        for (Message message : messages) {
            RelateDTO relateDTO = new RelateDTO();
            relateDTO.setUserId(message.getUserId());
            relateDTO.setProductId(message.getGoodId());
            relateDTO.setIndex(message.getScore());
            relateDTOList.add(relateDTO);
        }
        if (CollectionUtils.isEmpty(relateDTOList)) {
            log.info("--------------------List<RelateDTO>Is empty!");
        }

        return relateDTOList;
    }

    private List<ProductDTO> generateProducts() {
        List<ProductDTO> productDTOList = new ArrayList<>();
        List<Good> goods = goodService.list();
        for (Good good : goods) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(good.getId());
            productDTO.setProductPrice(good.getSaleMoney().toString());
            productDTOList.add(productDTO);
        }
        if (CollectionUtils.isEmpty(productDTOList)) {
            log.info("----------------------List<ProductDTO>Is empty!");
        }
        return productDTOList;
    }

}
