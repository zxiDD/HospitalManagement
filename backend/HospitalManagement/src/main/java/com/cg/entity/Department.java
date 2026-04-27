package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {
	
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Id
    @Column(name = "departmentid")
    private Integer departmentId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "head", nullable = false)
    private Physician head;

}

