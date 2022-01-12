package com.oleh.chui.learning_platform.repository;

import com.oleh.chui.learning_platform.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
