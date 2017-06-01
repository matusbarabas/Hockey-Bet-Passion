package hockey.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import hockey.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Trieda obsahujuca funkcionalitu okna o zapasoch.
 * Vykonava vkladanie dat do databazy.
 * @author Matúš Barabás
 *
 */
public class Matches_controller {
	private static Stage tradePlayerStage;
	private Query q;
	private Query q1;
	
	ObservableList<String> list = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> g_home_team;
	@FXML
	private ComboBox<String> g_guest_team;
	@FXML
	private TextField g_hg;
	@FXML
	private TextField g_vg;
	@FXML
	private CheckBox g_ot;
	@FXML
	private CheckBox g_so;
	@FXML
	private DatePicker g_date;	
	@FXML
	private TextField h_line;
	@FXML
	private TextField d_line;
	@FXML
	private TextField g_line;
	@FXML
	private DatePicker b_date;	
	
	@FXML
	private void initialize(){
		list.clear();
		
		q = Main.session.createSQLQuery("SELECT name FROM teams");
		List<String> result = q.list();
		
		for (String x : result) {
			list.add(x);
		}
		
		g_home_team.setItems(list);
		g_guest_team.setItems(list);
	}
	
	/*Pridanie zapasu do tabulky games_14_15 na zaklade hodnot zadanych pouzivatelom.*/
	@FXML
	private void add_to_games(){
		Transaction t = Main.session.beginTransaction();
		
		q = Main.session.createSQLQuery("INSERT INTO games_14_15 (date_of_match, visitor, goals_visitor, home, goals_home, note) "
				+ "VALUES (:date, (SELECT id FROM teams WHERE name = :gteam), :vg, (SELECT id FROM teams WHERE name = :hteam), :hg, :note)");
		
		q.setParameter("hteam", g_home_team.getValue());
		q.setParameter("gteam", g_guest_team.getValue());
		q.setParameter("date", Date.valueOf(g_date.getValue()));
		q.setParameter("vg", Integer.parseInt(g_vg.getText()));
		q.setParameter("hg", Integer.parseInt(g_hg.getText()));
		
		if(g_ot.isSelected()){
			q.setParameter("note", "OT");
		}else if(g_so.isSelected()){
			q.setParameter("note", "SO");
		}else{
			q.setParameter("note", null);
		}
		q.executeUpdate();
		
		t.commit();
	}
	
	/*Vlozenie dat do dvoch tabuliek sucasne.
	 * Vkladanie do tabulky zapasov a na zaklade id z tejto tabulky potom pridanie do stavkovych prilezitosti.
	 * */
	@FXML 
	private void add_to_g_and_b(){
		Transaction t = null;
		
		try {
			 t = Main.session.beginTransaction();
		
			q = Main.session.createSQLQuery("INSERT INTO games_14_15 (date_of_match, visitor, goals_visitor, home, goals_home, note) "
					+ "VALUES (:date, (SELECT id FROM teams WHERE name = :gteam), :vg, (SELECT id FROM teams WHERE name = :hteam), :hg, :note)");
			
			q.setParameter("hteam", g_home_team.getValue());
			q.setParameter("gteam", g_guest_team.getValue());
			q.setParameter("date", Date.valueOf(g_date.getValue()));
			q.setParameter("vg", Integer.parseInt(g_vg.getText()));
			q.setParameter("hg", Integer.parseInt(g_hg.getText()));
			
			if(g_ot.isSelected()){
				q.setParameter("note", "OT");
			}else if(g_so.isSelected()){
				q.setParameter("note", "SO");
			}else{
				q.setParameter("note", null);
			}
			q.executeUpdate();
			
			q1 = Main.session.createSQLQuery("INSERT INTO betting_events (games_id, home_line, draw_line, visitor_line, date_of_bet) VALUES "
											+ "((SELECT id FROM games_14_15 WHERE date_of_match = :date AND "
											+ "visitor = (SELECT id FROM teams WHERE name = :gteam) AND "
											+ "goals_visitor = :vg AND "
											+ "home = (SELECT id FROM teams WHERE name = :hteam) AND "
											+ "goals_home = :hg), "
											+ ":hl, :dl, :vl, :bd)"
			);
			
			q1.setParameter("hl", Double.parseDouble(h_line.getText()));
			q1.setParameter("dl", Double.parseDouble(d_line.getText()));
			q1.setParameter("vl", Double.parseDouble(g_line.getText()));
			q1.setParameter("bd", Date.valueOf(b_date.getValue()));
			q1.setParameter("hteam", g_home_team.getValue());
			q1.setParameter("gteam", g_guest_team.getValue());
			q1.setParameter("date", Date.valueOf(g_date.getValue()));
			q1.setParameter("vg", Integer.parseInt(g_vg.getText()));
			q1.setParameter("hg", Integer.parseInt(g_hg.getText()));
			q1.executeUpdate();
			
			t.commit();
		} catch (Exception e) {
			if(t != null){
				t.rollback();
			}
		}
	}
	
	/*Volanie metody na zobrazenie okna o vsetkych odohranych zapasoch.*/
	@FXML
	private void go_showMatches() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Matches_all.fxml"));
		BorderPane matches = loader.load();
		
		tradePlayerStage = new Stage();
		tradePlayerStage.setTitle("All matches");
		tradePlayerStage.initModality(Modality.NONE);
		tradePlayerStage.initOwner(Main.primaryStage);
		Scene scene = new Scene(matches);
		tradePlayerStage.setScene(scene);
		tradePlayerStage.showAndWait();
		
	}
	
	public static void show_matches() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Matches.fxml"));
		BorderPane matches = loader.load();
		Main.mainLayout.setCenter(matches);
	}
	
	public static void close_showMatches(){
		tradePlayerStage.close();
	}
}
