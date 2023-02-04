module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.neo4j.driver;

    opens it.unipi.dii.aide.lsmsd.recipeshare to javafx.fxml;
    exports it.unipi.dii.aide.lsmsd.recipeshare.gui;
    opens it.unipi.dii.aide.lsmsd.recipeshare.gui to javafx.fxml;
    exports it.unipi.dii.aide.lsmsd.recipeshare;
    exports it.unipi.dii.aide.lsmsd.recipeshare.gui.row;
    opens it.unipi.dii.aide.lsmsd.recipeshare.gui.row to javafx.fxml;
    exports it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview;
    opens it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview to javafx.fxml;
}