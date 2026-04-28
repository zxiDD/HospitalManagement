package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;
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
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<TrainedIn> getByPhysicianId(Integer physicianId) {
        return repository.findById_Physician(physicianId);
    }

    @Override
    public List<TrainedIn> getByTreatmentId(Integer treatmentId) {
        return repository.findById_Treatment(treatmentId);
    }

}