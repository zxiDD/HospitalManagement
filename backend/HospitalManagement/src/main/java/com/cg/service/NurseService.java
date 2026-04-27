package com.cg.service;

import com.cg.entity.Nurse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NurseService {

    List<Nurse> getAll();

    Nurse getById(Integer id);

//    Page<Nurse> getAllPaged(int page, int size);

    List<Nurse> getAllSorted();

    boolean exists(Integer id);

    long count();
}