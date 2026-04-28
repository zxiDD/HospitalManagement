package com.cg.service;

import com.cg.dto.StayDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface StayService {

    List<StayDTO> getAll();

    StayDTO getById(Integer id);

    List<StayDTO> getByPatientSsn(Long ssn);

    List<StayDTO> getByRoomNumber(Integer roomNumber);

    List<StayDTO> getStaysAfter(LocalDateTime date);

    List<StayDTO> getActiveStays();

    List<StayDTO> getPatientStayHistory(Long ssn);

    boolean exists(Integer id);

    long count();
}