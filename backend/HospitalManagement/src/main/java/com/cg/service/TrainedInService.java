package com.cg.service;

import java.util.List;

import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;

public interface TrainedInService {

    List<TrainedIn> getAll();

    TrainedIn getById(TrainedInId id);

    List<TrainedIn> getByPhysicianId(Long physicianId);

    List<TrainedIn> getByTreatmentId(Long treatmentId);
}