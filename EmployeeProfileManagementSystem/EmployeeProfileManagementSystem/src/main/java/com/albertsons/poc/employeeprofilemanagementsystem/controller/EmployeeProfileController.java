package com.albertsons.poc.employeeprofilemanagementsystem.controller;

import com.albertsons.poc.employeeprofilemanagementsystem.entity.Validation;
import com.albertsons.poc.employeeprofilemanagementsystem.exception.EmployeeIdNotFoundException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.albertsons.poc.employeeprofilemanagementsystem.entity.EmployeeProfile;
import com.albertsons.poc.employeeprofilemanagementsystem.service.EmployeeProfileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class EmployeeProfileController {

	@Autowired
    EmployeeProfileService empProfileService;
	@GetMapping(value = "/EmpProfiles/{id}") 
	public ResponseEntity<EmployeeProfile> getEmpProfile(@PathVariable("id") int id){
		try {
			return new ResponseEntity<>(empProfileService.retrieveEmpProfile(id), HttpStatus.OK );
		}
		
		catch(EmployeeIdNotFoundException e) {
			 return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND );
		}
	}

	@GetMapping(value = "/ProjectId/{id}")
	public ResponseEntity<?> getEmployeesByProjId(@PathVariable("id") int id){
			return new ResponseEntity<>(empProfileService.getEmployeesByProjId(id), HttpStatus.OK );

	}
	
	@GetMapping(value = "/EmpProfiles")
	public ResponseEntity<?> getAllEmpProfiles() {
		return new ResponseEntity<>(empProfileService.retrieveAllEmpProfiles(), HttpStatus.OK);
	}

	@PostMapping(value = "/EmpProfiles")
	public ResponseEntity<Object> postEmpProfile(@RequestBody EmployeeProfile empProfile) {
		empProfileService.createEmpProfile(empProfile);
		return new ResponseEntity<>("Employee Profile is created successsfully", HttpStatus.OK);
	}

	@DeleteMapping(value = "/EmpProfiles/{id}")
	public ResponseEntity<Object> deleteEmployeeProfile(@PathVariable("id") int id) {
		empProfileService. deleteEmpProfile(id);
		return new ResponseEntity<>("Employee Profile is deleted successsfully", HttpStatus.OK);
	}
	@GetMapping(value = "/EmpProfilesLocDetails")
	public Map<String, Object> EmpOnLocDetailList() {
		return empProfileService.EmpOnLocDeatils();
	}

	@GetMapping(value="/EmployeeOnBench/{color}")
	public List<EmployeeProfile> EmployeeDetails(@PathVariable("color") String color){
		return empProfileService.EmployeeOnLoc(color);
	}

	@GetMapping("/upload-csv-file")
	public Validation uploadCSVFile(@RequestParam("file") MultipartFile file) throws IOException, IllegalAccessException {

		List<EmployeeProfile> employeeProfiles= empProfileService.getCSVFile(file);
		 Validation v=empProfileService.validateProfiles(employeeProfiles);
		 return v;
}

}
