package com.example.springcloud.controller;

import com.example.springcloud.entity.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
public class ConsulController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consul")
    public CommonResult consul(){
        return new CommonResult(200, "success", UUID.randomUUID());
    }

}
