module JBS {
    requires java.sql;           // For java.sql package (database access)
    requires java.desktop;       // For java.awt package (desktop UI elements)
    requires atlantafx.base;     // For atlantafx.base.theme package (external dependency)

    requires javafx.controls;    // Ensure JavaFX modules are declared
    requires javafx.fxml;
    requires java.mail;

    opens org.example.jbs to javafx.fxml;  // Allow access to your package for JavaFX
    exports org.example.jbs;               // Export your package
}
