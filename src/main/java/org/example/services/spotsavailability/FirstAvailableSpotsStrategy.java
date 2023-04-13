package org.example.services.spotsavailability;

import org.example.enums.ParkingSpotStatus;
import org.example.enums.ParkingSpotType;
import org.example.exceptions.UnknownVehicleTypeExcpetion;
import org.example.models.ParkingLot;
import org.example.models.ParkingSpot;
import org.example.services.ParkingSpotFactory;

import java.util.Optional;
import java.util.logging.Logger;

public class FirstAvailableSpotsStrategy implements FindAvailableSpotsStrategy {
    private final Logger logger = Logger.getLogger(FirstAvailableSpotsStrategy.class
            .getClass().getName());
    @Override
    public Optional<ParkingSpot> findSpot(ParkingLot parkingLot, String vehicleType) throws UnknownVehicleTypeExcpetion {
        ParkingSpotType spotType = ParkingSpotFactory.getSpotTypeForVehicle(vehicleType);
        for (ParkingSpot spot:
                parkingLot.getParkingSpots()) {
            synchronized (spot) {
                if (spot.getSpotType().equals(spotType) && spot.getSpotStatus().equals(ParkingSpotStatus.VACANT)) {
                    spot.setSpotStatus(ParkingSpotStatus.FILLED);
                    return Optional.of(spot);
                }
            }
        }
        logger.info("Unable to find spot. All spots filled");
        return Optional.empty();
    }
}
