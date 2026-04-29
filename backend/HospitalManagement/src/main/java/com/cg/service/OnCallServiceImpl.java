package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;
import com.cg.exception.BadRequestException;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.IllegalOperationException;
import com.cg.exception.ResourceNotFoundException;
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
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OnCall record not found"));
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

        if (o.getId() == null) {
            throw new BadRequestException("OnCall ID cannot be null");
        }

        if (repository.existsById(o.getId())) {
            throw new DuplicateResourceException("OnCall already exists");
        }

        if (o.getOnCallEnd().isBefore(o.getOnCallStart())) {
            throw new IllegalOperationException("End time cannot be before start time");
        }

        return repository.save(o);
    }
    
    @Override
    public void delete(OnCallId id) {
        repository.deleteById(id);
    }

}