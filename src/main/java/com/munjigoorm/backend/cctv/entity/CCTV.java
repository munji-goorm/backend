package com.munjigoorm.backend.cctv.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cctv_data")
public class CCTV {

    @Id
    @Column(name = "url")
    private String cctv_Url;

    @Column(name = "cctv_name")
    private String cctvName;

    @Column(name = "x_coord")
    private String xCoord;

    @Column(name = "y_coord")
    private String yCoord;
}
