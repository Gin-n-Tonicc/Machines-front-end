package com.machines.machines_front_end.dtos;

import com.machines.machines_front_end.enums.OfferSaleType;
import com.machines.machines_front_end.enums.OfferState;
import com.machines.machines_front_end.enums.OfferType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class OfferDTO extends BaseDTO {
    private UUID id;
    private String title;
    private String phoneNumber;
    private String description;
    private String websiteURL;
    private double price;
    private boolean isBulgarian;
    private boolean autoUpdate = false;
    private OfferState offerState;
    private OfferSaleType offerSaleType;
    private OfferType offerType = OfferType.BASIC;

    // *********************************
    // **   EXTRA INFO | EXTRA INFO   **
    // *********************************
    private int manufactureYear;//
    private String model;//
    private double powerSupplyVoltage;//
    private String fuelType;//
    private double horsePower;//
    private String consumption;//
    private double outputPower;//
    private String productivity;//
    private double capacity;//
    private double minRevolutions;//
    private double nominalRevolutions;//
    private double maxRevolutions;//
    private String dimensions;
    private double ownWeight;
    private String workMoves;

    public boolean isBulgarian() {
        return isBulgarian;
    }

    public void setBulgarian(boolean bulgarian) {
        isBulgarian = bulgarian;
    }
}
