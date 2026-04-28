package com.cg.service;

import com.cg.dto.StayDTO;
import com.cg.entity.Patient;
import com.cg.entity.Room;
import com.cg.entity.Stay;
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
    
    @Override
    public StayDTO create(StayDTO dto) {

        // 🔹 Fetch Patient
        Patient patient = patientRepository.findById(dto.getPatientSsn())
                .orElseThrow(() -> new RuntimeException(
                        "Patient not found with ssn: " + dto.getPatientSsn()));

        // 🔹 Fetch Room
        Room room = roomRepository.findById(dto.getRoomNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Room not found with number: " + dto.getRoomNumber()));


        Stay stay = new Stay();
        stay.setPatient(patient);
        stay.setRoom(room);
        stay.setStayStart(dto.getStayStart());
        stay.setStayEnd(dto.getStayEnd());

        Stay saved = stayRepository.save(stay);

        return new StayDTO(
                saved.getStayId(), // auto-generated
                saved.getPatient().getSsn(),
                saved.getPatient().getName(),
                saved.getRoom().getRoomNumber(),
                saved.getRoom().getRoomType(),
                saved.getStayStart(),
                saved.getStayEnd()
        );
    }
    
}