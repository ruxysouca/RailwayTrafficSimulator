package service;

import database.ConflictDAO;
import database.StationDAO;
import database.TrainDAO;
import model.Station;
import model.Train;
import model.ConflictRecord;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RailwayService {
    private final StationDAO stationDAO;
    private final TrainDAO trainDAO;
    private final ConflictDAO conflictDAO;

    private List<Station> stations;
    private List<Train> trains;

    public RailwayService() {
        this.stationDAO = new StationDAO();
        this.trainDAO = new TrainDAO();
        this.conflictDAO = new ConflictDAO();
        this.stations = new ArrayList<>();
        this.trains = new ArrayList<>();
    }

    public RailwayService(StationDAO stationDAO, TrainDAO trainDAO, ConflictDAO conflictDAO) {
        this.stationDAO = stationDAO;
        this.trainDAO = trainDAO;
        this.conflictDAO = conflictDAO;
        this.stations = new ArrayList<>();
        this.trains = new ArrayList<>();
    }

    public void loadNetworkData() throws SQLException {
        this.stations = stationDAO.getAllStations();
        this.trains = trainDAO.getAllTrains(this.stations);
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Train> getTrains() {
        return trains;
    }

    public boolean applyDelayToTrain(String trainCode, int fromStationId, int delayMinutes) throws SQLException {
        if (trainCode == null || delayMinutes <= 0) {
            return false;
        }

        for (Train train : this.trains) {
            if (train.getTrainCode().equalsIgnoreCase(trainCode.trim())) {
                train.propagateDelay(fromStationId, delayMinutes);
                trainDAO.updateTrainStopDelays(train);
                return true;
            }
        }

        return false;
    }

    public List<Train> getTrainsAtStationAtTime(int stationId, LocalTime time) {
        List<Train> occupyingTrains = new ArrayList<>();
        if (time == null) {
            return occupyingTrains;
        }

        Station targetStation = null;
        for (Station station : this.stations) {
            if (station.getId() == stationId) {
                targetStation = station;
                break;
            }
        }

        if (targetStation == null) {
            return occupyingTrains;
        }

        for (Train train : this.trains) {
            if (targetStation.occupiesTrackAt(train, time)) {
                occupyingTrains.add(train);
            }
        }

        return occupyingTrains;
    }

    public boolean hasTrackConflict(int stationId, LocalTime time) {
        if (time == null) {
            return false;
        }

        Station targetStation = null;
        for (Station station : this.stations) {
            if (station.getId() == stationId) {
                targetStation = station;
                break;
            }
        }

        if (targetStation == null) {
            return false;
        }

        List<Train> occupyingTrains = getTrainsAtStationAtTime(stationId, time);
        return occupyingTrains.size() > targetStation.getTotalTracks();
    }

    public ConflictRecord resolveConflict(int stationId, String trainCode, int userId,
                                          LocalTime conflictTime, int extraDelayMinutes) throws SQLException {
        if (trainCode == null || conflictTime == null || extraDelayMinutes <= 0) {
            return null;
        }

        Train targetTrain = null;
        for (Train train : this.trains) {
            if (train.getTrainCode().equalsIgnoreCase(trainCode.trim())) {
                targetTrain = train;
                break;
            }
        }

        if (targetTrain == null) {
            return null;
        }

        targetTrain.propagateDelay(stationId, extraDelayMinutes);
        trainDAO.updateTrainStopDelays(targetTrain);

        int conflictId = conflictDAO.insertConflict(
                stationId, targetTrain.getId(), userId, conflictTime, extraDelayMinutes
        );

        return new ConflictRecord(
                conflictId, stationId, targetTrain.getId(), userId, conflictTime, extraDelayMinutes
        );
    }
}

