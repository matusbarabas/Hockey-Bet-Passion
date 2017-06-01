package hockey.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;

import hockey.Statistics_of_Bets;
import hockey.Betting_events;
import hockey.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

/**
 * Trieda, ktora obsahuje celu funkcionalitu okna Account.
 * Robi viacero dopytov do databazy.
 * Zobrazuje data do tabuliek v okne Account.
 * Zobrazuje statisticky stavkoveho uctu.
 * @author Matúš Barabás
 *
 */
public class Account_controller {
	final double MONEY_ACCOUNT = 50; //konstanta, ktora inicializuje zaciatocnu sumu na ucte
	private Query q;
	private Double sum;
	
	ObservableList<Statistics_of_Bets> list_table1 = FXCollections.observableArrayList();
	ObservableList<Betting_events> list_table2 = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Statistics_of_Bets> table1;
	@FXML
	private TableColumn<Statistics_of_Bets, Integer> a_id;
	@FXML
	private TableColumn<Statistics_of_Bets, String> a_home_team;
	@FXML
	private TableColumn<Statistics_of_Bets, Integer> a_hs;
	@FXML
	private TableColumn<Statistics_of_Bets, Integer> a_gs;
	@FXML
	private TableColumn<Statistics_of_Bets, String> a_n;
	@FXML
	private TableColumn<Statistics_of_Bets, String> a_guest_team;
	@FXML
	private TableColumn<Statistics_of_Bets, Integer> a_bet;
	@FXML
	private TableColumn<Statistics_of_Bets, Double> a_money_bet;	
	@FXML
	private TableColumn<Statistics_of_Bets, Double> a_may_win;
	@FXML
	private TableColumn<Statistics_of_Bets, String> a_result;
	@FXML
	private TableColumn<Statistics_of_Bets, Date> a_date_of_bet;
	
	/***************************************************************/
	
	@FXML
	private TableView<Betting_events> table2;
	@FXML
	private TableColumn<Betting_events, Integer> s_id;
	@FXML
	private TableColumn<Betting_events, String> s_home_team;
	@FXML
	private TableColumn<Betting_events, String> s_guest_team;
	@FXML
	private TableColumn<Betting_events, Double> s_h_line;	
	@FXML
	private TableColumn<Betting_events, Double> s_d_line;
	@FXML
	private TableColumn<Betting_events, Double> s_v_line;
	
	/***************************************************************/
	
	@FXML
	private TextField f_id;
	@FXML
	private TextField f_bet;
	@FXML
	private TextField f_money;
	@FXML
	private Label f_even_win;
	
	/***************************************************************/
	
	@FXML
	private TextField c_id;
	@FXML
	private RadioButton c_w;
	@FXML
	private RadioButton c_l;
	@FXML
	private RadioButton c_c;
	
	/***************************************************************/
	
	@FXML
	private Label act_acc;
	@FXML
	private Label benefit;
	@FXML
	private Label games_win;
	@FXML
	private Label games_lost;
	@FXML
	private Label games_canc;
	@FXML
	private Label success;
	
	/***************************************************************/
	
	/*Zobrazenie tabulky o stavkovych prilezitostiach spolu s kurzami.*/
	@FXML
	private void show_betting_events(){
		list_table2.clear();
		
		q = Main.session.createSQLQuery
				("SELECT b.id, b.home_line, b.draw_line, b.visitor_line, "
						+ "t1.name AS home_team, "
						+ "t2.name AS guest_team "
						+ "FROM betting_events b "
						+ "JOIN games_14_15 g ON  g.id = b.games_id "
						+ "JOIN teams t1 ON t1.id = g.home "
						+ "JOIN teams t2 ON t2.id = g.visitor "
						+ "ORDER BY b.date_of_bet"
						);
		
		q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> result = q.list();
		for (Object object : result) {
			Map riadok = (Map)object;
			double h_line = (double) riadok.get("home_line");
			double d_line = (double) riadok.get("draw_line");
			double v_line = (double) riadok.get("visitor_line");
			
			list_table2.add(new Betting_events(
					riadok.get("id").hashCode(),
					riadok.get("home_team").toString(), 
					riadok.get("guest_team").toString(), 
					h_line, 
					d_line, 
					v_line));
			
		}
		
		s_id.setCellValueFactory(new PropertyValueFactory<Betting_events, Integer>("id"));
		s_home_team.setCellValueFactory(new PropertyValueFactory<Betting_events, String>("home_team"));
		s_guest_team.setCellValueFactory(new PropertyValueFactory<Betting_events, String>("guest_team"));
		s_h_line.setCellValueFactory(new PropertyValueFactory<Betting_events, Double>("h_line"));
		s_d_line.setCellValueFactory(new PropertyValueFactory<Betting_events, Double>("d_line"));
		s_v_line.setCellValueFactory(new PropertyValueFactory<Betting_events, Double>("v_line"));
		
		table2.setItems(list_table2);
	}
	
