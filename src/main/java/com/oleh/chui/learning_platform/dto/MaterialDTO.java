package com.oleh.chui.learning_platform.dto;

import com.oleh.chui.learning_platform.entity.Material;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class MaterialDTO {

    private final List<Material> materialList;

    public MaterialDTO() {
        materialList = new ArrayList<>();
    }

    public void addMaterial(Material material) {
        this.materialList.add(material);
    }

}
