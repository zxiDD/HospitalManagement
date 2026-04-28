package com.cg.service;

import com.cg.dto.StayDTO;
import com.cg.entity.Stay;
import com.cg.repo.StayRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StayServiceImpl implements StayService {

    @Autowired
    private StayRepository stayRepository;

    @Override
    public List<StayDTO> getAll() {
        List<Stay> list = stayRepository.findAll();
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(new StayDTO(
                    s.getStayId(),
                    s.getPatient().getSsn(),
                    s.getPatient().getName(),
                    s.getRoom().getRoomNumber(),
                    s.getRoom().getRoomType(),
                    s.getStayStart(),
                    s.getStayEnd()
            ));
        }

        return dtoList;
    }

    @Override
    public StayDTO getById(Integer id) {
        Stay s = stayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stay not found with id: " + id));

        return new StayDTO(
                s.getStayId(),
                s.getPatient().getSsn(),
                s.getPatient().getName(),
                s.getRoom().getRoomNumber(),
                s.getRoom().getRoomType(),
                s.getStayStart(),
                s.getStayEnd()
        );
    }

    @Override
    public List<StayDTO> getByPatientSsn(Long ssn) {
        List<Stay> list = stayRepository.findByPatient_Ssn(ssn);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(new StayDTO(
                    s.getStayId(),
                    s.getPatient().getSsn(),
                    s.getPatient().getName(),
                    s.getRoom().getRoomNumber(),
                    s.getRoom().getRoomType(),
                    s.getStayStart(),
                    s.getStayEnd()
            ));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getByRoomNumber(Integer roomNumber) {
        List<Stay> list = stayRepository.findByRoom_RoomNumber(roomNumber);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(new StayDTO(
                    s.getStayId(),
                    s.getPatient().getSsn(),
                    s.getPatient().getName(),
                    s.getRoom().getRoomNumber(),
                    s.getRoom().getRoomType(),
                    s.getStayStart(),
                    s.getStayEnd()
            ));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getStaysAfter(LocalDateTime date) {
        List<Stay> list = stayRepository.findByStayStartAfter(date);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(new StayDTO(
                    s.getStayId(),
                    s.getPatient().getSsn(),
                    s.getPatient().getName(),
                    s.getRoom().getRoomNumber(),
                    s.getRoom().getRoomType(),
                    s.getStayStart(),
                    s.getStayEnd()
            ));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getActiveStays() {
        List<Stay> list = stayRepository.findActiveStays(LocalDateTime.now());
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(new StayDTO(
                    s.getStayId(),
                    s.getPatient().getSsn(),
                    s.getPatient().getName(),
                    s.getRoom().getRoomNumber(),
                    s.getRoom().getRoomType(),
                    s.getStayStart(),
                    s.getStayEnd()
            ));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getPatientStayHistory(Long ssn) {
        List<Stay> list = stayRepository.findByPatientSsnOrderByStayStartDesc(ssn);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(new StayDTO(
                    s.getStayId(),
                    s.getPatient().getSsn(),
                    s.getPatient().getName(),
                    s.getRoom().getRoomNumber(),
                    s.getRoom().getRoomType(),
                    s.getStayStart(),
                    s.getStayEnd()
            ));
        }

        return dtoList;
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