	/*Vypocitanie eventualnej vyhry, podla kurzu v tabulke.*/
	@FXML
	private void eventual_win(){
		String pom = "";
		
		switch(Integer.parseInt(f_bet.getText())){
			case(1):{
				pom = "home_line";
				break;
			}
			case(0):{
				pom = "draw_line";
				break;
			}
			case(2):{
				pom = "visitor_line";
				break;
			}
		}
				
		q = Main.session.createSQLQuery("SELECT " + pom + " FROM betting_events WHERE id = " + Integer.parseInt(f_id.getText()));
		List<Double> line = q.list();
		sum =  line.get(0) * Double.parseDouble(f_money.getText());
		f_even_win.setText(String.valueOf(sum) + "€");
	}	
	
	/*Pridanie stavkovej prileztosti do uctu spolu so vsetkymi informaciami.*/
	@FXML
	private void add_to_account(){
		Transaction t = Main.session.beginTransaction();
		q = Main.session.createSQLQuery("INSERT INTO account (betting_event_id, bet, money_bet, may_win) VALUES (:id, :bet, :money_bet, :may_win)");
		q.setParameter("id", Integer.parseInt(f_id.getText()));
		q.setParameter("bet", Integer.parseInt(f_bet.getText()));
		q.setParameter("money_bet", Double.parseDouble(f_money.getText()));
		q.setParameter("may_win", sum);
		q.executeUpdate();
		t.commit();
	}
	
