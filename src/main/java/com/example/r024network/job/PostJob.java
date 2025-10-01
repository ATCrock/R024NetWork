package com.example.r024network.job;


import com.example.r024network.Service.PostService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PostJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        PostService postService = (PostService) context.getJobDetail().getJobDataMap().get("postService");
        //postService.postSingleConfession();
        //还需要修改postdata，加入status和tick
        //明天再说
    }
}
