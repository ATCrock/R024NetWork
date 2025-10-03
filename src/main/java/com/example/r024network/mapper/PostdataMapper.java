package com.example.r024network.mapper;

import com.example.r024network.entity.Postdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author yuziyang
* @description 针对表【postdata】的数据库操作Mapper
* @createDate 2025-10-01 22:20:37
* @Entity com.example.r024network.entity.Postdata
*/
public interface PostdataMapper extends BaseMapper<Postdata> {
    @Update("UPDATE post SET ViewCount = ViewCount + 1 WHERE id = #{id}")
    void increaseViewCount(@Param("id") int id);
    // 挂着，不打算做热度那个了
    // 具体实现在getPost里面分页，分到了对应的post就+1
    // 按照浏览+点赞*2+评论*2*评论字段长度*0.1分热度 可以考虑内容权重，扫一下评论中是否有和标题相近字眼 收藏什么的再说
    // 考虑先对整体进行排查和优化，测试一下乱七八糟的可能性内容
}




