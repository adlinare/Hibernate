package hibernate.prueba1.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import hibernate.prueba1.HibernateUtil;
import hibernate.prueba1.entity.Ayuntamiento;
import hibernate.prueba1.entity.Hijo;
import hibernate.prueba1.entity.Pasaporte;
import hibernate.prueba1.entity.Persona;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class PersonaController implements Initializable {

    // --- FXML FIELDS (TAB PANEL) ---
    // Nota: Es mejor usar el tipo de entidad correcto en el TableView
    @FXML private TableView<Ayuntamiento> tAyuntamientoTabPanel;
    @FXML private TableView<Persona> tPersonaTabPanel; // Asumiendo que has asignado este nombre
    
    @FXML private TableColumn<Persona, Long> tcIdPersonaTab;
    @FXML private TableColumn<Persona, String> tcDniPersonaTab;
    @FXML private TableColumn<Persona, String> tcNombrePersonaTab;
    @FXML private TableColumn<Persona, Integer> tcEdadPersonaTab;
    
    @FXML private TableColumn<Ayuntamiento, Long> tcIdAyuntamientoTab;
    @FXML private TableColumn<Ayuntamiento, String> tcNombreAyuntamientoTab;
    // --------------------------------

    // --- FXML FIELDS (MAIN VIEW) ---
    @FXML private TableColumn<Persona, String> tcDni;
    @FXML private TableColumn<Persona, String> tcNombre;
    @FXML private Button bAdd;
    @FXML private Button bDelete;
    @FXML private TextField tfEdad;
    @FXML private TextField tfNombre;
    @FXML private TableColumn<Persona, Integer> tcEdad;
    @FXML private TableView<Persona> tPersona;
    @FXML private TableColumn<Persona, Long> tcId;
    @FXML private TableColumn<Persona, Ayuntamiento> tcAyun;
    @FXML private Button bUpdate;
    @FXML private Button BtnAdd;
    @FXML private TextField tfDni;
    @FXML private ChoiceBox<Ayuntamiento> CBAyunta;
    // -------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // --- 1. CONFIGURACIÓN DE COLUMNAS (TABLA PRINCIPAL) ---
        tcId.setCellValueFactory(new PropertyValueFactory<Persona,Long>("id"));
        tcEdad.setCellValueFactory(new PropertyValueFactory<Persona,Integer>("edad"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<Persona,String>("nombre"));
        tcDni.setCellValueFactory(new PropertyValueFactory<Persona,String>("dni"));
        tcAyun.setCellValueFactory(new PropertyValueFactory<Persona,Ayuntamiento>("ayuntamiento"));
        
        // 2. Cargar Personas (Tabla principal)
        ObservableList<Persona> personas = getAll();
        tPersona.setItems(personas);
        
        // 3. Cargar Ayuntamientos para el ChoiceBox
        ObservableList<Ayuntamiento> ayuntamientos = getAllAyuntamiento();
        // AÑADIR EL VALOR NULL PARA PERMITIR DESASIGNAR UN AYUNTAMIENTO
        ayuntamientos.add(null);
        CBAyunta.setItems(ayuntamientos);

        // --- 4. CONFIGURACIÓN DE COLUMNAS (TAB PANEL: PERSONA) ---
        // Asumiendo que has renombrado el FXML TableView a tPersonaTabPanel
        if (tcIdPersonaTab != null) { // Comprobación de que el elemento existe
            tcIdPersonaTab.setCellValueFactory(new PropertyValueFactory<Persona, Long>("id"));
            tcDniPersonaTab.setCellValueFactory(new PropertyValueFactory<Persona, String>("dni"));
            tcNombrePersonaTab.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
            tcEdadPersonaTab.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));
        }
        
        // --- 5. CONFIGURACIÓN DE COLUMNAS (TAB PANEL: AYUNTAMIENTO) ---
        if (tcIdAyuntamientoTab != null) {
            tcIdAyuntamientoTab.setCellValueFactory(new PropertyValueFactory<Ayuntamiento, Long>("id"));
            tcNombreAyuntamientoTab.setCellValueFactory(new PropertyValueFactory<Ayuntamiento, String>("nombre"));
        }
        
        // 6. Cargar datos en las tablas del Tab Panel
        if (tPersonaTabPanel != null) {
            tPersonaTabPanel.setItems(getAll()); // Reutiliza el método getAll para cargar Personas
        }
        if (tAyuntamientoTabPanel != null) {
            tAyuntamientoTabPanel.setItems(getAllAyuntamiento()); // Reutiliza el método getAllAyuntamiento
        }
        
        for (Ayuntamiento ayuntamiento : getAyuntamientosConMultiplesPersonas()) {
        	System.out.println(ayuntamiento);
        	for (Persona persona : getPersonasPorAyuntamiento(ayuntamiento)) {
				System.out.println(persona.getNombre());
			}
			
		}
        
    }
    
    // -------------------------------------------------------------------
    // --- LÓGICA DE INTERFAZ (FX ACTIONS) ---
    // -------------------------------------------------------------------

    // ... (El resto de tus métodos FXML como oaAddF, oaUpdate, etc. quedan sin cambios) ...
    // Para brevedad, el resto del código es el mismo que proporcionaste.

    @FXML
    void oACliked() {
        Persona p = tPersona.getSelectionModel().getSelectedItem();
        if (p != null) {
            Ayuntamiento ayun = p.getAyuntamiento();
            
            tfDni.setText(p.getDni());
            tfNombre.setText(p.getNombre());
            tfEdad.setText(String.valueOf(p.getEdad()));
            
            // Si ayun es null, el ChoiceBox selecciona el item null que añadimos.
            CBAyunta.getSelectionModel().select(ayun);
        }
    }

    @FXML
    void oaAdd(ActionEvent event) {
        addRandom();
        initialize(null, null);
    }
    
    @FXML
    void oaAddF(ActionEvent event) {
        // Validación y obtención de datos
        String dni = tfDni.getText();
        String nombre = tfNombre.getText();
        Integer edad;
        try {
            edad = Integer.parseInt(tfEdad.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Entrada", "La edad debe ser un número entero.");
            return; 
        }
        Ayuntamiento ayuntamientoSeleccionado = CBAyunta.getSelectionModel().getSelectedItem();
        
        // NOTA: Se eliminó la validación 'ayuntamientoSeleccionado == null' aquí
        if (dni.isEmpty() || nombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor, rellena DNI, Nombre, Edad.");
            return;
        }
        Long idAyuntamiento = null;
        if (ayuntamientoSeleccionado != null) {
        	// 1. Obtener el ID del objeto Detached antes de abrir la sesión
            idAyuntamiento = ayuntamientoSeleccionado.getId();
        }
        
        
        // 2. Guardar el objeto usando Hibernate
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Persona nuevaPersona = new Persona();
            nuevaPersona.setDni(dni);
            nuevaPersona.setNombre(nombre);
            nuevaPersona.setEdad(edad);
            
            Ayuntamiento ayunManaged = null;
            if (ayuntamientoSeleccionado != null) {
            	// SOLUCIÓN CON FIND: Cargar la entidad Ayuntamieno en la sesión actual (Managed)
            	ayunManaged = session.find(Ayuntamiento.class, idAyuntamiento);
            }
            
            // Asignar el objeto Managed (o null) a la nueva persona
            nuevaPersona.setAyuntamiento(ayunManaged);
            
            session.persist(nuevaPersona);
            tx.commit();
            
            // 3. Limpiar y refrescar
            limpiarCampos();
            initialize(null, null); 
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Persona añadida con éxito: " + nombre);
            
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Persistencia", "No se pudo guardar la persona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    void oaDelete(ActionEvent event) {
        Persona p = tPersona.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Por favor, selecciona una persona para eliminar.");
            return;
        }

        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            // Adjuntar (Find) para asegurar que la entidad está Managed
            Persona personaManaged = session.find(Persona.class, p.getId());
            
            // 2. Eliminar
            if (personaManaged != null) {
                session.remove(personaManaged);
            }
            tx.commit();
            
            // 3. Limpiar y refrescar
            limpiarCampos();
            initialize(null, null);
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Persona eliminada con éxito.");
            
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Eliminación", "No se pudo eliminar la persona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    void oaUpdate(ActionEvent event) {
        Persona p = tPersona.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Por favor, selecciona una persona para actualizar.");
            return;
        }
        
        // Validación similar a oaAddF
        String dni = tfDni.getText();
        String nombre = tfNombre.getText();
        Integer edad;
        try {
            edad = Integer.parseInt(tfEdad.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Entrada", "La edad debe ser un número entero.");
            return; 
        }
        Ayuntamiento ayuntamientoSeleccionado = CBAyunta.getSelectionModel().getSelectedItem();

        // CORRECCIÓN: SE PERMITE QUE EL AYUNTAMIENTO SEA NULL para actualizar.
        if (dni.isEmpty() || nombre.isEmpty()) {
             mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor, rellena DNI, Nombre y Edad.");
             return;
        }
        
        Long idAyuntamiento = null;
        if (ayuntamientoSeleccionado != null) {
            idAyuntamiento = ayuntamientoSeleccionado.getId();
        }

        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            // 1. Obtener la versión Managed de la Persona seleccionada (por si se modificó)
            Persona personaToUpdate = session.find(Persona.class, p.getId());
            
            // 2. Obtener la versión Managed del Ayuntamiento (o null si no hay selección)
            Ayuntamiento ayunManaged = null;
            if (ayuntamientoSeleccionado != null) {
                ayunManaged = session.find(Ayuntamiento.class, idAyuntamiento);
            }
            
            // 3. Actualizar los datos del objeto Managed
            personaToUpdate.setDni(dni);
            personaToUpdate.setNombre(nombre);
            personaToUpdate.setEdad(edad);
            // Asigna el objeto Managed o null
            personaToUpdate.setAyuntamiento(ayunManaged);
            
            // El commit guardará automáticamente los cambios de personaToUpdate.
            tx.commit();
            
            // 4. Limpiar y refrescar
            limpiarCampos();
            initialize(null, null);
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Persona actualizada con éxito.");
            
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Actualización", "No se pudo actualizar la persona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // -------------------------------------------------------------------
    // --- MÉTODOS DE HIBERNATE (Trasladados desde el CRUD) ---
    // -------------------------------------------------------------------
    
    public ObservableList<Ayuntamiento> getAllAyuntamiento(){
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            
            Query<Ayuntamiento> query = session.createQuery("FROM Ayuntamiento", Ayuntamiento.class);
            List<Ayuntamiento> results = query.list();
            
            session.getTransaction().commit(); 

            return FXCollections.observableArrayList(results);

        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public ObservableList<Persona> getAll(){
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            
            Query<Persona> query = session.createQuery("FROM Persona", Persona.class);
            List<Persona> results = query.list();
            
            session.getTransaction().commit();

            return FXCollections.observableArrayList(results);

        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public void addRandom() {
        try (Session session = HibernateUtil.getCurrentSession()) {

            Persona p = new Persona("23423423E", "Pepe", 33);
            Pasaporte pass = new Pasaporte("124131313");
            Ayuntamiento ayun = new Ayuntamiento("Osuna");
            Hijo h1 = new Hijo("Juan");
            Hijo h2 = new Hijo("Pepillo");

            session.beginTransaction();
            session.persist(ayun); 
            p.setAyuntamiento(ayun);
            
            session.persist(pass);
            p.setPasaporte(pass);
            
            session.persist(h1);
            session.persist(h2);
            p.getHijos().add(h1);
            p.getHijos().add(h2);
            
            session.persist(p);
            
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------
    // --- MÉTODOS DE UTILIDAD ---
    // -------------------------------------------------------------------

    private void mostrarAlerta(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void limpiarCampos() {
        tfDni.clear();
        tfNombre.clear();
        tfEdad.clear();
        CBAyunta.getSelectionModel().clearSelection();
    }
    
    public ObservableList<Ayuntamiento> getAyuntamientosConMultiplesPersonas() {
        // Usamos openSession() para la consulta
        try (Session session = HibernateUtil.getCurrentSession()) {
            
            session.beginTransaction();
            
            // Consulta HQL:
            // 1. SELECT a: Selecciona el objeto Ayuntamiento.
            // 2. FROM Persona p JOIN p.ayuntamiento a: Une Persona con Ayuntamiento.
            // 3. GROUP BY a: Agrupa por cada Ayuntamiento.
            // 4. HAVING COUNT(p) > 1: Filtra solo aquellos grupos que tienen más de una Persona.
            String hql = "SELECT a FROM Persona p JOIN p.ayuntamiento a GROUP BY a HAVING COUNT(p) > 1";
            
            Query<Ayuntamiento> query = session.createQuery(hql, Ayuntamiento.class);
            List<Ayuntamiento> results = query.list();
            
            session.getTransaction().commit(); 

            return FXCollections.observableArrayList(results);

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Consulta", "No se pudieron obtener los Ayuntamientos con múltiples personas.");
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public ObservableList<Persona> getPersonasPorAyuntamiento(Ayuntamiento ayuntamiento) {
        if (ayuntamiento == null || ayuntamiento.getId() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Búsqueda Invalida", "El Ayuntamiento proporcionado es nulo o no tiene ID.");
            return FXCollections.observableArrayList();
        }
        
        // Usamos openSession() para la consulta
        try (Session session = HibernateUtil.getCurrentSession() ) {
            
            session.beginTransaction();
            
            // Consulta HQL:
            // 1. WHERE p.ayuntamiento: Busca la relación mapeada en la entidad Persona.
            // 2. = :ayunParam: Utiliza un parámetro para evitar inyección SQL y mejorar la eficiencia.
            String hql = "FROM Persona p WHERE p.ayuntamiento = :ayunParam";
            
            Query<Persona> query = session.createQuery(hql, Persona.class);
            
            // Asignamos el objeto Ayuntamiento directamente al parámetro
            query.setParameter("ayunParam", ayuntamiento);
            
            List<Persona> results = query.list();
            
            session.getTransaction().commit(); 

            return FXCollections.observableArrayList(results);

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Consulta", 
                          "Error al buscar personas en el Ayuntamiento: " + e.getMessage());
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public ObservableList<Persona> getPersonasMayoresDe30() {
        
        // Usamos openSession() para la consulta
        try (Session session = HibernateUtil.getCurrentSession()) {
            
            session.beginTransaction();
            
            // Consulta HQL: Filtra todas las entidades Persona donde el campo 'edad' > 30.
            String hql = "FROM Persona p WHERE p.edad > 30";
            
            Query<Persona> query = session.createQuery(hql, Persona.class);
            
            List<Persona> results = query.list();
            
            session.getTransaction().commit(); 

            return FXCollections.observableArrayList(results);

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Consulta", 
                          "Error al buscar personas mayores de 30: " + e.getMessage());
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
}