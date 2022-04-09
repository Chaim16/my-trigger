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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Test
    public void queryByCondition() {
        ElasticsearchService elasticsearchService = (ElasticsearchService) SpringBeanFactory.getBean("elasticsearchService");
        Job job = new Job();
        Application app = new Application();
        elasticsearchService.queryJobByCondition(job, app, new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 0;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        });
    }

}
