package com.maxim.jpr.Util;

import com.maxim.jpr.Models.Station;

import java.util.ArrayList;

public class StationSupplier {

    public static ArrayList<Station> getStations() {
        ArrayList<Station> stations = new ArrayList<Station>();
        stations.add(new Station("http://sky1.torontocast.com:9029/stream/1/?cb=500961.mp3", "J-Pop Powerplay Kawaii", "Kawaiiiiiiii", null));

        return stations;
    }
}
