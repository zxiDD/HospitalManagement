package com.cg.service;

import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;
import com.cg.repo.PrescribesRepository;
import com.cg.service.PrescribesService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescribesServiceImpl implements PrescribesService {

    private final PrescribesRepository repo;

    public PrescribesServiceImpl(PrescribesRepository repo) {
        this.repo = repo;
    }

    public List<Prescribes> getAll() {
        return repo.findAll();
    }

    public Prescribes getById(PrescribesId id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    public Page<Prescribes> getAllPaged(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    public long count() {
        return repo.count();
    }
}