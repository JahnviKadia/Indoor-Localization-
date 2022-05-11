package com.mc2022.template;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wifi")
public class WifiModel {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    public int getId ( ) {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    @ColumnInfo(name = "location")
    private String location;

    public String getLocation ( ) {
        return location;
    }

    public void setLocation (String location) {
        this.location = location;
    }

    @ColumnInfo(name = "R1")
    private double R1;

    public double getR1 ( ) {
        return R1;
    }

    public void setR1 (double r1) {
        R1 = r1;
    }

    public double getR2 ( ) {
        return R2;
    }

    public void setR2 (double r2) {
        R2 = r2;
    }

    public double getR3 ( ) {
        return R3;
    }

    public void setR3 (double r3) {
        R3 = r3;
    }

    public double getR4 ( ) {
        return R4;
    }

    public void setR4 (double r4) {
        R4 = r4;
    }

    @ColumnInfo(name = "R2")
    private double R2;

    public String getLoc ( ) {
        return location;
    }

    public void setLoc (String loc) {
        this.location = loc;
    }

    @ColumnInfo(name = "R3")
    private double R3;

    @ColumnInfo(name = "R4")
    private double R4;

    @ColumnInfo(name = "R5")
    private double R5;

    public double getR5 ( ) {
        return R5;
    }

    public void setR5 (double r5) {
        R5 = r5;
    }

    public WifiModel (String loc, double r1, double r2, double r3, double r4, double r5) {
        location = loc;
        R1 = r1;
        R2 = r2;
        R3 = r3;
        R4 = r4;
        R5 = r5;
    }

    public WifiModel ( ) {
    }
}
