package hockey.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Transaction;

import hockey.Live;
import hockey.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import redis.clients.jedis.Jedis;

/**
 * Trieda, ktora pracuje s nerelacnou databazou Redis.
 * Poskytuje funkcionalitu pre online textove prenosy zapasov spolu so sledovanim statistik.
 * @author Matúš Barabás
 *
 */
public class Live_controller {
	private Jedis jedis;
	private Query q;
	private static Stage lifeStage;
	private ObservableList<Live> table_list = FXCollections.observableArrayList();
	private int poc_home = 0;
	private int poc_vis = 0;
	
	@FXML
	private Button b_s_t;
	@FXML
	private ComboBox<String> home_team;	
	@FXML
	private ComboBox<String> visitor_team;
	@FXML
	private ComboBox<String> p_home;
	@FXML
	private ComboBox<String> p_home1;	
	@FXML
	private ComboBox<String> p_home2;	
	@FXML
	private ComboBox<String> p_vis;
	@FXML
	private ComboBox<String> p_vis1;
	@FXML
	private ComboBox<String> p_vis2;
	@FXML
	private Label h;
	@FXML
	private Label v;
	@FXML
	private Label h_g;
	@FXML
	private Label v_g;
	@FXML
	private Label h_s;
	@FXML
	private Label v_s;
	@FXML
	private Label h_pm;
	@FXML
	private Label v_pm;
	@FXML
	private RadioButton two_min;
	@FXML
	private RadioButton five_min;
	@FXML
	private RadioButton ten_min;
	@FXML
	private Label period;
	@FXML
	private CheckBox first;
	@FXML
	private CheckBox second;
	@FXML
	private CheckBox third;
	@FXML
	private CheckBox ot;
	@FXML
	private CheckBox so;
	@FXML
	private TextArea write_text;
	@FXML
	private TextArea home_goals_text;
	@FXML
	private TextArea vis_goals_text;
	@FXML
	private TableView<Live> text_table;
	@FXML
	private TableColumn<Live, String> text_column;

