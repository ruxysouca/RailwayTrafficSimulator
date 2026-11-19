package database;

import model.ConflictRecord;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConflictDAO {

    public int insertConflict(int stationId, int trainId, int userId,
                              LocalTime conflictTime, int extraDelayApplied) throws SQLException {
        String sql = "INSERT INTO conflicts (station_id, train_id, user_id, conflict_time, extra_delay) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            pstmt.setInt(2, trainId);
            pstmt.setInt(3, userId);
            pstmt.setTime(4, Time.valueOf(conflictTime));
            pstmt.setInt(5, extraDelayApplied);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Failed to insert conflict record for train ID: " + trainId);
    }

    public List<ConflictRecord> getAllConflicts() throws SQLException {
        List<ConflictRecord> conflicts = new ArrayList<>();
        String sql = "SELECT id, station_id, train_id, user_id, conflict_time, extra_delay " +
                "FROM conflicts ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                conflicts.add(mapRowToConflictRecord(rs));
            }
        }

        return conflicts;
    }

    public List<ConflictRecord> getConflictsByStationId(int stationId) throws SQLException {
        // TODO : implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteAllConflicts() throws SQLException {
        // TODO : implement
    }

    private ConflictRecord mapRowToConflictRecord(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int stationId = rs.getInt("station_id");
        int trainId = rs.getInt("train_id");
        int userId = rs.getInt("user_id");
        LocalTime conflictTime = rs.getTime("conflict_time").toLocalTime();
        int extraDelay = rs.getInt("extra_delay");

        return new ConflictRecord(id, stationId, trainId, userId, conflictTime, extraDelay);
    }
}
