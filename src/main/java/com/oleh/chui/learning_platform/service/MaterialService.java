package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.Material;
import com.oleh.chui.learning_platform.repository.MaterialRepository;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public void save(Material material) {
        materialRepository.save(material);
    }

}
