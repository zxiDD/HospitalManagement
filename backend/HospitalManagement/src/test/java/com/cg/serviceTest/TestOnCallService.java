package com.cg.serviceTest;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;
import com.cg.exception.*;
import com.cg.repo.OnCallRepository;
import com.cg.service.OnCallServiceImpl;

@ExtendWith(MockitoExtension.class)
class TestOnCallService {

    @Mock
    private OnCallRepository repo;

    @InjectMocks
    private OnCallServiceImpl service;

    private OnCall o;
    private OnCallId id;

    @BeforeEach
    void setup() {
        id = new OnCallId();
        o = new OnCall();
        o.setId(id);
        o.setOnCallStart(LocalDateTime.now());
        o.setOnCallEnd(LocalDateTime.now().plusHours(2));
    }

    @Test
    void testGetByIdSuccess() {
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(o));

        Assertions.assertNotNull(service.getById(id));

        Mockito.verify(repo).findById(id);
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
        Mockito.when(repo.save(o)).thenReturn(o);

        Assertions.assertNotNull(service.save(o));
    }


    @Test
    void testDuplicate() {
        Mockito.when(repo.existsById(id)).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> service.save(o));
    }

    @Test
    void testInvalidTime() {
        o.setOnCallEnd(LocalDateTime.now().minusHours(1));

        Assertions.assertThrows(IllegalOperationException.class,
                () -> service.save(o));
    }


    @Test
    void testDelete() {
        service.delete(id);

        Mockito.verify(repo).deleteById(id);
    }
}