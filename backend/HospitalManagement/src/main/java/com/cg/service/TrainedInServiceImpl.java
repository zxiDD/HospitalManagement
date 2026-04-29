package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;
import com.cg.exception.BadRequestException;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.IllegalOperationException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.TrainedInRepository;

@Service
public class TrainedInServiceImpl implements TrainedInService {

    @Autowired
    private TrainedInRepository repository;

    @Override
    public List<TrainedIn> getAll() {
        return repository.findAll();
    }

    @Override
    public TrainedIn getById(TrainedInId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training record not found"));
    }

    @Override
    public List<TrainedIn> getByPhysicianId(Integer physicianId) {
        return repository.findById_Physician(physicianId);
    }

    @Override
    public List<TrainedIn> getByTreatmentId(Integer treatmentId) {
        return repository.findById_Treatment(treatmentId);
    }
    
    @Override
    public TrainedIn save(TrainedIn t) {

        if (t.getId() == null) {
            throw new BadRequestException("Training ID cannot be null");
        }

        if (repository.existsById(t.getId())) {
            throw new DuplicateResourceException("Training already exists");
        }

        if (t.getCertificationExpires().isBefore(t.getCertificationDate())) {
            throw new IllegalOperationException("Expiry cannot be before certification date");
        }

        return repository.save(t);
    }

}