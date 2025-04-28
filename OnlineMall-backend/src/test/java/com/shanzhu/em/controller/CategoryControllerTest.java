package com.shanzhu.em.controller;

import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Category;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @MockBean
    private RedisTemplate<String, User> redisTemplate; // Add this mock
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private final Category mockCategory = new Category() {{
        setId(1L);
        setName("Electronics");
    }};

    @Test
    void findCategory_Success() throws Exception {
        Mockito.when(categoryService.getById(1L)).thenReturn(mockCategory);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Electronics"));
    }

    @Test
    void findAllCategory_Success() throws Exception {
        Mockito.when(categoryService.list()).thenReturn(Arrays.asList(mockCategory));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Electronics"));
    }

    @Test
    void saveCategory_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Category\"}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));

        Mockito.verify(categoryService).save(Mockito.any(Category.class));
    }

    @Test
    void addCategory_Success() throws Exception {
        Mockito.doNothing().when(categoryService).add(Mockito.any(Category.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Category\"}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    void updateCategory_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Category\"}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));

        Mockito.verify(categoryService).updateById(Mockito.any(Category.class));
    }

    @Test
    void deleteCategory_Success() throws Exception {
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("code", 200);
        expectedResponse.put("msg", "success");

        Mockito.when(categoryService.delete(1L)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/delete")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }
}