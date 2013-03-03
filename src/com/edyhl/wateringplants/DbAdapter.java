package com.edyhl.wateringplants;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbAdapter 
{
	public static final String IDDB = "_id";
	public static final String IMAGEDB = "image";
	public static final String NAMEDB = "name";
	public static final String NEXTDATEDB = "nextdate";
	public static final String WATEREDDB = "watered";
	public static final String LASTDATEDB = "lastdate";
	public static final String NOTESDB = "notes";
	public static final String DAYSDB = "days";
	public static final String PLANTEDDB = "planted";
	public static final String CREATEDDB = "created";
	public static final String LASTALARMDB = "lastalarm";
	
	
	private final static long dayInMillisecond = 86400000;
	private final static long minuteInMillisecond = 60000;
//	private static final String TAG = "DbAdapter";
	private static final String DBNAME = "wateringplants";
	private static final String DBTABLE = "plants";
	private static final int DBVERSION = 8;
	private final Context mContext;
	private DbHelper mDbHelper;
	private SQLiteDatabase mSQLiteDatabase;
	
	private static final String CREATEDB = 
	//		"create table plants (_id integer primary key autoincrement, "
	//		+ "image text, name text, watered text, notes text, time text, days text);";
			"create table plants (_id integer primary key autoincrement, image text, name text, nextdate text not null, watered integer not null, lastdate text, notes text, days integer not null, planted text, created text, lastalarm text);";
		
	
	
	
	//1
	private static class DbHelper extends SQLiteOpenHelper 
	{

		//1.1
		DbHelper(Context context) {
			super(context, DBNAME, null, DBVERSION);
		}
		
		//1.2
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATEDB);
			
		}
		
		//1.3
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//	Log.w(TAG, "Warning: Upgrading database from Version " + oldVersion + " to Version " + newVersion 
		//			+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS plants");
			onCreate(db);			
		}
					
	}
	
	
	//2
	public DbAdapter(Context DbContext) 
	{
		this.mContext = DbContext;		
	}
	
	//3
	public DbAdapter open() throws SQLException 
	{
		mDbHelper = new DbHelper(mContext);
		mSQLiteDatabase = mDbHelper.getWritableDatabase();
		return this;		
	}
	
	//4
	public void close() 
	{
		mSQLiteDatabase.close();
		mDbHelper.close();		
	}
	
	//5
	public long createPlant(String image, String name, String nextdate, int watered, String lastdate, String notes, int days, String planted, String created, String lastalarm) 
	{
			ContentValues cv = new ContentValues();
			cv.put(IMAGEDB, image);
			cv.put(NAMEDB, name);
			cv.put(NEXTDATEDB, nextdate);
			cv.put(WATEREDDB, watered);
			cv.put(LASTDATEDB, lastdate);
			cv.put(NOTESDB, notes);
			cv.put(DAYSDB, days);
			cv.put(PLANTEDDB, planted);
			cv.put(CREATEDDB, created);
			cv.put(CREATEDDB, created);
			cv.put(LASTALARMDB, lastalarm);
			
			return mSQLiteDatabase.insert(DBTABLE, null, cv);
			
	}
	
	//6
	public boolean deletePlant(long DbId) 
	{
		return mSQLiteDatabase.delete(DBTABLE, IDDB + "=" + DbId, null) > 0;		
	}
	
	//6.6
	public int deleteAllData() 
	{
		return mSQLiteDatabase.delete(DBTABLE,null,null);
		
	}
			
	//7
	public Cursor getAllPlants(int sortColumnFV, int orderTypeFV, String currentTimeInMillis) 
	{
		switch(sortColumnFV)
		{
		case 1:
			if(orderTypeFV == 2)
				return mSQLiteDatabase.query(
					DBTABLE, 
					new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
					null, null, null, null, "CASE WHEN "+ NEXTDATEDB + " < " + currentTimeInMillis +" THEN 1 ELSE 2 END, " + NEXTDATEDB + " DESC");
			else 
				return mSQLiteDatabase.query(
						DBTABLE, 
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, "CASE WHEN "+ NEXTDATEDB + " >= " + currentTimeInMillis +" THEN 1 ELSE 2 END, " + NEXTDATEDB + " ASC");
		case 2:
			if(orderTypeFV == 1)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, NEXTDATEDB + " ASC");
			else if(orderTypeFV == 2)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, NEXTDATEDB + " DESC");
			else return null;
		case 3:
			if(orderTypeFV == 1)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, CREATEDDB + " ASC");
			else if(orderTypeFV == 2)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, CREATEDDB + " DESC");
			else return null;
		case 4:
			if(orderTypeFV == 1)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, PLANTEDDB + " ASC");
			else if(orderTypeFV == 2)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, PLANTEDDB + " DESC");
			else return null;
		case 5:
			if(orderTypeFV == 1)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, LASTDATEDB + " ASC");
			else if(orderTypeFV == 2)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, LASTDATEDB + " DESC");
			else return null;
		case 6:
			if(orderTypeFV == 1)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, NAMEDB + " COLLATE NOCASE");
			else if(orderTypeFV == 2)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, NAMEDB + " COLLATE NOCASE DESC");
			else return null;
		case 7:
			if(orderTypeFV == 1)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, DAYSDB + " ASC");
			else if(orderTypeFV == 2)
				return mSQLiteDatabase.query(
						DBTABLE,
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						null, null, null, null, DAYSDB + " DESC");
			else return null;
		}
		return mSQLiteDatabase.query(
				DBTABLE, 
				new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
				null, null, null, null, "CASE WHEN "+ NEXTDATEDB + " >= " + currentTimeInMillis +" THEN 1 ELSE 2 END, " + NEXTDATEDB + " ASC");
		
	}
	
	
	//7.1
	public Cursor getPlantsWithinMinutes(int sortColumnFV, int orderTypeFV, String currentTimeInMillis, long minusMinutes, long plusMinutes) 
	{
		long current2 = Long.parseLong(currentTimeInMillis);
			long currentPlusLong = current2 + plusMinutes*minuteInMillisecond;
			long currentMinusLong = current2 - minusMinutes*minuteInMillisecond;
			String currentPlus = String.valueOf(currentPlusLong);
			String currentMinus = String.valueOf(currentMinusLong);
//			long current3 = current2 + minuteInMillisecond;
			current2 = current2 - minuteInMillisecond;
			String currentTimeInMillis2 = String.valueOf(current2);
//			String currentTimeInMillis3 = String.valueOf(current3);
			
			switch(sortColumnFV)
			{
			case 1:
				if(orderTypeFV == 2)
					return mSQLiteDatabase.query(
						DBTABLE, 
						new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
						"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
						+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
						null, null, null, 
						"CASE WHEN "+ NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis 
										+ " THEN 1 ELSE 2 END, " + NEXTDATEDB + " DESC");
		//				"CASE WHEN "+ NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis 
		//				+ " THEN 1 ELSE WHEN " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis
		//				+ " THEN 2 END, " + NEXTDATEDB + " DESC");
				else 
					return mSQLiteDatabase.query(
							DBTABLE, 
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( "+ NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, 
							"CASE WHEN "+ NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis 
							+ " THEN 1 ELSE 2 END, " + NEXTDATEDB + " ASC");
			case 2:
				if(orderTypeFV == 1)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, NEXTDATEDB + " ASC");
				else if(orderTypeFV == 2)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, NEXTDATEDB + " DESC");
				else return null;
			case 3:
				if(orderTypeFV == 1)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, CREATEDDB + " ASC");
				else if(orderTypeFV == 2)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, CREATEDDB + " DESC");
				else return null;
			case 4:
				if(orderTypeFV == 1)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, PLANTEDDB + " ASC");
				else if(orderTypeFV == 2)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, PLANTEDDB + " DESC");
				else return null;
			case 5:
				if(orderTypeFV == 1)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, LASTDATEDB + " ASC");
				else if(orderTypeFV == 2)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, LASTDATEDB + " DESC");
				else return null;
			case 6:
				if(orderTypeFV == 1)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, NAMEDB + " COLLATE NOCASE");
				else if(orderTypeFV == 2)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, NAMEDB + " COLLATE NOCASE DESC");
				else return null;
			case 7:
				if(orderTypeFV == 1)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, DAYSDB + " ASC");
				else if(orderTypeFV == 2)
					return mSQLiteDatabase.query(
							DBTABLE,
							new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
							"( " + NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
							+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
							null, null, null, DAYSDB + " DESC");
				else return null;
			}
			return mSQLiteDatabase.query(
					DBTABLE, 
					new String[] {IDDB, IMAGEDB, NAMEDB, DAYSDB, NEXTDATEDB, WATEREDDB},
					"( "+ NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis2 
					+ " ) OR ( " + LASTALARMDB + " >= " + currentMinus + " AND " + LASTALARMDB + " <= " + currentTimeInMillis + " )",
					null, null, null, 
					"CASE WHEN "+ NEXTDATEDB + " <= " + currentPlus + " AND " + NEXTDATEDB + " >= " + currentTimeInMillis
					+ " THEN 1 ELSE 2 END, " + NEXTDATEDB + " ASC");
			
	}
	
	
	
	//8
	public Cursor getPlant(long DbId) throws SQLException {
		Cursor myCursor = mSQLiteDatabase.query(
				true, 
				DBTABLE, 
				null,
				IDDB + "=" + DbId,
				null, null, null, null, null);								
		if (myCursor != null) 
		{
			myCursor.moveToFirst();			
		}
		return myCursor;
	}
	
	//9
	public boolean updatePlant(long DbId, String image, String name, String nextdate, int watered, String lastdate, String notes, int days, String planted, String created) 
	{	
		ContentValues cv = new ContentValues();
		cv.put(IMAGEDB, image);
		cv.put(NAMEDB, name);
		cv.put(NEXTDATEDB, nextdate);
		cv.put(WATEREDDB, watered);
		cv.put(LASTDATEDB, lastdate);
		cv.put(NOTESDB, notes);
		cv.put(DAYSDB, days);
		cv.put(PLANTEDDB, planted);
		cv.put(CREATEDDB, created);
		return mSQLiteDatabase.update(DBTABLE, cv, IDDB + "=" + DbId, null) > 0;
	}
	
	public boolean updateCV(long DbId, ContentValues cv)
	{
		return mSQLiteDatabase.update(DBTABLE, cv, IDDB + "=" + DbId, null) > 0;
	}
	
	//10
	public boolean updateImage(long DbId, String image)
	{
		ContentValues cv = new ContentValues();
		cv.put(IMAGEDB, image);
		return mSQLiteDatabase.update(DBTABLE, cv, IDDB + "=" + DbId, null) > 0;
	}
	
	//10.1
	public String getImageUri(long DbId)
	{
		Cursor myCursor = mSQLiteDatabase.query(
				true,
				DBTABLE, 
				new String[] {IMAGEDB},
				IDDB + "=" + DbId, null, null, null, null, null);
		if (myCursor != null) 
		{
			myCursor.moveToFirst();			
			String imageUri = myCursor.getString(0);
			myCursor.close();
			return imageUri;
		}
		return null;
	}
	

	//10.2
	public int getIsWatered(long DbId)
	{
		Cursor myCursor = mSQLiteDatabase.query(
				true,
				DBTABLE, 
				new String[] {WATEREDDB},
				IDDB + "=" + DbId, null, null, null, null, null);
		if (myCursor != null) 
		{
			myCursor.moveToFirst();			
			int isWateredFV = myCursor.getInt(0);
			myCursor.close();
			return isWateredFV;
		}
		else return -1;
	}
	
	//10.3
	public String updateLastDateWater(long DbId, String newLastDateLong, int waterIt)
	{
		ContentValues cv = new ContentValues();
		cv.put(WATEREDDB, waterIt);
		cv.put(LASTDATEDB, newLastDateLong);		
		Cursor myCursor = mSQLiteDatabase.query(
				true,
				DBTABLE, 
				new String[] {LASTDATEDB},
				IDDB + "=" + DbId, null, null, null, null, null);
		String oldLastDate = null;
		if (myCursor != null) 
		{
			myCursor.moveToFirst();			
			oldLastDate = myCursor.getString(0);
		
			if(mSQLiteDatabase.update(DBTABLE, cv, IDDB + "=" + DbId, null) > 0)
			{
				myCursor.close();
				return oldLastDate;
			}
			else
			{
				myCursor.close();
				return null;
			}
		}
		return null;
	}

	
	//11.0
	private String getSmallestValueHelper(String fieldName2, long minuteIgnore2)
	{
		Cursor myCursor = mSQLiteDatabase.query(
				DBTABLE, 
				new String[] {IDDB, fieldName2, DAYSDB, WATEREDDB, LASTALARMDB},
				null, null, null, null, fieldName2 + " ASC");
		if(myCursor != null)
		{
			if(myCursor.moveToFirst())
			{
				String smallestValue = myCursor.getString(1);
				long nextdateTempLong = Long.parseLong(smallestValue);
				int daysInterval = myCursor.getInt(2);
		
				int setHour = 0;
				int setMinute = 0;
				int hourTemp = 0;
				int minuteTemp = 0;
				long daysLong = 0;
				Calendar myCalendar = Calendar.getInstance();
				long currentTimeInMillis = myCalendar.getTimeInMillis();
				long currentTimeInMillisMinDelay = currentTimeInMillis + minuteIgnore2*minuteInMillisecond;
				
				while( (daysInterval == 0 && nextdateTempLong <= currentTimeInMillisMinDelay)
						|| nextdateTempLong < 0 
					)
				{
					if( nextdateTempLong < 0)
					{
						nextdateTempLong *= -1;
						if (nextdateTempLong < currentTimeInMillis)
						{
							myCalendar.setTimeInMillis(nextdateTempLong);
							setHour = myCalendar.get(Calendar.HOUR_OF_DAY);
							setMinute = myCalendar.get(Calendar.MINUTE);
							daysLong = Long.parseLong( String.valueOf( myCursor.getInt(2) ) );
							long lastalarmTempLong = nextdateTempLong;
							int nextdatechange = 0;
							
							while(nextdateTempLong < currentTimeInMillis && daysLong > 0)
							{
								lastalarmTempLong = nextdateTempLong;
								nextdateTempLong = nextdateTempLong + daysLong*dayInMillisecond;
								nextdatechange = 1;
								myCalendar.setTimeInMillis(nextdateTempLong);
								hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
								minuteTemp = myCalendar.get(Calendar.MINUTE);
								if(hourTemp < setHour)
								{
									myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp);
								}
								else if (hourTemp > setHour)
								{
									myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp + 24);
								}
								if(minuteTemp < setMinute)
								{	
									myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp);
								}
								else if (minuteTemp > setMinute)
								{
									myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp + 60);
								}
								nextdateTempLong = myCalendar.getTimeInMillis();
							}
							nextdateTempLong *= -1;
							if(nextdatechange == 1)
							{
								ContentValues cv = new ContentValues();
								cv.put( NEXTDATEDB, String.valueOf(nextdateTempLong) );
								cv.put(WATEREDDB, 0);
								cv.put( LASTALARMDB, String.valueOf(lastalarmTempLong) );
								updateCV( Long.parseLong(myCursor.getString(0)), cv );
							}
						}
					}
					else if ( nextdateTempLong <= currentTimeInMillisMinDelay )
					{
						ContentValues cv2 = new ContentValues();
						cv2.put( LASTALARMDB, String.valueOf(nextdateTempLong) );
						updateCV( Long.parseLong(myCursor.getString(0)), cv2 );
					}
					
					
					if( myCursor.moveToNext() )
					{
						daysInterval = myCursor.getInt(2);
						smallestValue = myCursor.getString(1);
						nextdateTempLong = Long.parseLong(smallestValue);
					}
					else
					{
						myCursor.close();
						return null;
					}
				}
				if( nextdateTempLong > currentTimeInMillisMinDelay)
				{
					myCursor.close();
					return String.valueOf(nextdateTempLong);
				}
				else if(daysInterval > 0 && nextdateTempLong >= 0)
				{
					myCalendar.setTimeInMillis(nextdateTempLong);
					setHour = myCalendar.get(Calendar.HOUR_OF_DAY);
					setMinute = myCalendar.get(Calendar.MINUTE);
					daysLong = Long.parseLong( myCursor.getString(2) );
					long lastalarmTempLong = nextdateTempLong;
					
					while(nextdateTempLong <= currentTimeInMillisMinDelay)
					{
						lastalarmTempLong = nextdateTempLong;
						nextdateTempLong = nextdateTempLong + daysLong*dayInMillisecond; 
						myCalendar.setTimeInMillis(nextdateTempLong);
						hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
						minuteTemp = myCalendar.get(Calendar.MINUTE);
						if(hourTemp < setHour)
						{
							myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp);
						}
						else if (hourTemp > setHour)
						{
							myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp + 24);
						}
						if(minuteTemp < setMinute)
						{	
							myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp);
						}
						else if (minuteTemp > setMinute)
						{
							myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp + 60);
						}
						nextdateTempLong = myCalendar.getTimeInMillis();
					}
					ContentValues cv = new ContentValues();
					cv.put( NEXTDATEDB, String.valueOf(nextdateTempLong) );
					cv.put( LASTALARMDB, String.valueOf(lastalarmTempLong) );
					updateCV( Long.parseLong(myCursor.getString(0)), cv );
					myCursor.close();
			 		return getSmallestValueHelper(fieldName2, minuteIgnore2);
				}
			}
			else
			{
				myCursor.close();
				return null;
			}
		}
		myCursor.close();
		return null;		
	}
	
	//11
	public String getSmallestValue(String fieldName, long minuteIgnore)
	{
		Cursor myCursor = mSQLiteDatabase.query(
				DBTABLE, 
				new String[] {IDDB, fieldName, DAYSDB, WATEREDDB},
				null, null, null, null, fieldName + " ASC");
		
 		if(myCursor != null)
		{
			if(myCursor.moveToFirst())
			{
				
			String smallestValue = myCursor.getString(1);
			long nextdateTempLong = Long.parseLong(smallestValue);

			int daysInterval = myCursor.getInt(2);
			
			int setHour = 0;
			int setMinute = 0;
			int hourTemp = 0;
			int minuteTemp = 0;
			long daysLong = 0;
			Calendar myCalendar = Calendar.getInstance();
			long currentTimeInMillis = myCalendar.getTimeInMillis();
			long currentTimeInMillisMinDelay = currentTimeInMillis + minuteIgnore*minuteInMillisecond;
			while( (daysInterval == 0 && nextdateTempLong <= currentTimeInMillisMinDelay ) 
						|| (daysInterval != 0 && nextdateTempLong + minuteInMillisecond >= currentTimeInMillis && nextdateTempLong <= currentTimeInMillis ) 
						|| (daysInterval != 0 && nextdateTempLong <= currentTimeInMillisMinDelay && nextdateTempLong + minuteInMillisecond >= currentTimeInMillis)
						|| nextdateTempLong < 0
					)
			{
				if(nextdateTempLong < 0)
				{
					nextdateTempLong *= -1;
					if (nextdateTempLong < currentTimeInMillis)
					{
						myCalendar.setTimeInMillis(nextdateTempLong);
						setHour = myCalendar.get(Calendar.HOUR_OF_DAY);
						setMinute = myCalendar.get(Calendar.MINUTE);
						daysLong = Long.parseLong( myCursor.getString(2) );
						long lastalarmTempLong = nextdateTempLong;
						int nextdatechange = 0;
						while(nextdateTempLong < currentTimeInMillis && daysLong > 0)
						{
							lastalarmTempLong = nextdateTempLong;
							nextdateTempLong = nextdateTempLong + daysLong*dayInMillisecond;
							nextdatechange = 1;
							myCalendar.setTimeInMillis(nextdateTempLong);
							hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
							minuteTemp = myCalendar.get(Calendar.MINUTE);
							if(hourTemp < setHour)
							{
								myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp);
							}
							else if (hourTemp > setHour)
							{
								myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp + 24);
							}
							if(minuteTemp < setMinute)
							{	
								myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp);
							}
							else if (minuteTemp > setMinute)
							{
								myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp + 60);
							}
							nextdateTempLong = myCalendar.getTimeInMillis();
						}
						
						nextdateTempLong *= -1;
						if (nextdatechange == 1)
						{	
							ContentValues cv = new ContentValues();
							cv.put( NEXTDATEDB, String.valueOf(nextdateTempLong) );
							cv.put(WATEREDDB, 0);
							cv.put( LASTALARMDB, String.valueOf(lastalarmTempLong) );
							updateCV( Long.parseLong(myCursor.getString(0)), cv );
						}
					}
				}
				else if ( nextdateTempLong <= currentTimeInMillisMinDelay )
				{
					ContentValues cv2 = new ContentValues();
					cv2.put( LASTALARMDB, String.valueOf(nextdateTempLong) );
					updateCV( Long.parseLong(myCursor.getString(0)), cv2 );
				}
				
				
				
				//	if(daysInterval != 0 && nextdateTempLong + 60000 < Calendar.getInstance().getTimeInMillis())
				if( myCursor.moveToNext() )
				{
					daysInterval = myCursor.getInt(2);
					smallestValue = myCursor.getString(1);
					nextdateTempLong = Long.parseLong(smallestValue);
				}
				else //if no more cursor, get the smallest positive nextdate, update with interval, set the alarm with that nextdate.
				{
					myCursor.close();
					return getSmallestValueHelper(fieldName, minuteIgnore);
				}
			}
			
			if(daysInterval != 0 && nextdateTempLong >= 0 && nextdateTempLong < currentTimeInMillis - minuteInMillisecond)
			{
				myCalendar.setTimeInMillis( nextdateTempLong );
				setHour = myCalendar.get(Calendar.HOUR_OF_DAY);
				setMinute = myCalendar.get(Calendar.MINUTE);
				daysLong = Long.parseLong( myCursor.getString(2) );
				long lastalarmTempLong = nextdateTempLong;
				
				while(nextdateTempLong < currentTimeInMillis - minuteInMillisecond)
				{
					lastalarmTempLong = nextdateTempLong;
					nextdateTempLong = nextdateTempLong + daysLong*dayInMillisecond; 
					
				 	myCalendar.setTimeInMillis(nextdateTempLong);
				 	hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
				 	minuteTemp = myCalendar.get(Calendar.MINUTE);
				 	if(hourTemp < setHour)
				 	{
				 		myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp);
				 	}
				 	else if (hourTemp > setHour)
				 	{
				 		myCalendar.add(Calendar.HOUR_OF_DAY, setHour - hourTemp + 24);
				 	}
				 	if(minuteTemp < setMinute)
				 	{
				 		myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp);
				 	}
				 	else if (minuteTemp > setMinute)
				 	{
				 		myCalendar.add(Calendar.MINUTE, setMinute - minuteTemp + 60);
				 	}
				 	nextdateTempLong = myCalendar.getTimeInMillis();
				 	
				}
				 	
				 	ContentValues cv = new ContentValues();
					cv.put( NEXTDATEDB, String.valueOf(nextdateTempLong) );
			 		cv.put(WATEREDDB, 0);
			 		cv.put( LASTALARMDB, String.valueOf(lastalarmTempLong) );
			 		updateCV( Long.parseLong(myCursor.getString(0)), cv );
			 		myCursor.close();
			 		return getSmallestValue(fieldName, minuteIgnore);
			}
			else if(nextdateTempLong >= currentTimeInMillisMinDelay)
			{
					myCursor.close();
					return String.valueOf(nextdateTempLong);
			}
			else
			{
					myCursor.close();
					return null;
			}
			}
		}
 		
			myCursor.close();
			return null;
	}
				
	//12			
}
