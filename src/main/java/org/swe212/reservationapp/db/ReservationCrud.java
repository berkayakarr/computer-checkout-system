package org.swe212.reservationapp.db;

import org.swe212.reservationapp.dto.Reservation;
import java.sql.*;

public class ReservationCrud {
    private final String url = "jdbc:postgresql://localhost:5433/reservation_db";
    private final String user = "postgres";
    private final String pass = "postgres123";

    public int insertReservation(Reservation r) {
        if (!recordExists("students", r.getStudentId()) || !recordExists("computers", r.getComputerId())) {
            return -1;
        }

        String sql = "INSERT INTO reservations (student_id, computer_id, res_date, duration_hours) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, r.getStudentId());
            pstmt.setInt(2, r.getComputerId());
            pstmt.setDate(3, Date.valueOf(r.getResDate()));
            pstmt.setInt(4, r.getDuration());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) { return 0; }
    }

    // Yeni Eklenen Silme (İade Etme) Metodu
    public int deleteReservation(int studentId, int computerId) {
        String sql = "DELETE FROM reservations WHERE student_id = ? AND computer_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, computerId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) return 1;
            else return -1;
        } catch (SQLException e) {
            return 0;
        }
    }

    private boolean recordExists(String table, int id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT 1 FROM " + table + " WHERE id = " + id;
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql).next();
        } catch (SQLException e) { return false; }
    }
}