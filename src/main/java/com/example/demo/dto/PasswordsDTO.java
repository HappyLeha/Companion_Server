package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordsDTO {
    @NotBlank(message = "password shouldn't be empty")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9_]+",
            message = "password should includes only letters")
    private String password;

    @NotBlank(message = "repeatPassword shouldn't be empty")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9_]+",
            message = "repeatPassword should includes only letters")
    private String repeatPassword;
}
