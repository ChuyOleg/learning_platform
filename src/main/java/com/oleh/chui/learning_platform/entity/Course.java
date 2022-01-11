package com.oleh.chui.learning_platform.entity;

import com.oleh.chui.learning_platform.dto.CourseDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person creator;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "course")
    private Set<Material> materialSet;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "course")
    private Set<Question> questionSet;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "selectedCourses")
    private Set<Person> students;

    public Course(CourseDTO courseDTO, Set<Material> materialSet,
                  Set<Question> questionSet, Person creator) {
        this.name = courseDTO.getName();
        this.description = courseDTO.getDescription();
        this.category = courseDTO.getCategory();
        this.price = courseDTO.getPrice();
        this.language = courseDTO.getLanguage();
        this.isActive = true;
        this.creator = creator;
        this.materialSet = materialSet;
        this.questionSet = questionSet;
    }
}
