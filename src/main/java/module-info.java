module com.tk2pbo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tk2pbo to javafx.fxml;
    exports com.tk2pbo;
}
