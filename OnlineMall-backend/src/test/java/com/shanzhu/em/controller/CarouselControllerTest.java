package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Carousel;
import com.shanzhu.em.service.CarouselService;
import com.shanzhu.em.service.GoodService;
import com.shanzhu.em.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarouselControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarouselService carouselService;

    @Mock
    private UserService userService;

    @Mock
    private GoodService goodService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CarouselController carouselController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(carouselController).build();
    }

    @Test
    void findCarousel_WithExistingId_ShouldReturnCarousel() throws Exception {
        Carousel mockCarousel = new Carousel();
        mockCarousel.setId(1L);
        mockCarousel.setGoodId(100L);

        when(carouselService.getById(1L)).thenReturn(mockCarousel);

        mockMvc.perform(get("/api/carousel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.goodId").value(100));
    }

    @Test
    void findCarousel_WithNonExistingId_ShouldReturnEmpty() throws Exception {
        when(carouselService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/carousel/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void findAllCarousel_ShouldReturnList() throws Exception {
        Carousel carousel1 = new Carousel();
        carousel1.setId(1L);
        Carousel carousel2 = new Carousel();
        carousel2.setId(2L);

        when(carouselService.findAllCarousel()).thenReturn(Arrays.asList(carousel1, carousel2));

        mockMvc.perform(get("/api/carousel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }

    @Test
    void findAllCarousel_EmptyResult_ShouldReturnEmptyList() throws Exception {
        when(carouselService.findAllCarousel()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/carousel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void save_WithValidGoodId_ShouldSuccess() throws Exception {
        Carousel carousel = new Carousel();
        carousel.setGoodId(200L);

        when(goodService.existGood(200L)).thenReturn(true);

        mockMvc.perform(post("/api/carousel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"goodId\": 200 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(carouselService).saveOrUpdate(any(Carousel.class));
    }

    @Test
    void save_WithInvalidGoodId_ShouldReturnError() throws Exception {
        when(goodService.existGood(300L)).thenReturn(false);

        mockMvc.perform(post("/api/carousel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"goodId\": 300 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("No products found id = 300"));

        verify(carouselService, never()).saveOrUpdate(any());
    }

    @Test
    void update_WithInvalidGoodId_ShouldReturnError() throws Exception {
        when(goodService.existGood(500L)).thenReturn(false);

        mockMvc.perform(put("/api/carousel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"goodId\": 500 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("No products found id = 500"));

        verify(carouselService, never()).updateById(any());
    }

    @Test
    void delete_WithValidId_ShouldSuccess() throws Exception {
        mockMvc.perform(delete("/api/carousel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(carouselService).removeById(eq(1L));
    }
}