	@FXML
	private void initialize(){	

		try{
			jedis = new Jedis("localhost");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		ObservableList<String> list_teams = FXCollections.observableArrayList();
		table_list.clear();
		list_teams.clear();

		text_column.setCellValueFactory(new PropertyValueFactory<Live, String>("t_text"));
		text_column.setCellFactory(new Callback<TableColumn<Live,String>, TableCell<Live,String>>() {
			/**
			 * Metoda na zalomenie textu v tabulke.
			 */
            @Override
            public TableCell<Live, String> call( TableColumn<Live, String> param) {
                 final TableCell<Live, String> cell = new TableCell<Live, String>() {
                      private Text text;
                      @Override
                      public void updateItem(String item, boolean empty) {
                           super.updateItem(item, empty);
                           if (!isEmpty()) {
                                text = new Text(item.toString());
                                text.setWrappingWidth(549);
                                setGraphic(text);
                           }
                      }
                 };
                 return cell;
            }
		});
		if(jedis.get("s_t") == null){
			q = Main.session.createSQLQuery("SELECT name FROM teams");
			List<String> result = q.list();
			
			for (String x : result) {
				list_teams.add(x);
			}
			
			home_team.setItems(list_teams);
			visitor_team.setItems(list_teams);
		}else{
			period.setText(jedis.get("per"));
			home_team.setValue(jedis.get("h_t"));
			visitor_team.setValue(jedis.get("v_t"));
			b_s_t.setDisable(true);
			h.setText(jedis.get("h_t"));
			v.setText(jedis.get("v_t"));
			select_team();
		}
		actual();
	}
	
	@FXML
	private void hg_plus(){				//zvysovanie golov domaceho timu
		jedis.incr("hg");
		actual();
	}
	
	@FXML
	private void hg_minus(){			//znizovanie golov domaceho timu
		jedis.decr("hg");
		actual();
	}
	
	@FXML
	private void vg_plus(){				//zvysovanie golov hostujuceho timu
		jedis.incr("vg");
		actual();
	}
	
	@FXML
	private void vg_minus(){			//znizovanie golov hostujuceho timu
		jedis.decr("vg");
		actual();
	}
	
	@FXML
	private void hs_plus(){				//zvysovanie skore domaceho timu
		jedis.incr("hs");
		actual();
	}
	
	@FXML
	private void hs_minus(){			//znizovanie skore domaceho timu
		jedis.decr("hs");
		actual();
	}
	
	@FXML
	private void vs_plus(){				//zvysovanie skore hostujuceho timu
		jedis.incr("vs");
		actual();
	}
	
	@FXML
	private void vs_minus(){			//znizovanie skore hostujuceho timu
		jedis.decr("vs");
		actual();
	}
	
	@FXML
	private void pm_home(){				//pridavanie trestnych minut domacemu timu
		if(two_min.isSelected()){
			jedis.incrBy("hpm", 2);
		}else if(five_min.isSelected()){
			jedis.incrBy("hpm", 5);
		}else if(ten_min.isSelected()){
			jedis.incrBy("hpm", 10);
		}
		actual();
	}
	
	@FXML
	private void pm_visitor(){			//pridavanie trestnych minut hostujucemu timu
		if(two_min.isSelected()){
			jedis.incr("vpm");
		}else if(five_min.isSelected()){
			jedis.incrBy("vpm", 5);
		}else if(ten_min.isSelected()){
			jedis.incrBy("vpm", 10);
		}
		actual();
	}
	
	@FXML
	private void set_period(){			//nastavenie aktualnej tretiny, pripadne predlzenia ci samostatnych najazdov
		if(first.isSelected()){
			period.setText("1. PERIOD");
			jedis.set("per", "1. PERIOD");
		}else if(second.isSelected()){
			period.setText("2. PERIOD");
			jedis.set("per", "2. PERIOD");
		}else if(third.isSelected()){
			period.setText("3. PERIOD");
			jedis.set("per", "3. PERIOD");
		}else if(ot.isSelected()){
			period.setText("OT");
			jedis.set("per", "OT");
		}else if(so.isSelected()){
			period.setText("SO");
			jedis.set("per", "SO");
		}
		actual();
	}
	
	@FXML
	private void select_team(){
		ObservableList<String> list_h = FXCollections.observableArrayList();
		ObservableList<String> list_v = FXCollections.observableArrayList();
		list_h.clear();
		list_v.clear();
		
		q = Main.session.createSQLQuery("SELECT p.name FROM players_14_15 p "
										+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
										+ "INNER JOIN teams t ON t.id = pl.team_id "
										+ "WHERE t.name = :h");
		q.setParameter("h", home_team.getValue());
		List<String> result1 = q.list();

		for (String x : result1) {
			list_h.add(x);
		}
		
		p_home.setItems(list_h);
		p_home1.setItems(list_h);
		p_home2.setItems(list_h);
		
		q = Main.session.createSQLQuery("SELECT p.name FROM players_14_15 p "
										+ "INNER JOIN player_in_team pl ON pl.player_id = p.id "
										+ "INNER JOIN teams t ON t.id = pl.team_id "
										+ "WHERE t.name = :v");
		q.setParameter("v", visitor_team.getValue());
		List<String> result2 = q.list();

		for (String x : result2) {
			list_v.add(x);
		}

		p_vis.setItems(list_v);
		p_vis1.setItems(list_v);
		p_vis2.setItems(list_v);
		if(jedis.get("s_t") == null){
			h.setText(home_team.getValue());			/*vyber timov, inicializacia potrebnych premennych v Redise*/
			v.setText(visitor_team.getValue());
			jedis.set("hg", "0");
			jedis.set("vg", "0");
			jedis.set("hs", "0");
			jedis.set("vs", "0");
			jedis.set("hpm", "0");
			jedis.set("vpm", "0");
			jedis.set("h_t", home_team.getValue());
			jedis.set("v_t", visitor_team.getValue());
			poc_home = 1;
			poc_vis = 100;
			b_s_t.setDisable(true);
			jedis.set("s_t", "1");
		}
		actual();
	}
	
	@FXML
	public static void show_life() throws IOException{	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Live_Game.fxml"));
		BorderPane matches = loader.load();
		
		lifeStage = new Stage();
		lifeStage.setTitle("Live Game");
		lifeStage.initModality(Modality.APPLICATION_MODAL);
		lifeStage.initOwner(Main.primaryStage);
		Scene scene = new Scene(matches);
		lifeStage.setScene(scene);
		lifeStage.showAndWait();
	}
	
	@FXML
	private void text_to_table(){								//ulozenie textu do listu v Redise
		jedis.lpush("online", write_text.getText());
		write_text.clear();
		actual();
	}
	
	@FXML
	private void end(){											//vymazanie udajov z databazy po skonceni zapasu
		jedis.set("s_t", "0");
		jedis.flushDB();
		jedis.disconnect();
		lifeStage.close();
	}
	
	@FXML
	private void home_goal(){											//zaznamenavanie strelcov golov spolu s asistenciami domaceho timu do hash v Redise
		Map<String, String> input = new HashMap<String, String>();		//ulozenie nazvu hashu do listu v Redise
		input.put("goal", p_home.getValue());
		input.put("assist1", p_home1.getValue());
		input.put("assist2", p_home2.getValue());
		 
		jedis.hmset(Integer.toString(poc_home), input);
		jedis.lpush("goals_home", Integer.toString(poc_home));
		input.clear();
		poc_home++;
		actual();
	}
	
	@FXML
	private void vis_goal(){											//zaznamenavanie strelcov golov spolu s asistenciami hostujuceho timu do hash v Redise
		Map<String, String> input = new HashMap<String, String>();		//ulozenie nazvu hashu do listu v Redise
		input.put("goal", p_vis.getValue());
		input.put("assist1", p_vis1.getValue());
		input.put("assist2", p_vis2.getValue());
		
		jedis.hmset(Integer.toString(poc_vis), input);
		jedis.lpush("goals_vis", Integer.toString(poc_vis));
		input.clear();
		poc_vis++;
		actual();
	}
	
	@FXML
	private void save_game(){						//ulozenie zapasu do relacnej databazy PostgreSQL
		String pom = "";
		
		Transaction t = Main.session.beginTransaction();
		Date date = new Date();
		if(period.getText().equals("OT")){
			pom = "OT";
		}else if(period.getText().equals("SO")){
			pom = "SO";
		}
		
		q = Main.session.createSQLQuery("INSERT INTO games_14_15 (date_of_match, visitor, goals_visitor, home, goals_home, note) "
				+ "VALUES (:date, (SELECT id FROM teams WHERE name = :gteam), :vg, (SELECT id FROM teams WHERE name = :hteam), :hg, :note)");
		
		q.setParameter("hteam", home_team.getValue());
		q.setParameter("gteam", visitor_team.getValue());
		q.setParameter("date", date);
		q.setParameter("vg", Integer.parseInt(h_g.getText()));
		q.setParameter("hg", Integer.parseInt(v_g.getText()));
		q.setParameter("note", pom);
		
		q.executeUpdate();
		
		t.commit();
	}
	
	public void actual(){							//aktualizacia vsetkych prvkov v okne 
		List<String> list = null;
		List<String> list_goals_home = null;
		List<String> list_goals_vis = null;
		
		h_g.setText(jedis.get("hg"));
		v_g.setText(jedis.get("vg"));
		h_s.setText(jedis.get("hs"));
		v_s.setText(jedis.get("vs"));
		h_pm.setText(jedis.get("hpm"));
		v_pm.setText(jedis.get("vpm"));
		
		text_table.refresh();
		table_list.clear();
		list = jedis.lrange("online", 0 ,-1);
		for (String string : list) {
			table_list.add(new Live(string));
		}
		text_table.setItems(table_list);
		
		home_goals_text.clear();
		list_goals_home = jedis.lrange("goals_home", 0 ,-1);
		for (String string : list_goals_home) {
			home_goals_text.appendText("Goal: " + jedis.hget(string, "goal") + "\n" + "Assist: (" + jedis.hget(string, "assist1") + ", " + jedis.hget(string, "assist2") + " )\n");
		}
		
		vis_goals_text.clear();
		list_goals_vis = jedis.lrange("goals_vis", 0 ,-1);
		for (String string1 : list_goals_vis) {
			vis_goals_text.appendText("Goal: " + jedis.hget(string1, "goal") + "\n" + "Assist: (" + jedis.hget(string1, "assist1") + ", " + jedis.hget(string1, "assist2") + " )\n");
		}
		jedis.save();
	}
}
