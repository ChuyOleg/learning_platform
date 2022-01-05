package com.oleh.chui.learning_platform.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, max = 32, message = "Password length should be from 4 to 64")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Incorrect email format")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Incorrect birthday")
    private LocalDate birthday;

    @NotBlank(message = "Tax-Number is mandatory")
    private String taxNumber;
}
