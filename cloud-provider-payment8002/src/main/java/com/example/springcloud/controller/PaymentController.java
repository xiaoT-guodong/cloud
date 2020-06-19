package com.example.springcloud.controller;

import com.example.springcloud.entity.CommonResult;
import com.example.springcloud.entity.Payment;
import com.example.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Value("${server.port}")
    private int port;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Integer id){
        Payment payment = paymentService.getPaymentById(id);
        if(payment != null) {
            return new CommonResult(200, "success,port:  " + port, payment);
        }
        return new CommonResult(200, "success");
    }

    @GetMapping("/create/{param}")
    public CommonResult create(@PathVariable("param") String param){
        int result = paymentService.create(new Payment(param));
        if(result < 0) {
            return new CommonResult(500, "插入失败");
        }
        return new CommonResult(200, "success,port:  " + port, result);
    }

    @GetMapping("/timeout")
    @HystrixCommand(fallbackMethod = "timeout_fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public CommonResult timeout(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new CommonResult(200, "success");
    }

    public CommonResult timeout_fallback(){

        return new CommonResult(200, "success", "fallback");
    }

}
