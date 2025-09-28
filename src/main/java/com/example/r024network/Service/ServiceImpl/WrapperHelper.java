package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

@Service
public class WrapperHelper {
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
