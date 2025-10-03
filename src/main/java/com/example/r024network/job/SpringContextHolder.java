package com.example.r024network.job;
import com.example.r024network.Exception.APIException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        if (applicationContext == null) {
            throw new APIException(430, "Spring容器初始化失败");
        }
        return applicationContext.getBean(requiredType);
    }
}