package com.wipro.its.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    private String street;

    private String location;

    private String city;

    private String state;

    private String pincode;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String emailId;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Primary skills are required")
    private String primarySkills;

    private String secondarySkills;

    @NotNull(message = "Experience is required")
    @DecimalMin(value = "0.0", message = "Experience must be positive")
    private Float experience;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    private String designation;

    private Integer noticePeriod;
}
