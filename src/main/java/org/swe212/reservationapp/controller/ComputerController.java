package org.swe212.reservationapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.swe212.reservationapp.db.ComputerCrud;
import org.swe212.reservationapp.dto.Computer;
import java.util.Optional;

public class ComputerController {
    @FXML
    private TextField idField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;

    private ComputerCrud db = new ComputerCrud();

    @FXML
    private void handleClear() {
        this.idField.clear();
        this.brandField.clear();
        this.modelField.clear();
    }

    @FXML
    private void handleGet() {
        String idText = this.idField.getText().trim();
        if (idText.isEmpty()) {
            this.showError("Lütfen aramak istediğiniz bilgisayarın ID'sini girin.");
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            if (id <= 0) {
                this.showError("Hata: ID değeri 0'dan büyük pozitif bir sayı olmalıdır.");
                return;
            }
            Optional<Computer> c = this.db.getComputerById(id);
            if (c.isPresent()) {
                this.brandField.setText(c.get().getBrand());
                this.modelField.setText(c.get().getModel());
            } else {
                this.showError("Hata: Veritabanında ID'si " + id + " olan bir bilgisayar bulunamadı.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanına sadece rakam girmelisiniz.");
        }
    }

    @FXML
    private void handleSave() {
        if (this.idField.getText().trim().isEmpty() || this.brandField.getText().trim().isEmpty() || this.modelField.getText().trim().isEmpty()) {
            this.showError("Hata: Bilgisayar kaydetmek için ID, BRAND ve MODEL alanlarının hepsini doldurmalısınız.");
            return;
        }
        try {
            int id = Integer.parseInt(this.idField.getText().trim());
            if (id <= 0) {
                this.showError("Hata: ID değeri pozitif bir sayı olmalıdır.");
                return;
            }
            Computer c = new Computer(id, this.brandField.getText().trim(), this.modelField.getText().trim());
            int res = this.db.insertComputer(c);
            if (res == -1) {
                this.showError("Hata: ID'si " + id + " olan bir bilgisayar zaten kayıtlı.");
            } else if (res == 1) {
                this.showInfo("Başarılı: Bilgisayar sisteme kaydedildi.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanına harf değil, sadece rakam girmelisiniz.");
        }
    }

    @FXML
    private void handleUpdate() {
        if (this.idField.getText().trim().isEmpty() || this.brandField.getText().trim().isEmpty() || this.modelField.getText().trim().isEmpty()) {
            this.showError("Hata: Güncelleme yapmak için tüm alanları doldurmalısınız.");
            return;
        }
        try {
            int id = Integer.parseInt(this.idField.getText().trim());
            if (id <= 0) {
                this.showError("Hata: ID pozitif bir sayı olmalıdır.");
                return;
            }
            Computer c = new Computer(id, this.brandField.getText().trim(), this.modelField.getText().trim());
            int res = this.db.updateComputer(c);
            if (res == -1) {
                this.showError("Hata: Güncellenmek istenen bilgisayar ID'si (" + id + ") bulunamadı.");
            } else if (res == 1) {
                this.showInfo("Başarılı: Bilgisayar bilgileri güncellendi.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanına sadece rakam girmelisiniz.");
        }
    }

    @FXML
    private void handleDelete() {
        String idText = this.idField.getText().trim();
        if (idText.isEmpty()) {
            this.showError("Hata: Lütfen silmek istediğiniz bilgisayarın ID'sini girin.");
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            int res = this.db.deleteComputer(id);
            if (res == -1) {
                this.showError("Hata: Silinmek istenen bilgisayar (" + id + ") zaten mevcut değil.");
            } else if (res == 1) {
                this.handleClear();
                this.showInfo("Başarılı: Bilgisayar sistemden silindi.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanına sadece rakam girmelisiniz.");
        }
    }

    @FXML
    private void handleClose() {
        ((Stage)this.idField.getScene().getWindow()).close();
    }

    private void showError(String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    private void showInfo(String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}