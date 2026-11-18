package database;

import model.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationDAO {

    public void insertStation(String name, int totalTracks) throws SQLException {
        String sql = "INSERT INTO stations (name, total_tracks) VALUES (?, ?) " +
                "ON CONFLICT (name) DO UPDATE SET total_tracks = EXCLUDED.total_tracks";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, totalTracks);
            pstmt.executeUpdate();
        }
    }

    public List<Station> getAllStations() throws SQLException {
        List<Station> stations = new ArrayList<>();
        String sql = "SELECT id, name, total_tracks FROM stations ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int totalTracks = rs.getInt("total_tracks");

                stations.add(new Station(id, name, totalTracks));
            }
        }

        return stations;
    }
}
