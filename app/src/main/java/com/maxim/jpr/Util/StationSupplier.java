package com.maxim.jpr.Util;

import com.maxim.jpr.Models.Station;

import java.util.ArrayList;

public class StationSupplier {

    public static ArrayList<Station> getStations() {
        ArrayList<Station> stations = new ArrayList<Station>();
        stations.add(new Station("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg", "test", "tessstttt", null));

        return stations;
    }
}
