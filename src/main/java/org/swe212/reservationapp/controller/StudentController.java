package org.swe212.reservationapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.swe212.reservationapp.db.StudentCrud;
import org.swe212.reservationapp.dto.Student;
import java.util.Optional;

public class StudentController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField deptField;

    private StudentCrud db = new StudentCrud();

    @FXML
    private void handleClear() {
        this.idField.clear();
        this.nameField.clear();
        this.deptField.clear();
    }

    @FXML
    private void handleGet() {
        String idText = this.idField.getText().trim();
        if (idText.isEmpty()) {
            this.showError("Lütfen aramak istediğiniz öğrencinin ID'sini girin.");
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            if (id <= 0) {
                this.showError("Hata: ID değeri 0'dan büyük pozitif bir sayı olmalıdır.");
                return;
            }
            Optional<Student> s = this.db.getStudentById(id);
            if (s.isPresent()) {
                this.nameField.setText(s.get().getName());
                this.deptField.setText(s.get().getDepartment());
            } else {
                this.showError("Hata: Veritabanında ID'si " + id + " olan bir öğrenci bulunamadı.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanına sadece rakam girmelisiniz (Örn: 1, 2).");
        }
    }

    @FXML
    private void handleSave() {
        if (this.idField.getText().trim().isEmpty() || this.nameField.getText().trim().isEmpty() || this.deptField.getText().trim().isEmpty()) {
            this.showError("Hata: Öğrenci kaydetmek için ID, NAME ve DEPT alanlarının hepsini doldurmalısınız.");
            return;
        }
        try {
            int id = Integer.parseInt(this.idField.getText().trim());
            if (id <= 0) {
                this.showError("Hata: ID değeri 0'dan büyük pozitif bir sayı olmalıdır.");
                return;
            }
            Student s = new Student(id, this.nameField.getText().trim(), this.deptField.getText().trim());
            int res = this.db.insertStudent(s);
            if (res == -1) {
                this.showError("Hata: ID'si " + id + " olan bir öğrenci zaten sistemde kayıtlı. Farklı bir ID deneyin.");
            } else if (res == 1) {
                this.showInfo("Başarılı: Öğrenci sisteme kaydedildi.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanına harf veya özel karakter değil, sadece rakam girmelisiniz.");
        }
    }

    @FXML
    private void handleUpdate() {
        if (this.idField.getText().trim().isEmpty() || this.nameField.getText().trim().isEmpty() || this.deptField.getText().trim().isEmpty()) {
            this.showError("Hata: Güncelleme yapmak için tüm alanları doldurmalısınız.");
            return;
        }
        try {
            int id = Integer.parseInt(this.idField.getText().trim());
            if (id <= 0) {
                this.showError("Hata: ID değeri pozitif bir sayı olmalıdır.");
                return;
            }
            Student s = new Student(id, this.nameField.getText().trim(), this.deptField.getText().trim());
            int res = this.db.updateStudent(s);
            if (res == -1) {
                this.showError("Hata: Güncellenmek istenen ID (" + id + ") bulunamadı. Önce GET ile öğrenciyi getirin.");
            } else if (res == 1) {
                this.showInfo("Başarılı: Öğrenci bilgileri güncellendi.");
            }
        } catch (NumberFormatException e) {
            this.showError("Hata: ID alanına sadece rakam girmelisiniz.");
        }
    }

    @FXML
    private void handleDelete() {
        String idText = this.idField.getText().trim();
        if (idText.isEmpty()) {
            this.showError("Hata: Lütfen silmek istediğiniz öğrencinin ID'sini girin.");
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            int res = this.db.deleteStudent(id);
            if (res == -1) {
                this.showError("Hata: Silinmek istenen ID (" + id + ") zaten veritabanında yok.");
            } else if (res == 1) {
                this.handleClear();
                this.showInfo("Başarılı: Öğrenci sistemden silindi.");
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