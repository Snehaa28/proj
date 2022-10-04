package com.albertsons.poc.employeeprofilemanagementsystem.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.*;

import com.albertsons.poc.employeeprofilemanagementsystem.entity.EmployeeProfile;
import com.albertsons.poc.employeeprofilemanagementsystem.entity.Validation;
import com.albertsons.poc.employeeprofilemanagementsystem.exception.EmployeeIdNotFoundException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albertsons.poc.employeeprofilemanagementsystem.dao.EmployeeProfileRepository;
import org.springframework.web.multipart.MultipartFile;


@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    @Autowired
    EmployeeProfileRepository empProfileRepository;

    @Override
    public EmployeeProfile retrieveEmpProfile(int id) throws EmployeeIdNotFoundException {
        if (!empProfileRepository.findById(id).isPresent())
            throw new EmployeeIdNotFoundException();
        else
            return empProfileRepository.findById(id).get();
    }

    @Override
    public void createEmpProfile(EmployeeProfile empProfile) {

        empProfileRepository.save(empProfile);
    }

    @Override
    public void deleteEmpProfile(int id) {
        empProfileRepository.deleteById(id);
    }

    @Override
    public List<EmployeeProfile> retrieveAllEmpProfiles() {
        return empProfileRepository.findAll();
    }

    @Override
    public List<EmployeeProfile> getEmployeesByProjId(int projId) {
        return empProfileRepository.findByProjId(projId);
    }

    @Override
    public Map<String, Object> EmpOnLocDeatils() {

        return empProfileRepository.EmpOnLocDetails();
    }

    @Override
    public List<EmployeeProfile> EmployeeOnLoc(String color) {
        switch (color) {
            case "yellow":
                System.out.println(color);
                System.out.println(empProfileRepository.yellowEmployee());
                return empProfileRepository.yellowEmployee();

            case "orange":
                System.out.println(color);
                System.out.println(empProfileRepository.orangeEmployee());
                return empProfileRepository.orangeEmployee();

            default:
                return empProfileRepository.redEmployee();
        }

    }

    public List<EmployeeProfile> getCSVFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<EmployeeProfile> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(EmployeeProfile.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<EmployeeProfile> employees = csvToBean.parse();

            return employees;
        }
    }

    public Validation validateProfiles(List<EmployeeProfile> employeeProfiles) throws IllegalAccessException {
        int counter = 0;
        int invalid = 0;
        HashMap<String, List<String>> errorMap = new HashMap<>();
        Validation validatedObj = new Validation();
        List<EmployeeProfile> emp = new ArrayList<>();
        String empId;
        String empName;
        String empPhone;
        String empEmail;
        int empExperience;
        List<String> empTechStack;
        int projId;
        Date allocation_expiration_date;
        Date allocation_start_date;
        String a = "Expiration date should be null when projId=100";
        for (int i = 1; i < employeeProfiles.size(); i++) {
            List<String> list = new ArrayList<>();
            empId = String.valueOf(employeeProfiles.get(i).getEmpId());
            empName = employeeProfiles.get(i).getEmpName();
            empPhone = employeeProfiles.get(i).getEmpPhone();
            empEmail = employeeProfiles.get(i).getEmpEmail();
            empTechStack = employeeProfiles.get(i).getEmpTechStack();
            empExperience = employeeProfiles.get(i).getEmpExperience();
            projId = employeeProfiles.get(i).getProjId();
            allocation_expiration_date = employeeProfiles.get(i).getAllocation_expiration_date();
            allocation_start_date = employeeProfiles.get(i).getAllocation_start_date();
            if (!empId.equals("0") && !empName.equals("") && !empPhone.equals("") && !empEmail.equals("") && !empTechStack.equals("") && empExperience != 0 && allocation_start_date != null) {
                if (projId != 100 && allocation_expiration_date != null) {
                    counter++;
                    emp.add(employeeProfiles.get(i));

                } else {
                    if (projId == 100 && allocation_expiration_date == null) {
                        counter++;
                        emp.add(employeeProfiles.get(i));
                    } else {

                        list.add(a);
                        errorMap.put(empId, list);

                    }

                }
            } else {

                if (empName.equals(""))
                    list.add("EmpName field is empty");
                if (empEmail.equals(""))
                    list.add("Email field is empty");
                if (empPhone.equals(""))
                    list.add("Phone field is empty");
                if (empTechStack.isEmpty())
                    list.add("Tech stack field empty");
                if (empExperience == 0)
                    list.add("Experience field is empty");
                if (allocation_start_date == null)
                    list.add("Start date field is empty");
                if (allocation_expiration_date == null) {
                    if (projId != 100)
                        list.add("Expiration date field cannot be empty");
                    else
                        list.add(a);
                }
                if (projId == 0)
                    list.add("ProjectId field cannot be empty");

                if (empId.equals("0") ){
                    list.add("EmpId field is empty");
                    errorMap.put(empName, list);
                }

                else
                errorMap.put(empId, list);
            }
        }
        validatedObj.setCounter(counter);
        validatedObj.setHashMap(errorMap);
        empProfileRepository.saveAll(emp);
        return validatedObj;
    }
}
