
package com.cg.repo;

import com.cg.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
	List<Physician> findByPositionAndIsActiveTrue(String position);
	
	List<Physician> findByIsActiveTrue();
	
	Optional<Physician> findByEmployeeIdAndIsActiveTrue(Integer employeeId);
}