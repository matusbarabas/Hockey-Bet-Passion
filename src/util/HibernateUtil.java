package util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Trieda zabezpecujuca pripojenie databazy s pomocou konfiguracneho suboru.
 * @author Matúš Barabás
 *
 */
public class HibernateUtil {

//	private static final Logger LOG = Logger.getLogger(HibernateUtil.class.getName());
	
    private static final SessionFactory sessionFactory = buildSessionFactory();

    @SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception
 //           LOG.log(Level.SEVERE, "Initial SessionFactory creation failed.", ex);
           throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}