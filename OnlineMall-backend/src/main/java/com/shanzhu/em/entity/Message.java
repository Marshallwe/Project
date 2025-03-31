package com.shanzhu.em.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;



@Data
public class Message {

    private Integer id;


    private Integer temp_id;


    private String title;


    private String content;


    private Integer score;


    private Long userId;


    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date time;

    private Long goodId;


    List<Replay> replays;

}