package com.ekomora.springjwt.controllers;

import com.ekomora.springjwt.DTO.MaterialDto;
import com.ekomora.springjwt.models.Material;
import com.ekomora.springjwt.repository.MaterialRepository;
import com.ekomora.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class MaterialController {
    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/materials")
    public List<MaterialDto> getAllMaterials() {
        return materialRepository.findAllProjectedBy();
    }

    @GetMapping("/users/{userId}/materials")
    public ResponseEntity<List<Material>> getAllMaterialsByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found User with id = " + userId);
        }

        List<Material> materials = materialRepository.findAllByUserId(userId);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/users/materials/{userId}")
    public ResponseEntity<List<MaterialDto>> getMaterialsByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found User with id = " + userId);
        }

        List<MaterialDto> materials = materialRepository.findAllProjectedByUserId(userId);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/materials")
    public ResponseEntity<Material> createMaterial(@PathVariable(value = "userId") Long userId,
                                                   @RequestBody Material materialRequest) {
        Material material = userRepository.findById(userId).map(user -> {
            materialRequest.setUser(user);
            return materialRepository.save(materialRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));

        return new ResponseEntity<>(material, HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}/materials/{materialId}")
    public Material updateMaterial(@PathVariable(value = "userId") Long userId,
                                   @PathVariable(value = "materialId") Long materialId,
                                   @Valid @RequestBody Material materialRequest) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return materialRepository.findById(materialId).map(material -> {
            material.setTitle(materialRequest.getTitle());
            material.setInventoryNumber(materialRequest.getInventoryNumber());
            material.setDateStart(materialRequest.getDateStart());
            material.setType(materialRequest.getType());
            material.setAmount(materialRequest.getAmount());
            material.setPrice(materialRequest.getPrice());

            return materialRepository.save(material);
        }).orElseThrow(() -> new ResourceNotFoundException("MaterialId " + materialId + "not found"));
    }

    @DeleteMapping("/users/{userId}/materials/{materialId}")
    public ResponseEntity<?> deleteMaterial(@PathVariable(value = "userId") Long userId,
                                            @PathVariable(value = "materialId") Long materialId) {
        return materialRepository.findByIdAndUserId(materialId, userId).map(material -> {
            materialRepository.delete(material);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Material not found with id " + materialId + " and UserId " + userId));
    }
}
