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

    public String getSsid ( ) {
        return ssid;
    }

    public void setSsid (String ssid) {
        this.ssid = ssid;
    }

    public int getRssi ( ) {
        return rssi;
    }

    public void setRssi (int rssi) {
        this.rssi = rssi;
    }

    @ColumnInfo(name = "SSID")
    private String ssid;

    @ColumnInfo(name = "RSSI")
    private int rssi;

    public WifiModel (String ssid, int rssi) {
        this.ssid = ssid;
        this.rssi = rssi;
    }
}
