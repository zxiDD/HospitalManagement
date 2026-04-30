package com.cg.serviceTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.entity.Procedures;
import com.cg.repo.ProceduresRepository;
import com.cg.service.ProceduresServiceImpl;

@ExtendWith(MockitoExtension.class)
class TestProceduresService {

    @Mock
    private ProceduresRepository repo;

    @InjectMocks
    private ProceduresServiceImpl service;

    private Procedures p;

    @BeforeEach
    void setup() {
        p = new Procedures();
    }

    // ✅ GET ALL
    @Test
    void testGetAll() {
        Mockito.when(repo.findAll()).thenReturn(List.of(p));

        Assertions.assertFalse(service.getAllProcedures().isEmpty());
    }

    // ✅ GET BY ID
    @Test
    void testGetById() {
        Mockito.when(repo.findById(1)).thenReturn(Optional.of(p));

        Assertions.assertTrue(service.getProcedureById(1).isPresent());
    }

    // ✅ GET BY NAME
    @Test
    void testByName() {
        Mockito.when(repo.findByName("test")).thenReturn(List.of(p));

        Assertions.assertFalse(service.getProceduresByName("test").isEmpty());
    }

    // ✅ GET BY COST
    @Test
    void testByCost() {
        Mockito.when(repo.findByCost(BigDecimal.TEN)).thenReturn(List.of(p));

        Assertions.assertFalse(service.getProceduresByCost(BigDecimal.TEN).isEmpty());
    }

    // ✅ SAVE
    @Test
    void testSave() {
        Mockito.when(repo.save(p)).thenReturn(p);

        Assertions.assertNotNull(service.saveProcedures(p));
    }
}