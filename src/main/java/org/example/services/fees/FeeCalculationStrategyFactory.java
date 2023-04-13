package org.example.services.fees;

import org.example.enums.LocationType;
import org.example.exceptions.UnknownLocationTypeExcpetion;

public class FeeCalculationStrategyFactory {
    public static FeeCalculationStrategy getFeeCalculationStrategy(LocationType locationType) throws UnknownLocationTypeExcpetion {
        return switch (locationType) {
            case MALL, AIRPORT -> new WindowFeeCalculationStrategy();
            case STADIUM -> new SumBracketsFeeCalculationStrategy();
            default -> throw new UnknownLocationTypeExcpetion();
        };

    }
}
