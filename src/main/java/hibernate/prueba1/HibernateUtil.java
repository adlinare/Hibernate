package hibernate.prueba1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	private static Session session = null;

	private static void buildSessionFactory() {
		try {
			// Creación del objeto session factory object.
			// Creación de la configuración
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			// Creación de de la session factory
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		buildSessionFactory();
		return sessionFactory;
	}

	public static void openSession() {
		// Abrimos la sesión a partir de la Factoria de secciones.
		if ((sessionFactory == null))
			buildSessionFactory();
		session = sessionFactory.openSession();
	}

	public static Session getCurrentSession() {
		// Devolvemos el objeto de sesión, abriendolo si esnecesario
		if ((session == null) || (!session.isOpen()))
			openSession();
		return session;
	}

	public static void closeSessionFactory() {
		// Cierre de los objetos de sesión y factoría de sesiones.
		if (session != null)
			session.close();
		if (sessionFactory != null)
			sessionFactory.close();

	}
}
