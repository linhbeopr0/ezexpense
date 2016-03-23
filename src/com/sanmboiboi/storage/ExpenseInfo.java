package com.sanmboiboi.storage;

public class ExpenseInfo {
	int id;
	String category;
	double balance;
	String strPeopleParticipate;
	String path;
	String date;
	String note;

	public ExpenseInfo() {
		id = 0;
		category = "";
		balance = 0;
		note = "";
		strPeopleParticipate = ""; // distinguish by "/"
		path = "";
		date = ""; // distinguish by "/"
	}

	public ExpenseInfo(int i, String c, double m, String str, String p,
			String d, String n) {
		id = i;
		category = c;
		balance = m;
		strPeopleParticipate = str;
		path = p;
		date = d;
		note = n;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getStrPeopleParticipate() {
		return strPeopleParticipate;
	}

	public void setStrPeopleParticipate(String strPeopleParticipate) {
		this.strPeopleParticipate = strPeopleParticipate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
