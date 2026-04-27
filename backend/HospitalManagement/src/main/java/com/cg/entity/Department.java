package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {
	
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Id
    @Column(name = "departmentid")
    private Integer departmentId;

    @Column(name = "name", nullable = false, length = 30,unique=true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "head", nullable = false)
    private Physician head;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Physician getHead() {
		return head;
	}

	public void setHead(Physician head) {
		this.head = head;
	}

}

