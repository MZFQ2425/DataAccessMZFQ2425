module org.mzfq2425.finalactivity.finalactivity {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;

    opens org.mzfq2425.finalactivity.finalactivity to javafx.fxml;
    opens org.mzfq2425.finalactivity.finalactivity.model to org.hibernate.orm.core;
    exports org.mzfq2425.finalactivity.finalactivity;
}