package com.employee.info.controller;

import com.employee.info.Entity.EmpTaxInfo;
import com.employee.info.Entity.EmployeeEntity;
import com.employee.info.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/save-employee")
    public String saveEmployee(EmployeeEntity employee){
        try {
            employeeService.validateEmployee(employee);
        } catch (Exception e){
            return "All fields are mandatory for employee";
        }
        employeeService.saveEmployee(employee);
        return "Saved the employee successfully";
    }

    @GetMapping("employee-list")
    public List<EmployeeEntity> employeeList(){
        return employeeService.fetchEmployee();
    }

    @GetMapping("employee-tax")
    public EmpTaxInfo employeeTax(Long empCode){
        return employeeService.getTaxDetails(empCode);
    }


}