package com.shanzhu.em.entity.vo;

import lombok.Data;


@Data
public class UserVo {


    private int id;


    private String username;


    private String nickname;


    private String avatarUrl;


    private String token;

    private String role;

}
