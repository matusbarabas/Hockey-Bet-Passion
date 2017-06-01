package hockey.controller;

import java.sql.Date;

import org.hibernate.Query;
import org.hibernate.Transaction;

import hockey.Main;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Trieda obsahujuca funkcionalitu o preradeni hraca z timu do timu.
 * @author Matúš Barabás
 *
 */
public class Trade_controller {
	
	private Query q;
	private Query q1;
	
	@FXML
	private TextField t_name;
	@FXML
	private TextField t_new_team;
	@FXML
	private DatePicker d_picker;
	
	/*Najprv aktualizaciu udaju vo forme vlozenie datumu k danemu hracovi a potom vytvorenie noveho zaznamu uz s novym timom.*/
	@FXML
	private void make_trade(){
		Transaction t = null;
		try {
			t = Main.session.beginTransaction();
			q = Main.session.createSQLQuery("UPDATE player_in_team pl SET date_to = :date_to "
											+ "WHERE pl.player_id = (SELECT p.id FROM players_14_15 p WHERE name = :name) "
											+ "AND pl.date_to IS null");
			q.setParameter("date_to", Date.valueOf(d_picker.getValue()));
			q.setParameter("name", t_name.getText());
			q.executeUpdate();
			
			q1 = Main.session.createSQLQuery("INSERT INTO player_in_team (date_from, date_to, team_id, player_id) VALUES "
											+ "(:date, null, (SELECT id FROM teams WHERE shortcut = :new_team), "
											+ "(SELECT p.id FROM players_14_15 p WHERE p.name = :name))");
			q1.setParameter("date", Date.valueOf(d_picker.getValue()));
			q1.setParameter("name", t_name.getText());
			q1.setParameter("new_team", t_new_team.getText());
			q1.executeUpdate();
			
			t.commit();
		} catch (Exception e) {
			if(t != null){
				t.rollback();
			}
		}
		
	}
	
	@FXML
	private void close_trade_window(){
		Players_controller.close_trade();
	}
}
