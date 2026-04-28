package com.cg.service;

import com.cg.entity.Stay;
import com.cg.repo.StayRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StayServiceImpl implements StayService {
	@Autowired
    private StayRepository stayRepository;

    @Override
    public List<Stay> getAll() {
        return stayRepository.findAll();
    }

    @Override
    public Stay getById(Integer id) {
        return stayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stay not found with id: " + id));
    }

    @Override
    public List<Stay> getByPatientSsn(Long ssn) {
        return stayRepository.findByPatient_Ssn(ssn);
    }

    @Override
    public List<Stay> getByRoomNumber(Integer roomNumber) {
        return stayRepository.findByRoom_RoomNumber(roomNumber);
    }

    @Override
    public List<Stay> getStaysAfter(LocalDateTime date) {
        return stayRepository.findByStayStartAfter(date);
    }

    @Override
    public List<Stay> getActiveStays() {
        return stayRepository.findActiveStays(LocalDateTime.now());
    }

    @Override
    public List<Stay> getPatientStayHistory(Long ssn) {
        return stayRepository.findByPatientSsnOrderByStayStartDesc(ssn);
    }

    @Override
    public boolean exists(Integer id) {
        return stayRepository.existsById(id);
    }

    @Override
    public long count() {
        return stayRepository.count();
    }
}