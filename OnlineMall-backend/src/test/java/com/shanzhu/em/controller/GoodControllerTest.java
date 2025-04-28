package com.shanzhu.em.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Good;
import com.shanzhu.em.entity.Standard;
import com.shanzhu.em.entity.vo.GoodVo;
import com.shanzhu.em.service.GoodService;
import com.shanzhu.em.service.StandardService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SuppressWarnings("unchecked")
class GoodControllerTest {

    @Mock
    private GoodService goodService;

    @Mock
    private StandardService standardService;

    @InjectMocks
    private GoodController goodController;

    @Test
    void save_ValidGood_ReturnsId() {
        Good testGood = new Good();
        Long expectedId = 100L;

        when(goodService.saveOrUpdateGood(testGood)).thenReturn(expectedId);

        R<Long> result = goodController.save(testGood);

        assertEquals(expectedId, result.getData());
        verify(goodService).saveOrUpdateGood(testGood);
    }

    @Test
    void findGoodPage_EdgeValues_HandlesCorrectly() {
        IPage<GoodVo> mockPage = mock(IPage.class);
        when(mockPage.getSize()).thenReturn(1000L);

        when(goodService.findPage(0, 1000, "", null))
                .thenReturn(mockPage);

        R<IPage<GoodVo>> result = goodController.findGoodPage(0, 1000, "", null);

        assertEquals(1000L, result.getData().getSize());
    }


}