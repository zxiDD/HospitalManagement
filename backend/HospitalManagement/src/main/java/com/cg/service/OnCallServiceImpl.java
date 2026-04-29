package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;
import com.cg.repo.OnCallRepository;

@Service
public class OnCallServiceImpl implements OnCallService {

    @Autowired
    private OnCallRepository repository;

    @Override
    public List<OnCall> getAll() {
        return repository.findAll();
    }

    @Override
    public OnCall getById(OnCallId id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<OnCall> getByNurseId(Integer nurseId) {
        return repository.findById_Nurse(nurseId);
    }

    @Override
    public List<OnCall> getByBlockCode(String blockCode) {
        return repository.findById_BlockCode(blockCode);
    }
    
    @Override
    public OnCall save(OnCall o) {
        return repository.save(o);
    }
    
    @Override
    public void delete(OnCallId id) {
        repository.deleteById(id);
    }

}