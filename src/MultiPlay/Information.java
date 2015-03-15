package MultiPlay;

public class Information {
	String IP = null, name, sex, age,password,level,rate;
	int Id,score, win, lose;

	void setIP(String ip) {
		IP = ip;
	}

	String getIP() {
		return IP;
	}

	void setname(String name) {
		this.IP = name;
	}

	String getname() {
		return name;
	}
	void setsex(String sex) {
		this.sex = sex;
	}

	String getsex() {
		return sex;
	}
	void setage(String age) {
		this.age = age;
	}

	String getage() {
		return age;
	}
	void setpassword(String pw) {
		this.password = pw;
	}

	String getpassword() {
		return password;
	}
	void setscore(int score) {
		this.score=score;
	}

	int getscore() {
		return score;
	}
	void setwin(int win) {
		this.win = win;
	}

	int getwin() {
		return win;
	}
	void setlose(int lose) {
		this.lose = lose;
	}

	int getlose() {
		return lose;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}


	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
}
