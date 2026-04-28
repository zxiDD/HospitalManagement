package com.cg.service;

import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;
import com.cg.repo.PrescribesRepository;
import com.cg.service.PrescribesService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescribesServiceImpl implements PrescribesService {

    private final PrescribesRepository repo;

    public PrescribesServiceImpl(PrescribesRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Prescribes> getAll() {
        return repo.findAll();
    }

    @Override
    public Prescribes getById(PrescribesId id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    @Override
    public List<Prescribes> getAllSorted() {
        return repo.findAll(Sort.by("id.date")); // sort by date
    }

    @Override
    public long count() {
        return repo.count();
    }
}