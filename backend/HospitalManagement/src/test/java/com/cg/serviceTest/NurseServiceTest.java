package com.cg.serviceTest;

import com.cg.entity.Nurse;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.NurseRepository;
import com.cg.service.NurseServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class NurseServiceTest {

    @Mock
    private NurseRepository repo;

    @InjectMocks
    private NurseServiceImpl service;

    private Nurse nurse;
    private Optional<Nurse> optNurse1, optNurse2;

    @BeforeEach
    void setUp() {
        nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John");
        nurse.setPosition("Staff");
        nurse.setRegistered(true);
        nurse.setSsn(12345L);
        nurse.setIsActive(true);

        optNurse1 = Optional.of(nurse);
        optNurse2 = Optional.empty();
    }

    @Test
    void testGetAll() {
        Mockito.when(repo.findAll()).thenReturn(List.of(nurse));

        List<Nurse> result = service.getAll();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(repo).findAll();
    }

    @Test
    void testGetById_Success() {
        Mockito.when(repo.findById(1)).thenReturn(optNurse1);

        Nurse result = service.getById(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("John", result.getName());
    }

    @Test
    void testGetById_NotFound() {
        Mockito.when(repo.findById(1)).thenReturn(optNurse2);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1));
    }

    @Test
    void testSave_Success() {
        Mockito.when(repo.existsById(1)).thenReturn(false);
        Mockito.when(repo.save(nurse)).thenReturn(nurse);

        Nurse saved = service.save(nurse);

        Assertions.assertNotNull(saved);
        Mockito.verify(repo).save(nurse);
    }

    @Test
    void testSave_AlreadyExists() {
        Mockito.when(repo.existsById(1)).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class,
                () -> service.save(nurse));
    }

    @Test
    void testDelete_Success() {
        Mockito.when(repo.findById(1)).thenReturn(optNurse1);

        service.delete(1);

        Assertions.assertFalse(nurse.getIsActive());
        Mockito.verify(repo).save(nurse);
    }

    @Test
    void testDelete_NotFound() {
        Mockito.when(repo.findById(1)).thenReturn(optNurse2);

        Assertions.assertThrows(RuntimeException.class,
                () -> service.delete(1));
    }
}