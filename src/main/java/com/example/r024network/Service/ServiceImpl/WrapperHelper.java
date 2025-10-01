package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

@Service
public class WrapperHelper {
    // 一个快速返回queryWrapper的类，目前支持单个int和string类型的数据查找
    public <T> QueryWrapper<T> convert(String wrapperItem, Integer data) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(wrapperItem, data);
        return queryWrapper;
    }

    public <T> QueryWrapper<T> convert(String wrapperItem, String data) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(wrapperItem, data);
        return queryWrapper;
    }

}
