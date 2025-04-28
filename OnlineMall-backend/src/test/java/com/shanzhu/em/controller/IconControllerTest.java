package com.shanzhu.em.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Category;
import com.shanzhu.em.entity.Icon;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.service.IconService;
import com.shanzhu.em.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IconControllerTest {

    @Mock
    private IconService iconService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private IconController iconController;

    @Test
    void findIcon_ExistingId_ReturnsIcon() {
        Long testId = 1L;
        Icon mockIcon = new Icon();
        mockIcon.setId(testId);

        when(iconService.getById(testId)).thenReturn(mockIcon);

        R<Icon> result = iconController.findIcon(testId);

        assertEquals(testId, result.getData().getId());
        assertEquals("200", result.getCode());
    }

    @Test
    void findAllIcon_NotEmpty_ReturnsList() {
        Icon iconWithCategory = new Icon();
        iconWithCategory.setCategories(Collections.singletonList(new Category()));

        when(iconService.getIconCategoryMapList())
                .thenReturn(Collections.singletonList(iconWithCategory));

        R<List<Icon>> result = iconController.findAllIcon();

        assertFalse(result.getData().isEmpty());
        assertNotNull(result.getData().get(0).getCategories());
    }


    @Test
    void update_ExistingIcon_ReturnsSuccess() {
        Icon existingIcon = new Icon();
        existingIcon.setId(100L);

        R<Void> result = iconController.update(existingIcon);

        verify(iconService).updateById(existingIcon);
        assertEquals("200", result.getCode());
    }

    @Test
    void delete_ValidId_ReturnsOperationResult() {
        Long testId = 999L;
        Map<String, Object> mockResult = Map.of("success", true, "message", "deleted");

        when(iconService.deleteById(testId)).thenReturn(mockResult);

        Map<String, Object> result = iconController.delete(testId);

        assertEquals(mockResult, result);
        assertTrue((Boolean) result.get("success"));
    }


    @Test
    void findIcon_NonExistingId_ReturnsNull() {
        Long invalidId = -1L;
        when(iconService.getById(invalidId)).thenReturn(null);

        R<Icon> result = iconController.findIcon(invalidId);
        assertNull(result.getData());
        assertEquals("200", result.getCode());
    }

    @Test
    void delete_SystemIcon_BlocksDeletion() {
        Long systemIconId = 1L;
        Map<String, Object> mockResult = Map.of("success", false, "message", "System icon cannot be deleted");

        when(iconService.deleteById(systemIconId)).thenReturn(mockResult);

        Map<String, Object> result = iconController.delete(systemIconId);
        assertFalse((Boolean) result.get("success"));
    }
}