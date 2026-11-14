package model;

import java.time.LocalTime;

public class ConflictRecord {
    private int id;
    private int stationId;
    private int trainId;
    private int userId;
    private LocalTime conflictTime;
    private int extraDelayApplied;

    public ConflictRecord(int id, int stationId, int trainId, int userId, LocalTime conflictTime, int extraDelayApplied) {
        this.id = id;
        this.stationId = stationId;
        this.trainId = trainId;
        this.userId = userId;
        this.conflictTime = conflictTime;
        this.extraDelayApplied = extraDelayApplied;
    }

    public int getId() {
        return id;
    }

    public int getStationId() {
        return stationId;
    }

    public int getTrainId() {
        return trainId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalTime getConflictTime() {
        return conflictTime;
    }

    public int getExtraDelayApplied() {
        return extraDelayApplied;
    }

    public String toLogMessage() {
        return String.format("[Conflict #%d] Station ID: %d | Train ID: %d | Time: %s | Extra delay: +%d min | User ID: %d",
                this.id, this.stationId, this.trainId, this.conflictTime, this.extraDelayApplied, this.userId);
    }

    public boolean isCritical(int thresholdMinutes) {
        return this.extraDelayApplied > thresholdMinutes;
    }
}
