package model;

import java.time.LocalTime;

public class TrainStop {
    private Station station;
    private LocalTime plannedArrival;
    private LocalTime plannedDeparture;
    private int accumulatedDelayMinutes;
    private LocalTime actualArrival;
    private LocalTime actualDeparture;

    public TrainStop(Station station, LocalTime plannedArrival, LocalTime plannedDeparture) {
        this.station = station;
        this.plannedArrival = plannedArrival;
        this.plannedDeparture = plannedDeparture;
        this.accumulatedDelayMinutes = 0;
        this.actualArrival = plannedArrival;
        this.actualDeparture = plannedDeparture;
    }

    public Station getStation() {
        return station;
    }

    public LocalTime getPlannedArrival() {
        return plannedArrival;
    }

    public LocalTime getPlannedDeparture() {
        return plannedDeparture;
    }

    public int getAccumulatedDelayMinutes() {
        return accumulatedDelayMinutes;
    }

    public LocalTime getActualArrival() {
        return actualArrival;
    }

    public LocalTime getActualDeparture() {
        return actualDeparture;
    }

    public void calculateActualTimes() {
        if (this.plannedArrival != null)
            this.actualArrival = this.plannedArrival.plusMinutes(this.accumulatedDelayMinutes);

        if (this.plannedDeparture != null)
            this.actualDeparture = this.plannedDeparture.plusMinutes(this.accumulatedDelayMinutes);
    }

    public void addDelay(int minutes) {
        this.accumulatedDelayMinutes += minutes;
        calculateActualTimes();
    }

    public boolean isOrigin() {
        return this.plannedArrival == null;
    }

    public boolean isDestination() {
        return this.plannedDeparture == null;
    }

    public boolean exceedsDayBoundary() {
        if (this.plannedArrival != null) {
            int totalMinutes = this.plannedArrival.getHour() * 60 + this.plannedArrival.getMinute() + this.accumulatedDelayMinutes;
            if (totalMinutes >= 24 * 60)
                return true;
        }

        if (this.plannedDeparture != null) {
            int totalMinutes = this.plannedDeparture.getHour() * 60 + this.plannedDeparture.getMinute() + this.accumulatedDelayMinutes;
            if (totalMinutes >= 24 * 60)
                return true;
        }

        return false;
    }
}
