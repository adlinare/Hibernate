package hibernate.prueba1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "PERSONAS")
public class Persona implements Serializable{
	
	@Id
	@SequenceGenerator(name = "persona_seq", sequenceName = "SEC_PERSONAS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
	private long id;

	@Column(name = "DNI_USUARIO", length = 9, nullable = false, unique = true)
	private String dni;
	
	@Column(name = "NOMBRE_COMPLETO", length = 100)
	private String nombre;
	
	@Column(name = "EDAD_ACTUAL")
	private Integer edad;
	
	//Relacion navegable en un unico sentido, no tenemos titular en Pasaporte
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_PASAPOERE", unique = true, nullable = true)
	private Pasaporte pasaporte;
	
	//Relacion navegable en un unico sentido
	@ManyToOne()
	@JoinColumn(name = "ID_AYUNTAMIENTO", nullable = true)
	private Ayuntamiento ayuntamiento;
	
	//Relacion navegable en un unico sentido
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "PERSONAS_HIJOS",
			joinColumns = @JoinColumn(name = "PERSONA_ID"),
			inverseJoinColumns = @JoinColumn(name = "HIJO_ID")
			)
	private List<Hijo> hijos = new ArrayList<>(); //Super importante incializarlo 
	
	
	
	
	//@Transient indica que un atributo no se debe guardar en la base de datos
	//@Temporal Para dates o calendar y oblida a especificar si se guarda fecha y hora o ambos
	
	
	public Persona( String dni, String nombre, Integer edad) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.edad = edad;
	}

	public Persona() {
		super();
	}

	public long getId() {
		return id;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getDni() {
		return dni;
	}

	public Pasaporte getPasaporte() {
		return pasaporte;
	}

	public void setPasaporte(Pasaporte pasaporte) {
		this.pasaporte = pasaporte;
	}

	public Ayuntamiento getAyuntamiento() {
		return ayuntamiento;
	}

	public void setAyuntamiento(Ayuntamiento ayuntamiento) {
		this.ayuntamiento = ayuntamiento;
	}

	public List<Hijo> getHijos() {
		return hijos;
	}

	public void setHijos(List<Hijo> hijos) {
		this.hijos = hijos;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
	
	
}
