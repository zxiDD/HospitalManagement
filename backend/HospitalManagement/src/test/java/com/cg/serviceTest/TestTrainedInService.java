package com.cg.serviceTest;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.*;

import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;
import com.cg.exception.*;
import com.cg.repo.TrainedInRepository;
import com.cg.service.TrainedInServiceImpl;

@SpringBootTest
class TestTrainedInService {

    @MockitoBean
    private TrainedInRepository repo;

    @Autowired
    private TrainedInServiceImpl service;

    private TrainedIn t;
    private TrainedInId id;

    @BeforeEach
    void setup() {
        id = new TrainedInId();
        t = new TrainedIn();
        t.setId(id);
        t.setCertificationDate(LocalDateTime.now());
        t.setCertificationExpires(LocalDateTime.now().plusDays(5));
    }

    @Test
    void testGetByIdSuccess() {
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(t));

        Assertions.assertNotNull(service.getById(id));
    }

    @Test
    void testGetByIdFail() {
        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getById(id));
    }

 
    @Test
    void testSaveSuccess() {
        Mockito.when(repo.existsById(id)).thenReturn(false);
        Mockito.when(repo.save(t)).thenReturn(t);

        Assertions.assertNotNull(service.save(t));
    }

    @Test
    void testDuplicate() {
        Mockito.when(repo.existsById(id)).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> service.save(t));
    }

    @Test
    void testInvalidDates() {
        t.setCertificationExpires(LocalDateTime.now().minusDays(1));

        Assertions.assertThrows(IllegalOperationException.class,
                () -> service.save(t));
    }
}