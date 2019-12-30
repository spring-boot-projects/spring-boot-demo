package com.xkcoding.dubbo.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xkcoding.dubbo.common.service.HelloService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello服务API
 */
@RestController
@Slf4j
public class HelloController {

    @Reference
    private HelloService helloService;

    @GetMapping("/sayHello")
    public String sayHello(@RequestParam(defaultValue = "xkcoding") String name) {
        log.info("i'm ready to call someone......");
        return helloService.sayHello(name);
    }


    @PostConstruct
    private void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private HelloService mockService;

    @GetMapping("/mock")
    public String mock(@RequestParam(defaultValue = "xkcoding") String name) {
        log.info("i'm ready to call mock......");
        Mockito.when(mockService.sayHello(name)).thenReturn("mock say hello...");
        return mockService.sayHello(name);
    }
}
