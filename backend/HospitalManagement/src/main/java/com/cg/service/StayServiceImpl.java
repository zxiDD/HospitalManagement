package com.cg.service;

import com.cg.dto.StayDTO;
import com.cg.entity.Patient;
import com.cg.entity.Room;
import com.cg.entity.Stay;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PatientRepository;
import com.cg.repo.RoomRepository;
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

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoomRepository roomRepository;
    
    // 🔹 helper method (cleaner code)
    private StayDTO mapToDTO(Stay s) {
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
    public List<StayDTO> getAll() {
        List<Stay> list = stayRepository.findAll();
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(mapToDTO(s));
        }

        return dtoList;
    }

    @Override
    public StayDTO getById(Integer id) {
    	if(id<=0) {
    		throw new BadRequestException("Id can not be negative or 0");
    	}
        Stay s = stayRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Stay not found with id: " + id));

        return mapToDTO(s);
    }

    @Override
    public List<StayDTO> getByPatientSsn(Long ssn) {
        List<Stay> list = stayRepository.findByPatient_Ssn(ssn);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(mapToDTO(s));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getByRoomNumber(Integer roomNumber) {
    	if(roomNumber<=0) {
    		throw new BadRequestException("Id can not be negative or 0");
    	}
        List<Stay> list = stayRepository.findByRoom_RoomNumber(roomNumber);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(mapToDTO(s));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getStaysAfter(LocalDateTime date) {
        List<Stay> list = stayRepository.findByStayStartAfter(date);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(mapToDTO(s));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getActiveStays() {
        List<Stay> list = stayRepository.findActiveStays(LocalDateTime.now());
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(mapToDTO(s));
        }

        return dtoList;
    }

    @Override
    public List<StayDTO> getPatientStayHistory(Long ssn) {
        List<Stay> list = stayRepository.findByPatientSsnOrderByStayStartDesc(ssn);
        List<StayDTO> dtoList = new ArrayList<>();

        for (Stay s : list) {
            dtoList.add(mapToDTO(s));
        }

        return dtoList;
    }

//    @Override
//    public boolean exists(Integer id) {
//        return stayRepository.existsById(id);
//    }
//
//    @Override
//    public long count() {
//        return stayRepository.count();
//    }

    @Override
    public StayDTO create(StayDTO dto) {

        Patient patient = patientRepository.findById(dto.getPatientSsn())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found with ssn: " + dto.getPatientSsn()));

        Room room = roomRepository.findById(dto.getRoomNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room not found with number: " + dto.getRoomNumber()));

        Stay stay = new Stay();
        stay.setPatient(patient);
        stay.setRoom(room);
        stay.setStayStart(dto.getStayStart());
        stay.setStayEnd(dto.getStayEnd());

        Stay saved = stayRepository.save(stay);

        return mapToDTO(saved);
    }

    @Override
    public boolean isPatientActive(Long ssn) {
        return stayRepository.existsActiveStayByPatient(ssn);
    }


}