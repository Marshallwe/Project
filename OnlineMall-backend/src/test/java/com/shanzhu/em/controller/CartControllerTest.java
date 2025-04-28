package com.shanzhu.em.controller;

import cn.hutool.core.date.DateUtil;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Cart;
import com.shanzhu.em.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void findUserCart_ExistingId_ShouldReturnCart() throws Exception {

        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setUserId(100L);

        when(cartService.getById(1L)).thenReturn(mockCart);

        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.userId").value(100));

        verify(cartService).getById(1L);
    }

    @Test
    void findUserCart_NonExistingId_ShouldReturnEmpty() throws Exception {
        when(cartService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/cart/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void findAllUserCart_ShouldReturnList() throws Exception {
        Cart cart1 = new Cart();
        cart1.setId(1L);
        Cart cart2 = new Cart();
        cart2.setId(2L);

        when(cartService.list()).thenReturn(Arrays.asList(cart1, cart2));

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }


    @Test
    void save_ValidCart_ShouldSetCreateTime() throws Exception {
        String testTime = DateUtil.now();

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"userId\": 200, \"goodId\": 300 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartService).save(cartCaptor.capture());

        Cart savedCart = cartCaptor.getValue();
        assertThat(savedCart.getCreateTime()).isNotNull();
        assertThat(savedCart.getUserId()).isEqualTo(200L);
        assertThat(savedCart.getGoodId()).isEqualTo(300L);
    }

    @Test
    void update_ValidCart_ShouldSuccess() throws Exception {
        mockMvc.perform(put("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"count\": 5 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartService).updateById(cartCaptor.capture());

        Cart updatedCart = cartCaptor.getValue();
        assertThat(updatedCart.getId()).isEqualTo(1L);
        assertThat(updatedCart.getCount()).isEqualTo(5);
    }

    @Test
    void delete_ExistingId_ShouldSuccess() throws Exception {
        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(cartService).removeById(1L);
    }

    @Test
    void delete_NonExistingId_ShouldStillSuccess() throws Exception {
        mockMvc.perform(delete("/api/cart/999"))
                .andExpect(status().isOk());

        verify(cartService).removeById(999L);
    }
}