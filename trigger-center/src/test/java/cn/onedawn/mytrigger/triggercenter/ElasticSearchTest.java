package cn.onedawn.mytrigger.triggercenter;

import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.mapper.ApplicationMapper;
import cn.onedawn.mytrigger.triggercenter.mapper.JobMapper;
import cn.onedawn.mytrigger.triggercenter.service.ElasticsearchService;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ElasticSearchTest {

    @Autowired
    JobMapper jobMapper;

    @Autowired
    ApplicationMapper applicationMapper;

    @Test
    public void sendIndexRequestTest() {
        ElasticsearchService elasticsearchService = (ElasticsearchService) SpringBeanFactory.getBean("elasticsearchService");
        Job job = jobMapper.selectJobById(2L);
        Application app = applicationMapper.selectAppById(job.getApp());
        elasticsearchService.Index(job, app);
    }
}
