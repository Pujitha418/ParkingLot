package org.example.services.fees;

import org.example.enums.VehicleType;
import org.example.models.HourFeeMap;
import org.example.models.ParkingLot;
import org.example.models.ParkingTicket;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class SumBracketsFeeCalculationStrategy implements FeeCalculationStrategy{
    private final Logger logger = Logger.getLogger(WindowFeeCalculationStrategy.class
            .getClass().getName());
    @Override
    public Double calculate(ParkingLot parkingLot, ParkingTicket ticket, Date exitTime) {
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

        int remHours = (int) numHours;
        for (int i = 0; i < priceMap.size()-1; i++) {
            int bracketEnd = priceMap.get(i+1).getHour();
            Double currPrice = priceMap.get(i).getPrice();
            price += currPrice;
            remHours = Math.max(0, remHours-(bracketEnd-priceMap.get(i).getHour()+(priceMap.get(i).getHour()==0?-1:1)));

            if (remHours == 0)
                    break;
        }

        logger.info("Derived price per hour-l1 :: "+price+" remHours::"+remHours);
        price += (remHours*priceMap.get(priceMap.size()-1).getPrice());

        logger.info("Derived price per hour :: "+price);

        return price;
    }
}
