package com.cg.serviceTest;

import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PhysicianRepository;
import com.cg.service.PhysicianServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PhysicianServiceTest {

    @MockitoBean   // ✅ correct
    private PhysicianRepository repo;

    @Autowired   // ✅ correct
    private PhysicianServiceImpl service;

    private Physician physician;
    private Optional<Physician> optPhysician1, optPhysician2;

    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr. Smith");
        physician.setPosition("Cardiologist");
        physician.setSsn(1111L);
        physician.setIsActive(true);

        optPhysician1 = Optional.of(physician);
        optPhysician2 = Optional.empty();
    }

    @Test
    void testGetAll() {
        Mockito.when(repo.findAll()).thenReturn(List.of(physician));

        List<Physician> result = service.getAll();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(repo).findAll();
    }

    @Test
    void testGetById_Success() {
        Mockito.when(repo.findById(1)).thenReturn(optPhysician1);

        Physician result = service.getById(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Dr. Smith", result.getName());
        Mockito.verify(repo).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        Mockito.when(repo.findById(1)).thenReturn(optPhysician2);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1));

        Mockito.verify(repo).findById(1);
    }

    @Test
    void testGetAllPaged() {
        Page<Physician> page = new PageImpl<>(List.of(physician));

        Mockito.when(repo.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Physician> result = service.getAllPaged(0, 5);

        Assertions.assertEquals(1, result.getContent().size());
        Mockito.verify(repo).findAll(Mockito.any(PageRequest.class));
    }

    @Test
    void testGetByPosition() {
        Mockito.when(repo.findByPosition("Cardiologist"))
                .thenReturn(List.of(physician));

        List<Physician> result = service.getByPosition("Cardiologist");

        Assertions.assertEquals(1, result.size());
        Mockito.verify(repo).findByPosition("Cardiologist");
    }

    @Test
    void testSave_Success() {
        Mockito.when(repo.existsById(1)).thenReturn(false);
        Mockito.when(repo.save(physician)).thenReturn(physician);

        Physician saved = service.save(physician);

        Assertions.assertNotNull(saved);
        Mockito.verify(repo).save(physician);
    }

    @Test
    void testSave_AlreadyExists() {
        Mockito.when(repo.existsById(1)).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class,
                () -> service.save(physician));

        Mockito.verify(repo).existsById(1);
    }

    @Test
    void testDelete_Success() {
        Mockito.when(repo.findById(1)).thenReturn(optPhysician1);

        service.delete(1);

        Assertions.assertFalse(physician.getIsActive());
        Mockito.verify(repo).save(physician);
    }

    @Test
    void testDelete_NotFound() {
        Mockito.when(repo.findById(1)).thenReturn(optPhysician2);

        Assertions.assertThrows(RuntimeException.class,
                () -> service.delete(1));

        Mockito.verify(repo).findById(1);
    }
}