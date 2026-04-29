package com.cg.service;

import com.cg.entity.Nurse;

import java.util.List;

public interface NurseService {

    List<Nurse> getAll();

    Nurse getById(Integer id);

    List<Nurse> getAllSorted();

    boolean exists(Integer id);
    
    Nurse save(Nurse nurse);

    long count();
    
    void delete(Integer employeeId);
}