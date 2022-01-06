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
    @NotBlank()
    private String username;

    @NotBlank()
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$")
    private String password;

    @NotBlank
    private String passwordCopy;

    @NotBlank()
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past()
    @NotNull
    private LocalDate birthday;

    @NotBlank()
    private String taxNumber;
}
