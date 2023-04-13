package org.example.services.spotsavailability;

import org.example.exceptions.SpotNotAvailableException;
import org.example.exceptions.UnknownVehicleTypeExcpetion;
import org.example.models.ParkingLot;
import org.example.models.ParkingSpot;

import java.util.Optional;

public interface FindAvailableSpotsStrategy {
    Optional<ParkingSpot> findSpot(ParkingLot parkingLot, String vehicleType) throws UnknownVehicleTypeExcpetion, SpotNotAvailableException;
}
