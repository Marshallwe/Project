package com.example.spring.mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spring.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface MessageMapper {

    List<Message> findAllMessage();

    @Select("select id,id as temp_id,title,content,time,score,user_id from message where good_id = #{goodId} order by id desc")
    @Results({
            @Result(property = "replays", column = "temp_id", many = @Many(select = "com.shanzhu.em.mapper" +
                    ".ReplayMapper.findAllById"))
    })
    IPage<Message> findAll(Page page, Long goodId);


    @Select("select id,title,content,time,score,user_id from message where id = #{id}")
    @Results({
            @Result(property = "replays", column = "id", many = @Many(select = "com.shanzhu.em.mapper.ReplayMapper" +
                    ".findAllById"))
    })
    Message findById(Integer id);


    @Delete("delete from message where id = #{id}")
    Integer delete(Integer id);


    @Update("update message set title = #{title}, content = #{content}, time = #{time}, score = #{score} + where " +
            "id = #{id}")
    Integer update(Message message);


    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into message(title, content, time, good_id, score, user_id) values(#{title},#{content},#{time},#{goodId},#{score},#{userId})")
    int add(Message message);
}
