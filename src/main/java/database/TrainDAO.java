package database;

import model.Station;
import model.Train;
import model.TrainStop;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;

public class TrainDAO {

    public int insertTrain(String trainCode) throws SQLException {
        String sql = "INSERT INTO trains (train_code) VALUES (?) " +
                "ON CONFLICT (train_code) DO UPDATE SET train_code = EXCLUDED.train_code " +
                "RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trainCode);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Failed to insert or retrieve train: " + trainCode);
    }

    public void insertTrainStop(int trainId, int stationId, int stopOrder,
                                LocalTime plannedArrival, LocalTime plannedDeparture) throws SQLException {
        String sql = "INSERT INTO train_stops (train_id, station_id, stop_order, planned_arrival, " +
                "planned_departure, accumulated_delay, actual_arrival, actual_departure) " +
                "VALUES (?, ?, ?, ?, ?, 0, ?, ?) " +
                "ON CONFLICT (train_id, stop_order) DO UPDATE SET " +
                "station_id = EXCLUDED.station_id, " +
                "planned_arrival = EXCLUDED.planned_arrival, " +
                "planned_departure = EXCLUDED.planned_departure, " +
                "accumulated_delay = 0, " +
                "actual_arrival = EXCLUDED.actual_arrival, " +
                "actual_departure = EXCLUDED.actual_departure";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, stationId);
            pstmt.setInt(3, stopOrder);
            setNullableTime(pstmt, 4, plannedArrival);
            setNullableTime(pstmt, 5, plannedDeparture);
            setNullableTime(pstmt, 6, plannedArrival);
            setNullableTime(pstmt, 7, plannedDeparture);
            pstmt.executeUpdate();
        }
    }

    private void setNullableTime(PreparedStatement pstmt, int index, LocalTime time) throws SQLException {
        if (time == null) {
            pstmt.setNull(index, Types.TIME);
        } else {
            pstmt.setTime(index, Time.valueOf(time));
        }
    }

    public List<Train> getAllTrains(List<Station> stations) throws SQLException {
        List<Train> trains = new ArrayList<>();
        String sqlTrains = "SELECT id, train_code FROM trains ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlTrains);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String trainCode = rs.getString("train_code");
                Train train = new Train(id, trainCode);

                loadStopsForTrain(conn, train, stations);
                trains.add(train);
            }
        }

        return trains;
    }

    private void loadStopsForTrain(Connection conn, Train train, List<Station> stations) throws SQLException {
        String sqlStops = "SELECT station_id, planned_arrival, planned_departure, accumulated_delay " +
                "FROM train_stops WHERE train_id = ? ORDER BY stop_order";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlStops)) {
            pstmt.setInt(1, train.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int stationId = rs.getInt("station_id");
                    Station station = findStationById(stations, stationId);

                    Time arrSql = rs.getTime("planned_arrival");
                    Time depSql = rs.getTime("planned_departure");
                    LocalTime plannedArrival = (arrSql != null) ? arrSql.toLocalTime() : null;
                    LocalTime plannedDeparture = (depSql != null) ? depSql.toLocalTime() : null;
                    int accumulatedDelay = rs.getInt("accumulated_delay");

                    if (station != null) {
                        TrainStop stop = new TrainStop(station, plannedArrival, plannedDeparture);
                        if (accumulatedDelay > 0) {
                            stop.addDelay(accumulatedDelay);
                        }
                        train.addStop(stop);
                    }
                }
            }
        }
    }

    public void updateTrainStopDelays(Train train) throws SQLException {
        String sql = "UPDATE train_stops SET accumulated_delay = ?, actual_arrival = ?, actual_departure = ? " +
                "WHERE train_id = ? AND station_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (TrainStop stop : train.getStops()) {
                pstmt.setInt(1, stop.getAccumulatedDelayMinutes());
                setNullableTime(pstmt, 2, stop.getActualArrival());
                setNullableTime(pstmt, 3, stop.getActualDeparture());
                pstmt.setInt(4, train.getId());
                pstmt.setInt(5, stop.getStation().getId());
                pstmt.executeUpdate();
            }
        }
    }

    private Station findStationById(List<Station> stations, int stationId) {
        for (Station s : stations) {
            if (s.getId() == stationId) {
                return s;
            }
        }

        return null;
    }
}