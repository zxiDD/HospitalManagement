package com.cg.service;

import com.cg.dto.MedicationDTO;
import com.cg.entity.Medication;
import com.cg.entity.Patient;
import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PatientRepository;
import com.cg.repo.PrescribesRepository;
import com.cg.service.PrescribesService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescribesServiceImpl implements PrescribesService {

	private final PrescribesRepository repo;
	private final PatientRepository patientRepository;

	public PrescribesServiceImpl(PrescribesRepository repo, PatientRepository patientRepository) {
		this.repo = repo;
		this.patientRepository = patientRepository;
	}

	@Override
	public List<Prescribes> getAll() {
		return repo.findAll();
	}

	@Override
	public Prescribes getById(PrescribesId id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
	}

	@Override
	public List<Prescribes> getAllSorted() {
		return repo.findAll(Sort.by("id.date")); // sort by date
	}

//    @Override
//    public long count() {
//        return repo.count();
//    }

	@Override
	public Prescribes save(Prescribes prescribes) {
		return repo.save(prescribes);
	}

	@Override
	public List<MedicationDTO> getMedicationsByPatient(Long ssn) {

		Patient patient = patientRepository.findBySsnAndIsActiveTrue(ssn)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

		List<Prescribes> prescriptions = repo.findByIdPatient(ssn);

		return prescriptions.stream()

				.map(prescription -> {
					Medication medication = prescription.getMedication();

					MedicationDTO dto = new MedicationDTO();
					dto.setCode(medication.getCode());
					dto.setName(medication.getName());
					dto.setBrand(medication.getBrand());
					dto.setDescription(medication.getDescription());

					return dto;
				})

				.toList();
	}
}