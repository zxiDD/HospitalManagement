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

		public Medication() {
			super();
		}

		public Medication(Integer code, String name, String brand, String description) {
			super();
			this.code = code;
			this.name = name;
			this.brand = brand;
			this.description = description;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBrand() {
			return brand;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	
}
