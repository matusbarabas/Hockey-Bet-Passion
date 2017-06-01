package hockey.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;

import hockey.Teams;
import hockey.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * Trieda obsahujuca funkcionalitu o timoch.
 * Umoznuje pridanie timu do databazy.
 * @author Matúš Barabás
 *
 */
public class Teams_controller {
		
	@FXML
	private GridPane aaa;
	@FXML
	private Label info;
	@FXML 
	private TableView table;	
	@FXML
	private TableColumn name;	
	@FXML
	private TableColumn conference;	
	@FXML
	private TableColumn devision;
	@FXML
	private TextField team_name;
	@FXML
	private TextField team_devision;
	@FXML
	private TextField team_conference;
	
	ObservableList<Teams> list = FXCollections.observableArrayList();
	
	/*Zobrazenie vsetkych timov do tabulky.*/
	@FXML
	private void show(){
		list.clear();
		
		Query query = Main.session.createSQLQuery("SELECT * FROM teams");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> result = query.list();
		
		for (Object object : result) {
			Map riadok = (Map)object;
			list.add(new Teams(riadok.get("name").toString(), riadok.get("conference").toString(), riadok.get("devision").toString()));
		}
		
		name.setCellValueFactory(new PropertyValueFactory<Teams, String>("Name"));
		conference.setCellValueFactory(new PropertyValueFactory<Teams, String>("Conference"));
		devision.setCellValueFactory(new PropertyValueFactory<Teams, String>("Devision"));
		
		table.setItems(list);
	}
	
	/*Pridanie timu do databazy.*/
	@FXML
	private void add_team(){
		Transaction t = Main.session.beginTransaction();
		t = Main.session.beginTransaction();
		Query query = Main.session.createSQLQuery("insert into teams (name, conference, devision) values(" 
				+ ":meno,"
				+ "'" + team_conference.getText() + "',"
				+ "'" + team_devision.getText() + "')"
				);
		
		query.setParameter("meno", team_name.getText());
		
		query.executeUpdate();
		info.setText("Team added");
		t.commit();
	}
	
	public static void show_teams() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Teams.fxml"));
		BorderPane teams = loader.load();
		Main.mainLayout.setCenter(teams);
	}
}
