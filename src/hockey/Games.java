package hockey;

import java.util.Date;

/**
 * Trieda na ukladanie dat z tabulky games_14_15.
 * @author Matúš Barabás
 *
 */
public class Games {
	private Integer match_id;
	private String home_team;
	private Integer home_score;
	private Integer visitor_score;
	private String visitor_team;
	private String ot_os;
	private Date date_of_match;
	
	
	public Games(Integer match_id, String home_team, Integer home_score, Integer visitor_score, String visitor_team, String ot_os, Date date_of_match) {
		super();
		this.match_id = match_id;
		this.home_team = home_team;
		this.home_score = home_score;
		this.visitor_score = visitor_score;
		this.visitor_team = visitor_team;
		this.ot_os = ot_os;
		this.date_of_match = date_of_match;
	}


	public String getHome_team() {
		return home_team;
	}


	public void setHome_team(String home_team) {
		this.home_team = home_team;
	}


	public Integer getHome_score() {
		return home_score;
	}


	public void setHome_score(Integer home_score) {
		this.home_score = home_score;
	}


	public Integer getVisitor_score() {
		return visitor_score;
	}


	public void setVisitor_score(Integer visitor_score) {
		this.visitor_score = visitor_score;
	}


	public String getVisitor_team() {
		return visitor_team;
	}


	public void setVisitor_team(String visitor_team) {
		this.visitor_team = visitor_team;
	}


	public String getOt_os() {
		return ot_os;
	}


	public void setOt_os(String ot_os) {
		this.ot_os = ot_os;
	}


	public Date getDate_of_match() {
		return date_of_match;
	}


	public void setDate_of_match(Date date_of_match) {
		this.date_of_match = date_of_match;
	}


	public Integer getMatch_id() {
		return match_id;
	}


	public void setMatch_id(Integer match_id) {
		this.match_id = match_id;
	}
	
}
