module com.knox.cs.snakegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.knox.cs.snakegame to javafx.fxml;
    exports com.knox.cs.snakegame;
}
