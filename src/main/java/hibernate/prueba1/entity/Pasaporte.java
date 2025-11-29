package hibernate.prueba1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PASAPORTE") //schema no es necesario especificarlo ya que estamos en el mismo usuario de la conexion, si quisieramos tomar tablas de otro usuario si habria que ponerlo
public class Pasaporte {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Debe funcionar si SELEC VERSION FROM V$INSTANCE devuelve 21 o superior
	private long id;
	
	@Column(name = "IDENTIFICADOR")
	private String identificador;
	//No creo una entidad Persona ya que no hago la relacion navegable en ambos sentidos
	
	public Pasaporte() {
		super();
	}

	public Pasaporte(String identificador) {
		super();
		this.identificador = identificador;
	}

	public long getId() {
		return id;
	}
	
	
}
