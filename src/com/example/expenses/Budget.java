package com.example.expenses;

public class Budget {

	private int id;
	private double wk_days;
	private double wk_ends;
	private double budget;
	private double expenses;
	private String month_year;
	private double tmp;
	
	public Budget(){
		this.wk_days = this.wk_ends = this.budget = this.expenses = this.tmp = 0;
		this.month_year = new String();
		this.id = 0;
	}
	
	//setters
	public void setId(int id){
		this.id = id;
	}
	
	public void setWkDays(double wk_days){
		this.wk_days = wk_days;
	}
	
	public void setWkEnds(double wk_ends){
		this.wk_ends = wk_ends;
	}
	
	public void setBudget(double budget){
		this.budget = budget;
	}
	
	public void setExpenses(double expenses){
		this.expenses = expenses;
	}
	
	
	public void setTmp(double tmp){
		this.tmp = tmp;
	}
	
	public void setMonthYear(String month_year){
		this.month_year = month_year;
	}
	
	//getters
	public int getId(){
		return this.id;
	}
	
	public double getWkDays(){
		return this.wk_days;
	}
	
	public double getWkEnds(){
		return this.wk_ends;
	}
	
	public double getBudget(){
		return this.budget;
	}
	
	public double getExpenses(){
		return this.expenses;
	}
	
	public double getTmp(){
		return this.tmp;
	}
	
	public String getMonthYear(){
		return this.month_year;
	}
	
	
	
}//end class
