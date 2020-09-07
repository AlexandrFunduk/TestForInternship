package com.space.controller;

import com.space.exception.ShipValidationException;
import com.space.model.ShipType;
import com.space.service.ShipDto;
import com.space.service.ShipService;
import com.space.specification.SearchCriteria;
import com.space.specification.SearchOperation;
import com.space.specification.ShipSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rest")

public class ShipsController {

    private final ShipService shipService;

    public ShipsController(ShipService shipService) {
        this.shipService = shipService;
    }

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    @ResponseBody
    public List<ShipDto> findAllShips(@RequestParam Optional<String> name,
                                      @RequestParam Optional<String> planet,
                                      @RequestParam Optional<ShipType> shipType,
                                      @RequestParam Optional<Long> after,
                                      @RequestParam Optional<Long> before,
                                      @RequestParam Optional<Boolean> isUsed,
                                      @RequestParam Optional<Double> minSpeed,
                                      @RequestParam Optional<Double> maxSpeed,
                                      @RequestParam Optional<Integer> minCrewSize,
                                      @RequestParam Optional<Integer> maxCrewSize,
                                      @RequestParam Optional<Double> minRating,
                                      @RequestParam Optional<Double> maxRating,
                                      @RequestParam(defaultValue = "ID") ShipOrder order,
                                      @RequestParam(defaultValue = "0") Integer pageNumber,
                                      @RequestParam(defaultValue = "3") Integer pageSize) {
        ShipSpecification ssFilter = new ShipSpecification();

        name.ifPresent(s -> ssFilter.add(new SearchCriteria("name", s, SearchOperation.MATCH)));
        planet.ifPresent(s -> ssFilter.add(new SearchCriteria("planet", s, SearchOperation.MATCH)));
        shipType.ifPresent(type -> ssFilter.add(new SearchCriteria("shipType", type, SearchOperation.EQUAL)));
        after.ifPresent(aLong -> ssFilter.add(new SearchCriteria("prodDate", new Date(aLong), SearchOperation.GREATER_THAN_EQUAL)));
        before.ifPresent(aLong -> ssFilter.add(new SearchCriteria("prodDate", new Date(aLong), SearchOperation.LESS_THAN_EQUAL)));
        isUsed.ifPresent(aBoolean -> ssFilter.add(new SearchCriteria("isUsed", aBoolean ? 1 : 0, SearchOperation.EQUAL)));
        minSpeed.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("speed", aDouble, SearchOperation.GREATER_THAN_EQUAL)));
        maxSpeed.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("speed", aDouble, SearchOperation.LESS_THAN_EQUAL)));
        minCrewSize.ifPresent(integer -> ssFilter.add(new SearchCriteria("crewSize", integer, SearchOperation.GREATER_THAN_EQUAL)));
        maxCrewSize.ifPresent(integer -> ssFilter.add(new SearchCriteria("crewSize", integer, SearchOperation.LESS_THAN_EQUAL)));
        minRating.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("rating", aDouble, SearchOperation.GREATER_THAN_EQUAL)));
        maxRating.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("rating", aDouble, SearchOperation.LESS_THAN_EQUAL)));
        Pageable pageable = PageRequest.of(pageNumber > 0 ? pageNumber : 0, pageSize > 0 ? pageSize : 3, Sort.by(order.getFieldName()));
        return shipService.findAll(ssFilter, pageable);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    @ResponseBody
    public Integer getShipsCount(@RequestParam Optional<String> name,
                                 @RequestParam Optional<String> planet,
                                 @RequestParam Optional<ShipType> shipType,
                                 @RequestParam Optional<Long> after,
                                 @RequestParam Optional<Long> before,
                                 @RequestParam Optional<Boolean> isUsed,
                                 @RequestParam Optional<Double> minSpeed,
                                 @RequestParam Optional<Double> maxSpeed,
                                 @RequestParam Optional<Integer> minCrewSize,
                                 @RequestParam Optional<Integer> maxCrewSize,
                                 @RequestParam Optional<Double> minRating,
                                 @RequestParam Optional<Double> maxRating) {

        ShipSpecification ssFilter = new ShipSpecification();
        name.ifPresent(s -> ssFilter.add(new SearchCriteria("name", s, SearchOperation.MATCH)));
        planet.ifPresent(s -> ssFilter.add(new SearchCriteria("planet", s, SearchOperation.MATCH)));
        shipType.ifPresent(type -> ssFilter.add(new SearchCriteria("shipType", type, SearchOperation.EQUAL)));
        after.ifPresent(aLong -> ssFilter.add(new SearchCriteria("prodDate", new Date(aLong), SearchOperation.GREATER_THAN_EQUAL)));
        before.ifPresent(aLong -> ssFilter.add(new SearchCriteria("prodDate", new Date(aLong), SearchOperation.LESS_THAN_EQUAL)));
        isUsed.ifPresent(aBoolean -> ssFilter.add(new SearchCriteria("isUsed", aBoolean ? 1 : 0, SearchOperation.EQUAL)));
        minSpeed.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("speed", aDouble, SearchOperation.GREATER_THAN_EQUAL)));
        maxSpeed.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("speed", aDouble, SearchOperation.LESS_THAN_EQUAL)));
        minCrewSize.ifPresent(integer -> ssFilter.add(new SearchCriteria("crewSize", integer, SearchOperation.GREATER_THAN_EQUAL)));
        maxCrewSize.ifPresent(integer -> ssFilter.add(new SearchCriteria("crewSize", integer, SearchOperation.LESS_THAN_EQUAL)));
        minRating.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("rating", aDouble, SearchOperation.GREATER_THAN_EQUAL)));
        maxRating.ifPresent(aDouble -> ssFilter.add(new SearchCriteria("rating", aDouble, SearchOperation.LESS_THAN_EQUAL)));
        return shipService.count(ssFilter);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST)
    @ResponseBody
    public ShipDto createShip(@RequestBody ShipDto shipDto) {
        return shipService.save(shipDto);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ShipDto getShip(@PathVariable Long id) {
        if (id <= 0) throw new ShipValidationException("id <= 0");
        return shipService.getById(id);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ShipDto updateShip(@RequestBody ShipDto shipDao, @PathVariable Long id) {
        if (id <= 0) throw new ShipValidationException("id <= 0");

        ShipDto mergeShip = shipService.getById(id);
        if (shipDao.getName() != null) mergeShip.setName(shipDao.getName());
        if (shipDao.getPlanet() != null) mergeShip.setPlanet(shipDao.getPlanet());
        if (shipDao.getShipType() != null) mergeShip.setShipType(shipDao.getShipType());
        if (shipDao.getProdDate() != null) mergeShip.setProdDate(shipDao.getProdDate());
        if (shipDao.getUsed() != null) mergeShip.setUsed(shipDao.getUsed());
        if (shipDao.getSpeed() != null) mergeShip.setSpeed(shipDao.getSpeed());
        if (shipDao.getCrewSize() != null) mergeShip.setCrewSize(shipDao.getCrewSize());
        if (shipDao.getRating() != null) mergeShip.setRating(shipDao.getRating());
        return shipService.save(mergeShip);
    }

    @DeleteMapping("/ships/{id}")
    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteShip(@PathVariable Long id) {
        if (id <= 0) throw new ShipValidationException("id <= 0");
        shipService.deleteById(id);
    }
}
