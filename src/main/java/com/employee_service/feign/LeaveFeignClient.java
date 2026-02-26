package com.employee_service.feign;

import com.employee_service.config.FeignConfig;
import com.employee_service.dto.LeaveRequest;
import com.employee_service.dto.LeaveResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "LEAVE-SERVICE",
        configuration = FeignConfig.class
)
public interface LeaveFeignClient {

    @PostMapping("/leave/apply")
    LeaveResponse applyLeave(@RequestBody LeaveRequest request);

    @GetMapping("/leave/my")
    List<LeaveResponse> getMyLeaves();
}