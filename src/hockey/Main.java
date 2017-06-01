package hockey;

import java.io.IOException;
import java.util.ResourceBundle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.HibernateUtil;

/**
 * Hlavna trieda, ktora vola metodu na spustenie hlavneho okna.
 * Otvara session.
 * Po zatvoreni aplikacie uzatvara session.
 * @author Matúš Barabás
 *
 */
public class Main extends Application {
	public static Session session;
	public static Stage primaryStage;
	public static BorderPane mainLayout;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Main.primaryStage = primaryStage;
		Main.primaryStage.setTitle("HOCKEY-BET-PASSION");
		
		show_main_window(1);
	}

	public static void show_main_window(int decision) throws IOException {
		ResourceBundle r = null;
		
		switch(decision){
			case 1: {
				r = ResourceBundle.getBundle("English");
				break;
				}
			case 2:{	
				r = ResourceBundle.getBundle("Slovencina");
				break;
				}
			default: r = ResourceBundle.getBundle("English");
		}
	
		mainLayout = FXMLLoader.load(Main.class.getResource("view/Main_window.fxml"), r);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {		
		SessionFactory sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();		
		launch(args);
		session.close();
	}
}
