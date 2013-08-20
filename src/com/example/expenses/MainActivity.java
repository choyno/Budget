package com.example.expenses;

import java.text.NumberFormat;
import java.util.Calendar;

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

public class MainActivity extends Activity implements OnClickListener {
	
	//activity_main.xml
	public Button calculate, reset;
	public EditText wk_days, wk_ends, budget;
	
	public TextView notice;
	
	//declare instance variable
	public Budget b = new Budget();
	
	//db handler
	DatabaseHandler db = new DatabaseHandler(this);
	
	public Calendar cal = Calendar.getInstance();
	public String month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
	
	//declare Intent here
	Intent intent;
	
	//currency format
	NumberFormat df = NumberFormat.getCurrencyInstance();
	
	public boolean hit_button = false;
	
	public double expAddOns = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
		//db.deleteBudget(1);
		//redirect to second page which is expenseActivity if already exist in the list
				
		intent = getIntent();
		String expenseActivity = intent.getStringExtra(ExpenseActivity.EXTRA_EDIT_MAIN);
		
		if (!db.isExistMonthYear(month_year))
			this.setContentView();
		else if (db.isExistMonthYear(month_year) &&  ( expenseActivity != null  && expenseActivity.equals("expenseActivity") )  )
			this.setContentView();
		else
			this.to();
	}//end on create
	
	public void setMainButton(){
		calculate = (Button) findViewById(R.id.calculate);
		reset = (Button) findViewById(R.id.reset);
		
		calculate.setOnClickListener(this);
		
	}//end setMainButton

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	@Override
	protected void onPause(){
		super.onPause();
		//if (!db.isExistMonthYear(month_year))
		if ( !hit_button )
			android.os.Process.killProcess(android.os.Process.myPid());
		
		hit_button = false;
	}//end
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.calculate:
				hit_button = false;
				this.calculate();
				break;
			case R.id.reset:
				this.resetData();
				hit_button = false;
				break;
		}//end switch
	}//end 
	
	public void resetData(){
		//as if reset but rather delete :-)
		db.deleteBudget(b.getId());
		b = new Budget();
		this.setContentView();
	}//end resetData
	
	public void to(){
		intent = new Intent(this, ExpenseActivity.class);
		intent.putExtra("EXTRA_MONTH_YEAR", month_year);
		startActivity(intent);
	}//end to
	
	public void setContentView(){
		setContentView(R.layout.activity_main);
		
		this.setMainButton(); //init button first
		
		reset.setVisibility(View.INVISIBLE); //set default to invisible reset button
		
		if (!db.isExistMonthYear(month_year)) return;
		
		b = db.getBudget(0, month_year);
			
		notice = (TextView) findViewById(R.id.notice);
		budget = (EditText) findViewById(R.id.budget);
		wk_days = (EditText) findViewById(R.id.wk_days);
		wk_ends = (EditText) findViewById(R.id.wk_ends);
		
		budget.setText(Double.toString(b.getBudget()));
		wk_days.setText(Double.toString(b.getWkDays()));
		wk_ends.setText(Double.toString(b.getWkEnds()));
		
		expAddOns = b.getExpenses() - (((b.getWkDays()*5) + (b.getWkEnds()*2)) * 4);
		
		
		if ( expAddOns == 0 ) return;
		
		calculate.setText("Continue");
		notice.setText("Exp. Add-ons: "+df.format( expAddOns ));
		reset.setVisibility(View.VISIBLE);
		
		//setOnClickListener
		reset.setOnClickListener(this);
	}//end setLayoutMain
	
	public void calculate(){
		
		budget = (EditText) findViewById(R.id.budget);
		wk_days = (EditText) findViewById(R.id.wk_days);
		wk_ends = (EditText) findViewById(R.id.wk_ends);

		try{
			
			double tmp_wk_days = 0, tmp_wk_ends = 0, tmp_budget = 0;
			double expenses = 0;
			
			String str_wk_days = wk_days.getText().toString();
			String str_wk_ends = wk_ends.getText().toString();


			tmp_budget = Double.parseDouble( budget.getText().toString() );
			tmp_wk_days = str_wk_days.equals("") ? 0 : Double.parseDouble( str_wk_days );
			tmp_wk_ends = str_wk_ends.equals("") ? 0 : Double.parseDouble( str_wk_ends );
			
			expenses = (( tmp_wk_days * 5 ) + ( tmp_wk_ends * 2 )) * 4;
			
			//conditions here determine if month_year exist in db
			if ( db.isExistMonthYear(month_year)){
				b = db.getBudget(0, month_year);
				
				//first check the additional add | minus expense [ in table e.g 082013 expenses = (wk_ends + wk_days)  ]
				//the result of this is to add the current expenses 
				expenses +=	b.getExpenses() - (((b.getWkDays()*5) + (b.getWkEnds()*2)) * 4);		
								
				String str_str = "New Expense:"+expenses+", old_expense:"+b.getExpenses()+" old-wk-days:"+b.getWkDays()+", old-wk-ends:"+b.getWkEnds();
				Log.d(" expe_value :", str_str);
				
				this.setBudgetMember(tmp_budget, tmp_wk_days, tmp_wk_ends, expenses);
				db.updateBudget(b);
			}
			else{ 
				this.setBudgetMember(tmp_budget, tmp_wk_days, tmp_wk_ends, expenses);
				db.addExpenses(b);
			}//end else
			
			
			//call other activity
			this.to();
			
		}catch(Exception e){
			notice = (TextView) findViewById(R.id.notice);
			notice.setText("Budget must have an amount.");
		}//end
		
	}//end calculate
	
	
	public void setBudgetMember(double sBudget, double sWkDays, double sWkEnds, double sExpenses){
		b.setBudget(sBudget);
		b.setWkDays(sWkDays);
		b.setWkEnds(sWkEnds);
		b.setExpenses(sExpenses);
		b.setMonthYear(month_year);
	}//end setBudgetMember
	
	
	
	

}//end classs
