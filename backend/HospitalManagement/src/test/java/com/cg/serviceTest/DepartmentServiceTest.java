package com.cg.serviceTest;

import com.cg.dto.DepartmentDTO;
import com.cg.entity.Department;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.DepartmentRepository;
import com.cg.repo.PhysicianRepository;
import com.cg.service.DepartmentService;
import com.cg.service.DepartmentServiceImpl;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

@SpringBootTest
public class DepartmentServiceTest {

    @MockitoBean
    private DepartmentRepository departmentRepository;

    @MockitoBean
    private PhysicianRepository physicianRepository;

   @Autowired
   private DepartmentServiceImpl departmentService;   

    private Department dept;
    private Physician physician;

    @BeforeEach
    public void setup() {

        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr A");

        dept = new Department();
        dept.setDepartmentId(10);
        dept.setName("Cardiology");
        dept.setHead(physician);
    }

    @Test
    public void testGetById_success() {

        Mockito.when(departmentRepository.findById(10))
                .thenReturn(Optional.of(dept));

        DepartmentDTO result = departmentService.getById(10);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Cardiology", result.getName());

        Mockito.verify(departmentRepository).findById(10);
    }

    @Test
    public void testGetById_notFound() {

        Mockito.when(departmentRepository.findById(20))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> departmentService.getById(20));

        Mockito.verify(departmentRepository).findById(20);
    }

    @Test
    public void testGetAll() {

        List<Department> list = Arrays.asList(dept);

        Mockito.when(departmentRepository.findAll())
                .thenReturn(list);

        List<DepartmentDTO> result = departmentService.getAll();

        Assertions.assertEquals(1, result.size());

        Mockito.verify(departmentRepository).findAll();
    }

    @Test
    public void testGetByName_success() {

        Mockito.when(departmentRepository.findByName("Cardiology"))
                .thenReturn(Optional.of(dept));

        DepartmentDTO result = departmentService.getByName("Cardiology");

        Assertions.assertEquals("Cardiology", result.getName());

        Mockito.verify(departmentRepository).findByName("Cardiology");
    }

    @Test
    public void testGetByName_notFound() {

        Mockito.when(departmentRepository.findByName("XYZ"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> departmentService.getByName("XYZ"));

        Mockito.verify(departmentRepository).findByName("XYZ");
    }

    @Test
    public void testGetByHeadId() {

        Mockito.when(departmentRepository.findByHead_EmployeeId(1))
                .thenReturn(Arrays.asList(dept));

        List<DepartmentDTO> result = departmentService.getByHeadId(1);

        Assertions.assertEquals(1, result.size());

        Mockito.verify(departmentRepository).findByHead_EmployeeId(1);
    }

    @Test
    public void testCreate_success() {

        DepartmentDTO input = new DepartmentDTO(null, "Cardiology", 1, null);
        
        Mockito.when(departmentRepository.findByName("Cardiology"))
        .thenReturn(Optional.empty());
        
        Mockito.when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        Mockito.when(departmentRepository.save(Mockito.any(Department.class)))
                .thenReturn(dept);

        DepartmentDTO result = departmentService.create(input);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Cardiology", result.getName());

        Mockito.verify(physicianRepository).findById(1);
        Mockito.verify(departmentRepository).save(Mockito.any(Department.class));
    }

    @Test
    public void testCreate_physicianNotFound() {

        DepartmentDTO input = new DepartmentDTO(null, "Cardiology", 1, null);

        Mockito.when(physicianRepository.findById(1))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> departmentService.create(input));

        Mockito.verify(physicianRepository).findById(1);
    }
    
    @Test
    void testCreate_duplicateDepartment() {

        DepartmentDTO input =
            new DepartmentDTO(null, "Cardiology", 1, null);

        Mockito.when(departmentRepository.findByName("Cardiology"))
                .thenReturn(Optional.of(dept));

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> departmentService.create(input));

        Mockito.verify(departmentRepository)
                .findByName("Cardiology");
    }
    
    @Test
    void testGetById_invalidId() {
        Assertions.assertThrows(BadRequestException.class,
                () -> departmentService.getById(0));
    }

    @Test
    void testGetByHeadId_invalid() {
        Assertions.assertThrows(BadRequestException.class,
                () -> departmentService.getByHeadId(-1));
    }
    
    @Test
    void testGetByHeadId_empty() {

        Mockito.when(departmentRepository.findByHead_EmployeeId(1))
                .thenReturn(List.of());

        List<DepartmentDTO> result = departmentService.getByHeadId(1);

        Assertions.assertTrue(result.isEmpty());
    }
    
    
}