package com.cg.service;

import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PrescribesService {

    List<Prescribes> getAll();

    Prescribes getById(PrescribesId id);
    
    List<Prescribes> getAllSorted();

    long count();
}