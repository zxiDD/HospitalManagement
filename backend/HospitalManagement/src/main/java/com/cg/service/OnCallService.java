package com.cg.service;

import java.util.List;

import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;

public interface OnCallService {

    List<OnCall> getAll();

    OnCall getById(OnCallId id);

    List<OnCall> getByNurseId(Integer nurseId);

    List<OnCall> getByBlockCode(String blockCode);
}