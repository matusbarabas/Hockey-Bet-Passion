package hockey.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;

import hockey.Main;
import hockey.Players;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Trieda, ktora obsahuje celu funkcionalitu o hracoch.
 * Robi viacere dopyty do databzy, z ktorej vybera hracou na zaklade nejakych kriterii.
 * @author Matúš Barabás
 *
 */
public class Players_controller {
	
	private Query q;
	private static Stage tradePlayerStage;
	private static Stage goaliesStage;
	private ObservableList<Players> table_list = FXCollections.observableArrayList();

	@FXML
	private ComboBox<String> teams;	
	@FXML
	private TableView<Players> table;	
	@FXML
	private TableColumn<Players, String> name;
	@FXML
	private TableColumn<Players, String> team;
	@FXML
	private TableColumn<Players, Date> date_from;
	@FXML
	private TableColumn<Players, Date> date_to;
	@FXML
	private TableColumn<Players, String> pos;
	@FXML
	private TableColumn<Players, Integer> goals;	
	@FXML
	private TableColumn<Players, Integer> assist;
	@FXML
	private TableColumn<Players, Integer> points;
	
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
		
		name.setCellValueFactory(new PropertyValueFactory<Players, String>("p_name"));
		team.setCellValueFactory(new PropertyValueFactory<Players, String>("p_team"));
		date_from.setCellValueFactory(new PropertyValueFactory<Players, Date>("date_f"));
		date_to.setCellValueFactory(new PropertyValueFactory<Players, Date>("date_t"));
		pos.setCellValueFactory(new PropertyValueFactory<Players, String>("p_pos"));
		goals.setCellValueFactory(new PropertyValueFactory<Players, Integer>("p_goals"));
		assist.setCellValueFactory(new PropertyValueFactory<Players, Integer>("p_assist"));
		points.setCellValueFactory(new PropertyValueFactory<Players, Integer>("p_points"));
	}
	
	@FXML
	private void go_trade() throws IOException{	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Players_trade.fxml"));
		BorderPane matches = loader.load();
		
		tradePlayerStage = new Stage();
		tradePlayerStage.setTitle("Trade player");
		tradePlayerStage.initModality(Modality.WINDOW_MODAL);
		tradePlayerStage.initOwner(Main.primaryStage);
		Scene scene = new Scene(matches);
		tradePlayerStage.setScene(scene);
		tradePlayerStage.showAndWait();
	}
	
	@FXML
	private void go_goalies() throws IOException{	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Goalies.fxml"));
		BorderPane matches = loader.load();
		
		goaliesStage = new Stage();
		goaliesStage.setTitle("Goalies");
		goaliesStage.initModality(Modality.APPLICATION_MODAL);
		goaliesStage.initOwner(Main.primaryStage);
		Scene scene = new Scene(matches);
		goaliesStage.setScene(scene);
		goaliesStage.showAndWait();
	}
	
	@FXML
	private void show_players_table(){
		make(1);
	}
	
	@FXML
	private void best_players(){
		make(2);
	}
	
	@FXML
	private void best_forwards(){
		make(3);
	}
	
	@FXML
	private void best_defence(){
		make(4);
	}
	
	@FXML
	private void most_goals(){
		make(5);
	}
	
	@FXML
	private void most_assist(){
		make(6);
	}
	
	@FXML
	private void trades(){
		table_list.clear();
		/*Zobrazuje v tabulke iba hracov, ktori prestupili z nejakeho timu do timu aj s prislusnymi datumami.*/
		q = Main.session.createSQLQuery("SELECT p.name, t.shortcut, pl.date_from, pl.date_to FROM player_in_team pl "
										+ "JOIN teams t ON t.id = pl.team_id "
										+ "JOIN players_14_15 p ON pl.player_id = p.id "
										+ "WHERE pl.date_to IS NOT null");
		
		q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> result = q.list();
		
		for (Object object : result) {
			Map riadok = (Map)object;
			Date pom = (Date) riadok.get("date_from");
			Date pom1 = (Date) riadok.get("date_to");
			table_list.add(new Players(
					riadok.get("name").toString(), 
					riadok.get("shortcut").toString(), 
					pom,
					pom1,
					null ,null, null, null));
		}
		table.setItems(table_list);
	}
	
	private void make(int choice){
		table_list.clear();
		
		switch(choice){
			case 1:{
				/*Zobrazenie prehladu vsetkych hracov alebo hracov z danho timu.*/
				if(teams.getValue() == null){
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE pl.date_to IS null");
				}else{
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE t.name = '" + teams.getValue() + "' AND pl.date_to IS null" );
				}
				break;
			}
			case 2:{
				/*Zobrazenie 10 najlepsich hracov podla pcotu bodov.*/
				if(teams.getValue() == null){
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE pl.date_to IS null "
													+ "ORDER BY points DESC LIMIT 10");
				}else{
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE t.name = '" + teams.getValue() + "' AND pl.date_to IS null"
													+ "ORDER BY points DESC LIMIT 10");
				}		
				break;
			}
			case 3:{
				/*Zobrezenie 10 najlepsich utocnikov.*/
				if(teams.getValue() == null){
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE pl.date_to IS null AND pos = 'C' OR pos = 'LW' OR pos = 'RW' "
													+ "ORDER BY points DESC LIMIT 10");
				}else{
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE t.name = '" + teams.getValue() + "' AND pl.date_to IS null AND pos = 'C' OR pos = 'LW' OR pos = 'RW' "
													+ "ORDER BY points DESC LIMIT 10");
				}		
				break;
			}
			case 4:{
				/*Zobrazenie 10 najlepsich obrancov.*/
				if(teams.getValue() == null){
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE pl.date_to IS null AND pos = 'D' "
													+ "ORDER BY points DESC LIMIT 10");
				}else{
					q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
													+ "FROM players_14_15 p "
													+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
													+ "INNER JOIN teams t ON t.id = pl.team_id "
													+ "WHERE t.name = '" + teams.getValue() + "' AND pl.date_to IS null AND pos = 'D' "
													+ "ORDER BY points DESC LIMIT 10");
				}		
				break;
			}
			case 5:{
				/*Zobrazenie hraca, ktory strelil najviac golov.*/
				q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
												+ "FROM players_14_15 p "
												+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
												+ "INNER JOIN teams t ON t.id = pl.team_id "
												+ "WHERE goals =(SELECT max(goals) FROM players_14_15 p)");	
				break;
			}
			case 6:{
				/*Zobrazenie hraca, ktory ma na konte najviac asistencii.*/
				q = Main.session.createSQLQuery("SELECT p.name, t.shortcut AS team, pl.date_from, pl.date_to, p.pos, p.goals, p.assist, p.points "
												+ "FROM players_14_15 p "
												+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
												+ "INNER JOIN teams t ON t.id = pl.team_id "
												+ "WHERE assist =(SELECT max(assist) FROM players_14_15 p)");			
				break;
			}
		}
		
		q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> result = q.list();
		
		for (Object object : result) {
			Map riadok = (Map)object;
			Date pom = (Date) riadok.get("date_from");
			Date pom1 = null;
			table_list.add(new Players(
					riadok.get("name").toString(), 
					riadok.get("team").toString(), 
					pom,
					pom1,
					riadok.get("pos").toString(), 
					riadok.get("goals").hashCode(), 
					riadok.get("assist").hashCode(), 
					riadok.get("points").hashCode()));
		}
		
		table.setItems(table_list);
		teams.getSelectionModel().clearSelection();
	}
	
	public static  void show_players() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Players.fxml"));
		BorderPane players = loader.load();
		Main.mainLayout.setCenter(players);
	}
	
	public static void close_trade(){
		tradePlayerStage.close();
	}
	
	public static void close_goalies(){
		goaliesStage.close();
	}
}
