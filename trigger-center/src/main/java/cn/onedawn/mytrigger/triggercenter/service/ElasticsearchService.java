package cn.onedawn.mytrigger.triggercenter.service;

import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.impl.JobServiceImpl;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticsearchService {

    private static String elasticsearchHost;
    private static Integer elasticsearchPort;
    private static String elasticsearchIndexName;

    private static volatile RestHighLevelClient client = null;
    private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);


    @Value("${elasticsearch.host}")
    public void setElasticsearchHost(String elasticsearchHost){
        ElasticsearchService.elasticsearchHost = elasticsearchHost;
    }

    @Value("${elasticsearch.port}")
    public void setElasticsearchPort(Integer elasticsearchPort){
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

}
