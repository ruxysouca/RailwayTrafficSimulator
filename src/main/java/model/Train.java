package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Train {
    private int id;
    private String trainCode;
    private List<TrainStop> stops;

    public Train(int id, String trainCode){
        this.id = id;
        this.trainCode = trainCode;
        this.stops = new ArrayList<>();
    }

    public int getId(){
        return id;
    }

    public String getTrainCode(){
        return  trainCode;
    }

    public List<TrainStop> getStops(){
        return stops;
    }

    public void addStop(TrainStop stop){
        if(stop != null)
            this.stops.add(stop);
    }

    public int getTotalDelayAtDestination(){
        if(this.stops.isEmpty())
            return 0;

        TrainStop lastStop = this.stops.get(this.stops.size() - 1);
        return lastStop.getAccumulatedDelayMinutes();
    }

    public void propagateDelay(int fromStationId, int minutes){
        boolean stationFound = false;

        for (TrainStop stop : this.stops){
            if (stop.getStation().getId() == fromStationId)
                stationFound = true;

            if (stationFound)
                stop.addDelay(minutes);
        }
    }

    public boolean isAtStation(int stationId, LocalTime time){
        for (TrainStop stop : this.stops){
            if (stop.getStation().getId() == stationId){
                LocalTime arrival = stop.getActualArrival();
                LocalTime departure = stop.getActualDeparture();

                if (arrival == null && departure != null)
                    return time.equals(departure);

                if (arrival != null && departure == null)
                    return time.equals(arrival);

                if (arrival != null && departure != null)
                    return !time.isBefore(arrival) && !time.isAfter(departure);
            }
        }

        return false;
    }
}
