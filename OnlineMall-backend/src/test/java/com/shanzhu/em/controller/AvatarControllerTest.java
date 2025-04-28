package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Avatar;
import com.shanzhu.em.service.AvatarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AvatarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AvatarService avatarService;

    @InjectMocks
    private AvatarController avatarController;

    @BeforeEach
    void setup() {
        Mockito.reset(avatarService);
        mockMvc = MockMvcBuilders.standaloneSetup(avatarController).build();
    }

    @Test
    void uploadAvatar_ValidFile_ReturnsSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test content".getBytes()
        );

        when(avatarService.upload(any(MultipartFile.class))).thenReturn("/uploads/test.png");

        mockMvc.perform(multipart("/avatar").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("/uploads/test.png"));

        verify(avatarService).upload(any(MultipartFile.class));
    }
    @Test
    void download_ValidFileName_ReturnsFile() throws Exception {

        mockMvc.perform(get("/avatar/test.png"))
                .andExpect(status().isOk());

        verify(avatarService).download(eq("test.png"), any(HttpServletResponse.class));
    }

    @Test
    void deleteById_Success_ReturnsSuccess() throws Exception {
        when(avatarService.delete(1)).thenReturn(true);

        mockMvc.perform(delete("/avatar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(avatarService).delete(1);
    }

    @Test
    void deleteById_Failure_ReturnsError() throws Exception {
        when(avatarService.delete(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/avatar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Status.CODE_500))
                .andExpect(jsonPath("$.msg").value("Deletion failure"));

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(avatarService).delete(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(1);
    }

    @Test
    void findUserAvatarPage_ValidPagination_ReturnsPageData() throws Exception {
        Page<Avatar> mockPage = new Page<>();
        mockPage.setRecords(Collections.singletonList(new Avatar()));
        mockPage.setTotal(10L);

        when(avatarService.selectPage(anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc.perform(get("/avatar/page")
                        .param("pageNum", "2")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records.length()").value(1))
                .andExpect(jsonPath("$.data.total").value(10));

        verify(avatarService).selectPage(eq(5), eq(5));
    }
}