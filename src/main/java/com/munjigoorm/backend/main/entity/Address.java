package com.munjigoorm.backend.main.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "korea_loc")
public class Address {

    @Id
    @Column(name = "full_addr")
    private String fullAddr;

    @Column(name = "short_addr")
    private String shortAddr;

    @Column(name = "x_coord")
    private Double xCoord;

    @Column(name = "y_coord")
    private Double yCoord;

    @Column(name="station_name")
    private String stationName;

    @Builder
    public Address(String fullAddr, String shortAddr, Double xCoord, Double yCoord, String stationName) {
        this.fullAddr = fullAddr;
        this.shortAddr = shortAddr;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.stationName = stationName;
    }
}
