package org.swe212.reservationapp.db;

import org.swe212.reservationapp.dto.Computer;
import java.sql.*;
import java.util.Optional;

/**
 * Bilgisayar envanteri üzerindeki veritabanı işlemlerini yöneten sınıf.
 */
public class ComputerCrud {
    private final String url = "jdbc:postgresql://localhost:5433/reservation_db";
    private final String user = "postgres";
    private final String pass = "postgres123";

    // Veritabanında ID araması yapar
    public Optional<Computer> getComputerById(int id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT * FROM computers WHERE id = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return Optional.of(new Computer(rs.getInt("id"), rs.getString("brand"), rs.getString("model")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    // Yeni bilgisayar ekler, ID varsa -1 döner
    public int insertComputer(Computer c) {
        if (getComputerById(c.getId()).isPresent()) return -1;
        String sql = "INSERT INTO computers (id, brand, model) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, c.getId());
            pstmt.setString(2, c.getBrand());
            pstmt.setString(3, c.getModel());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) { return 0; }
    }

    // Bilgisayar bilgilerini (Marka/Model) günceller
    public int updateComputer(Computer c) {
        if (!getComputerById(c.getId()).isPresent()) return -1;
        String sql = "UPDATE computers SET brand = ?, model = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getBrand());
            pstmt.setString(2, c.getModel());
            pstmt.setInt(3, c.getId());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) { return 0; }
    }

    // Bilgisayarı sistemden siler
    public int deleteComputer(int id) {
        if (!getComputerById(id).isPresent()) return -1;
        String sql = "DELETE FROM computers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) { return 0; }
    }
}