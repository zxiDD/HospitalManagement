package com.cg.service;

import com.cg.entity.Nurse;
import com.cg.repo.NurseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository repo;

    public NurseServiceImpl(NurseRepository repo) {
        this.repo = repo;
    }

    public List<Nurse> getAll() {
        return repo.findAll();
    }

    public Nurse getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
    }

    public List<Nurse> getAllSorted() {
        return repo.findAll(Sort.by("name"));
    }

    public boolean exists(Integer id) {
        return repo.existsById(id);
    }

    public long count() {
        return repo.count();
    }
}