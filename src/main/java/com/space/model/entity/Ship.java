package com.space.model.entity;

import com.space.model.ShipType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ship")
public class Ship {

    private static final int currentYear = 3019;

    /**
     * ID корабля
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название корабля (до 50 знаков включительно)
     */
    @Column(name = "name")
    private String name; //ID корабля

    /**
     * Планета пребывания (до 50 знаков включительно)
     */
    @Column(name = "planet")
    private String planet;

    /**
     * Тип корабля
     */
    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    /**
     * Дата выпуска.
     * Диапазон значений года 2800..3019 включительно
     */
    @Column(name = "prodDate")
    private Date prodDate;

    /**
     * Использованный / новый
     */
    @Column(name = "isUsed")
    private Boolean isUsed;

    /**
     * Максимальная скорость корабля.
     * Диапазон значений 0,01..0,99 включительно.
     * Используй математическое округление до сотых
     */
    @Column(name = "speed")
    private Double speed;

    /**
     * Количество членов экипажа.
     * Диапазон значений 1..9999 включительно
     */
    @Column(name = "crewSize")
    private Integer crewSize;

    /**
     * Рейтинг корабля.
     * Используй математическое округление до сотых.
     */
    @Column(name = "rating")
    private Double rating;

    public Ship() {
    }

    public Ship(String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed != null && isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        calculateRating();
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public Long getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
        calculateRating();
    }

    public void setUsed(Boolean used) {
        isUsed = used;
        calculateRating();
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
        calculateRating();
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void calculateRating() {
        if (speed != null && isUsed != null && prodDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(prodDate);
            rating = round((80 * speed * (isUsed ? 0.5 : 1)) / (currentYear - cal.get(Calendar.YEAR) + 1));
        }
    }

    public static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return Objects.equals(id, ship.id) &&
                Objects.equals(name, ship.name) &&
                Objects.equals(planet, ship.planet) &&
                shipType == ship.shipType &&
                Objects.equals(prodDate, ship.prodDate) &&
                Objects.equals(isUsed, ship.isUsed) &&
                Objects.equals(speed, ship.speed) &&
                Objects.equals(crewSize, ship.crewSize) &&
                Objects.equals(rating, ship.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
