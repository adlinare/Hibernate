module hibernate.prueba1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.sql;
    requires java.naming;
	requires javafx.base;
    

    // 1. Exporta el paquete principal
    // (Necesario para iniciar la aplicación, contiene App.java)
    exports hibernate.prueba1;

    // 2. Abre el paquete principal para JavaFX (código FXML)
    // (Si Main.fxml está aquí)
    opens hibernate.prueba1 to javafx.fxml;
    
    // 3. Abre el paquete de Entidades para Hibernate
    // (Hibernate necesita acceder a las clases @Entity)
    opens hibernate.prueba1.entity to javafx.base, javafx.fxml, org.hibernate.orm.core;
    // 4. Abre el paquete de Controladores para JavaFX
    // (JavaFX necesita acceder a los métodos @FXML)
    opens hibernate.prueba1.controller to javafx.fxml;
}
