package hockey;

/**
 * Trieda na pripravu dat z tabulky teams.
 * @author Matúš Barabás
 *
 */
public class Teams {
	private String name;
	private String conference;
	private String devision;
	
	public Teams(String name, String conference, String devision) {
		super();
		this.name = name;
		this.conference = conference;
		this.devision = devision;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public String getDevision() {
		return devision;
	}

	public void setDevision(String devision) {
		this.devision = devision;
	}
	
	
}