	/*Zobrazenie tabulky o statistikach na ucte.*/
	@FXML
	private void show_stats(){
		list_table1.clear();
		
		q = Main.session.createSQLQuery
				("SELECT a.id, t1.shortcut AS home_team, g.goals_home, g.goals_visitor, g.note, "
						+ "t2.shortcut AS guest_team, a.bet, a.money_bet, a.may_win, a.result, b.date_of_bet  FROM account a "
						+ "JOIN betting_events b ON a.betting_event_id = b.id "
						+ "JOIN games_14_15 g ON b.games_id = g.id "
						+ "JOIN teams t1 ON g.home = t1.id "
						+ "JOIN teams t2 ON g.visitor = t2.id "
						+ "ORDER BY b.date_of_bet"
						);
		
		q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> result = q.list();
		for (Object object : result) {
			Map riadok = (Map)object;
			String pom, pom1;
			double money_bet = (double) riadok.get("money_bet");
			double may_win = (double) riadok.get("may_win");
			Date date_of_bet = (Date) riadok.get("date_of_bet");
			if(riadok.get("result") == null)
				pom = "";
			else
				pom = riadok.get("result").toString();
			
			if(riadok.get("note") == null)
				pom1 = "";
			else
				pom1 = riadok.get("note").toString();
			
			list_table1.add(new Statistics_of_Bets(
					riadok.get("id").hashCode(),
					riadok.get("home_team").toString(), 
					riadok.get("goals_home").hashCode(),
					riadok.get("goals_visitor").hashCode(),
					pom1,
					riadok.get("guest_team").toString(),
					riadok.get("bet").hashCode(),
					money_bet, 
					may_win, 
					pom,
					date_of_bet
					));
			
		}
		
		a_id.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, Integer>("id"));
		a_home_team.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, String>("home_team"));
		a_hs.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, Integer>("hs"));
		a_gs.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, Integer>("gs"));
		a_n.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, String>("note"));
		a_guest_team.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, String>("guest_team"));
		a_bet.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, Integer>("bet"));
		a_money_bet.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, Double>("money_bet"));
		a_may_win.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, Double>("may_win"));
		a_result.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, String>("result"));
		a_date_of_bet.setCellValueFactory(new PropertyValueFactory<Statistics_of_Bets, Date>("date_of_bet"));
		
		table1.setItems(list_table1);
	}
	
	/*Aktualizacia udajov v tabulke o statistikach na ucte, oznacenie zapasu ako vitazneho,
	 prehraneho alebo zruseneho a na zaklade tejto hodnoty naplnenie troch dalsich stlpcov.*/
	@FXML
	private void save(){
		Transaction t = Main.session.beginTransaction();
		String pom = "";
		
		if(c_w.isSelected()){
			pom = "'WIN'";
		}else if(c_l.isSelected()){
			pom = "'LOST'";
		}else if(c_c.isSelected()){
			pom = "'CANC'";
		}
		
		q = Main.session.createSQLQuery("UPDATE account SET result = " + pom + " WHERE id = " + Integer.parseInt(c_id.getText()));
		q.executeUpdate();
		q = Main.session.createSQLQuery("UPDATE account SET "
										+ "profit = (SELECT CASE WHEN result = 'WIN' THEN " 
												 	+ "((SELECT may_win FROM account WHERE id = :id) - (SELECT money_bet FROM account WHERE id = :id)) "
													+ "ELSE 0 END "
													+ "FROM account WHERE id = :id), "	
										+ "loss = (SELECT CASE WHEN result = 'LOST' THEN (SELECT money_bet FROM account WHERE id = :id) "
				       							+ "ELSE 0 END "
												+ "FROM account WHERE id = :id), "
										+ "success = (SELECT CASE WHEN result = 'WIN' THEN 1 "
					       							+ "WHEN result = 'CANC' THEN 1 "
					       							+ "ELSE 0 END "
				   									+ "FROM account WHERE id = :id) "
										+ "WHERE id = :id");
		q.setParameter("id", Integer.parseInt(c_id.getText()));
		q.executeUpdate();
		t.commit();
		update_stats();
	}
	
	/*Aktualizacia udajov o statistikach na ucte.*/
	@FXML
	private void update_stats(){
		String win = "0", lost = "0", canc = "0";;
		q = Main.session.createSQLQuery("SELECT (sum(profit) - sum(loss)) AS act_acc FROM account");
		Query q1 = Main.session.createSQLQuery("SELECT result, count(result) FROM account GROUP BY result");
		Query q2 = Main.session.createSQLQuery("SELECT avg(success) FROM account");
		q1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Double> result = q.list();
		List<Map> result1 = q1.list();
		List<Double> result2 = q2.list();
		
		for (Object object : result1) {	
			Map riadok = (Map)object;
			
			if(riadok.get("result") == null){
				continue;
			}
			
			if(riadok.get("result").toString().equals("LOST")){
				lost = riadok.get("count").toString();
			}

			if(riadok.get("result").toString().equals("WIN")){
				win = riadok.get("count").toString();
			}
			
			if(riadok.get("result").toString().equals("CANC")){
				canc = riadok.get("count").toString();
			}
		}
		
		act_acc.setText((round((Double.parseDouble(result.get(0).toString())), 2) + MONEY_ACCOUNT) + " €");
		benefit.setText(Double.toString((round((Double.parseDouble(result.get(0).toString())), 2))) + " €");
		games_win.setText(win);
		games_lost.setText(lost);
		games_canc.setText(canc);
		success.setText(Double.toString((round((Double.parseDouble(result2.get(0).toString())), 2) * 100)) + " %");
	}
	
	/*Vymazanie stavkovej prilezitosti z tabulky.*/
	@FXML
	private void delete(){
		q = Main.session.createSQLQuery("DELETE FROM account WHERE id = :id");
		q.setParameter("id", Integer.parseInt(c_id.getText()));
		q.executeUpdate();
	}
	
	/*Zobrazenie okna Account*/
	public static void show_account() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Account.fxml"));
		BorderPane matches = loader.load();
		Main.mainLayout.setCenter(matches);
	}
	
	/*Funkcia na zaokruhlovanie cisel na dany pocet desatinnych miest.*/
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
