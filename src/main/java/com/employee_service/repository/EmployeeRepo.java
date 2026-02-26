package com.employee_service.repository;

import com.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.isDeleted = true")
    List<Employee> findDeletedEmployees();

    List<Employee> findByIsDeletedFalse();

    Optional<Employee> findByIdAndIsDeletedFalse(Long id);
}
