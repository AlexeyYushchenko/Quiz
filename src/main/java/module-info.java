module com.yushchenkoaleksey.edu.quiz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires org.jsoup;
    requires java.prefs;

    opens com.yushchenkoaleksey.edu.quiz to javafx.fxml;
    exports com.yushchenkoaleksey.edu.quiz;
    exports com.yushchenkoaleksey.edu.quiz.model;
    exports com.yushchenkoaleksey.edu.quiz.repository;
}

