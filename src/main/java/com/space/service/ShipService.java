package com.space.service;

import com.space.exception.ShipValidationException;
import com.space.specification.ShipSpecification;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipService {
    List<ShipDto> findAll(ShipSpecification specification, Pageable pageable);

    ShipDto save(ShipDto shipDto) throws ShipValidationException;

    ShipDto getById(Long id);

    Integer count();

    Integer count(ShipSpecification shipSpecification);

    void deleteById(Long id);
}
