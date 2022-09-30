package com.ekomora.springjwt.repository;

import com.ekomora.springjwt.DTO.MaterialDto;
import com.ekomora.springjwt.models.Material;
import com.ekomora.springjwt.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByUser(User user, Sort sort);

    List<Material> findByUserId(Long userId);

    Optional<Material> findByIdAndUserId(Long id, Long userId);

    List<Material> findAllByUserId(Long userId);

    //    @Query(value = "select new com.ekomora.springjwt.DTO.MaterialDto(m.id, m.title, m.inventoryNumber," +
//            "m.dateStart, m.type, m.amount, m.price, m.user) from Material m")
    List<MaterialDto> findAllProjectedBy();
}
