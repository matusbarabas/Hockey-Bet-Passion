package hockey;

import java.util.Date;

/**
 * Trieda na pripravu dat z tabulky account.
 * @author Matúš Barabás
 *
 */
public class Statistics_of_Bets {
	private Integer id;
	private String home_team;
	private Integer hs;
	private Integer gs;
	private String note;
	private String guest_team;
	private Integer bet;
	private Double money_bet;
	private Double may_win;
	private String result;
	private Date date_of_bet;

	public Statistics_of_Bets(Integer id, String home_team, Integer hs, Integer gs, String note, String guest_team,
			Integer bet, Double money_bet, Double may_win, String result, Date date_of_bet) {
		super();
		this.id = id;
		this.home_team = home_team;
		this.hs = hs;
		this.gs = gs;
		this.note = note;
		this.guest_team = guest_team;
		this.bet = bet;
		this.money_bet = money_bet;
		this.may_win = may_win;
		this.result = result;
		this.date_of_bet = date_of_bet;
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
	public Integer getBet() {
		return bet;
	}
	public void setBet(Integer bet) {
		this.bet = bet;
	}
	public Double getMoney_bet() {
		return money_bet;
	}
	public void setMoney_bet(Double money_bet) {
		this.money_bet = money_bet;
	}
	public Double getMay_win() {
		return may_win;
	}
	public void setMay_win(Double may_win) {
		this.may_win = may_win;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Date getDate_of_bet() {
		return date_of_bet;
	}
	public void setDate_of_bet(Date date_of_bet) {
		this.date_of_bet = date_of_bet;
	}

	public Integer getHs() {
		return hs;
	}

	public void setHs(Integer hs) {
		this.hs = hs;
	}

	public Integer getGs() {
		return gs;
	}

	public void setGs(Integer gs) {
		this.gs = gs;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
