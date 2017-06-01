package hockey;

/**
 * Trieda na pripravu dat z tabulky betting_events.
 * @author Matúš Barabás
 *
 */

public class Betting_events {
	private Integer id;
	private String home_team;
	private String guest_team;
	private Double h_line;
	private Double d_line;
	private Double v_line;
	
	public Betting_events(Integer id, String home_team, String guest_team, Double h_line, Double d_line, Double v_line) {
		super();
		this.id = id;
		this.home_team = home_team;
		this.guest_team = guest_team;
		this.h_line = h_line;
		this.d_line = d_line;
		this.v_line = v_line;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHome_team() {
		return home_team;
	}
	public void setHome_team(String home_team) {
		this.home_team = home_team;
	}
	public String getGuest_team() {
		return guest_team;
	}
	public void setGuest_team(String guest_team) {
		this.guest_team = guest_team;
	}
	public Double getH_line() {
		return h_line;
	}
	public void setH_line(Double h_line) {
		this.h_line = h_line;
	}
	public Double getD_line() {
		return d_line;
	}
	public void setD_line(Double d_line) {
		this.d_line = d_line;
	}
	public Double getV_line() {
		return v_line;
	}
	public void setV_line(Double v_line) {
		this.v_line = v_line;
	}
}
