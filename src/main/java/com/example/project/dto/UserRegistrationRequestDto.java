package com.example.project.dto;

import com.example.project.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(
        first = "password",
        second = "repeatPassword",
        message = "Passwords must match"
)
public class UserRegistrationRequestDto {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Incorrect format email")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 6, max = 100, message = "Password must be "
            + "between 6 and 100 characters long")
    private String password;

    @NotBlank(message = "Repeat password cannot be empty.")
    private String repeatPassword;

    @NotBlank(message = "Name cannot be empty.")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    private String lastName;

    private String shippingAddress;
}
