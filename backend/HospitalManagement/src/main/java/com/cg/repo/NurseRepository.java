package com.cg.repo;

import com.cg.entity.Nurse;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {
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