package com.cg.service;

import com.cg.entity.Nurse;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.BadRequestException;
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

    @Override
    public List<Nurse> getAll() {
        return repo.findAll();
    }

    @Override
    public Nurse getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Nurse not found with id: " + id)
                );
    }

    @Override
    public List<Nurse> getAllSorted() {
        return repo.findAll(Sort.by("name"));
    }

    @Override
    public boolean exists(Integer id) {
        return repo.existsById(id);
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public Nurse save(Nurse nurse) {

        if (repo.existsById(nurse.getEmployeeId())) {
            throw new BadRequestException(
                    "Nurse already exists with id: " + nurse.getEmployeeId()
            );
        }

        return repo.save(nurse);
    }
    
    @Override
    public void delete(Integer employeeId) {

        Nurse nurse = repo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));

        nurse.setIsActive(false); 

        repo.save(nurse);
    }
    
}