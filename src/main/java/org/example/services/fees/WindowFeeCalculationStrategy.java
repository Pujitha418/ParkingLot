package org.example.services.fees;

import org.example.enums.LocationType;
import org.example.enums.VehicleType;
import org.example.models.HourFeeMap;
import org.example.models.ParkingLot;
import org.example.models.ParkingTicket;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class WindowFeeCalculationStrategy implements FeeCalculationStrategy{
    private final Logger logger = Logger.getLogger(WindowFeeCalculationStrategy.class
            .getClass().getName());
    @Override
    public Double calculate(ParkingLot parkingLot, ParkingTicket ticket, Date exitTime) {
        LocationType locationType = parkingLot.getLocationType();
        VehicleType vehicleType = ticket.getVehicle().getVehicleType();
        Date entryTime = ticket.getEntryTime();
        //Date exitTime = new Date();
        Double price = 0.0;

        long duration = exitTime.getTime()-entryTime.getTime();
        long numHours = (duration / (1000 * 60 * 60));

        System.out.println("exitTime = " + exitTime.getTime());
        System.out.println("entryTime = " + entryTime.getTime());
        logger.info("Parking Duration in hours :: "+numHours);

        List<HourFeeMap> priceMap = parkingLot.getFeeModelList().get(vehicleType).getPriceMap();
        logger.info(priceMap.toString());

        //search on priceMap to get exact price. ans = exitTime-entryTime in hrs * price for hourly price, day_hrs/hrs*price
        //for day wise price, price for flat fees.
        int low = 0;
        int high = priceMap.size()-1;
        int mid;
        int k;

        while (low<=high) {
            mid = (low+high)/2;
            k = priceMap.get(mid).getHour();

            if (k == numHours) {
                price = priceMap.get(mid).getPrice();
                break;
            }
            if (k < numHours) {
                price = priceMap.get(mid).getPrice();
                low = mid+1;
            }
            else {
                high = mid-1;
            }
        }

        logger.info("Derived price per hour :: "+price);

        if (locationType.equals(LocationType.STADIUM) && numHours>=priceMap.get(priceMap.size()-1).getHour())
            return price*numHours;
        if (locationType.equals(LocationType.AIRPORT) && numHours>=priceMap.get(priceMap.size()-1).getHour())
            return price*(Math.max(numHours, 1)/24);

        return price;
    }
}
