package com.example.springcloud.controller;

import com.example.springcloud.entity.CommonResult;
import com.example.springcloud.entity.Payment;
import com.example.springcloud.service.OperfeignService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class PaymentController {

    public CommonResult defaultFallback(){
        return new CommonResult(500, "error", "错误，返回默认！");
    }

    private static String IP_ADDRESS = "http://CLOUD-PAYMENT-SERVICE/";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Integer id){
        return restTemplate.getForObject(IP_ADDRESS + "get/{id}", CommonResult.class, id);
    }

    @GetMapping("/create/{param}")
    public CommonResult create(@PathVariable("param") String param){
        return restTemplate.getForObject(IP_ADDRESS + "create/{param}", CommonResult.class, param);
    }

    @GetMapping("/discovery")
    public CommonResult discovery(){
        StringBuffer sb = new StringBuffer();
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            sb.append("instances:");
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                sb.append("    serviceId:");
                sb.append(instance.getServiceId());
                sb.append("    host:");
                sb.append(instance.getHost());
                sb.append("    port:");
                sb.append(instance.getPort());
                sb.append("    uri:");
                sb.append(instance.getUri());
            }
        }
        return new CommonResult(200, "success", sb.toString());
    }

    @Autowired
    private OperfeignService operfeignService;

    @GetMapping("/openfeign/{id}")
    public CommonResult openfeign(@PathVariable("id") Integer id){
        return operfeignService.getPaymentById(id);
    }

    @GetMapping("/timeoutDefault")
    @HystrixCommand
    public CommonResult timeoutDefault(){
        int num = 10/0;
        return operfeignService.timeout();
    }

    @GetMapping("/timeout")
    @HystrixCommand(fallbackMethod = "timeout_fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    public CommonResult timeout(){
        return operfeignService.timeout();
    }

    public CommonResult timeout_fallback(){
        return new CommonResult(200, "success", "服务繁忙，请稍后再试！");
    }

    @GetMapping("/number/{num}")
    @HystrixCommand(fallbackMethod = "number_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
    })
    public CommonResult number(@PathVariable("num") Integer num){
        if(num < 0) {
            throw new RuntimeException("can not is -num");
        }
        return new CommonResult(200, "success", "服务繁忙，请稍后再试！O(∩_∩)O");
    }

    public CommonResult number_fallback(@PathVariable("num") Integer num){
        return new CommonResult(200, "success", "服务繁忙，请稍后再试！┭┮﹏┭┮");
    }

}
