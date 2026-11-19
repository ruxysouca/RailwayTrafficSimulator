package service;

import database.ConflictDAO;
import database.StationDAO;
import database.TrainDAO;
import model.Station;
import model.Train;

import java.sql.SQLException;
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
}
