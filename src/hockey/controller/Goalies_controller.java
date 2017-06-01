package hockey.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;

import hockey.Main;
import hockey.Goalies;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Trieda obsahujuca funkcionalitu okna o brankaroch.
 * Robi dopyty do databazy na zaklade zadanych poziadaviek.
 * Zobrazuje do tabulky vybrane statistiky.
 * @author Matúš Barabás
 *
 */
public class Goalies_controller {
	
	private Query q;
	private ObservableList<Goalies> table_list = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> teams;	
	@FXML
	private CheckBox best_goalies;
	@FXML
	private CheckBox most_shots;
	@FXML
	private CheckBox most_wins;
	@FXML
	private CheckBox average_age;
	@FXML
	private CheckBox average_shots;
	@FXML
	private CheckBox average_saves;
	@FXML
	private CheckBox average_pm;
	@FXML
	private CheckBox sum_shots;
	@FXML
	private CheckBox sum_saves;
	@FXML
	private CheckBox sum_pm;
	@FXML
	private Label text;
	
	@FXML
	private TableView<Goalies> table;
	@FXML
	private TableColumn<Goalies, String> g_name;
	@FXML
	private TableColumn<Goalies, Integer> g_age;
	@FXML
	private TableColumn<Goalies, String> g_team;
	@FXML
	private TableColumn<Goalies, Integer> g_games;
	@FXML
	private TableColumn<Goalies, Integer> g_w;
	@FXML
	private TableColumn<Goalies, Integer> g_l;
	@FXML
	private TableColumn<Goalies, Integer> g_ga;
	@FXML
	private TableColumn<Goalies, Integer> g_shots;
	@FXML
	private TableColumn<Goalies, Integer> g_saves;
	@FXML
	private TableColumn<Goalies, Double> g_saves_p;
	@FXML
	private TableColumn<Goalies, Integer> g_shutout;
	@FXML
	private TableColumn<Goalies, Integer> g_goals;
	@FXML
	private TableColumn<Goalies, Integer> g_assist;
	@FXML
	private TableColumn<Goalies, Integer> g_points;
	@FXML
	private TableColumn<Goalies, Integer> g_pm;
	
	@FXML
	private TextField m_name;
	@FXML
	private TextField m_age;
	@FXML
	private TextField m_team;
	
