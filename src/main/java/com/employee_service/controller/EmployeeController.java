package com.employee_service.controller;

import com.employee_service.dto.EmployeeRequest;
import com.employee_service.dto.EmployeeResponse;
import com.employee_service.dto.LeaveRequest;
import com.employee_service.dto.LeaveResponse;
import com.employee_service.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService empService;

    @GetMapping("allemployee")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployee(){
        List<EmployeeResponse> res = empService.findAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/insertEmp")
    public ResponseEntity<EmployeeResponse> insertEmployee(
            @Valid
            @RequestBody
            EmployeeRequest employee){
        EmployeeResponse empRes = empService.insertEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(empRes);
    }

    @GetMapping("/getEmpById/{id}")
    public ResponseEntity<EmployeeResponse> getEmpById(@PathVariable Long id){
        EmployeeResponse es = empService.getEmpById(id);
        return ResponseEntity.status(HttpStatus.OK).body(es);
    }

    @PostMapping("updateEmployee/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest employee){
        EmployeeResponse es = empService.updateEmployee(id,employee);
        return ResponseEntity.status(HttpStatus.OK).body(es);
    }

    @DeleteMapping("deleteEmpById/{id}")
    public ResponseEntity<Map<String , String>> deleteEmpById(@PathVariable Long id){
        empService.deleteById(id);
        Map<String, String> res = new HashMap<>();
        res.put("message","Employee deleted successfuly");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

//    @PostMapping("/createLeave")
//    public LeaveResponse createLeave(@RequestBody LeaveRequest request) {
//        return empService.createLeave(request);
//    }

    @PostMapping("/createLeave")
    public ResponseEntity<String> createLeave(
            @RequestBody LeaveRequest request,
            HttpServletRequest httpRequest) {

        String email = httpRequest.getHeader("X-User-Name");

        return ResponseEntity.ok(
                empService.createLeave(request, email)
        );
    }

    @GetMapping("/myLeaves")
    public ResponseEntity<?> getMyLeaves() {
        return ResponseEntity.ok(
                empService.getMyLeaves()
        );
    }

    @PostMapping("/createFromAuth")
    public ResponseEntity<?> createFromAuth(@RequestBody EmployeeRequest request) {

        empService.createFromAuth(request);

        return ResponseEntity.ok("Employee created");
    }
}
