package com.example.springcloud.service;

import com.example.springcloud.entity.CommonResult;
import com.example.springcloud.service.imp.OpenfeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE", fallback = OpenfeignFallback.class)
public interface OperfeignService {

    @GetMapping("/get/{id}")
    CommonResult getPaymentById(@PathVariable("id") Integer id);

    @GetMapping("/timeout")
    CommonResult timeout();

}
