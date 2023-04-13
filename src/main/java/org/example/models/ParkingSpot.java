package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.ParkingSpotStatus;
import org.example.enums.ParkingSpotType;

@Getter
@Setter
public class ParkingSpot extends BaseModel {
    private String spotNumber;
    private ParkingSpotType spotType;
    private ParkingSpotStatus spotStatus;

    public ParkingSpot(String spotNumber, ParkingSpotType parkingSpotType, ParkingSpotStatus parkingSpotStatus) {
        super();
        this.setSpotNumber(spotNumber);
        this.setSpotType(parkingSpotType);
        this.setSpotStatus(parkingSpotStatus);
    }
}
