package org.example.services;

import org.example.enums.ParkingSpotStatus;
import org.example.enums.ParkingSpotType;
import org.example.enums.VehicleType;
import org.example.exceptions.UnknownVehicleTypeExcpetion;
import org.example.models.ParkingSpot;

public class ParkingSpotFactory {
    public static ParkingSpot createSpotForVehicle(String vehicleType, String spotNumber) throws UnknownVehicleTypeExcpetion {
        return switch (vehicleType.toLowerCase()) {
            case "motorcycle", "scooter" -> new ParkingSpot(spotNumber, ParkingSpotType.SMALL, ParkingSpotStatus.VACANT);
            case "car", "suv" -> new ParkingSpot(spotNumber, ParkingSpotType.MEDIUM, ParkingSpotStatus.VACANT);
            case "bus", "truck" -> new ParkingSpot(spotNumber, ParkingSpotType.BIG, ParkingSpotStatus.VACANT);
            default -> throw new UnknownVehicleTypeExcpetion();
        };

    }

    public static ParkingSpotType getSpotTypeForVehicle(String vehicleType) throws UnknownVehicleTypeExcpetion {
        return switch (vehicleType.toLowerCase()) {
            case "motorcycle", "scooter" -> ParkingSpotType.SMALL;
            case "car", "suv" -> ParkingSpotType.MEDIUM;
            case "bus", "truck" -> ParkingSpotType.BIG;
            default -> throw new UnknownVehicleTypeExcpetion();
        };
    }

    public static VehicleType getVehicleType(String vehicleType) throws UnknownVehicleTypeExcpetion {
        return switch (vehicleType.toLowerCase()) {
            case "motorcycle", "scooter" -> VehicleType.SMALL;
            case "car", "suv" -> VehicleType.MEDIUM;
            case "bus", "truck" -> VehicleType.BIG;
            default -> throw new UnknownVehicleTypeExcpetion();
        };
    }
}