	@FXML
	private void initialize(){
		ObservableList<String> list = FXCollections.observableArrayList();
		list.clear();		
		q = Main.session.createSQLQuery("SELECT name FROM teams");
		List<String> result = q.list();
		
		for (String x : result) {
			list.add(x);
		}

		teams.setItems(list);
		
		g_name.setCellValueFactory(new PropertyValueFactory<Goalies, String>("name"));
		g_age.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("age"));
		g_team.setCellValueFactory(new PropertyValueFactory<Goalies, String>("team"));
		g_games.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("gp"));
		g_w.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("w"));
		g_l.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("l"));
		g_ga.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("ga"));
		g_shots.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("sa"));
		g_saves.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("saves"));
		g_saves_p.setCellValueFactory(new PropertyValueFactory<Goalies, Double>("s_p"));
		g_shutout.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("shutout"));
		g_goals.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("goals"));
		g_assist.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("assist"));
		g_points.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("points"));
		g_pm.setCellValueFactory(new PropertyValueFactory<Goalies, Integer>("pm"));
		
	}
	
	/*Zobrazuje do tabulky alebo do label vybranu statistiku tykajucu sa brankarov.*/
	@FXML
	private void show_goalies(){
		int choice = 0;
		table_list.clear();
		
		if(best_goalies.isSelected())
			choice = 1;
		else if(most_shots.isSelected())
			choice = 2;
		else if(most_wins.isSelected())
			choice = 3;
		else if(average_age.isSelected())
			choice = 4;
		else if(average_shots.isSelected())
			choice = 5;
		else if(average_saves.isSelected())
			choice = 6;
		else if(average_pm.isSelected())
			choice = 7;
		else if(sum_shots.isSelected())
			choice = 8;
		else if(sum_saves.isSelected())
			choice = 9;
		else if(sum_pm.isSelected())
			choice = 10;
		else if(!m_name.getText().equals(""))
			choice = 11;
		else choice = 0;
		
		switch(choice){
		case 1:{
			/*Zobrazenie 10 najlepsich brankov podla uspesnosti zakrokov.*/
			q = Main.session.createSQLQuery("SELECT g.name, g.age, t.shortcut AS team, g.games_played, g.wins, g.lost, g.goals_against, g.shots_against, "
											+ "g.saves, g.save_percentage, g.shutouts, g.goals, g.assist, g.points, g.penalty_min FROM goalies g "
											+ "JOIN teams t ON g.team_id = t.id "
											+ "WHERE games_played > 20 "
											+ "AND save_percentage > (SELECT max(save_percentage) FROM goalies) - 0.1 "
											+ "ORDER BY save_percentage DESC LIMIT 10");
			show_in_table();
			break;
		}
		case 2:{
			/*Zobrazenie brankara, na ktoreho slo najviac striel.*/
			q = Main.session.createSQLQuery("SELECT g.name, g.age, t.shortcut AS team, g.games_played, g.wins, g.lost, g.goals_against, g.shots_against, "
											+ "g.saves, g.save_percentage, g.shutouts, g.goals, g.assist, g.points, g.penalty_min FROM goalies g "
											+ "JOIN teams t ON g.team_id = t.id "
											+ "WHERE shots_against = (SELECT max(shots_against) FROM goalies)");
			show_in_table();
			break;
		}
		case 3:{
			/*Zobrazenie brankara, ktory ma na konte najviac vitazstiev.*/
			q = Main.session.createSQLQuery("SELECT g.name, g.age, t.shortcut AS team, g.games_played, g.wins, g.lost, g.goals_against, g.shots_against, "
											+ "g.saves, g.save_percentage, g.shutouts, g.goals, g.assist, g.points, g.penalty_min FROM goalies g "
											+ "JOIN teams t ON g.team_id = t.id "
											+ "WHERE wins = (SELECT max(wins) FROM goalies)");
			show_in_table();
			break;
		}
		case 4:{
			/*Priemerny vek brankarov.*/
			q = Main.session.createSQLQuery("SELECT avg(age) FROM goalies");
			List<BigDecimal> result = q.list();
			text.setText(Double.toString((Math.round(Double.parseDouble((result.get(0).toString()))))));
			break;
		}
		case 5:{
			/*Priemerny pocet striel*/
			q = Main.session.createSQLQuery("SELECT avg(shots_against) FROM goalies");
			List<BigDecimal> result = q.list();
			text.setText(Double.toString((Math.round(Double.parseDouble((result.get(0).toString()))))));
			break;
		}
		case 6:{
			/*Priemerny pocet zasahov.*/
			q = Main.session.createSQLQuery("SELECT avg(saves) FROM goalies");
			List<BigDecimal> result = q.list();
			text.setText(Double.toString((Math.round(Double.parseDouble((result.get(0).toString()))))));
			break;
		}
		case 7:{
			/*Priemerny pocet trestnych minut.*/
			q = Main.session.createSQLQuery("SELECT avg(penalty_min) FROM goalies");
			List<BigDecimal> result = q.list();
			text.setText(Double.toString((Math.round(Double.parseDouble((result.get(0).toString()))))));
			break;
		}
		case 8:{
			/*Sucet vsetkych striel, ktore smerovali na brankarov.*/
			q = Main.session.createSQLQuery("SELECT sum(shots_against) FROM goalies");
			List<BigInteger> result = q.list();
			text.setText(Integer.toString(Integer.parseInt(result.get(0).toString())));
			break;
		}
		case 9:{
			/*Suma vsetych zasahov brankarov*/
			q = Main.session.createSQLQuery("SELECT sum(saves) FROM goalies");
			List<BigInteger> result = q.list();
			text.setText(Integer.toString(Integer.parseInt(result.get(0).toString())));
			break;
		}
		case 10:{
			/*Sucet vsetkych trestnych minut vsetkych brankarov.*/
			q = Main.session.createSQLQuery("SELECT sum(penalty_min) FROM goalies");
			List<BigInteger> result = q.list();
			text.setText(Integer.toString(Integer.parseInt(result.get(0).toString())));
			break;
		}
		case 11:{
			show_goalie();
			break;
		}
		case 0:{
			/*Zobrazenie vsetkych brankarov alebo konkretnych brankarov z daneho timu.*/
			if(teams.getValue() == null){
					q = Main.session.createSQLQuery("SELECT g.name, g.age, t.shortcut AS team, g.games_played, g.wins, g.lost, g.goals_against, g.shots_against, "
													+ "g.saves, g.save_percentage, g.shutouts, g.goals, g.assist, g.points, g.penalty_min FROM goalies g "
													+ "JOIN teams t ON g.team_id = t.id");
			}else{
					q = Main.session.createSQLQuery("SELECT g.name, g.age, t.shortcut AS team, g.games_played, g.wins, g.lost, g.goals_against, g.shots_against, "
													+ "g.saves, g.save_percentage, g.shutouts, g.goals, g.assist, g.points, g.penalty_min FROM goalies g "
													+ "JOIN teams t ON g.team_id = t.id "
													+ "WHERE t.name = '" + teams.getValue() + "'");
			}
			show_in_table();
			break;
		}
		}
	}

	private void show_goalie() {
		/*Zobrazenie prehladu vsetkych brankarov.*/
		q = Main.session.createSQLQuery("SELECT g.name, g.age, t.shortcut AS team, g.games_played, g.wins, g.lost, g.goals_against, g.shots_against, "
										+ "g.saves, g.save_percentage, g.shutouts, g.goals, g.assist, g.points, g.penalty_min FROM goalies g "
										+ "JOIN teams t ON g.team_id = t.id "
										+ "WHERE g.name = :name");
		q.setParameter("name", m_name.getText());
		m_name.clear();
		m_age.clear();
		m_team.clear();
		show_in_table();
	}

	private void show_in_table(){
		q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> result = q.list();
		for (Object object : result) {
			Map riadok = (Map)object;
			double pom = (double) riadok.get("save_percentage");
	
			table_list.add(new Goalies(
					riadok.get("name").toString(),
					riadok.get("age").hashCode(), 
					riadok.get("team").toString(),
					riadok.get("games_played").hashCode(),
					riadok.get("wins").hashCode(),
					riadok.get("lost").hashCode(),
					riadok.get("goals_against").hashCode(),
					riadok.get("shots_against").hashCode(),
					riadok.get("saves").hashCode(),
					pom,
					riadok.get("shutouts").hashCode(),
					riadok.get("goals").hashCode(),
					riadok.get("assist").hashCode(),
					riadok.get("points").hashCode(),
					riadok.get("penalty_min").hashCode()
					));
	
		}

		table.setItems(table_list);
		teams.getSelectionModel().clearSelection();
	}
	
	@FXML
	private void delete_goalie(){
		/*Vymazanie brankara z databazy.*/
		Transaction t = Main.session.beginTransaction();
		q = Main.session.createSQLQuery("DELETE FROM goalies WHERE name = :name AND age = :age AND team_id = (SELECT t.id FROM teams t WHERE t.shortcut = :team)");
		q.setParameter("name", m_name.getText());
		q.setParameter("age", Integer.parseInt(m_age.getText()));
		q.setParameter("team", m_team.getText());
		q.executeUpdate();
		m_name.clear();
		m_age.clear();
		m_team.clear();
		t.commit(); 
	}
	
	@FXML
	private void close_goalies_window(){
		Players_controller.close_goalies();
	}
}
