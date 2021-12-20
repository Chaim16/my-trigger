package cn.onedawn.mytrigger.triggercenter.config;

import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName BeanConfig.java
 * @Description TODO 注入bean
 * @createTime 2021年12月20日 16:52:00
 */
@Configuration
public class BeanConfig {
    @Bean(name = "beanService")
    public SpringBeanFactory initBeanService() {
        return new SpringBeanFactory();
    }
}
