package com.example.expenses;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	//db version
	private static final int VERSION = 1;
	
	//DB NAME
	private static final String DB_NAME = new String("expensesManager");
	
	//expenses table name && its data type..
	private static final String EXPENSES_TABLE = new String("expenses");
	
	//expenses column
	private static final String ID = new String("id");
	private static final String BUDGET = new String("budget");
	private static final String MONTH_YEAR = new String("month_year");
	private static final String TOTAL = new String("total");
	private static final String WK_DAYS = new String("wk_days");
	private static final String WK_ENDS = new String("wk_ends");
	
	
	
	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {		
		String CREATE_EXPENSES_TABLE = "CREATE TABLE "+EXPENSES_TABLE+" ( "+ID+" INTEGER PRIMARY KEY , "+BUDGET+" REAL , "+MONTH_YEAR+" TEXT , "+TOTAL+" REAL , "+WK_DAYS+" REAL , "+WK_ENDS+" REAL)";
		db.execSQL(CREATE_EXPENSES_TABLE);
	}//end onCreate

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
		db.execSQL("DROP TABLE IF EXISTS"+EXPENSES_TABLE);
		
		//create table again
		onCreate(db);
	}//end onUpgrad
	
	//operation
	
	public void addExpenses(Budget budget){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(BUDGET, budget.getBudget());
		values.put(MONTH_YEAR, budget.getMonthYear());
		values.put(TOTAL, budget.getExpenses());
		values.put(WK_DAYS, budget.getWkDays());
		values.put(WK_ENDS, budget.getWkEnds());
		
		//inserting row
		db.insert(EXPENSES_TABLE, null, values);
		db.close();
	}//end addExpenses
	
	
	//check if already have log on month_year
	public boolean isExistMonthYear(String month_year){
		SQLiteDatabase db = this.getReadableDatabase();
		
		//String query = "Select * from "+EXPENSES_TABLE+" where month_year = '"+month_year+"'";
		//Cursor cursor = db.rawQuery(query, null);
		String where_str = MONTH_YEAR+" = "+month_year;
		Cursor cursor = db.query(EXPENSES_TABLE, null, where_str, null, null, null, null);
		
		return cursor.getCount() == 0 ? false : true;
	}//end getExpenses
	
	//get expenses by its month_year
	public Budget getBudget(int id, String month_year){
		SQLiteDatabase db = this.getReadableDatabase();

		String query = new String("Select * from "+EXPENSES_TABLE+" ");
		if (month_year.equals("") || id != 0) 
			query += " where id ="+id;
		else
			query += " where month_year = '"+month_year+"'";
		
		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		Budget b = new Budget();
		
		b.setId(Integer.parseInt(cursor.getString(0)));
		b.setBudget( Double.parseDouble(cursor.getString(1)) );
		b.setMonthYear( cursor.getString(2) );
		b.setExpenses( Double.parseDouble( cursor.getString(3) ) );
		b.setWkDays( Double.parseDouble( cursor.getString( 4 ) ) );
		b.setWkEnds( Double.parseDouble( cursor.getString(5) ) );
		
		return b;
	}//end
	
	public int updateBudget(Budget b){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(BUDGET, b.getBudget());
		values.put(TOTAL, b.getExpenses());
		values.put(WK_DAYS, b.getWkDays());
		values.put(WK_ENDS, b.getWkEnds());
		
		
		return db.update(EXPENSES_TABLE, values, MONTH_YEAR+"=?", new String[] { b.getMonthYear() });
	}//end updateBudget
	
	
	
	public List<Budget> getAllBudget(){
		List<Budget> budgets = new ArrayList<Budget>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "Select * from "+EXPENSES_TABLE;
		
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()){
			do{
				Budget b = new Budget();
				
				b.setId(Integer.parseInt(cursor.getString(0)));
				b.setBudget( Double.parseDouble(cursor.getString(1)) );
				b.setMonthYear( cursor.getString(2) );
				b.setExpenses( Double.parseDouble( cursor.getString(3) ) );
				b.setWkDays( Double.parseDouble( cursor.getString( 4 ) ) );
				b.setWkEnds( Double.parseDouble( cursor.getString(5) ) );
				
				budgets.add(b);
			}while(cursor.moveToNext());
		}//if
		
		
		return budgets;
	}//end getAllBudget
	
	public void deleteBudget(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(EXPENSES_TABLE, ID+"=?", new String[] { String.valueOf(id)});
		db.close();
	}//end
	
	
	

}//end class
