module com.comicland.comicland {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.comicland.comicland to javafx.fxml;
    exports com.comicland.comicland;
}