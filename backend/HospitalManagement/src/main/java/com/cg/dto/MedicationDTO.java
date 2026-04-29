package com.cg.dto;

import jakarta.validation.constraints.*;

public class MedicationDTO {

    private Integer code;

    @NotBlank(message = "Medication name is required")
    @Size(max = 30, message = "Name must not exceed 30 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only letters and numbers allowed")
    private String name;

    @NotBlank(message = "Brand is required")
    @Size(max = 30, message = "Brand must not exceed 30 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only letters and numbers allowed")
    private String brand;

    @NotBlank(message = "Description is required")
    @Size(max = 30, message = "Description must not exceed 30 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only letters and numbers allowed")
    private String description;

    public MedicationDTO() {}

    public MedicationDTO(Integer code, String name, String brand, String description) {
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