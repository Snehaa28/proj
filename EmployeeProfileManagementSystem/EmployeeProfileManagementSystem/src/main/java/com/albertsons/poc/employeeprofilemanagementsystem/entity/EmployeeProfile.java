package com.albertsons.poc.employeeprofilemanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity

@Table(name="profile")
public class EmployeeProfile {

	@Id
	@CsvBindByName
	private int empId;
	@CsvBindByName
	private String empName;
	@CsvBindByName
	//@Pattern(regexp="(^$|[0-9]{10})")
	private String empPhone;
	@CsvBindByName
	private String empEmail;
	@CsvBindByName
	private int empExperience;
	//@CsvBindByName
	@Convert(converter = StringListConverter.class)

	@CsvBindAndSplitByName(elementType= String.class,splitOn = "\\|",collectionType = ArrayList.class )
	private List<String> empTechStack;
	@CsvBindByName
	private int projId;
	@CsvBindByName
	@CsvDate(value = "dd-MM-yyyy")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date allocation_expiration_date;
	@CsvBindByName
	@CsvDate(value = "dd-MM-yyyy")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date allocation_start_date;


	public EmployeeProfile() {
		super();
	}
	
	public EmployeeProfile(int empId, String empName,String empEmail,String empPhone, int empExperience, List<String> empTechStack,
			int projId,Date allocation_expiration_date,Date allocation_start_date) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empPhone = empPhone;
		this.empEmail=empEmail;
		this.empExperience = empExperience;
		this.empTechStack = empTechStack;
		this.projId = projId;
		this.allocation_expiration_date=allocation_expiration_date;
		this.allocation_start_date=allocation_start_date;

	}


	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpPhone() {
		return empPhone;
	}

	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	public int getEmpExperience() {
		return empExperience;
	}

	public void setEmpExperience(int empExperience) {
		this.empExperience = empExperience;
	}

	public List<String> getEmpTechStack() {
		return empTechStack;
	}

	public void setEmpTechStack(List<String> empTechStack) {
		this.empTechStack = empTechStack;
	}

	public int getProjId() {
		return projId;
	}

	public void setProjId(int projId) {
		this.projId = projId;
	}

	public Date getAllocation_start_date() {
		return allocation_start_date;
	}
	public void setAllocation_start_date(Date allocation_start_date) {
		this.allocation_start_date = allocation_start_date;
	}

	public Date getAllocation_expiration_date() {
		return allocation_expiration_date;
	}


	public void setAllocation_expiration_date(Date allocation_expiration_date) {
		this.allocation_expiration_date = allocation_expiration_date;
	}


}
