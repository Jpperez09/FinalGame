module com.knox.cs.snakegame {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.knox.cs.snakegame to javafx.fxml;
    exports com.knox.cs.snakegame;
}
