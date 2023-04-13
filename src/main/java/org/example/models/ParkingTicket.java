package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ParkingTicket extends BaseModel {
    private Vehicle vehicle;
    private ParkingSpot parkingSpot;
    private Date entryTime;

    public ParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot, Date entryTime) {
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = entryTime;
    }
}
