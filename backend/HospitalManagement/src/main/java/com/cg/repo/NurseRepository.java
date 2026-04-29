package com.cg.repo;

import com.cg.entity.Nurse;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {
	
	List<Nurse> findByIsActiveTrue();

	@Query("""
			    SELECT COUNT(oc) > 0
			    FROM OnCall oc
			    WHERE oc.nurse.employeeId = :nurseId
			      AND oc.onCallStart <= :start
			      AND oc.onCallEnd   >= :end
			""")
	boolean isNurseOnCallDuring(@Param("nurseId") Integer nurseId, @Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}