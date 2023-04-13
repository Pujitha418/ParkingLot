package org.example.services;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.LocationType;
import org.example.enums.ParkingLotStatus;
import org.example.enums.ParkingSpotStatus;
import org.example.enums.VehicleType;
import org.example.exceptions.SpotNotAvailableException;
import org.example.exceptions.UnknownLocationTypeExcpetion;
import org.example.exceptions.UnknownVehicleTypeExcpetion;
import org.example.models.*;
import org.example.services.fees.FeeCalculationStrategyFactory;
import org.example.services.fees.FeeModelCreationService;
import org.example.services.spotsavailability.FindAvailableSpotsStrategy;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class ParkingLotService {
    private FindAvailableSpotsStrategy findAvailableSpotsStrategy;
    private FeeModelCreationService feeModelCreationService;

    public ParkingLotService(FindAvailableSpotsStrategy findAvailableSpotsStrategy, FeeModelCreationService feeModelCreationService) {
        this.findAvailableSpotsStrategy = findAvailableSpotsStrategy;
        this.feeModelCreationService = feeModelCreationService;
    }

    public ParkingLot createParkingLot(List<ParkingSpot> parkingSpots, LocationType locationType) throws UnknownLocationTypeExcpetion {
        Map<VehicleType, FeeModel> feeModels = feeModelCreationService.create(locationType);
        return new ParkingLot(
                parkingSpots,
                ParkingLotStatus.AVAILABLE,
                FeeCalculationStrategyFactory.getFeeCalculationStrategy(locationType),
                feeModels,
                locationType,
                parkingSpots.size()
        );
    }

    public ParkingSpot createParkingSpot(LocationType locationType, String vehicleType, String spotNumber) throws UnknownVehicleTypeExcpetion {
        VehicleType mappedVehicleType = ParkingSpotFactory.getVehicleType(vehicleType);
        if (!locationType.equals(LocationType.MALL) && mappedVehicleType.equals(VehicleType.BIG))
            throw new RuntimeException("Stadium/Airport Locations dont support Big Vehicles");
        return ParkingSpotFactory.createSpotForVehicle(vehicleType, spotNumber);
    }

    public ParkingSpot createParkingSpot(ParkingLot parkingLot, String vehicleType, String spotNumber) throws UnknownVehicleTypeExcpetion {
        VehicleType mappedVehicleType = ParkingSpotFactory.getVehicleType(vehicleType);
        if (!parkingLot.getLocationType().equals(LocationType.MALL) && mappedVehicleType.equals(VehicleType.BIG))
            throw new RuntimeException("Stadium/Airport Locations dont support Big Vehicles");
        return ParkingSpotFactory.createSpotForVehicle(vehicleType, spotNumber);
    }

    public ParkingTicket createParkingTicket(ParkingLot parkingLot, String vehicleType, String vehicleNumber)
            throws UnknownVehicleTypeExcpetion, SpotNotAvailableException {
        Optional<ParkingSpot> parkingSpotOptional = findAvailableSpotsStrategy.findSpot(parkingLot, vehicleType);
        if (parkingSpotOptional.isEmpty()) throw new SpotNotAvailableException(vehicleType);
        ParkingSpot parkingSpot = parkingSpotOptional.get();


        Vehicle vehicle = new Vehicle(vehicleNumber, ParkingSpotFactory.getVehicleType(vehicleType));
        return new ParkingTicket(vehicle, parkingSpot, new Date());
    }

    public Receipt createReceipt(ParkingLot parkingLot, ParkingTicket parkingTicket, Date exitTime) {
        Double fees = parkingLot.getFeeCalculationStrategy().calculate(parkingLot, parkingTicket, exitTime);
        parkingTicket.getParkingSpot().setSpotStatus(ParkingSpotStatus.VACANT);
        return new Receipt(parkingTicket, exitTime, fees);
    }

}
