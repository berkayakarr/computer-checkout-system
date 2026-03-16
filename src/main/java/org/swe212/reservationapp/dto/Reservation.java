package org.swe212.reservationapp.dto;

import java.time.LocalDate;

public class Reservation {
    private int studentId;
    private int computerId;
    private LocalDate resDate;
    private int duration;

    public Reservation() {}
    public Reservation(int studentId, int computerId, LocalDate resDate, int duration) {
        this.studentId = studentId;
        this.computerId = computerId;
        this.resDate = resDate;
        this.duration = duration;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getComputerId() { return computerId; }
    public void setComputerId(int computerId) { this.computerId = computerId; }
    public LocalDate getResDate() { return resDate; }
    public void setResDate(LocalDate resDate) { this.resDate = resDate; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}