package hockey;

import java.sql.Date;

/**
 * Trieda na pripravu dat z tabulky players_14_15
 * @author Matúš Barabás
 *
 */
public class Players {
	private String p_name;
	private String p_team;
	private Date date_f;
	private Date date_t;
	private String p_pos;
	private Integer p_goals;
	private Integer p_assist;
	private Integer p_points;
	
	public Players(String p_name, String p_team, Date date_f, Date date_t, String p_pos, Integer p_goals,
			Integer p_assist, Integer p_points) {
		super();
		this.p_name = p_name;
		this.p_team = p_team;
		this.date_f = date_f;
		this.date_t = date_t;
		this.p_pos = p_pos;
		this.p_goals = p_goals;
		this.p_assist = p_assist;
		this.p_points = p_points;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getP_team() {
		return p_team;
	}

	public void setP_team(String p_team) {
		this.p_team = p_team;
	}

	public Date getDate_f() {
		return date_f;
	}

	public void setDate_f(Date date_f) {
		this.date_f = date_f;
	}

	public Date getDate_t() {
		return date_t;
	}

	public void setDate_t(Date date_t) {
		this.date_t = date_t;
	}

	public String getP_pos() {
		return p_pos;
	}

	public void setP_pos(String p_pos) {
		this.p_pos = p_pos;
	}

	public Integer getP_goals() {
		return p_goals;
	}

	public void setP_goals(Integer p_goals) {
		this.p_goals = p_goals;
	}

	public Integer getP_assist() {
		return p_assist;
	}

	public void setP_assist(Integer p_assist) {
		this.p_assist = p_assist;
	}

	public Integer getP_points() {
		return p_points;
	}

	public void setP_points(Integer p_points) {
		this.p_points = p_points;
	}

}
