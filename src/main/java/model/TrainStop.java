package model;

import java.time.LocalTime;

public class TrainStop {
    private Station station;
    private LocalTime plannedArrival;
    private LocalTime plannedDeparture;
    private int accumulatedDelayMinutes;
    private LocalTime actualArrival;
    private LocalTime actualDeparture;

    public TrainStop(Station station, LocalTime plannedArrival, LocalTime plannedDeparture){
        this.station = station;
        this.plannedArrival = plannedArrival;
        this.plannedDeparture = plannedDeparture;
        this.accumulatedDelayMinutes = 0;
        this.actualArrival = plannedArrival;
        this.actualDeparture = plannedDeparture;
    }

    public Station getStation(){
        return station;
    }

    public LocalTime getPlannedArrival(){
        return plannedArrival;
    }

    public LocalTime getPlannedDeparture(){
        return plannedDeparture;
    }

    public int getAccumulatedDelayMinutes(){
        return accumulatedDelayMinutes;
    }

    public LocalTime getActualArrival(){
        return actualArrival;
    }

    public LocalTime getActualDeparture(){
        return actualDeparture;
    }
}
