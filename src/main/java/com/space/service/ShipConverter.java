package com.space.service;


import com.space.model.entity.Ship;
import org.springframework.stereotype.Component;

@Component
public class ShipConverter {
    public Ship fromShipDtoToShip(ShipDto shipDto) {

        Ship ship = new Ship();
        if (shipDto != null) {
            ship.setId(shipDto.getId());
            ship.setName(shipDto.getName());
            ship.setPlanet(shipDto.getPlanet());
            ship.setShipType(shipDto.getShipType());
            ship.setProdDate(shipDto.getProdDate());
            ship.setUsed(shipDto.getUsed() != null && shipDto.getUsed());
            ship.setSpeed(shipDto.getSpeed());
            ship.setCrewSize(shipDto.getCrewSize());
        }
        return ship;
    }

    public ShipDto fromShipToShipDto(Ship ship) {
        ShipDto shipDto = new ShipDto();
        if (ship != null) {
            shipDto.setId(ship.getId());
            shipDto.setName(ship.getName());
            shipDto.setPlanet(ship.getPlanet());
            shipDto.setShipType(ship.getShipType());
            shipDto.setProdDate(ship.getProdDate());
            shipDto.setUsed(ship.getUsed());
            shipDto.setSpeed(ship.getSpeed());
            shipDto.setCrewSize(ship.getCrewSize());
            shipDto.setRating(ship.getRating());
        }
        return shipDto;
    }
}
