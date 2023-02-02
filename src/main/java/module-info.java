module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1.gui;
    opens com.example.demo1.gui to javafx.fxml;
    exports com.example.demo1;
    exports com.example.demo1.gui.row;
    opens com.example.demo1.gui.row to javafx.fxml;
    exports com.example.demo1.gui.tableview;
    opens com.example.demo1.gui.tableview to javafx.fxml;
}