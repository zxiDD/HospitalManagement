package com.cg.serviceTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cg.entity.Undergoes;
import com.cg.entity.UndergoesId;
import com.cg.repo.UndergoesRepository;
import com.cg.service.UndergoesServiceImpl;

@SpringBootTest
class TestUndergoesService {

    @MockitoBean
    private UndergoesRepository repo;

    @Autowired
    private UndergoesServiceImpl service;

    private Undergoes u;
    private UndergoesId id;

    @BeforeEach
    void setup() {
        id = new UndergoesId();
        u = new Undergoes();
    }


    @Test
    void testGetAll() {
        Mockito.when(repo.findAll()).thenReturn(List.of(u));

        Assertions.assertFalse(service.getAllUndergoes().isEmpty());
    }


    @Test
    void testGetById() {
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(u));

        Assertions.assertTrue(service.getUndergoesById(id).isPresent());
    }

    @Test
    void testGetByPatient() {
        Mockito.when(repo.findByPatient_Ssn(1L)).thenReturn(List.of(u));

        Assertions.assertFalse(service.getUndergoesByPatient(1L).isEmpty());
    }


    @Test
    void testBetweenDates() {
        Mockito.when(repo.findById_DateUndergoesBetween(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(u));

        Assertions.assertFalse(service.getUndergoesBetweenDates(
                LocalDateTime.now(), LocalDateTime.now()).isEmpty());
    }


    @Test
    void testSave() {
        Mockito.when(repo.save(u)).thenReturn(u);

        Assertions.assertNotNull(service.saveUndergoes(u));
    }
}