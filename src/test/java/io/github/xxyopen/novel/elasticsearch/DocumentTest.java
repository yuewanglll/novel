package io.github.xxyopen.novel.elasticsearch;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;


public class DocumentTest {

    private RestHighLevelClient client;

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.174.128:9200")
        ));
    }

    @Test
    public void testIndexRequest() throws IOException {
        Product product = new Product(
                "p1010",  // id
                "小米15手机为战斗而胜的手机",  // name
                2999,  // price
                100,  // stock
                "/images/laptop.jpg",  // image
                "手机",  // category
                "小米",  // brand
                356,  // sold
                128,  // commentCount
                false,  // isAD
                new Date()  // updateTime
        );
        //创建请求对象
        IndexRequest request = new IndexRequest("items").id(product.getId());
        // 将Product对象转换为JSON字符串
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(product);
        request.source(jsonString, XContentType.JSON);
       //发送请求
        client.index(request, RequestOptions.DEFAULT);

    }


    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }
}