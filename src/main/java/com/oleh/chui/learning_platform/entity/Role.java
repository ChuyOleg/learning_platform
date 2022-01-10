package com.oleh.chui.learning_platform.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleEnum role;

    public enum RoleEnum {
        ADMIN,
        USER,
        UNKNOWN;
    }

}


