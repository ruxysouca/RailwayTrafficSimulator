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
        // TODO : implement
    }

    public void updateTrainStopDelays(Train train) throws SQLException {
        // TODO : implement
    }

    private Station findStationById(List<Station> stations, int stationId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}