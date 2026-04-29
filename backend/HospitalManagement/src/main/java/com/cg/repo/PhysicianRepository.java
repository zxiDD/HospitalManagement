
package com.cg.repo;

import com.cg.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
	List<Physician> findByPosition(String position);
}