package com.oleh.chui.learning_platform.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_details")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private BigDecimal money;

    @NonNull
    @Column(name = "tax_number")
    private String taxNumber;

}
