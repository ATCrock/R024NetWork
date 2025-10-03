package com.example.r024network.job;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Helper.ResultHelper;
import com.example.r024network.Service.PostService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PostJob implements Job {
    @Override
    public void execute(JobExecutionContext context){
        try {
            PostService postService = SpringContextHolder.getBean(PostService.class);
            postService.checkScheduledPost();
        } catch (Exception e) {
            throw new APIException(454, e.getMessage());
        }
    }

}
