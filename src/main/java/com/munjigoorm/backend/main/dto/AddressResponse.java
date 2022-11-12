package com.munjigoorm.backend.main.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

    private String fullAddr;

    private String shortAddr;

    private Double xCoord;

    private Double yCoord;

    @Builder
    public AddressResponse(String fullAddr, String shortAddr, Double xCoord, Double yCoord) {
        this.fullAddr = fullAddr;
        this.shortAddr = shortAddr;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }
}
