package com.example.r024network.job;


import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.PostService;
import jakarta.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class PostJob implements Job {


    @Override
    public void execute(JobExecutionContext context){
        try {
            PostService postService = SpringContextHolder.getBean(PostService.class);
            // ?
            postService.checkScheduledPost();
        } catch (Exception e) {
            throw new APIException(444, e.getMessage());
        }
        //postService.postSingleConfession();
        //还需要修改postdata，加入status和tick
        //明天再说
    }
}
