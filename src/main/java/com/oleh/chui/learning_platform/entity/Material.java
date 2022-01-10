package com.oleh.chui.learning_platform.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "materials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;

    @ToString.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Material(String data) {
        this.data = data;
    }

}
