package com.cg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;

public interface OnCallRepository extends JpaRepository<OnCall, OnCallId> {
	List<OnCall> findById_Nurse(Integer nurseId);
	List<OnCall> findById_BlockCode(String blockCode);
}