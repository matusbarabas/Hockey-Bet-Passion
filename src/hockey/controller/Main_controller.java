package hockey.controller;

import java.io.IOException;
import hockey.Main;
import javafx.fxml.FXML;

/**
 * Trieda, ktora je kontrolerom pre hlavnu obrazovku.
 * Vola metody inych tried na zobrazenie danach okien.
 * @author Matúš Barabás
 *
 */
public class Main_controller {
	
	private Main main;
	
	@FXML
	private void go_players() throws IOException{
		Players_controller.show_players();
	}
	
	@FXML
	private void go_teams() throws IOException{
		Teams_controller.show_teams();
	}
	
	@FXML
	private void go_matches() throws IOException{
		Matches_controller.show_matches();
	}
	
	@FXML
	private void go_main() throws IOException{
		main.show_main_window(1);
	}
	
	@FXML
	private void go_account() throws IOException{
		Account_controller.show_account();
	}
	
	@FXML
	private void go_life() throws IOException{
		Live_controller.show_life();
	}
	
	@FXML
	private void go_slovencina() throws IOException{
		hockey.Main.show_main_window(2);
	}
	
	@FXML
	private void go_english() throws IOException{
		hockey.Main.show_main_window(1);
	}
}
