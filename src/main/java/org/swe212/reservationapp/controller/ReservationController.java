package org.swe212.reservationapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.swe212.reservationapp.db.ReservationCrud;
import org.swe212.reservationapp.dto.Reservation;
import java.time.LocalDate;

public class ReservationController {
    @FXML
    private TextField studentIdField;
    @FXML
    private TextField computerIdField;
    @FXML
    private TextField durationField;
    @FXML
    private DatePicker datePicker;

    private ReservationCrud db = new ReservationCrud();

    @FXML
    private void handleClear() {
        this.studentIdField.clear();
        this.computerIdField.clear();
        this.durationField.clear();
        this.datePicker.setValue(null);
    }

    @FXML
    private void handleClose() {
        ((Stage)this.studentIdField.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        String sIdText = this.studentIdField.getText().trim();
        String cIdText = this.computerIdField.getText().trim();
        String durText = this.durationField.getText().trim();
        LocalDate date = this.datePicker.getValue();

        if (sIdText.isEmpty() || cIdText.isEmpty() || durText.isEmpty() || date == null) {
            this.showError("Hata: Lütfen Student ID, Computer ID, Date ve Duration alanlarını eksiksiz doldurun.");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            this.showError("Hata: Geçmiş bir tarihe (" + date + ") rezervasyon yapamazsınız. Lütfen bugünü veya geleceği seçin.");
            return;
        }

        int sId;
        int cId;
        int duration;

        try {
            sId = Integer.parseInt(sIdText);
            if (sId <= 0) {
                this.showError("Hata: Student ID 0'dan büyük pozitif bir sayı olmalıdır.");
                return;
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: Student ID alanına harf yazılamaz, geçerli bir sayı girin.");
            return;
        }

        try {
            cId = Integer.parseInt(cIdText);
            if (cId <= 0) {
                this.showError("Hata: Computer ID 0'dan büyük pozitif bir sayı olmalıdır.");
                return;
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: Computer ID alanına harf yazılamaz, geçerli bir sayı girin.");
            return;
        }

        try {
            duration = Integer.parseInt(durText);
            if (duration <= 0 || duration > 24) {
                this.showError("Hata: Ödünç alma süresi (Duration) en az 1 saat, en fazla 24 saat olmalıdır.");
                return;
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: Duration (Süre) alanına sadece geçerli bir saat rakamı girin.");
            return;
        }

        Reservation res = new Reservation(sId, cId, date, duration);
        int result = this.db.insertReservation(res);

        if (result == -1) {
            this.showError("İlişki Hatası: Veritabanında ID'si " + sId + " olan bir öğrenci veya ID'si " + cId + " olan bir bilgisayar bulunamadı. Lütfen önce bu kayıtların var olduğundan emin olun.");
        } else if (result == 1) {
            this.showInfo("Başarılı: Bilgisayar ödünç alma (checkout) işlemi başarıyla kaydedildi!");
            this.handleClear();
        } else {
            this.showError("Veritabanına bağlanırken veya kaydederken teknik bir hata oluştu.");
        }
    }

    // Yeni Eklenen Silme (İade Etme) Metodu
    @FXML
    private void handleDelete() {
        String sIdText = this.studentIdField.getText().trim();
        String cIdText = this.computerIdField.getText().trim();

        if (sIdText.isEmpty() || cIdText.isEmpty()) {
            this.showError("Hata: Silme işlemi için Student ID ve Computer ID alanlarını doldurmalısınız.");
            return;
        }

        try {
            int sId = Integer.parseInt(sIdText);
            int cId = Integer.parseInt(cIdText);

            if (sId <= 0 || cId <= 0) {
                this.showError("Hata: ID değerleri 0'dan büyük olmalıdır.");
                return;
            }

            int result = this.db.deleteReservation(sId, cId);

            if (result == -1) {
                this.showError("Hata: " + sId + " numaralı öğrencinin, " + cId + " numaralı bilgisayar için aktif bir rezervasyonu bulunamadı.");
            } else if (result == 1) {
                this.showInfo("Başarılı: Rezervasyon silindi (Bilgisayar iade edildi).");
                this.handleClear();
            } else {
                this.showError("Veritabanına bağlanırken bir hata oluştu.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanlarına sadece geçerli rakamlar girmelisiniz.");
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}