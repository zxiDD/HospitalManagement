package com.cg.repo;


import com.cg.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {

    // By patient ID (preferred)
    List<Stay> findByPatientSsn(Long patientId);

    // By room ID (preferred)
    List<Stay> findByRoomRoomNumber(Integer roomNumber);

    // Stays after a date
    List<Stay> findByStayStartAfter(LocalDateTime date);

    // Active stays (FIXED)
    @Query("SELECT s FROM Stay s WHERE s.stayStart <= :now AND (s.stayEnd IS NULL OR s.stayEnd >= :now)")
    List<Stay> findActiveStays(LocalDateTime now);

    // Ordered stays
    List<Stay> findByPatientSsnOrderByStayStartDesc(Long patientId);
}