package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.LocationType;
import org.example.enums.VehicleType;

import java.util.List;
import java.util.Map;

@Getter
public abstract class FeeModel extends BaseModel {
    private LocationType locationType;
    private VehicleType vehicleType;
    private List<HourFeeMap> priceMap; //store start  Interval & price in sorted order sorted by key=start interval

    public FeeModel(VehicleType vehicleType, LocationType locationType, List<HourFeeMap> priceMap) {
        this.locationType = locationType;
        this.vehicleType = vehicleType;
        this.priceMap = priceMap;
    }
}
