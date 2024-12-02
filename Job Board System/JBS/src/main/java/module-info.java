module org.example.jbs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires atlantafx.base;


    opens org.example.jbs to javafx.fxml;
    exports org.example.jbs;
}