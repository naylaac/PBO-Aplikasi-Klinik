import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;

public class KlinikGUI extends Application {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Pasien> daftarPasien = new ArrayList<>();
    private ArrayList<Dokter> daftarDokter = new ArrayList<>();
    private ArrayList<Obat> daftarObat = new ArrayList<>();
    private ArrayList<JadwalDokter> daftarJadwal = new ArrayList<>();
    private ArrayList<RekamMedis> daftarRekamMedis = new ArrayList<>();
    private ArrayList<Resep> daftarResep = new ArrayList<>();
    private ArrayList<Pembayaran> daftarPembayaran = new ArrayList<>();
    private Antrean antrean = new Antrean();

    private User userLogin;
    private Pasien pasienLogin;
    private Dokter dokterLogin;
    private Stage primaryStage;

    private int idPasienCounter = 1;
    private int idDokterCounter = 3;
    private int idObatCounter = 4;
    private int idRMCounter = 1;
    private int idBayarCounter = 1;
    private int idJadwalCounter = 3;
    private int idResepCounter = 1;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        initDataAwal();
        showLogin();
    }

    // ===================== DATA AWAL =====================
    private void initDataAwal() {
        users.add(new User("admin", "admin123", "admin"));
        users.add(new User("dokter1", "dok123", "dokter"));

        daftarDokter.add(new Dokter("D001", "dr. Andi Pratama", "Umum", "081234567890"));
        daftarDokter.add(new Dokter("D002", "dr. Sari Dewi", "Gigi", "081298765432"));

        daftarObat.add(new Obat("O001", "Paracetamol", "Analgesik", 100, 2000));
        daftarObat.add(new Obat("O002", "Amoxicillin", "Antibiotik", 50, 5000));
        daftarObat.add(new Obat("O003", "Antasida", "Lambung", 80, 3000));

        daftarJadwal.add(new JadwalDokter("J001", daftarDokter.get(0), "Senin", "08:00", "12:00", 20));
        daftarJadwal.add(new JadwalDokter("J002", daftarDokter.get(1), "Selasa", "09:00", "13:00", 15));

        // Pasien awal
        Pasien p1 = new Pasien("P001", "Budi Santoso", "Jl. Merdeka No.1", "081111111111", "01-01-1990", "Laki-laki");
        Pasien p2 = new Pasien("P002", "Siti Rahayu", "Jl. Sudirman No.5", "082222222222", "05-05-1995", "Perempuan");
        daftarPasien.add(p1);
        daftarPasien.add(p2);
        idPasienCounter = 3;

        // User untuk pasien (ID = username = password)
        users.add(new User("P001", "P001", "pasien"));
        users.add(new User("P002", "P002", "pasien"));
    }

    // ===================== LOGIN =====================
    private void showLogin() {
        primaryStage.setTitle("Aplikasi Klinik - Login");

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2c3e50;");

        Label lblJudul = new Label("APLIKASI KLINIK");
        lblJudul.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblJudul.setTextFill(Color.WHITE);

        Label lblSub = new Label("Sistem Manajemen Klinik");
        lblSub.setFont(Font.font("Arial", 14));
        lblSub.setTextFill(Color.web("#95a5a6"));

        VBox formBox = new VBox(12);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(350);
        formBox.setStyle("-fx-background-color: #34495e; -fx-background-radius: 10;");

        Label lblForm = new Label("Silakan Login");
        lblForm.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblForm.setTextFill(Color.WHITE);

        TextField txtUser = new TextField();
        txtUser.setPromptText("Username");
        txtUser.setStyle("-fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14;");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        txtPass.setStyle("-fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14;");

        Button btnLogin = new Button("LOGIN");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 14; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 10;");
        btnLogin.setCursor(javafx.scene.Cursor.HAND);

        Label lblInfo = new Label("admin/admin123 | dokter1/dok123 | pasien: P001/P001");
        lblInfo.setFont(Font.font("Arial", 11));
        lblInfo.setTextFill(Color.web("#7f8c8d"));

        Label lblError = new Label("");
        lblError.setTextFill(Color.RED);

        btnLogin.setOnAction(e -> {
            String u = txtUser.getText().trim();
            String p = txtPass.getText().trim();
            for (User usr : users) {
                if (usr.login(u, p)) {
                    userLogin = usr;
                    pasienLogin = null;
                    dokterLogin = null;

                    if (usr.getRole().equals("pasien")) {
                        for (Pasien ps : daftarPasien) {
                            if (ps.getIdPasien().equals(u)) {
                                pasienLogin = ps;
                                break;
                            }
                        }
                    } else if (usr.getRole().equals("dokter")) {
                        // cari dokter berdasarkan urutan (dokter1 = index 0, dll)
                        int idx = 0;
                        try { idx = Integer.parseInt(u.replace("dokter", "")) - 1; } catch (Exception ex) {}
                        if (idx >= 0 && idx < daftarDokter.size()) dokterLogin = daftarDokter.get(idx);
                    }

                    showMain();
                    return;
                }
            }
            lblError.setText("Username atau password salah!");
        });

        txtPass.setOnAction(e -> btnLogin.fire());

        formBox.getChildren().addAll(lblForm, txtUser, txtPass, btnLogin, lblError);
        root.getChildren().addAll(lblJudul, lblSub, formBox, lblInfo);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // ===================== MAIN =====================
    private void showMain() {
        String role = userLogin.getRole();
        primaryStage.setTitle("Aplikasi Klinik - " + userLogin.getUsername() + " (" + role + ")");

        BorderPane root = new BorderPane();

        VBox sidebar = new VBox(5);
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPadding(new Insets(0, 0, 10, 0));

        Label lblApp = new Label("  KLINIK APP");
        lblApp.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblApp.setTextFill(Color.WHITE);
        lblApp.setPadding(new Insets(20, 10, 20, 10));
        lblApp.setMaxWidth(Double.MAX_VALUE);
        lblApp.setStyle("-fx-background-color: #1a252f;");

        Label lblUser = new Label("  " + userLogin.getUsername() + " [" + role + "]");
        lblUser.setFont(Font.font("Arial", 12));
        lblUser.setTextFill(Color.web("#95a5a6"));
        lblUser.setPadding(new Insets(5, 10, 15, 10));

        StackPane konten = new StackPane();
        konten.setStyle("-fx-background-color: #ecf0f1;");

        // Menu berdasarkan role
        String[] menus;
        if (role.equals("admin")) {
            menus = new String[]{"Dashboard", "Pasien", "Dokter", "Antrean", "Obat", "Jadwal", "Pembayaran"};
        } else if (role.equals("dokter")) {
            menus = new String[]{"Dashboard", "Rekam Medis", "Resep Obat"};
        } else {
            menus = new String[]{"Nomor Antrean", "Rekam Medis Pribadi", "Resep Saya"};
        }

        sidebar.getChildren().addAll(lblApp, lblUser);

        for (String menu : menus) {
            Button btn = buatTombolSidebar(menu);
            btn.setOnAction(e -> {
                konten.getChildren().clear();
                switch (menu) {
                    case "Dashboard": konten.getChildren().add(buatDashboard()); break;
                    case "Pasien": konten.getChildren().add(buatPanelPasien()); break;
                    case "Dokter": konten.getChildren().add(buatPanelDokter()); break;
                    case "Antrean": konten.getChildren().add(buatPanelAntrean()); break;
                    case "Obat": konten.getChildren().add(buatPanelObat()); break;
                    case "Jadwal": konten.getChildren().add(buatPanelJadwal()); break;
                    case "Pembayaran": konten.getChildren().add(buatPanelPembayaran()); break;
                    case "Rekam Medis": konten.getChildren().add(buatPanelRekamMedis(false)); break;
                    case "Resep Obat": konten.getChildren().add(buatPanelResep(false)); break;
                    case "Nomor Antrean": konten.getChildren().add(buatPanelNomorAntrean()); break;
                    case "Rekam Medis Pribadi": konten.getChildren().add(buatPanelRekamMedisPribadi()); break;
                    case "Resep Saya": konten.getChildren().add(buatPanelResepPasien()); break;
                }
            });
            sidebar.getChildren().add(btn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);

        Button btnLogout = buatTombolSidebar("Logout");
        btnLogout.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; " +
                "-fx-font-size: 13; -fx-alignment: CENTER-LEFT; -fx-padding: 12 15; " +
                "-fx-border-width: 0; -fx-background-radius: 0;");
        btnLogout.setOnAction(e -> showLogin());
        sidebar.getChildren().add(btnLogout);

        // Tampilan awal sesuai role
        if (role.equals("admin")) konten.getChildren().add(buatDashboard());
        else if (role.equals("dokter")) konten.getChildren().add(buatPanelRekamMedis(false));
        else konten.getChildren().add(buatPanelNomorAntrean());

        root.setLeft(sidebar);
        root.setCenter(konten);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
    }

    private Button buatTombolSidebar(String teks) {
        Button btn = new Button(teks);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; " +
                "-fx-font-size: 13; -fx-alignment: CENTER-LEFT; -fx-padding: 12 15; " +
                "-fx-border-width: 0; -fx-background-radius: 0;");
        btn.setCursor(javafx.scene.Cursor.HAND);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 13; -fx-alignment: CENTER-LEFT; -fx-padding: 12 15; " +
                "-fx-border-width: 0; -fx-background-radius: 0;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; " +
                "-fx-font-size: 13; -fx-alignment: CENTER-LEFT; -fx-padding: 12 15; " +
                "-fx-border-width: 0; -fx-background-radius: 0;"));
        return btn;
    }

    // ===================== DASHBOARD =====================
    private VBox buatDashboard() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(25));
        panel.setStyle("-fx-background-color: #ecf0f1;");

        Label judul = new Label("Dashboard");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        GridPane kartu = new GridPane();
        kartu.setHgap(15);
        kartu.setVgap(15);

        kartu.add(buatKartu("Pasien", String.valueOf(daftarPasien.size()), "#3498db"), 0, 0);
        kartu.add(buatKartu("Dokter", String.valueOf(daftarDokter.size()), "#2ecc71"), 1, 0);
        kartu.add(buatKartu("Antrean", String.valueOf(antrean.getJumlahAntrean()), "#e67e22"), 2, 0);
        kartu.add(buatKartu("Obat", String.valueOf(daftarObat.size()), "#9b59b6"), 3, 0);
        kartu.add(buatKartu("Rekam Medis", String.valueOf(daftarRekamMedis.size()), "#e74c3c"), 0, 1);
        kartu.add(buatKartu("Jadwal", String.valueOf(daftarJadwal.size()), "#1abc9c"), 1, 1);
        kartu.add(buatKartu("Resep", String.valueOf(daftarResep.size()), "#2980b9"), 2, 1);
        kartu.add(buatKartu("Pembayaran", String.valueOf(daftarPembayaran.size()), "#f39c12"), 3, 1);

        panel.getChildren().addAll(judul, kartu);
        return panel;
    }

    private VBox buatKartu(String judul, String nilai, String warna) {
        VBox kartu = new VBox(8);
        kartu.setAlignment(Pos.CENTER);
        kartu.setPrefSize(160, 100);
        kartu.setStyle("-fx-background-color: " + warna + "; -fx-background-radius: 8;");

        Label lblNilai = new Label(nilai);
        lblNilai.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        lblNilai.setTextFill(Color.WHITE);

        Label lblJudul = new Label(judul);
        lblJudul.setFont(Font.font("Arial", 13));
        lblJudul.setTextFill(Color.web("#ecf0f1"));

        kartu.getChildren().addAll(lblNilai, lblJudul);
        return kartu;
    }

    // ===================== PANEL PASIEN (ADMIN) =====================
    private VBox buatPanelPasien() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Manajemen Pasien");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<Pasien> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<Pasien, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPasien"));
        TableColumn<Pasien, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        TableColumn<Pasien, String> colJK = new TableColumn<>("Jenis Kelamin");
        colJK.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        TableColumn<Pasien, String> colTgl = new TableColumn<>("Tgl Lahir");
        colTgl.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        TableColumn<Pasien, String> colTlp = new TableColumn<>("No Telp");
        colTlp.setCellValueFactory(new PropertyValueFactory<>("noTelp"));
        TableColumn<Pasien, String> colAlamat = new TableColumn<>("Alamat");
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        tabel.getColumns().addAll(colId, colNama, colJK, colTgl, colTlp, colAlamat);
        ObservableList<Pasien> data = FXCollections.observableArrayList(daftarPasien);
        tabel.setItems(data);

        HBox tombol = new HBox(10);
        Button btnTambah = new Button("+ Tambah Pasien");
        Button btnHapus = new Button("Hapus");
        btnTambah.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        btnHapus.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

        btnTambah.setOnAction(e -> {
            Dialog<Pasien> dialog = new Dialog<>();
            dialog.setTitle("Tambah Pasien");
            ButtonType btnSimpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField txtNama = new TextField(); txtNama.setPromptText("Nama lengkap");
            TextField txtAlamat = new TextField(); txtAlamat.setPromptText("Alamat");
            TextField txtTlp = new TextField(); txtTlp.setPromptText("08xx");
            TextField txtTgl = new TextField(); txtTgl.setPromptText("dd-mm-yyyy");
            ComboBox<String> cmbJK = new ComboBox<>();
            cmbJK.getItems().addAll("Laki-laki", "Perempuan");
            cmbJK.setValue("Laki-laki");

            grid.add(new Label("Nama:"), 0, 0); grid.add(txtNama, 1, 0);
            grid.add(new Label("Alamat:"), 0, 1); grid.add(txtAlamat, 1, 1);
            grid.add(new Label("No Telp:"), 0, 2); grid.add(txtTlp, 1, 2);
            grid.add(new Label("Tgl Lahir:"), 0, 3); grid.add(txtTgl, 1, 3);
            grid.add(new Label("Jenis Kelamin:"), 0, 4); grid.add(cmbJK, 1, 4);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn == btnSimpan && !txtNama.getText().isEmpty()) {
                    String id = String.format("P%03d", idPasienCounter++);
                    return new Pasien(id, txtNama.getText(), txtAlamat.getText(),
                            txtTlp.getText(), txtTgl.getText(), cmbJK.getValue());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(p -> {
                daftarPasien.add(p);
                data.add(p);
                // Otomatis buat user login untuk pasien baru
                users.add(new User(p.getIdPasien(), p.getIdPasien(), "pasien"));
                showAlert("Info", "Pasien ditambahkan!\nUsername: " + p.getIdPasien() + "\nPassword: " + p.getIdPasien());
            });
        });

        btnHapus.setOnAction(e -> {
            Pasien selected = tabel.getSelectionModel().getSelectedItem();
            if (selected != null) {
                daftarPasien.remove(selected);
                data.remove(selected);
            }
        });

        tombol.getChildren().addAll(btnTambah, btnHapus);
        panel.getChildren().addAll(judul, tombol, tabel);
        return panel;
    }

    // ===================== PANEL DOKTER (ADMIN) =====================
    private VBox buatPanelDokter() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Manajemen Dokter");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<Dokter> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<Dokter, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idDokter"));
        TableColumn<Dokter, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        TableColumn<Dokter, String> colSp = new TableColumn<>("Spesialisasi");
        colSp.setCellValueFactory(new PropertyValueFactory<>("spesialisasi"));
        TableColumn<Dokter, String> colTlp = new TableColumn<>("No Telp");
        colTlp.setCellValueFactory(new PropertyValueFactory<>("noTelp"));

        tabel.getColumns().addAll(colId, colNama, colSp, colTlp);
        ObservableList<Dokter> data = FXCollections.observableArrayList(daftarDokter);
        tabel.setItems(data);

        HBox tombol = new HBox(10);
        Button btnTambah = new Button("+ Tambah Dokter");
        btnTambah.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

        btnTambah.setOnAction(e -> {
            Dialog<Dokter> dialog = new Dialog<>();
            dialog.setTitle("Tambah Dokter");
            ButtonType btnSimpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField txtNama = new TextField(); txtNama.setPromptText("dr. Nama");
            TextField txtSp = new TextField();
            TextField txtTlp = new TextField();

            grid.add(new Label("Nama:"), 0, 0); grid.add(txtNama, 1, 0);
            grid.add(new Label("Spesialisasi:"), 0, 1); grid.add(txtSp, 1, 1);
            grid.add(new Label("No Telp:"), 0, 2); grid.add(txtTlp, 1, 2);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn == btnSimpan && !txtNama.getText().isEmpty()) {
                    String id = String.format("D%03d", idDokterCounter++);
                    return new Dokter(id, txtNama.getText(), txtSp.getText(), txtTlp.getText());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(d -> { daftarDokter.add(d); data.add(d); });
        });

        tombol.getChildren().add(btnTambah);
        panel.getChildren().addAll(judul, tombol, tabel);
        return panel;
    }

    // ===================== PANEL ANTREAN (ADMIN) =====================
    private VBox buatPanelAntrean() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Manajemen Antrean");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Label lblJumlah = new Label("Jumlah antrean: " + antrean.getJumlahAntrean());
        lblJumlah.setFont(Font.font("Arial", 14));

        TableView<String[]> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<String[], String> colNo = new TableColumn<>("No Urut");
        colNo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> colId = new TableColumn<>("ID Pasien");
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> colNama = new TableColumn<>("Nama Pasien");
        colNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));

        tabel.getColumns().addAll(colNo, colId, colNama);
        ObservableList<String[]> data = FXCollections.observableArrayList();
        tabel.setItems(data);

        HBox tombol = new HBox(10);
        Button btnDaftar = new Button("+ Daftar Pasien");
        Button btnPanggil = new Button("Panggil Berikutnya");
        btnDaftar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        btnPanggil.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

        btnDaftar.setOnAction(e -> {
            if (daftarPasien.isEmpty()) { showAlert("Info", "Belum ada pasien!"); return; }
            ComboBox<String> cmb = new ComboBox<>();
            for (Pasien p : daftarPasien) cmb.getItems().add(p.getNama());
            cmb.setValue(cmb.getItems().get(0));

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Daftar Antrean");
            ButtonType btnOk = new ButtonType("Daftar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnOk, ButtonType.CANCEL);
            VBox box = new VBox(10, new Label("Pilih Pasien:"), cmb);
            box.setPadding(new Insets(20));
            dialog.getDialogPane().setContent(box);
            dialog.setResultConverter(btn -> btn == btnOk ? cmb.getValue() : null);

            dialog.showAndWait().ifPresent(nama -> {
                for (Pasien p : daftarPasien) {
                    if (p.getNama().equals(nama)) {
                        int nomor = antrean.tambahAntrean(p);
                        lblJumlah.setText("Jumlah antrean: " + antrean.getJumlahAntrean());
                        data.add(new String[]{String.valueOf(nomor), p.getIdPasien(), p.getNama()});
                        showAlert("Sukses", "Nomor antrean: " + nomor);
                        break;
                    }
                }
            });
        });

        btnPanggil.setOnAction(e -> {
            Pasien p = antrean.panggilBerikutnya();
            if (p != null) {
                lblJumlah.setText("Jumlah antrean: " + antrean.getJumlahAntrean());
                if (!data.isEmpty()) data.remove(0);
                showAlert("Panggil", "Memanggil: " + p.getNama());
            } else {
                showAlert("Info", "Antrean kosong!");
            }
        });

        tombol.getChildren().addAll(btnDaftar, btnPanggil);
        panel.getChildren().addAll(judul, lblJumlah, tombol, tabel);
        return panel;
    }

    // ===================== PANEL OBAT (ADMIN) =====================
    private VBox buatPanelObat() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Manajemen Obat");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<Obat> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<Obat, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idObat"));
        TableColumn<Obat, String> colNama = new TableColumn<>("Nama Obat");
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaObat"));
        TableColumn<Obat, String> colJenis = new TableColumn<>("Jenis");
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        TableColumn<Obat, Integer> colStok = new TableColumn<>("Stok");
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        TableColumn<Obat, Double> colHarga = new TableColumn<>("Harga");
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));

        tabel.getColumns().addAll(colId, colNama, colJenis, colStok, colHarga);
        ObservableList<Obat> data = FXCollections.observableArrayList(daftarObat);
        tabel.setItems(data);

        HBox tombol = new HBox(10);
        Button btnTambah = new Button("+ Tambah Obat");
        btnTambah.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

        btnTambah.setOnAction(e -> {
            Dialog<Obat> dialog = new Dialog<>();
            dialog.setTitle("Tambah Obat");
            ButtonType btnSimpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField txtNama = new TextField();
            TextField txtJenis = new TextField();
            TextField txtStok = new TextField();
            TextField txtHarga = new TextField();

            grid.add(new Label("Nama Obat:"), 0, 0); grid.add(txtNama, 1, 0);
            grid.add(new Label("Jenis:"), 0, 1); grid.add(txtJenis, 1, 1);
            grid.add(new Label("Stok:"), 0, 2); grid.add(txtStok, 1, 2);
            grid.add(new Label("Harga:"), 0, 3); grid.add(txtHarga, 1, 3);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn == btnSimpan && !txtNama.getText().isEmpty()) {
                    String id = String.format("O%03d", idObatCounter++);
                    return new Obat(id, txtNama.getText(), txtJenis.getText(),
                            Integer.parseInt(txtStok.getText()), Double.parseDouble(txtHarga.getText()));
                }
                return null;
            });

            dialog.showAndWait().ifPresent(o -> { daftarObat.add(o); data.add(o); });
        });

        tombol.getChildren().add(btnTambah);
        panel.getChildren().addAll(judul, tombol, tabel);
        return panel;
    }

    // ===================== PANEL JADWAL (ADMIN) =====================
    private VBox buatPanelJadwal() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Jadwal Dokter");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<JadwalDokter> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<JadwalDokter, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idJadwal"));
        TableColumn<JadwalDokter, String> colHari = new TableColumn<>("Hari");
        colHari.setCellValueFactory(new PropertyValueFactory<>("hari"));
        TableColumn<JadwalDokter, String> colMulai = new TableColumn<>("Jam Mulai");
        colMulai.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        TableColumn<JadwalDokter, String> colSelesai = new TableColumn<>("Jam Selesai");
        colSelesai.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
        TableColumn<JadwalDokter, Integer> colKuota = new TableColumn<>("Kuota");
        colKuota.setCellValueFactory(new PropertyValueFactory<>("kuota"));

        tabel.getColumns().addAll(colId, colHari, colMulai, colSelesai, colKuota);
        ObservableList<JadwalDokter> data = FXCollections.observableArrayList(daftarJadwal);
        tabel.setItems(data);

        HBox tombol = new HBox(10);
        Button btnTambah = new Button("+ Tambah Jadwal");
        btnTambah.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

        btnTambah.setOnAction(e -> {
            if (daftarDokter.isEmpty()) { showAlert("Error", "Belum ada dokter!"); return; }
            Dialog<JadwalDokter> dialog = new Dialog<>();
            dialog.setTitle("Tambah Jadwal");
            ButtonType btnSimpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.setPadding(new Insets(20));

            ComboBox<String> cmbDokter = new ComboBox<>();
            for (Dokter d : daftarDokter) cmbDokter.getItems().add(d.getNama());
            cmbDokter.setValue(cmbDokter.getItems().get(0));
            ComboBox<String> cmbHari = new ComboBox<>();
            cmbHari.getItems().addAll("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu");
            cmbHari.setValue("Senin");
            TextField txtMulai = new TextField("08:00");
            TextField txtSelesai = new TextField("12:00");
            TextField txtKuota = new TextField("20");

            grid.add(new Label("Dokter:"), 0, 0); grid.add(cmbDokter, 1, 0);
            grid.add(new Label("Hari:"), 0, 1); grid.add(cmbHari, 1, 1);
            grid.add(new Label("Jam Mulai:"), 0, 2); grid.add(txtMulai, 1, 2);
            grid.add(new Label("Jam Selesai:"), 0, 3); grid.add(txtSelesai, 1, 3);
            grid.add(new Label("Kuota:"), 0, 4); grid.add(txtKuota, 1, 4);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn == btnSimpan) {
                    Dokter d = daftarDokter.get(cmbDokter.getSelectionModel().getSelectedIndex());
                    String id = String.format("J%03d", idJadwalCounter++);
                    return new JadwalDokter(id, d, cmbHari.getValue(),
                            txtMulai.getText(), txtSelesai.getText(), Integer.parseInt(txtKuota.getText()));
                }
                return null;
            });

            dialog.showAndWait().ifPresent(j -> { daftarJadwal.add(j); data.add(j); });
        });

        tombol.getChildren().add(btnTambah);
        panel.getChildren().addAll(judul, tombol, tabel);
        return panel;
    }

    // ===================== PANEL PEMBAYARAN (ADMIN) =====================
    private VBox buatPanelPembayaran() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Pembayaran");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<Pembayaran> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<Pembayaran, String> colId = new TableColumn<>("ID Bayar");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPembayaran"));
        TableColumn<Pembayaran, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        TableColumn<Pembayaran, Double> colTotal = new TableColumn<>("Total (Rp)");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalBayar"));
        TableColumn<Pembayaran, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabel.getColumns().addAll(colId, colTgl, colTotal, colStatus);
        ObservableList<Pembayaran> data = FXCollections.observableArrayList(daftarPembayaran);
        tabel.setItems(data);

        HBox tombol = new HBox(10);
        Button btnProses = new Button("Proses Pembayaran");
        Button btnLunas = new Button("Tandai Lunas");
        btnProses.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        btnLunas.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

        btnProses.setOnAction(e -> {
            if (daftarRekamMedis.isEmpty()) { showAlert("Error", "Belum ada rekam medis!"); return; }
            Dialog<Pembayaran> dialog = new Dialog<>();
            dialog.setTitle("Proses Pembayaran");
            ButtonType btnSimpan = new ButtonType("Proses", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.setPadding(new Insets(20));

            ComboBox<String> cmbRM = new ComboBox<>();
            for (RekamMedis rm : daftarRekamMedis)
                cmbRM.getItems().add(rm.getIdRekamMedis() + " - " + rm.getPasien().getNama());
            cmbRM.setValue(cmbRM.getItems().get(0));
            TextField txtBiaya = new TextField("50000");

            grid.add(new Label("Rekam Medis:"), 0, 0); grid.add(cmbRM, 1, 0);
            grid.add(new Label("Biaya Konsultasi (Rp):"), 0, 1); grid.add(txtBiaya, 1, 1);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn == btnSimpan) {
                    RekamMedis rm = daftarRekamMedis.get(cmbRM.getSelectionModel().getSelectedIndex());
                    String id = String.format("PAY%03d", idBayarCounter++);
                    return new Pembayaran(id, rm, Double.parseDouble(txtBiaya.getText()),
                            java.time.LocalDate.now().toString());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(p -> {
                daftarPembayaran.add(p);
                data.add(p);
                showAlert("Sukses", "Total bayar: Rp " + p.getTotalBayar());
            });
        });

        btnLunas.setOnAction(e -> {
            Pembayaran selected = tabel.getSelectionModel().getSelectedItem();
            if (selected != null) { selected.bayar(); tabel.refresh(); }
        });

        tombol.getChildren().addAll(btnProses, btnLunas);
        panel.getChildren().addAll(judul, tombol, tabel);
        return panel;
    }

    // ===================== PANEL REKAM MEDIS (DOKTER/ADMIN) =====================
    private VBox buatPanelRekamMedis(boolean readOnly) {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Rekam Medis");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<RekamMedis> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<RekamMedis, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idRekamMedis"));
        TableColumn<RekamMedis, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        TableColumn<RekamMedis, String> colKeluhan = new TableColumn<>("Keluhan");
        colKeluhan.setCellValueFactory(new PropertyValueFactory<>("keluhan"));
        TableColumn<RekamMedis, String> colDiagnosis = new TableColumn<>("Diagnosis");
        colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));

        tabel.getColumns().addAll(colId, colTgl, colKeluhan, colDiagnosis);
        ObservableList<RekamMedis> data = FXCollections.observableArrayList(daftarRekamMedis);
        tabel.setItems(data);

        panel.getChildren().addAll(judul, tabel);

        if (!readOnly) {
            HBox tombol = new HBox(10);
            Button btnTambah = new Button("+ Input Rekam Medis");
            btnTambah.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

            btnTambah.setOnAction(e -> {
                if (daftarPasien.isEmpty()) { showAlert("Error", "Belum ada pasien!"); return; }

                Dialog<RekamMedis> dialog = new Dialog<>();
                dialog.setTitle("Input Rekam Medis");
                ButtonType btnSimpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10); grid.setVgap(10);
                grid.setPadding(new Insets(20));

                ComboBox<String> cmbPasien = new ComboBox<>();
                for (Pasien p : daftarPasien) cmbPasien.getItems().add(p.getNama());
                cmbPasien.setValue(cmbPasien.getItems().get(0));

                // Kalau dokter login, nama dokter otomatis
                Label lblDokterInfo = new Label(dokterLogin != null ? dokterLogin.getNama() : "-");
                ComboBox<String> cmbDokter = new ComboBox<>();
                if (dokterLogin == null) {
                    for (Dokter d : daftarDokter) cmbDokter.getItems().add(d.getNama());
                    cmbDokter.setValue(cmbDokter.getItems().get(0));
                }

                TextField txtTgl = new TextField(java.time.LocalDate.now().toString());
                TextField txtKeluhan = new TextField();
                TextField txtDiagnosis = new TextField();

                grid.add(new Label("Pasien:"), 0, 0); grid.add(cmbPasien, 1, 0);
                grid.add(new Label("Dokter:"), 0, 1);
                grid.add(dokterLogin != null ? lblDokterInfo : cmbDokter, 1, 1);
                grid.add(new Label("Tanggal:"), 0, 2); grid.add(txtTgl, 1, 2);
                grid.add(new Label("Keluhan:"), 0, 3); grid.add(txtKeluhan, 1, 3);
                grid.add(new Label("Diagnosis:"), 0, 4); grid.add(txtDiagnosis, 1, 4);
                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(btn -> {
                    if (btn == btnSimpan) {
                        Pasien p = daftarPasien.get(cmbPasien.getSelectionModel().getSelectedIndex());
                        Dokter d = dokterLogin != null ? dokterLogin :
                                daftarDokter.get(cmbDokter.getSelectionModel().getSelectedIndex());
                        String id = String.format("RM%03d", idRMCounter++);
                        return new RekamMedis(id, p, d, txtTgl.getText(), txtKeluhan.getText(), txtDiagnosis.getText());
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(rm -> {
                    daftarRekamMedis.add(rm);
                    data.add(rm);
                });
            });

            tombol.getChildren().add(btnTambah);
            panel.getChildren().add(1, tombol);
        }

        return panel;
    }

    // ===================== PANEL RESEP (DOKTER) =====================
    private VBox buatPanelResep(boolean readOnly) {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Resep Obat");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<Resep> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<Resep, String> colId = new TableColumn<>("ID Resep");
        colId.setCellValueFactory(new PropertyValueFactory<>("idResep"));
        TableColumn<Resep, String> colObat = new TableColumn<>("Nama Obat");
        colObat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getObat().getNamaObat()));
        TableColumn<Resep, Integer> colJml = new TableColumn<>("Jumlah");
        colJml.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        TableColumn<Resep, String> colAturan = new TableColumn<>("Aturan Pakai");
        colAturan.setCellValueFactory(new PropertyValueFactory<>("aturanPakai"));

        tabel.getColumns().addAll(colId, colObat, colJml, colAturan);
        ObservableList<Resep> data = FXCollections.observableArrayList(daftarResep);
        tabel.setItems(data);

        panel.getChildren().addAll(judul, tabel);

        if (!readOnly) {
            HBox tombol = new HBox(10);
            Button btnTambah = new Button("+ Beri Resep");
            btnTambah.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");

            btnTambah.setOnAction(e -> {
                if (daftarObat.isEmpty()) { showAlert("Error", "Belum ada data obat!"); return; }

                Dialog<Resep> dialog = new Dialog<>();
                dialog.setTitle("Beri Resep Obat");
                ButtonType btnSimpan = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10); grid.setVgap(10);
                grid.setPadding(new Insets(20));

                ComboBox<String> cmbObat = new ComboBox<>();
                for (Obat o : daftarObat) cmbObat.getItems().add(o.getNamaObat());
                cmbObat.setValue(cmbObat.getItems().get(0));
                TextField txtJumlah = new TextField("1");
                TextField txtAturan = new TextField();
                txtAturan.setPromptText("cth: 3x1 sehari sesudah makan");

                grid.add(new Label("Obat:"), 0, 0); grid.add(cmbObat, 1, 0);
                grid.add(new Label("Jumlah:"), 0, 1); grid.add(txtJumlah, 1, 1);
                grid.add(new Label("Aturan Pakai:"), 0, 2); grid.add(txtAturan, 1, 2);
                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(btn -> {
                    if (btn == btnSimpan) {
                        Obat o = daftarObat.get(cmbObat.getSelectionModel().getSelectedIndex());
                        String id = String.format("RES%03d", idResepCounter++);
                        return new Resep(id, o, Integer.parseInt(txtJumlah.getText()), txtAturan.getText());
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(r -> {
                    daftarResep.add(r);
                    data.add(r);
                });
            });

            tombol.getChildren().add(btnTambah);
            panel.getChildren().add(1, tombol);
        }

        return panel;
    }

    // ===================== PANEL PASIEN - NOMOR ANTREAN =====================
    private VBox buatPanelNomorAntrean() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(25));
        panel.setAlignment(Pos.TOP_CENTER);

        Label judul = new Label("Nomor Antrean Anda");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        VBox infoBox = new VBox(15);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(30));
        infoBox.setMaxWidth(400);
        infoBox.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10;");

        Label lblNama = new Label("Pasien: " + (pasienLogin != null ? pasienLogin.getNama() : "-"));
        lblNama.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblNama.setTextFill(Color.WHITE);

        Label lblAntrean = new Label("Sisa Antrean: " + antrean.getJumlahAntrean() + " orang");
        lblAntrean.setFont(Font.font("Arial", 14));
        lblAntrean.setTextFill(Color.web("#ecf0f1"));

        Label lblInfo = new Label("Silakan hubungi resepsionis\nuntuk mendaftar antrean");
        lblInfo.setFont(Font.font("Arial", 13));
        lblInfo.setTextFill(Color.web("#ecf0f1"));
        lblInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        infoBox.getChildren().addAll(lblNama, lblAntrean, lblInfo);
        panel.getChildren().addAll(judul, infoBox);
        return panel;
    }

    // ===================== PANEL PASIEN - REKAM MEDIS PRIBADI =====================
    private VBox buatPanelRekamMedisPribadi() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Rekam Medis Pribadi");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<RekamMedis> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<RekamMedis, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idRekamMedis"));
        TableColumn<RekamMedis, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        TableColumn<RekamMedis, String> colKeluhan = new TableColumn<>("Keluhan");
        colKeluhan.setCellValueFactory(new PropertyValueFactory<>("keluhan"));
        TableColumn<RekamMedis, String> colDiagnosis = new TableColumn<>("Diagnosis");
        colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));

        tabel.getColumns().addAll(colId, colTgl, colKeluhan, colDiagnosis);

        // Filter hanya rekam medis milik pasien yang login
        ObservableList<RekamMedis> data = FXCollections.observableArrayList();
        if (pasienLogin != null) {
            for (RekamMedis rm : daftarRekamMedis) {
                if (rm.getPasien().getIdPasien().equals(pasienLogin.getIdPasien())) {
                    data.add(rm);
                }
            }
        }
        tabel.setItems(data);

        Label lblInfo = new Label(data.isEmpty() ? "Belum ada rekam medis." : "Total: " + data.size() + " rekam medis");
        lblInfo.setFont(Font.font("Arial", 13));

        panel.getChildren().addAll(judul, lblInfo, tabel);
        return panel;
    }

    // ===================== PANEL PASIEN - RESEP SAYA =====================
    private VBox buatPanelResepPasien() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));

        Label judul = new Label("Resep Obat Saya");
        judul.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TableView<Resep> tabel = new TableView<>();
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        TableColumn<Resep, String> colId = new TableColumn<>("ID Resep");
        colId.setCellValueFactory(new PropertyValueFactory<>("idResep"));
        TableColumn<Resep, String> colObat = new TableColumn<>("Nama Obat");
        colObat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getObat().getNamaObat()));
        TableColumn<Resep, Integer> colJml = new TableColumn<>("Jumlah");
        colJml.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        TableColumn<Resep, String> colAturan = new TableColumn<>("Aturan Pakai");
        colAturan.setCellValueFactory(new PropertyValueFactory<>("aturanPakai"));

        tabel.getColumns().addAll(colId, colObat, colJml, colAturan);

        // Tampilkan semua resep (bisa dikembangkan filter per pasien via RekamMedis)
        ObservableList<Resep> data = FXCollections.observableArrayList(daftarResep);
        tabel.setItems(data);

        Label lblInfo = new Label(data.isEmpty() ? "Belum ada resep." : "Total: " + data.size() + " resep");
        lblInfo.setFont(Font.font("Arial", 13));

        panel.getChildren().addAll(judul, lblInfo, tabel);
        return panel;
    }

    // ===================== HELPER =====================
    private void showAlert(String judul, String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}