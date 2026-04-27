package com.cg.service;


import com.cg.entity.Stay;
import java.time.LocalDateTime;
import java.util.List;

public interface StayService {

    List<Stay> getAll();

    Stay getById(Integer id);

    List<Stay> getByPatientSsn(Long ssn);

    List<Stay> getByRoomNumber(Integer roomNumber);

    List<Stay> getStaysAfter(LocalDateTime date);

    List<Stay> getActiveStays();

    List<Stay> getPatientStayHistory(Long ssn);

    boolean exists(Integer id);

    long count();
}