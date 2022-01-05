package com.oleh.chui.learning_platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String category;

    private BigDecimal price;

    @NonNull
    private String language;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person creator;

    @OneToMany(mappedBy = "course")
    private Set<Material> materialSet;

    @OneToMany(mappedBy = "course")
    private Set<Question> questionSet;

    @ManyToMany(mappedBy = "selectedCourses")
    private Set<Person> students;
}
