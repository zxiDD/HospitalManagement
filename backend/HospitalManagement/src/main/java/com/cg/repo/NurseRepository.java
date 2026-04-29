package com.cg.repo;

import com.cg.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {
	
	List<Nurse> findByIsActiveTrue();
}