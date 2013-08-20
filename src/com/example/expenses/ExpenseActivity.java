package com.example.expenses;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ExpenseActivity extends Activity implements OnClickListener {
	
	

	//Button
	public Button minus, plus, edit_budget;
	
	//TextView
	public TextView ex_budget, ex_expenses, ex_remaining, ex_notice, list_budgets;
	
	//EditText
	public EditText tmp;
	
	//declare instance variable
	public Budget b = new Budget();
	
	//currency format
	NumberFormat df = NumberFormat.getCurrencyInstance();
	
	//db handler
	DatabaseHandler db = new DatabaseHandler(this);
	
	public static Calendar cal = Calendar.getInstance();
	public String month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
	
	
	public static final String EXTRA_EDIT_MAIN = new String("expenseActivity");

	private static final String EXTRA_MONTH_YEAR = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
	
	//declare Intent here
	Intent intent;
	
	public boolean hit_button = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_expense);
		this.setContentView();
	}//end onCreate
	
	@Override
	protected void onStart(){
		super.onStart();
		this.isNewMonth();
	}//end onStart
	
	@Override
	protected void onResume(){
		super.onResume();
		this.isNewMonth();
	}//end redirectTo
	
	@Override
	protected void onPause(){
		super.onPause();
		//month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
		//if ( !db.isExistMonthYear(month_year) ){
		if ( !hit_button )
			android.os.Process.killProcess(android.os.Process.myPid());
		hit_button = false;
		//}
	}//end

	/**
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return super.onKeyDown(keyCode, event);
	}//end onKeyDown
	**/
	
	
	//if newMonth it will render the main activity
	public void isNewMonth(){
		month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
		if ( !db.isExistMonthYear(month_year) ){
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}//end if
	}//end redirectTo
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense, menu);
		return true;
	}
	
	public void setContentView(){
		//set button 
		this.setExpenseButton();
		
		
		//set budget through DatabaseHelper
		if (db.isExistMonthYear(month_year))
			b = db.getBudget(0, month_year);
		else b = new Budget();
		
		double expenses = b.getExpenses();
		double budget = b.getBudget();
		double remaining_budget = budget - expenses;
		
		ex_budget = (TextView) findViewById(R.id.ex_budget);
		ex_expenses = (TextView) findViewById(R.id.ex_expenses);
		ex_remaining = (TextView) findViewById(R.id.ex_remaining);
		
		
		
		ex_budget.setText( df.format(budget));
		ex_expenses.setText( df.format(expenses));
		ex_remaining.setText( df.format(remaining_budget));
	
		//Debugging purposes if has list in table
		List<Budget> budgets = db.getAllBudget();
		
		for(Budget bs : budgets){
			String log = "ID:"+bs.getId()+" BUDGET:"+bs.getBudget()+" MONTH_YEAR:"+bs.getMonthYear()+" EXPENSES:"+bs.getExpenses()+" WK_DAYS:"+bs.getWkDays()+" WK_ENDS:"+bs.getWkEnds();
			Log.d("expenses ~ "+log, log);
		}//end for
	}//end setContentView
	
	public void setExpenseButton(){
		plus = (Button) findViewById(R.id.plus);
		minus = (Button) findViewById(R.id.minus);
		edit_budget = (Button) findViewById(R.id.edit_budget);
		list_budgets = (Button) findViewById(R.id.list_budgets);
		
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		edit_budget.setOnClickListener(this);
		list_budgets.setOnClickListener(this);
	}//end setExpenseButton

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.plus:
				this.calculateExpenses('+');
				break;
			case R.id.minus:
				this.calculateExpenses('-');
				break;
			case R.id.edit_budget:
				hit_button = true;
				this.to('m');
				break;
			case R.id.list_budgets:
				hit_button = true;
				this.to('l');
				break;
		}//switch
	}//end onClick
	
	//@option { m = main | l = list }
	public void to(char option){
		
		if (option == 'm'){
			intent = new Intent(this, MainActivity.class);
			intent.putExtra(EXTRA_EDIT_MAIN, "expenseActivity");
		}
		if (option == 'l')
			intent = new Intent(this, ListsActivity.class);
		
		intent.putExtra(EXTRA_MONTH_YEAR, month_year);
		startActivity(intent);
	}//end
	
	
	public void calculateExpenses(char option){
		
		try{
			double tmp_tmp = 0;
			tmp = (EditText) findViewById(R.id.tmp);
			
			tmp_tmp = Double.parseDouble(tmp.getText().toString());
			
			tmp.setText("");
			

			ex_expenses = (TextView) findViewById(R.id.ex_expenses);
			ex_remaining = (TextView) findViewById(R.id.ex_remaining);
			
			double tmp_expenses = 0, tmp_remaining = 0;
			if (option == '+')
				tmp_expenses  = b.getExpenses() + tmp_tmp;
			else
				tmp_expenses = b.getExpenses() - tmp_tmp;
			
			tmp_remaining = b.getBudget() - tmp_expenses;
			
			//set expenses in budget class
			b.setExpenses(tmp_expenses);
			
			//set to ui
			ex_expenses.setText( df.format(tmp_expenses));
			ex_remaining.setText( df.format(tmp_remaining));
			
			//update in row in table
			db.updateBudget(b);
		}catch(Exception e){
			ex_notice = (TextView) findViewById(R.id.ex_notice);
			ex_notice.setText("please, input an amount.");
		}
	}//end calculateExpense
	

}//end class
