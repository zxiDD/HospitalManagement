package com.cg.serviceTest;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.dto.DepartmentDTO;
import com.cg.entity.*;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AffiliatedWithRepository;
import com.cg.repo.DepartmentRepository;
import com.cg.repo.PhysicianRepository;
import com.cg.service.AffiliatedWithService;
import com.cg.service.AffiliatedWithServiceImpl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class AffiliatedWithServiceTest {

    @MockitoBean
    private AffiliatedWithRepository affiliatedRepo;

    @MockitoBean
    private PhysicianRepository physicianRepo;

    @MockitoBean
    private DepartmentRepository departmentRepo;

    @Autowired
//    @InjectMocks
    private AffiliatedWithServiceImpl service;

    private AffiliatedWith aff;
    private Physician physician;
    private Department department;
    private AffiliatedWithId id;

    @BeforeEach
    void setup() {

        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr. John");

        department = new Department();
        department.setDepartmentId(10);
        department.setName("Cardiology");
        department.setHead(physician);

        id = new AffiliatedWithId(1, 10);

        aff = new AffiliatedWith();
        aff.setId(id);
        aff.setPhysician(physician);
        aff.setDepartment(department);
        aff.setPrimaryAffiliation(true);
    }

    // ===================== GET METHODS =====================

    @Test
    void testGetAll() {

        Mockito.when(affiliatedRepo.findAll())
                .thenReturn(List.of(aff));

        List<AffiliatedWithDTO> result = service.getAll();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(affiliatedRepo).findAll();
    }

    @Test
    void testGetById_success() {

        Mockito.when(affiliatedRepo.findById(id))
                .thenReturn(Optional.of(aff));

        AffiliatedWithDTO result = service.getById(id);

        Assertions.assertNotNull(result);
        Mockito.verify(affiliatedRepo).findById(id);
    }

    @Test
    void testGetById_notFound() {

        Mockito.when(affiliatedRepo.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getById(id));

        Mockito.verify(affiliatedRepo).findById(id);
    }

    @Test
    void testGetByPhysicianId() {

        Mockito.when(affiliatedRepo.findByPhysician_EmployeeId(1))
                .thenReturn(List.of(aff));

        List<AffiliatedWithDTO> result = service.getByPhysicianId(1);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(affiliatedRepo).findByPhysician_EmployeeId(1);
    }

    @Test
    void testGetByDepartmentId() {

        Mockito.when(affiliatedRepo.findByDepartment_DepartmentId(10))
                .thenReturn(List.of(aff));

        List<AffiliatedWithDTO> result = service.getByDepartmentId(10);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(affiliatedRepo).findByDepartment_DepartmentId(10);
    }

    @Test
    void testGetPrimaryAffiliations() {

        Mockito.when(affiliatedRepo.findByPrimaryAffiliationTrue())
                .thenReturn(List.of(aff));

        List<AffiliatedWithDTO> result = service.getPrimaryAffiliations();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(affiliatedRepo).findByPrimaryAffiliationTrue();
    }

    @Test
    void testGetPrimaryDepartment_success() {

        Mockito.when(affiliatedRepo
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1))
                .thenReturn(Optional.of(aff));

        DepartmentDTO result = service.getPrimaryDepartment(1);

        Assertions.assertEquals("Cardiology", result.getName());
        Mockito.verify(affiliatedRepo)
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1);
    }

    @Test
    void testGetPrimaryDepartment_notFound() {

        Mockito.when(affiliatedRepo
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getPrimaryDepartment(1));

        Mockito.verify(affiliatedRepo)
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1);
    }

    // ===================== CREATE =====================

    @Test
    void testCreate_success() {

        AffiliatedWithDTO input =
                new AffiliatedWithDTO(1, null, 10, null, true);

        Mockito.when(affiliatedRepo
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1))
                .thenReturn(Optional.empty());

        Mockito.when(physicianRepo.findById(1))
                .thenReturn(Optional.of(physician));

        Mockito.when(departmentRepo.findById(10))
                .thenReturn(Optional.of(department));

        Mockito.when(affiliatedRepo.save(Mockito.any()))
                .thenReturn(aff);

        AffiliatedWithDTO result = service.create(input);

        Assertions.assertNotNull(result);

        Mockito.verify(affiliatedRepo)
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1);
        Mockito.verify(physicianRepo).findById(1);
        Mockito.verify(departmentRepo).findById(10);
        Mockito.verify(affiliatedRepo).save(Mockito.any());
    }

    @Test
    void testCreate_duplicatePrimary() {

        AffiliatedWithDTO input =
                new AffiliatedWithDTO(1, null, 10, null, true);

        Mockito.when(affiliatedRepo
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1))
                .thenReturn(Optional.of(aff));

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> service.create(input));

        Mockito.verify(affiliatedRepo)
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(1);

        Mockito.verify(affiliatedRepo, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void testCreate_physicianNotFound() {

        AffiliatedWithDTO input =
                new AffiliatedWithDTO(1, null, 10, null, false);

        Mockito.when(physicianRepo.findById(1))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.create(input));

        Mockito.verify(physicianRepo).findById(1);

        Mockito.verify(affiliatedRepo, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void testCreate_departmentNotFound() {

        AffiliatedWithDTO input =
                new AffiliatedWithDTO(1, null, 10, null, false);

        Mockito.when(physicianRepo.findById(1))
                .thenReturn(Optional.of(physician));

        Mockito.when(departmentRepo.findById(10))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.create(input));

        Mockito.verify(physicianRepo).findById(1);
        Mockito.verify(departmentRepo).findById(10);

        Mockito.verify(affiliatedRepo, Mockito.never())
                .save(Mockito.any());
    }
}