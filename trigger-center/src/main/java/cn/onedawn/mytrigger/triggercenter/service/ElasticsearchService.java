package cn.onedawn.mytrigger.triggercenter.service;

import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.impl.JobServiceImpl;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import javafx.print.PrinterJob;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@DependsOn("applicationServiceImpl")
public class ElasticsearchService {

    private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    private static String elasticsearchHost;
    private static Integer elasticsearchPort;
    private static String elasticsearchIndexName;
    @Autowired
    private  ApplicationService applicationService;



    @Value("${elasticsearch.host}")
    public void setElasticsearchHost(String elasticsearchHost) {
        ElasticsearchService.elasticsearchHost = elasticsearchHost;
    }

    @Value("${elasticsearch.port}")
    public void setElasticsearchPort(Integer elasticsearchPort) {
        ElasticsearchService.elasticsearchPort = elasticsearchPort;
    }

    @Value("${elasticsearch.index.name}")
    public void setElasticsearchIndexName(String elasticsearchIndexName) {
        ElasticsearchService.elasticsearchIndexName = elasticsearchIndexName;
    }

    public IndexRequest getIndexRequest(String docId) {
        return new IndexRequest(elasticsearchIndexName, "_doc", docId);
    }

    public void Index(Job job, Application app) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", job.getId());
        jsonMap.put("status", job.getStatus());
        jsonMap.put("triggerTime", job.getTriggerTime());
        jsonMap.put("callName", job.getCallName());
        jsonMap.put("callType", job.getCallType());
        jsonMap.put("callData", job.getCallData());
        jsonMap.put("callHost", job.getCallHost());
        jsonMap.put("cron", job.getCron());
        jsonMap.put("createTime", job.getCreateTime());
        jsonMap.put("modifyTime", job.getModifyTime());
        jsonMap.put("appName", app.getName());
        jsonMap.put("callerrorRetryCount", job.getCallerrorRetryCount());
        jsonMap.put("runRetry", job.getRunRetry());
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort, "http")))) {
            IndexRequest indexRequest = getIndexRequest(String.valueOf(job.getId()));
            indexRequest.source(jsonMap);
            IndexResponse indexResponse = null;
            try {
                indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            } catch (ElasticsearchException e) {
                if (e.status() == RestStatus.CONFLICT) {
                    logger.error("ES client send request failed error_info：{}", e.getDetailedMessage());
                    logger.error("index exception", e);
                }
            }
            if (indexResponse != null) {
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    logger.info("ES store succceed! jobId:{}", job.getId());
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    logger.info("ES update succceed! jobId:{}", job.getId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("create client failed! e:{}", e.getMessage());
        }
    }

    public List<Job> queryJobByCondition(Job job, Application app, Pageable pageable) {
        List<Job> jobs = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(elasticsearchIndexName);
        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (job.getCallName() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callName", job.getCallName()));
        }
        if (job.getId() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("id", job.getId()));
        }
        if (job.getCallData() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callData", job.getCallData()));
        }
        if (job.getCallType() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callType", job.getCallType()));
        }
        if (job.getCallHost() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callHost", job.getCallHost()));
        }
        if (job.getCron() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("cron", job.getCron()));
        }
        if (app.getName() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("appName", app.getName()));
        }
        sourceBuilder.from((int) pageable.getOffset());
        sourceBuilder.sort("_score");
        sourceBuilder.size(pageable.getPageSize());
        sourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort, "http")))) {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Job task = new Job();
                task.setId(Long.valueOf((Integer) sourceAsMap.get("id")));
                task.setStatus(JobStatusType.finish);
                String callType = (String) sourceAsMap.get("call_type");
                task.setCallType("http".equals(callType) ? CallType.http : CallType.dubbo);
                Long appId = applicationService.findAppIdByAppName((String) sourceAsMap.get("appName"));
                task.setApp(appId);
                task.setCron(String.valueOf(sourceAsMap.get("cron")));
                task.setCallData(String.valueOf(sourceAsMap.get("call_data")));
                task.setCallHost(String.valueOf(sourceAsMap.get("call_host")));
                task.setRunRetry((Integer) sourceAsMap.get("run_retry"));
                task.setCallerrorRetryCount((Integer) sourceAsMap.get("callerror_retry_count"));
                task.setTriggerTime(String.valueOf(sourceAsMap.get("trigger_time")));
                task.setCreateTime(String.valueOf(sourceAsMap.get("create_time")));
                task.setModifyTime(String.valueOf(sourceAsMap.get("modify_time")));
                jobs.add(task);
            }
            return jobs;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("create client failed! e:{}", e.getMessage());
        }
        return null;
    }

}
