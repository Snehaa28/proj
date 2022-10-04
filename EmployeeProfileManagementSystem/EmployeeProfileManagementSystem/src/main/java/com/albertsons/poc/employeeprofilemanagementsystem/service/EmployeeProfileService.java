package com.albertsons.poc.employeeprofilemanagementsystem.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.albertsons.poc.employeeprofilemanagementsystem.entity.EmployeeProfile;
import com.albertsons.poc.employeeprofilemanagementsystem.entity.Validation;
import com.albertsons.poc.employeeprofilemanagementsystem.exception.EmployeeIdNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeProfileService {

	public EmployeeProfile retrieveEmpProfile(int id) throws EmployeeIdNotFoundException;
	public void createEmpProfile(EmployeeProfile empProfile); 
	public void deleteEmpProfile(int id);
	public List<EmployeeProfile> retrieveAllEmpProfiles();
	public List<EmployeeProfile> getEmployeesByProjId(int projId);
	public Map<String, Object> EmpOnLocDeatils();
	public List<EmployeeProfile> EmployeeOnLoc(String color);
	public List<EmployeeProfile> getCSVFile(MultipartFile file) throws IOException;
	public Validation validateProfiles(List<EmployeeProfile> employeeProfiles) throws IllegalAccessException;
}
