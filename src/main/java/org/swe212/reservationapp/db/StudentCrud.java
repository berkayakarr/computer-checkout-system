package org.swe212.reservationapp.db;

import org.swe212.reservationapp.dto.Student;
import java.sql.*;
import java.util.Optional;

/**
 * Öğrenci tablosu üzerindeki veritabanı işlemlerini yöneten sınıf.
 */
public class StudentCrud {
    private final String url = "jdbc:postgresql://localhost:5433/reservation_db";
    private final String user = "postgres";
    private final String pass = "postgres123";

    // ID'ye göre öğrenci arar
    public Optional<Student> getStudentById(int id) {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT * FROM students WHERE id = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return Optional.of(new Student(rs.getInt("id"), rs.getString("name"), rs.getString("department")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    // Yeni öğrenci ekler, ID çakışması varsa -1 döner
    public int insertStudent(Student s) {
        if (getStudentById(s.getId()).isPresent()) return -1;
        String sql = "INSERT INTO students (id, name, department) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s.getId());
            pstmt.setString(2, s.getName());
            pstmt.setString(3, s.getDepartment());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) { return 0; }
    }

    // Mevcut öğrenci bilgilerini günceller
    public int updateStudent(Student s) {
        if (!getStudentById(s.getId()).isPresent()) return -1;
        String sql = "UPDATE students SET name = ?, department = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getDepartment());
            pstmt.setInt(3, s.getId());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) { return 0; }
    }

    // Öğrenciyi sistemden siler
    public int deleteStudent(int id) {
        if (!getStudentById(id).isPresent()) return -1;
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) { return 0; }
    }
}