package com.cg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;

public interface TrainedInRepository extends JpaRepository<TrainedIn, TrainedInId> {
	List<TrainedIn> findById_Physician(Integer physicianId);
	List<TrainedIn> findById_Treatment(Integer treatmentId);
}