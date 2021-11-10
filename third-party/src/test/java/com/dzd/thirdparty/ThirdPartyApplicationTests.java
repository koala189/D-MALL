package com.dzd.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
class ThirdPartyApplicationTests {
    @Autowired
    OSS ossClient;

    @Test
    void contextLoads() {
        try {
            // 创建OSSClient实例。
            FileInputStream inputStream = new FileInputStream("D:\\example.txt");
            ossClient.putObject("dmall-hello", "example2.txt",inputStream);
        } catch (OSSException | FileNotFoundException e){
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

}
