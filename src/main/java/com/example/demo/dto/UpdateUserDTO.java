package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class UpdateUserDTO {

    @NotBlank(message = "firstName shouldn't be empty")
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+",
            message = "firstName should includes only letters")
    private String firstName;

    @NotBlank(message = "lastName shouldn't be empty")
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+",
            message = "lastName should includes only letters")
    private String lastName;

    @NotBlank(message = "email shouldn't be empty")
    @Email(message = "field email should be email")
    private String email;

    @Pattern(regexp = "\\+\\d{12}",
            message = "phone should be phone number")
    private String phone;

    private String photo;

    private String note;

    @Valid
    private PasswordsDTO passwords;
}
