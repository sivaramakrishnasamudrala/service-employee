package com.employee.info.service;

import com.employee.info.Entity.EmpTaxInfo;
import com.employee.info.Entity.EmployeeEntity;
import com.employee.info.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void saveEmployee(EmployeeEntity employee){
        employeeRepository.save(employee);
    }

    public List<EmployeeEntity> fetchEmployee(){
        return employeeRepository.findAll();
    }

    public void validateEmployee(EmployeeEntity employee){
        Objects.requireNonNull(employee.getFirstName());
        Objects.requireNonNull(employee.getLastName());
        Objects.requireNonNull(employee.getEmail());
        Objects.requireNonNull(employee.getPhoneNumbers());
        Objects.requireNonNull(employee.getDoj());
    }

    public static int getYearFromDate(Date date) {
        int result = -1;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = cal.get(Calendar.YEAR);
        }
        return result;
    }
    public EmpTaxInfo getTaxDetails(Long empCode){
        try {
            Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(empCode);
            if(employeeEntity.isPresent()){
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date joiningDate = sdf.parse(employeeEntity.get().getDoj());
                int nextYear = getYearFromDate(joiningDate) + 1;
                Date firstDate = sdf.parse("03/31/" + nextYear);

                long diffInMillies = Math.abs(firstDate.getTime() - joiningDate.getTime());
                long totalDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                double totalSalary = totalDays * employeeEntity.get().getSalary() / 30;

                double taxAmount = taxAmount(totalSalary);
                double cessAmount = cessAmount(totalSalary);

                EmpTaxInfo empTaxInfo = new EmpTaxInfo();
                empTaxInfo.setCessAmount(cessAmount);
                empTaxInfo.setTaxAmount(taxAmount);
                empTaxInfo.setEmployeeCode(employeeEntity.get().getEmployeeId());
                empTaxInfo.setFirstName(employeeEntity.get().getFirstName());
                empTaxInfo.setLastName(employeeEntity.get().getLastName());
                empTaxInfo.setYearlySalary(totalSalary);
                return empTaxInfo;
            }
        }catch (Exception ignored) {

        }
        return new EmpTaxInfo();
    }

    private double taxAmount(double salary){
        double tax = 0;
        if(salary<=250000)
            tax=0;
        else if(salary<=500000)
            tax=0.05*(salary-250000);
        else if(salary<=1000000)
            tax=(0.1*(salary-500000))+12500;
        else if(salary>1000000)
            tax=(0.2*(salary-1000000))+62500;
        return tax;
    }

    private double cessAmount(double salary){
        double cess = 0;
        if(salary > 2500000){
            cess = 0.02 * (salary - 2500000);
        }
        return cess;
    }
}
