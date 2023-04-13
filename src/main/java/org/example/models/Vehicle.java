package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.VehicleType;

@Getter
@Setter
public class Vehicle extends BaseModel {
    private String vehicleNumber;
    private VehicleType vehicleType;

    public Vehicle(String vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }
}
