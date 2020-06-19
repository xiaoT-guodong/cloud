package com.example.springcloud.service.imp;

import com.example.springcloud.entity.CommonResult;
import com.example.springcloud.service.OperfeignService;
import org.springframework.stereotype.Component;

@Component
public class OpenfeignFallback implements OperfeignService {

    @Override
    public CommonResult getPaymentById(Integer id) {
        return new CommonResult(500, "error", "8001或8002 getPaymentById服务宕机了！");
    }

    @Override
    public CommonResult timeout() {
        return new CommonResult(500, "error", "8001或8002 timeout服务宕机了！");
    }

}
