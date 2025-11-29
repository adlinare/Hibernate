package hibernate.prueba1.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import hibernate.prueba1.entity.Ayuntamiento;
import hibernate.prueba1.entity.HibernateUtil;
import hibernate.prueba1.entity.Hijo;
import hibernate.prueba1.entity.Pasaporte;
import hibernate.prueba1.entity.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonaCRUD {

	public ObservableList<Persona> getAll() {

		// 1. La sesión se abre y se garantiza que se cierra automáticamente
		try (Session session = HibernateUtil.getCurrentSession()) {

			// 2. La transacción sigue siendo necesaria para DML, pero es opcional para solo
			// SELECT
			// Aunque es buena práctica usarla siempre.
			session.beginTransaction();

			Query<Persona> query = session.createQuery("FROM Persona", Persona.class);
			List<Persona> results = query.list();

			session.getTransaction().commit();

			return FXCollections.observableArrayList(results);

		} catch (Exception e) {
			// Manejo de Rollback si es necesario
			// ...
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}
	}
	
	public void addRandom() {

		// 1. La sesión se abre y se garantiza que se cierra automáticamente
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
			// Manejo de Rollback si es necesario
			// ...
			e.printStackTrace();
		}
	}
	

}
