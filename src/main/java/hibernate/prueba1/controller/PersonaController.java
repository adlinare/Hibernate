package hibernate.prueba1.controller;

import java.net.URL;

import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.query.Query;

import hibernate.prueba1.entity.Ayuntamiento;
import hibernate.prueba1.entity.HibernateUtil;
import hibernate.prueba1.entity.Persona;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class PersonaController implements Initializable {

	@FXML
    private TableColumn<Persona, String> tcDni;

    @FXML
    private TableColumn<Persona, String> tcNombre;

    @FXML
    private Button bAdd;

    @FXML
    private Button bDelete;

    @FXML
    private TextField tfEdad;

    @FXML
    private TextField tfNombre;

    @FXML
    private TableColumn<Persona, Integer> tcEdad;

    @FXML
    private TableView<Persona> tPersona;

    @FXML
    private TableColumn<Persona, Long> tcId;
    

    @FXML
    private TableColumn<Persona, Ayuntamiento> tcAyun;

    @FXML
    private Button bUpdate;

    @FXML
    private TextField tfDni;
    
    PersonaCRUD pcrud = new PersonaCRUD();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		tcId.setCellValueFactory(new
				 PropertyValueFactory<Persona,Long>("id"));
		
		tcEdad.setCellValueFactory(new
				 PropertyValueFactory<Persona,Integer>("edad"));
		
		tcNombre.setCellValueFactory(new
				 PropertyValueFactory<Persona,String>("nombre"));
		
		tcDni.setCellValueFactory(new
				 PropertyValueFactory<Persona,String>("dni"));
		
		tcAyun.setCellValueFactory(new PropertyValueFactory<Persona,Ayuntamiento>("ayuntamiento"));
		
		ObservableList<Persona> personas = FXCollections.observableArrayList();
		
		personas = pcrud.getAll();

		tPersona.setItems(personas);
		

	}
	
    @FXML
    void oaAdd(ActionEvent event) {
    	pcrud.addRandom();
    	initialize(null, null);
    }

    @FXML
    void oaDelete(ActionEvent event) {

    }

    @FXML
    void oaUpdate(ActionEvent event) {

    }


}
