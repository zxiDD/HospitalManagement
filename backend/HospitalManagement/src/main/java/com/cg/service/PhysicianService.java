package com.cg.service;

import com.cg.entity.Physician;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PhysicianService {

    List<Physician> getAll();

    Physician getById(Integer id);

    Page<Physician> getAllPaged(int page, int size);

    List<Physician> getAllSorted();

    boolean exists(Integer id);

    long count();
}