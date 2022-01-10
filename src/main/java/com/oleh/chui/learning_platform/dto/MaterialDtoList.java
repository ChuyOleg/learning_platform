package com.oleh.chui.learning_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MaterialDtoList {

    @NotEmpty
    private List<MaterialDTO> materialDTOList;

    public MaterialDtoList() {
        materialDTOList = new ArrayList<>();
    }

    public void addMaterialDTO(MaterialDTO materialDTO) {
        this.materialDTOList.add(materialDTO);
    }

}
