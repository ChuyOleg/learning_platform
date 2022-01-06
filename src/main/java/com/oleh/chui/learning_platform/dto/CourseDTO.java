package com.oleh.chui.learning_platform.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String category;

    @NotNull
    private BigDecimal price;

    @NotBlank
    private String language;

}
