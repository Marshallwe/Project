package com.shanzhu.em.controller;

import com.shanzhu.em.common.R;
import com.shanzhu.em.utils.TokenUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoleController {

    @PostMapping("/role")
    public R<String> getUserRole() {
        return R.success(TokenUtils.getCurrentUser().getRole());
    }

}
