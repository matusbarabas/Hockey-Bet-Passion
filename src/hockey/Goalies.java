package hockey;

/**
 * Trieda na pripravu dat z tabulky goalies.
 * @author Matúš Barabás
 *
 */
public class Goalies {
	private String name;
	private Integer age;
	private String team;
	private Integer gp;
	private Integer w;
	private Integer l;
	private Integer ga;
	private Integer sa;
	private Integer saves;
	private Double s_p;
	private Integer shutout;
	private Integer goals;
	private Integer assist;
	private Integer points;
	private Integer pm;
	
	public Goalies(String name, Integer age, String team, Integer gp, Integer w, Integer l, Integer ga, Integer sa,
			Integer saves, Double s_p, Integer shutout, Integer goals, Integer assist, Integer points, Integer pm) {
		super();
		this.name = name;
		this.age = age;
		this.team = team;
		this.gp = gp;
		this.w = w;
		this.l = l;
		this.ga = ga;
		this.sa = sa;
		this.saves = saves;
		this.s_p = s_p;
		this.shutout = shutout;
		this.goals = goals;
		this.assist = assist;
		this.points = points;
		this.pm = pm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Integer getGp() {
		return gp;
	}

	public void setGp(Integer gp) {
		this.gp = gp;
	}

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getL() {
		return l;
	}

	public void setL(Integer l) {
		this.l = l;
	}

	public Integer getGa() {
		return ga;
	}

	public void setGa(Integer ga) {
		this.ga = ga;
	}

	public Integer getSa() {
		return sa;
	}

	public void setSa(Integer sa) {
		this.sa = sa;
	}

	public Integer getSaves() {
		return saves;
	}

	public void setSaves(Integer saves) {
		this.saves = saves;
	}

	public Double getS_p() {
		return s_p;
	}

	public void setS_p(Double s_p) {
		this.s_p = s_p;
	}

	public Integer getShutout() {
		return shutout;
	}

	public void setShutout(Integer shutout) {
		this.shutout = shutout;
	}

	public Integer getGoals() {
		return goals;
	}

	public void setGoals(Integer goals) {
		this.goals = goals;
	}

	public Integer getAssist() {
		return assist;
	}

	public void setAssist(Integer assist) {
		this.assist = assist;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getPm() {
		return pm;
	}

	public void setPm(Integer pm) {
		this.pm = pm;
	}
	
	
}
