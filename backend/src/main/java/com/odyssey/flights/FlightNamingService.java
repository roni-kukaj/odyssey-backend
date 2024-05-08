package com.odyssey.flights;

import com.odyssey.locations.Location;

import java.sql.Time;
import java.sql.Timestamp;

public class FlightNamingService {

    public static String getFlightName(Location origin, Location destination, Timestamp departure) {
        String originName = origin.getCity().toUpperCase();
        String destinationName = destination.getCity().toUpperCase();
        String time = departure.toString().replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll(" ", "")
                .replaceAll("\\.", "");
        String name = (originName.length() >= 3 ? originName.substring(0, 3) : originName) + "-" +
                (destinationName.length() >= 3 ? destinationName.substring(0, 3) : destinationName) + "@" +
                time;
        return name;
    }

}
