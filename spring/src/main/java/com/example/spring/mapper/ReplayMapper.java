package com.example.spring.mapper;

import com.example.spring.entity.Replay;
import org.apache.ibatis.annotations.*;

import java.util.List;



@Mapper
public interface ReplayMapper {


    @Select("select messageId,replayId,replay,replayTime from replay where messageId = #{messageId}")
    List<Replay> findAllById(Integer messageId);


    @Delete("delete from replay where replayId = #{replayId}")
    Integer delete(Integer replayId);


    @Update("update replay set title = #{title}, replay = #{replay}, replayTime = #{replayTime} where replayId = " +
            "#{replayId}")
    Integer update(Replay replay);


    @Options(useGeneratedKeys = true, keyProperty = "replayId")
    @Insert("insert into replay(messageId,replay,replayTime) values(#{messageId}, #{replay},#{replayTime})")
    int add(Replay replay);
}
