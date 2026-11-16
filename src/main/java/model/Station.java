package model;

import java.time.LocalTime;

public class Station {
    private int id;
    private String name;
    private int totalTracks;

    public Station(int id, String name, int totalTracks){
        this.id = id;
        this.name = name;
        this.totalTracks = totalTracks;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public boolean hasAvailableCapacity(int currentlyOccupiedTracks) {
        return currentlyOccupiedTracks < this.totalTracks;
    }

    public boolean occupiesTrackAt(Train train, LocalTime time) {
        if (train == null || time == null)
            return false;

        return train.isAtStation(this.id, time);
    }
}
