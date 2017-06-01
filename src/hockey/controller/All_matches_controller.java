package hockey.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;

import hockey.Games;
import hockey.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Trieda obsahujuca funkcionalitu okna o vsetkych odohranych zapasoch.
 * Robi jeden dopyt do databazy, z ktorej vybera bud vsetky zapasy alebo iba zapasy konkretneho timu.
 * @author Matúš Barabás
 *
 */
public class All_matches_controller {

	private Query q;
	
	ObservableList<String> list = FXCollections.observableArrayList();
	ObservableList<Games> table_list = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> teams;	
	@FXML
	private TableView<Games> table;	
	@FXML
	private TableColumn<Games, Integer> id;
	@FXML
	private TableColumn<Games, String> home_team;
	@FXML
	private TableColumn<Games, Integer> home_score;
	@FXML
	private TableColumn<Games, Integer> guest_score;
	@FXML
	private TableColumn<Games, String> guest_team;	
	@FXML
	private TableColumn<Games, String> ot_so;
	@FXML
	private TableColumn<Games, String> datum;
	
	/*Naplnenie ComboBoxu vsetkymi timamy z databazy*/
	@FXML
	private void initialize(){
		list.clear();
		
		q = Main.session.createSQLQuery("SELECT name FROM teams");
		List<String> result = q.list();
		
		for (String x : result) {
			list.add(x);
		}
		
		teams.setItems(list);
	}
	
	/*Zobrazenie vsetkych zapasov ale zapasov daneho timu.*/
	@FXML
	private void show_games(){
		String pom;
		table_list.clear();
		if(teams.getValue() == null){
			q = Main.session.createSQLQuery
					("SELECT g.id, g.date_of_match, g.goals_visitor, g.goals_home, g.note, t.name AS visitor, t2.name AS home"
					+ " FROM games_14_15 g"
					+ " INNER JOIN teams t"
					+ " ON t.id = g.visitor"
					+ " INNER JOIN teams t2"
					+ " ON t2.id = g.home");
		}else{
			q = Main.session.createSQLQuery
					("SELECT g.id, g.date_of_match, g.goals_visitor, g.goals_home, g.note, t.name AS visitor, t2.name AS home"
					+ " FROM games_14_15 g"
					+ " INNER JOIN teams t"
					+ " ON t.id = g.visitor"
					+ " INNER JOIN teams t2"
					+ " ON t2.id = g.home"
					+ " WHERE t.name =" + "'" + teams.getValue() + "'" + " OR t2.name =" + "'" + teams.getValue() + "'"	
					);
		}
		q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> result = q.list();
		
		for (Object object : result) {
			Map riadok = (Map)object;
			Date my_date = (Date) riadok.get("date_of_match");
			if(riadok.get("note") == null){
				pom = "";
			}else{
				pom = riadok.get("note").toString();
			}
			table_list.add(new Games(
					riadok.get("id").hashCode(),
					riadok.get("home").toString(), 
					riadok.get("goals_home").hashCode(), 
					riadok.get("goals_visitor").hashCode(), 
					riadok.get("visitor").toString(), 
					pom, 
					my_date));
		}
		
		id.setCellValueFactory(new PropertyValueFactory<Games, Integer>("match_id"));
		home_team.setCellValueFactory(new PropertyValueFactory<Games, String>("home_team"));
		guest_team.setCellValueFactory(new PropertyValueFactory<Games, String>("visitor_team"));
		home_score.setCellValueFactory(new PropertyValueFactory<Games, Integer>("home_score"));
		guest_score.setCellValueFactory(new PropertyValueFactory<Games, Integer>("visitor_score"));
		ot_so.setCellValueFactory(new PropertyValueFactory<Games, String>("ot_os"));
		datum.setCellValueFactory(new PropertyValueFactory<Games, String>("date_of_match"));
		
		table.setItems(table_list);
	}
	
	@FXML
	private void close_all_matches(){
		Matches_controller.close_showMatches();
	}	
}
