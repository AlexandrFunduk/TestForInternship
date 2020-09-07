package com.space.service;

import com.space.exception.ShipNotFoundException;
import com.space.exception.ShipValidationException;
import com.space.model.entity.Ship;
import com.space.repository.ShipRepository;
import com.space.specification.ShipSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Repository
@Transactional
public class ShipServiceImpl implements ShipService {
    @Autowired
    private final ShipRepository shipRepository;
    private final ShipConverter shipConverter;

    public ShipServiceImpl(ShipRepository shipRepository, ShipConverter shipConverter) {
        this.shipRepository = shipRepository;
        this.shipConverter = shipConverter;
    }

    @Transactional(readOnly = true)
    public List<ShipDto> findAll(ShipSpecification specification, Pageable pageable) {
        return shipRepository.findAll(specification, pageable).getContent().stream().map(shipConverter::fromShipToShipDto).collect(Collectors.toList());
    }

    @Override
    public Integer count() {
        return Math.toIntExact(shipRepository.count());
    }

    @Override
    public Integer count(ShipSpecification shipSpecification) {
        return Math.toIntExact(shipRepository.count(shipSpecification));
    }

    @Override
    public ShipDto getById(Long id) {
        Optional<Ship> ship = shipRepository.findById(id);
        if (ship.isPresent()) {
            return shipConverter.fromShipToShipDto(ship.get());
        }
        throw new ShipNotFoundException("Ship with id=" + id + " not found");
    }

    @Override
    public ShipDto save(ShipDto shipDto) throws ShipValidationException {
        validateShipDto(shipDto);
        Ship savedShip = shipRepository.save(shipConverter.fromShipDtoToShip(shipDto));
        return shipConverter.fromShipToShipDto(savedShip);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        getById(id);
        shipRepository.deleteById(id);
    }

    public void validateShipDto(ShipDto shipDto) throws ShipValidationException {
        Date minProdDate = new Date(26192235600000L); // 2800-01-01 00:00:00.000 = 26192235600000 msec
        Date maxProdDate = new Date(33134734799999L); // 3019-12-31 23:59:59.999 = 33134734799999 msec
        String message = "";
        if (shipDto.getName() == null || shipDto.getName().equals("") || shipDto.getName().length() >= 50) {
            message += "name = " + shipDto.getName() + "\n";
        }
        if (shipDto.getPlanet() == null || shipDto.getPlanet().equals("") || shipDto.getPlanet().length() >= 50) {
            message += "planet = " + shipDto.getPlanet() + "\n";
        }
        if (shipDto.getShipType() == null) {
            message += "shipType = null" + "\n";
        }
        if (shipDto.getProdDate() == null || shipDto.getProdDate().before(minProdDate) || shipDto.getProdDate().after(maxProdDate)) {
            message += "prodDate = " + shipDto.getProdDate() + "\n";
        }
        if (shipDto.getSpeed() == null || Ship.round(shipDto.getSpeed()) < 0.01 || Ship.round(shipDto.getSpeed()) > 0.99) {
            message += "speed = " + shipDto.getSpeed() + "\n";
        }
        if (shipDto.getCrewSize() == null || shipDto.getCrewSize() < 1 || shipDto.getCrewSize() > 9999) {
            message += "crewSize = " + shipDto.getCrewSize() + "\n";
        }
        if (message.length() != 0) {
            throw new ShipValidationException(message);
        }
    }


}
