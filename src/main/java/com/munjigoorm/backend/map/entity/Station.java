package com.munjigoorm.backend.map.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "air_station_info")
public class Station {

    @Id
    @Column(name = "station_name")
    private String stationName;

    @Column(name = "addr")
    private String addr;

    @Column(name = "x_coord")
    private Double dmX;

    @Column(name = "y_coord")
    private Double dmY;
}
