package com.albertsons.poc.employeeprofilemanagementsystem.dao;

import com.albertsons.poc.employeeprofilemanagementsystem.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Integer> {
    List<EmployeeProfile> findByProjId(int id);
    @Query(value = " select  count(CASE WHEN proj_id!=0 THEN 1 END)as Allocated " +
            ",count(CASE WHEN proj_id=0 THEN 1 END)as Bench ," +
            "count(CASE WHEN TIMESTAMPDIFF(day,allocation_start_date,CURRENT_DATE())<=5 and  proj_id=0 THEN 1 END) as yellow," +
            "count(CASE WHEN TIMESTAMPDIFF(day,allocation_start_date,CURRENT_DATE())>5 and TIMESTAMPDIFF(day,allocation_start_date,CURRENT_DATE())<=10  and proj_id=0 THEN 1 END) as orange ," +
            "count(CASE WHEN TIMESTAMPDIFF(day,allocation_start_date,CURRENT_DATE())>10 and  proj_id=0 THEN 1 END) as red " +
            "from  profile", nativeQuery = true)
    Map<String, Object> EmpOnLocDetails();


   @Query("from EmployeeProfile profile where TIMESTAMPDIFF(day,profile.allocation_start_date,CURRENT_DATE())<=5 and profile.projId=0")
    List<EmployeeProfile> yellowEmployee();

    @Query("from EmployeeProfile profile where TIMESTAMPDIFF(day,profile.allocation_start_date,CURRENT_DATE())>5  and TIMESTAMPDIFF(day,profile.allocation_start_date,CURRENT_DATE())<=10 and profile.projId=0")
    List<EmployeeProfile> orangeEmployee();

    @Query("from EmployeeProfile profile where TIMESTAMPDIFF(day,profile.allocation_start_date,CURRENT_DATE())>10 and profile.projId=0 ")
    List<EmployeeProfile> redEmployee();

}
