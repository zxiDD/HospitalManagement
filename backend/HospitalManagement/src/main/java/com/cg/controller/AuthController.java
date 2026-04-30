package com.cg.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.LoginResponseDTO;
import com.cg.dto.SignupDTO;
import com.cg.entity.Patient;
import com.cg.entity.Role;
import com.cg.entity.RolePk;
import com.cg.entity.User;
import com.cg.exception.ValidationException;
import com.cg.repo.PatientRepository;
import com.cg.repo.RoleRepository;
import com.cg.repo.UserRepository;
import com.cg.security.AuthRequest;
import com.cg.security.JWTService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthRequest authRequest) {

		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		if (!authentication.isAuthenticated()) {
			throw new UsernameNotFoundException("Invalid username or password");
		}

		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

		String token = jwtService.generateToken(authRequest.getUsername(), roles);

		User user = userRepository.findByUsername(authRequest.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		LoginResponseDTO dto = new LoginResponseDTO();

		dto.setToken(token);
		dto.setMsg("User authenticated successfully");
		dto.setUserName(authRequest.getUsername());
		dto.setTimestamp(LocalDateTime.now().toString());

		dto.setUserId(user.getLinkedEntityId());

		dto.setRoles(roles);

		return ResponseEntity.ok(dto);
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerPatient(@Valid @RequestBody SignupDTO dto, BindingResult br) {

		if (br.hasErrors()) {
			throw new ValidationException(br.getFieldErrors());
		}

		if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
		}

		if (patientRepository.findById(dto.getSsn()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient already exists");
		}

		if (patientRepository.findByPhone(dto.getPhoneNo()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists");
		}

		User user = new User();

		user.setUsername(dto.getUsername());

		user.setPassword(passwordEncoder.encode(dto.getPassword()));

		user.setIsActive(true);

		user.setRole(com.cg.enums.Role.PATIENT);

		RolePk rolePk = new RolePk();
		rolePk.setUserName(dto.getUsername());
		rolePk.setRoleName("ROLE_USER");

		Role role = new Role();
		role.setKey(rolePk);

		Patient patient = new Patient();

		patient.setSsn(dto.getSsn());

		patient.setName(dto.getPatientName());

		patient.setPhone(dto.getPhoneNo());

		patient.setAddress(dto.getAddress());

		patient.setInsuranceId(dto.getInsuranceId());

		Patient savedPatient = patientRepository.save(patient);

		user.setLinkedEntityId(savedPatient.getSsn());

		userRepository.save(user);
		roleRepository.save(role);

		return ResponseEntity.status(HttpStatus.CREATED).body("Patient registered successfully");
	}
}
