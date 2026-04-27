package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medication")
public class Medication {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "code")
	    private Integer code;

	    @Column(name = "name", nullable = false, length = 30)
	    private String name;

	    @Column(name = "brand", nullable = false, length = 30)
	    private String brand;

	    @Column(name = "description", nullable = false, length = 30)
	    private String description;

	
}
