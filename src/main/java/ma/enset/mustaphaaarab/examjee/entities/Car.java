package ma.enset.mustaphaaarab.examjee.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import ma.enset.mustaphaaarab.examjee.enums.FuelType;
import ma.enset.mustaphaaarab.examjee.enums.GearBox;
import ma.enset.mustaphaaarab.examjee.enums.VehicleType;

@Entity
@DiscriminatorValue("VOITURE")
public class Car extends Vehicle {
    @Column(nullable = false)
    private Integer nombrePortes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType typeCarburant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GearBox boiteVitesse;

    @Override
    public VehicleType getType() {
        return VehicleType.VOITURE;
    }

    public Integer getNombrePortes() {
        return nombrePortes;
    }

    public void setNombrePortes(Integer nombrePortes) {
        this.nombrePortes = nombrePortes;
    }

    public FuelType getTypeCarburant() {
        return typeCarburant;
    }

    public void setTypeCarburant(FuelType typeCarburant) {
        this.typeCarburant = typeCarburant;
    }

    public GearBox getBoiteVitesse() {
        return boiteVitesse;
    }

    public void setBoiteVitesse(GearBox boiteVitesse) {
        this.boiteVitesse = boiteVitesse;
    }
}
