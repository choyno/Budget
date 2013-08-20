package com.example.expenses;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ListsActivity extends Activity{

		//declare instance variable
		public Budget b = new Budget();
		
		//currency format
		NumberFormat df = NumberFormat.getCurrencyInstance();
		
		//db handler
		DatabaseHandler db = new DatabaseHandler(this);
		
		public TableLayout tl;
		public TableRow tr;
		
		public Calendar cal = Calendar.getInstance();
		public String month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
		
		//declare Intent here
		Intent intent;
		
		
		//list 
		//month year total expense savings
		public Button back;
		public TextView tvMonth, tvYear, tvTotal, tvExpense, tvSavings;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_lists);
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
	
	//if newMonth it will render the main activity
	public void isNewMonth(){
		month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
		if ( !db.isExistMonthYear(month_year) ){
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}//end if
	}//end redirectTo
	
	@Override
	protected void onPause(){
		super.onPause();
		//month_year = new String((cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR) );
		//if ( !db.isExistMonthYear(month_year) ){
		android.os.Process.killProcess(android.os.Process.myPid());
		//}
	}//end
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}//end onCreateOptions
	
	
	public void setContentView(){
		tl = (TableLayout) findViewById(R.id.maintable);
		addHeaders();
		addData();
	}//end setContentView

	
	//month year total expense savings
		public void addHeaders(){
			
			tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT
					));
			
			
			//Month
			tvMonth = new TextView(this);
			tvMonth.setText("Month");
			tvMonth.setTextColor(Color.GREEN);
			tvMonth.setPadding(5, 5, 5, 0);
			
			//Year
			tvYear = new TextView(this);
			tvYear.setText("Year");
			tvYear.setTextColor(Color.GREEN);
			tvYear.setPadding(5, 5, 5, 0);
			
			//Total
			tvTotal = new TextView(this);
			tvTotal.setText("Total");
			tvTotal.setTextColor(Color.GREEN);
			tvTotal.setPadding(5, 5, 5, 0);
			
			//Expense
			tvExpense = new TextView(this);
			tvExpense.setText("Expense");
			tvExpense.setTextColor(Color.GREEN);
			tvExpense.setPadding(5, 5, 5, 0);
			
			//Saving
			tvSavings = new TextView(this);
			tvSavings.setText("Savings");
			tvSavings.setTextColor(Color.GREEN);
			tvSavings.setPadding(5, 5, 5, 0);
			
			tr.addView(tvMonth);
			tr.addView(tvYear);
			tr.addView(tvTotal);
			tr.addView(tvExpense);
			tr.addView(tvSavings);
			
			//add table row to tablelayout
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT
					) );
			
		}//end addHeaders
		
		public void addData(){

			List<Budget> budgets = db.getAllBudget();
			
			String[] monthStr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
			
			for(Budget bs : budgets){
				
				tr = new TableRow(this);
				tr.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT
						));
				
				String m = bs.getMonthYear().toString().substring( 0, bs.getMonthYear().length() == 5 ? 1 : 2 );
				String y = bs.getMonthYear().substring( bs.getMonthYear().length() == 5 ? 1 : 2 );
				//Month
				tvMonth = new TextView(this);
				tvMonth.setText(monthStr[Integer.parseInt(m)-1]);
				tvMonth.setPadding(5, 5, 5, 0);
				
				//Year
				tvYear = new TextView(this);
				tvYear.setText(y);
				tvYear.setPadding(5, 5, 5, 0);
				
				//Total
				tvTotal = new TextView(this);
				tvTotal.setText(df.format(bs.getBudget()));
				tvTotal.setPadding(5, 5, 5, 0);
				
				//Expense
				tvExpense = new TextView(this);
				tvExpense.setText(df.format(bs.getExpenses()));
				tvExpense.setPadding(5, 5, 5, 0);
				
				//Saving
				tvSavings = new TextView(this);
				tvSavings.setText(df.format(bs.getBudget() - bs.getExpenses()));
				tvSavings.setPadding(5, 5, 5, 0);
				
				tr.addView(tvMonth);
				tr.addView(tvYear);
				tr.addView(tvTotal);
				tr.addView(tvExpense);
				tr.addView(tvSavings);
				
				//add table row to tablelayout
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT
						) );
			}//for	
		}//end addData
}//end class
