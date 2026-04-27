package com.cg.repo;


import com.cg.entity.Stay;
import com.cg.entity.Patient;
import com.cg.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {

    // All stays of a specific patient
    List<Stay> findByPatient(Patient patient);

    // All stays in a specific room
    List<Stay> findByRoom(Room room);

    // Stays that started after a given date
    List<Stay> findByStayStartAfter(LocalDateTime date);

    // Stays that are currently active (started but not yet ended)
    @Query("SELECT s FROM Stay s WHERE s.stayStart <= :now AND s.stayEnd >= :now")
    List<Stay> findActiveStays(LocalDateTime now);

    // All stays for a patient ordered by start date
    List<Stay> findByPatientOrderByStayStartDesc(Patient patient);

}