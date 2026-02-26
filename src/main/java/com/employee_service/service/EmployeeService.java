package com.employee_service.service;


import com.employee_service.dto.*;
import com.employee_service.exception.ResourceNotFoundException;
import com.employee_service.feign.LeaveFeignClient;
import com.employee_service.kafka.LeaveEventProducer;
import com.employee_service.model.Employee;
import com.employee_service.repository.EmployeeRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AuditorAware<String> auditorAware;

    @Autowired
    private LeaveFeignClient leaveFeignClient;

    @Autowired
    private LeaveEventProducer leaveEventProducer;


    public List<EmployeeResponse> findAllEmployees() {

        return employeeRepo.findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public EmployeeResponse insertEmployee(EmployeeRequest employee) {
        Employee emp = new Employee();
        emp.setName(employee.getName());
        emp.setEmail(employee.getEmail());
        emp.setDob(employee.getDob());
        emp.setContNo(employee.getContNo());
        emp.setGender(employee.getGender());
        emp.setCity(employee.getCity());

        employeeRepo.save(emp);


        EmployeeResponse response = new EmployeeResponse();
        response.setId(emp.getId());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        response.setDob(employee.getDob());
        response.setContNo(employee.getContNo());
        response.setGender(employee.getGender());
        response.setCity(employee.getCity());
        response.setCreatedAt(emp.getCreatedAt());
        response.setUpdatedAt(emp.getUpdatedAt());
        response.setCreatedBy(emp.getCreatedBy());
        response.setIsDeleted(emp.getIsDeleted());
        response.setDeletedAt(emp.getDeletedAt());
        response.setDeletedBy(emp.getDeletedBy());
        return response;
    }

    public EmployeeResponse getEmpById(Long id) {

        Employee emp = employeeRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id)
                );

        return mapToResponse(emp);
    }

    public EmployeeResponse getUserEmpById(Long id, String username, String role) {

        Employee emp = employeeRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if ("USER".equals(role) && !emp.getEmail().equals(username)) {
            throw new RuntimeException("Access Denied");
        }

        return mapToResponse(emp);
    }


    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        emp.setName(request.getName());
        emp.setEmail(request.getEmail());

        emp.setDob(request.getDob());
        emp.setContNo(request.getContNo());
        emp.setGender(request.getGender());
        emp.setCity(request.getCity());

        employeeRepo.save(emp);

        return mapToResponse(emp);
    }

    public void deleteById(Long id) {

        Employee emp = employeeRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id)
                );

        emp.setIsDeleted(true);
        emp.setDeletedAt(LocalDateTime.now());
        emp.setDeletedBy(
                auditorAware.getCurrentAuditor().orElse("SYSTEM")
        );

        employeeRepo.save(emp);
    }


    private EmployeeResponse mapToResponse(Employee emp) {

        EmployeeResponse response = new EmployeeResponse();

        response.setId(emp.getId());
        response.setName(emp.getName());
        response.setEmail(emp.getEmail());
        response.setCity(emp.getCity());
        response.setContNo(emp.getContNo());
        response.setDob(emp.getDob());
        response.setGender(emp.getGender());
        response.setCreatedAt(emp.getCreatedAt());
        response.setUpdatedAt(emp.getUpdatedAt());
        response.setCreatedBy(emp.getCreatedBy());
        response.setIsDeleted(emp.getIsDeleted());
        response.setDeletedAt(emp.getDeletedAt());
        response.setDeletedBy(emp.getDeletedBy());

        return response;
    }

//    @CircuitBreaker(name = "leaveService", fallbackMethod = "applyLeaveFallback")
//    public LeaveResponse createLeave(LeaveRequest request) {
//        return leaveFeignClient.applyLeave(request);
//    }

    public String createLeave(LeaveRequest request, String email) {

        LeaveCreateEvent event = new LeaveCreateEvent();
        event.setEmployeeEmail(email);
        event.setFromDate(request.getFromDate());
        event.setToDate(request.getToDate());
        event.setReason(request.getReason());

        leaveEventProducer.sendLeaveCreateEvent(event);

        return "Leave request submitted successfully";
    }

    @CircuitBreaker(name = "leaveService", fallbackMethod = "getMyLeavesFallback")
    public List<LeaveResponse> getMyLeaves() {
        return leaveFeignClient.getMyLeaves();
    }

    public LeaveResponse applyLeaveFallback(LeaveRequest request, Throwable ex) {

        LeaveResponse response = new LeaveResponse();
        response.setStatus("Leave Service is currently unavailable. Please try later.");

        return response;
    }

    public List<LeaveResponse> getMyLeavesFallback(Throwable ex) {

        LeaveResponse response = new LeaveResponse();
        response.setStatus("Leave Service is down");

        return List.of(response);
    }

    public void createFromAuth(EmployeeRequest request) {

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setCity(request.getCity());
        employee.setContNo(request.getContNo());
        employee.setDob(request.getDob());
        employee.setGender(request.getGender());

        employeeRepo.save(employee);
    }
}
