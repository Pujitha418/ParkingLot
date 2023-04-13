package org.example.services.fees;

import org.example.enums.LocationType;
import org.example.enums.VehicleType;
import org.example.models.*;

import java.util.*;

public class FeeModelCreationService {
    private final Map<LocationType, List<VehicleType>> locationVehicleMap = new HashMap<>();

    public FeeModelCreationService() {
        //initialize registry
        locationVehicleMap.put(LocationType.MALL, new ArrayList<>(List.of(VehicleType.SMALL, VehicleType.MEDIUM, VehicleType.BIG)));
        locationVehicleMap.put(LocationType.STADIUM, new ArrayList<>(List.of(VehicleType.SMALL, VehicleType.MEDIUM)));
        locationVehicleMap.put(LocationType.AIRPORT, new ArrayList<>(List.of(VehicleType.SMALL, VehicleType.MEDIUM)));
    }

    public Map<VehicleType, FeeModel> create(LocationType locationType) {
        return switch (locationType) {
            case MALL -> createMallFeeModel();
            case STADIUM -> createStadiumFeeModel();
            case AIRPORT -> createAirportFeeModel();
        };
    }

    private Map<VehicleType, FeeModel> createMallFeeModel() {
        Map<VehicleType, FeeModel> result =  new HashMap<>();
        LocationType locationType = LocationType.MALL;
        for (VehicleType vt:
                locationVehicleMap.get(locationType)) {
            switch (vt) {
                case SMALL -> result.put(vt, new SmallVehicleFeeModel(locationType, new ArrayList<>(
                        List.of(
                                new HourFeeMap(0, 10.0)
                        )
                )));
                case MEDIUM -> result.put(vt, new SmallVehicleFeeModel(locationType, new ArrayList<>(
                        List.of(
                                new HourFeeMap(0, 20.0)
                        )
                )));
                case BIG -> result.put(vt, new SmallVehicleFeeModel(locationType, new ArrayList<>(
                        List.of(
                                new HourFeeMap(0, 50.0)
                        )
                )));
            }
        }
        return result;
    }

    private Map<VehicleType, FeeModel> createStadiumFeeModel() {
        Map<VehicleType, FeeModel> result =  new HashMap<>();
        LocationType locationType = LocationType.STADIUM;
        for (VehicleType vt:
                locationVehicleMap.get(locationType)) {
            switch (vt) {
                case SMALL -> result.put(vt, new SmallVehicleFeeModel(locationType, new ArrayList<>(
                        List.of(
                                new HourFeeMap(0, Double.valueOf(30)),
                                new HourFeeMap(4, Double.valueOf(60)),
                                new HourFeeMap(12, Double.valueOf(100))
                        )
                )));
                case MEDIUM -> result.put(vt, new MediumVehicleFeeModel(locationType, new ArrayList<>(
                        List.of(
                                new HourFeeMap(0, Double.valueOf(60)),
                                new HourFeeMap(4, Double.valueOf(120)),
                                new HourFeeMap(12, Double.valueOf(200))
                        )
                )));
            }
        }
        return result;
    }

    private Map<VehicleType, FeeModel> createAirportFeeModel() {
        Map<VehicleType, FeeModel> result =  new HashMap<>();
        LocationType locationType = LocationType.AIRPORT;
        for (VehicleType vt:
                locationVehicleMap.get(locationType)) {
            switch (vt) {
                case SMALL -> result.put(vt, new SmallVehicleFeeModel(locationType, new ArrayList<>(
                        List.of(
                                new HourFeeMap(0, Double.valueOf(0)),
                                new HourFeeMap(1, Double.valueOf(40)),
                                new HourFeeMap(8, Double.valueOf(60)),
                                new HourFeeMap(24, Double.valueOf(80))
                        )
                )));
                case MEDIUM -> result.put(vt, new MediumVehicleFeeModel(locationType, new ArrayList<>(
                        List.of(
                                new HourFeeMap(0, Double.valueOf(60)),
                                new HourFeeMap(12, Double.valueOf(80)),
                                new HourFeeMap(24, Double.valueOf(100))
                         )
                )));
            }
        }
        return result;
    }
}
