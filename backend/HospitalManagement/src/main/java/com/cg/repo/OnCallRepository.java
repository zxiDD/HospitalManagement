package com.cg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;

public interface OnCallRepository extends JpaRepository<OnCall, OnCallId> {
	List<OnCall> findByIdNurseId(Long nurseId);
	List<OnCall> findByIdBlockCode(String blockCode);
}