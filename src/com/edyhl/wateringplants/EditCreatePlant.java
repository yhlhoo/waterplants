package com.edyhl.wateringplants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditCreatePlant extends Activity 
{
	static final int DATEDIALOG = 1;
	static final int TIMEDIALOG = 2;
	static final int NEXTDATEDIALOG = 3;
	

	
	public static final int PHOTO_ID = Menu.FIRST;
	public static final int GALLERY_ID = Menu.FIRST+1;
	public static final int DELETE_ID = Menu.FIRST+2;
	public static final int TIME_ID = Menu.FIRST+3;
	
	public static final int CAMERA = 3;
	public static final int GALLERY = 4;
	
	//private String imageUri;
	private final String na = "N/A";
	private final static long dayInMillisecond = 86400000;
//	private final static long hourInMillisecond = 3600000; //
	private final static int paintFlagLine = Paint.STRIKE_THRU_TEXT_FLAG;
	private int paintFlag = 257;
	private Calendar myCalendar = Calendar.getInstance();
	private long currentCalendarInMillis = myCalendar.getTimeInMillis();
 	private int myYear;
 	private int myMonth;
 	private int myDay;
 	private int myHour;
 	private int myMinute;
 	private int setYear;
 	private int setMonth;
 	private int setDay;
 	private int setHour;
 	private int setMinute;
 	private Uri noImageUri = Uri.parse( "android.resource://com.edyhl.wateringplants/drawable/noimage");
 	private Uri noImageUri2 = Uri.parse( "android.resource://com.edyhl.wateringplants/drawable/noimage2");
 	private String imageTemp, nameTemp, notesTemp, daysTempStr;
 	private String nextdateTemp, lastdateTemp, plantedTemp, createdTemp;
 	private int wateredTemp;
 	private int daysTemp = 1;
 	private long rowId;
 	private long nextdateTempLong, lastdateTempLong, lastdateTempLong2, plantedTempLong;
	
 	private String saveTemplateData = "saveTemplateDataValue";
 	private int isWaterButtonClicked = 0;
 	
 	private ImageView mecimage;
 	private EditText mecname;
 	private Button mecplanted;
 	private TextView meclastdate;
 	private Button mecwatered;
 	private EditText mecdays;
 	private TextView mecnextdate;
 	private EditText mecnotes;
 	private TextView meccreated;
 	private LinearLayout mecll;
 	private Button mectime;
 	private TextView meccreatedate;
 	private Button mecnextdatebutton;
//  	private Button mecsave;
  	private Button mecstop;
//  	private Button mecskip;
 	DbAdapter myDbHelper;
// 	private final Context myContext = this;
	
 	
 	public String TAG = "EditCreatePlant";
	//1
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editcreate);
 		myDbHelper = new DbAdapter(this);
 		myDbHelper.open();
 		
// 		final String sharedName = "WateringPlantsPrefs";
//        final String IGNOREMINUTESKEY = "IgnoreMinutesKey";
//        SharedPreferences mWateringPlants = getSharedPreferences(sharedName, Context.MODE_PRIVATE);
//        long ignoreMinutesLong = mWateringPlants.getLong(IGNOREMINUTESKEY, 0);
               
 		
 		mecimage = (ImageView) findViewById(R.id.ecimage);
 		mecname = (EditText) findViewById(R.id.ecname);
 		mecplanted = (Button) findViewById(R.id.ecplanted);
 		meclastdate = (TextView) findViewById(R.id.eclastdate);
 		mecwatered = (Button) findViewById(R.id.ecwatered);
 		mecdays = (EditText) findViewById(R.id.ecdays);
 		mecnextdate = (TextView) findViewById(R.id.ecnextdate);
 		mecnotes = (EditText) findViewById(R.id.ecnotes);
 		meccreated = (TextView) findViewById(R.id.eccreated);
 		mecll = (LinearLayout) findViewById(R.id.editcreate);
 		mecll.requestFocus();
 		mectime = (Button) findViewById(R.id.ectime);
 		meccreatedate = (TextView) findViewById(R.id.eccreatedate);
 		mecnextdatebutton = (Button) findViewById(R.id.ecnextdatebutton);
//  		mecsave = (Button) findViewById(R.id.ecsave);
  		mecstop = (Button) findViewById(R.id.ecstop);
//  		mecskip = (Button) findViewById(R.id.ecskip);
  		
  		paintFlag = mecnextdate.getPaintFlags();
  		
 		
 		if (savedInstanceState == null)
 		{
 			Bundle idExtra = getIntent().getExtras();
 			if (idExtra == null)
 				rowId = -11;
 			else rowId = Long.parseLong( idExtra.getString(DbAdapter.IDDB) );
 		}
 		else
 		{
 			rowId = Long.parseLong( String.valueOf(savedInstanceState.getSerializable(DbAdapter.IDDB)) );
 			imageTemp = String.valueOf(savedInstanceState.getSerializable(DbAdapter.IMAGEDB));
 			nameTemp = String.valueOf(savedInstanceState.getSerializable(DbAdapter.NAMEDB));
 			nextdateTemp = String.valueOf(savedInstanceState.getSerializable(DbAdapter.NEXTDATEDB));
 			wateredTemp = Integer.parseInt( String.valueOf(savedInstanceState.getSerializable(DbAdapter.WATEREDDB)) );
 			lastdateTemp = String.valueOf(savedInstanceState.getSerializable(DbAdapter.LASTDATEDB));
 			notesTemp = String.valueOf(savedInstanceState.getSerializable(DbAdapter.NOTESDB));
 			daysTemp = Integer.parseInt( String.valueOf(savedInstanceState.getSerializable(DbAdapter.DAYSDB)) );
 			plantedTemp = String.valueOf(savedInstanceState.getSerializable(DbAdapter.PLANTEDDB));
 			createdTemp = String.valueOf(savedInstanceState.getSerializable(DbAdapter.CREATEDDB));
 			lastdateTempLong = Long.parseLong( String.valueOf(savedInstanceState.getSerializable("ldtl")));
 			lastdateTempLong2 = Long.parseLong( String.valueOf(savedInstanceState.getSerializable("ldtl2")));
 			saveTemplateData = String.valueOf(savedInstanceState.getSerializable("saveTemplateData"));
 			
 		}
 			
 		
 		getDetail();
 	 
 		
 		
 		
 		mecdays.addTextChangedListener( new TextWatcher()
		{
			// @Override
			public void afterTextChanged(Editable s) 
			{
				daysTempStr= s.toString();
			
				if (daysTempStr != null && !daysTempStr.equals("") && mecdays.isFocused())
				{
					
					myCalendar = Calendar.getInstance();
					myYear = myCalendar.get(Calendar.YEAR);
				 	myMonth = myCalendar.get(Calendar.MONTH);
				 	myDay = myCalendar.get(Calendar.DAY_OF_MONTH);
					
				 	myCalendar.set(myYear, myMonth, myDay, setHour, setMinute, 0);
				
				 	daysTemp = Integer.parseInt(daysTempStr);
				 	myCalendar.setTimeInMillis( myCalendar.getTimeInMillis() + Long.parseLong(daysTempStr)*dayInMillisecond );
				 	final int hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
				 	final int minuteTemp = myCalendar.get(Calendar.MINUTE);
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
				 	if(nextdateTempLong >= 0)
				 		nextdateTempLong = myCalendar.getTimeInMillis();
				 	else 
				 		nextdateTempLong = myCalendar.getTimeInMillis() * -1;
				 	
				 	if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
				 		mecnextdate.setPaintFlags(paintFlagLine);
			 		else
			 			mecnextdate.setPaintFlags(paintFlag);
					mecnextdate.setText(myCalendar.getTime().toLocaleString());
				 	
				 	nextdateTemp = String.valueOf(nextdateTempLong);
				 	
				}
			}
			// @Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
            }
			// @Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
            }
		} );
 		
 		
 		
 		mecplanted.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				if(plantedTemp != null)
					myCalendar.setTimeInMillis(plantedTempLong);
				else
					myCalendar = Calendar.getInstance();
				myYear = myCalendar.get(Calendar.YEAR);
				myMonth = myCalendar.get(Calendar.MONTH);
				myDay = myCalendar.get(Calendar.DAY_OF_MONTH);
				
				showDialog(DATEDIALOG);
			}
		});
 		
 		mectime.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				if(nextdateTempLong >= 0)
					myCalendar.setTimeInMillis(nextdateTempLong);
				else if(nextdateTemp != null)
					myCalendar.setTimeInMillis(nextdateTempLong * -1);
				else 
					myCalendar = Calendar.getInstance();
				myHour = myCalendar.get(Calendar.HOUR_OF_DAY);
				myMinute = myCalendar.get(Calendar.MINUTE);
				showDialog(TIMEDIALOG);
			}
		});
 		
 		mecnextdatebutton.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				if(nextdateTemp != null)
				{
					if(nextdateTempLong >= 0)
						myCalendar.setTimeInMillis(nextdateTempLong);
					else
						myCalendar.setTimeInMillis(nextdateTempLong * -1);
				}
				else
					myCalendar = Calendar.getInstance();
				setYear = myCalendar.get(Calendar.YEAR);
				setMonth = myCalendar.get(Calendar.MONTH);
				setDay = myCalendar.get(Calendar.DAY_OF_MONTH);
				
				showDialog(NEXTDATEDIALOG);
			}
		});
 	/*	
 		mecsave.setOnClickListener(new OnClickListener()
 		{
 			public void onClick(View v)
 			{
 				myCalendar = Calendar.getInstance();
 				createdTemp = String.valueOf( myCalendar.getTimeInMillis() );
 				myDbHelper.createPlant( imageTemp, mecname.getText().toString(), nextdateTemp, wateredTemp, lastdateTemp, mecnotes.getText().toString(), daysTemp, plantedTemp, createdTemp );
 				Toast.makeText(EditCreatePlant.this, "TODO:"+String.valueOf(lastdateTemp), Toast.LENGTH_SHORT).show();
 				myDbHelper.close();
 				finish();
 			}
 		});
 		*/ 
 		mecimage.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
        		String strAvatarPrompt = "Take your picture to store your plant!";
                Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(pictureIntent, strAvatarPrompt), CAMERA);
				
			}
		});
	}
	 
	//1.1
	@Override
	protected void onDestroy()
	{
    	myDbHelper.close();
		super.onDestroy();
	}
	
	
	//1.2
	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case DATEDIALOG:
			final Button bb1 = (Button) findViewById(R.id.ecplanted);
			if(plantedTemp != null)
				myCalendar.setTimeInMillis(plantedTempLong);
			else
				myCalendar = Calendar.getInstance();
			myYear = myCalendar.get(Calendar.YEAR);
			myMonth = myCalendar.get(Calendar.MONTH);
			myDay = myCalendar.get(Calendar.DAY_OF_MONTH);
			
			
			DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
			{
				
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth)
				{
					myCalendar = Calendar.getInstance();
					myCalendar.set(year, monthOfYear, dayOfMonth);
		//			String dateString = myCalendar.getTime().toLocaleString();
					plantedTempLong = myCalendar.getTimeInMillis();
				 	plantedTemp = String.valueOf(plantedTempLong);
				 	if(plantedTempLong > Calendar.getInstance().getTimeInMillis() + 86400000)
				 		bb1.setText(na);
				 	else
				 	{
				 //		bb1.setText( dateString.substring(0, dateString.length()-11) );
				 		java.text.DateFormat df = android.text.format.DateFormat.getDateFormat(EditCreatePlant.this);
				 		bb1.setText(df.format(new Date(plantedTempLong)));
				 	}
				}
			}, myYear, myMonth, myDay);
			return dateDialog;
			
		case NEXTDATEDIALOG:
			if(nextdateTemp != null)
			{
				if(nextdateTempLong >= 0)
					myCalendar.setTimeInMillis(nextdateTempLong);
				else
					myCalendar.setTimeInMillis(nextdateTempLong * -1);
			}
			else
				myCalendar = Calendar.getInstance();
			setYear = myCalendar.get(Calendar.YEAR);
			setMonth = myCalendar.get(Calendar.MONTH);
			setDay = myCalendar.get(Calendar.DAY_OF_MONTH);
			
			
			DatePickerDialog nextdateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
			{
				
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth)
				{
					if(nextdateTemp != null)
					{
						if(nextdateTempLong >= 0)
							myCalendar.setTimeInMillis(nextdateTempLong);
						else
							myCalendar.setTimeInMillis(nextdateTempLong * -1);
					}
					else
						myCalendar = Calendar.getInstance();
					myCalendar.set(year, monthOfYear, dayOfMonth);
					if(nextdateTempLong >= 0)
						nextdateTempLong = myCalendar.getTimeInMillis();
					else
						nextdateTempLong = myCalendar.getTimeInMillis() * -1;
					
					
					if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
				 		mecnextdate.setPaintFlags(paintFlagLine);
			 		else
			 			mecnextdate.setPaintFlags(paintFlag);
					mecnextdate.setText( myCalendar.getTime().toLocaleString() );
					nextdateTemp = String.valueOf(nextdateTempLong);

							
				}
			}, setYear, setMonth, setDay);
			return nextdateDialog;
		case TIMEDIALOG:	
			final Button bb2 = (Button) findViewById(R.id.ectime);
			
			if(nextdateTempLong >= 0)
				myCalendar.setTimeInMillis(nextdateTempLong);
			else if(nextdateTemp != null)
				myCalendar.setTimeInMillis(nextdateTempLong * -1);
			else 
				myCalendar = Calendar.getInstance();
			myHour = myCalendar.get(Calendar.HOUR_OF_DAY);
			myMinute = myCalendar.get(Calendar.MINUTE);
			
			
			TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
			{
				
				public void onTimeSet(TimePicker view, int hourOfDay, int minute)
				{
					
					setHour = hourOfDay;
					setMinute = minute;
					bb2.setText( formatTime(hourOfDay, minute) );
					if(nextdateTempLong >= 0)
						myCalendar.setTimeInMillis(nextdateTempLong);
					else
						myCalendar.setTimeInMillis(nextdateTempLong * -1);
					myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					myCalendar.set(Calendar.MINUTE, minute);
					if(nextdateTempLong >= 0)
						nextdateTempLong = myCalendar.getTimeInMillis();
					else
						nextdateTempLong = myCalendar.getTimeInMillis() * -1;
					if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
				 		mecnextdate.setPaintFlags(paintFlagLine);
			 		else
			 			mecnextdate.setPaintFlags(paintFlag);
					mecnextdate.setText( myCalendar.getTime().toLocaleString() );
					
					
					nextdateTemp = String.valueOf(nextdateTempLong);
				}
			}, myHour, myMinute, false);
			return timeDialog;
			
		}
		return null;
	}
	 
	//1.3
	@Override
    protected void onSaveInstanceState(Bundle sis) {
        super.onSaveInstanceState(sis);
        
        nameTemp = mecname.getText().toString();
		notesTemp = mecnotes.getText().toString();
		
		if(rowId > 0)
		{
			sis.putSerializable("saveTemplateData", "1");
		}
		else
		{
			sis.putSerializable("saveTemplateData", "0");
			rowId = 0;
		}
        sis.putSerializable(DbAdapter.IDDB, rowId);
        sis.putSerializable(DbAdapter.IMAGEDB, imageTemp);
        sis.putSerializable(DbAdapter.NAMEDB, nameTemp);
        sis.putSerializable(DbAdapter.NEXTDATEDB, nextdateTemp);
        sis.putSerializable(DbAdapter.WATEREDDB, wateredTemp);
        sis.putSerializable(DbAdapter.LASTDATEDB, lastdateTemp);
        sis.putSerializable(DbAdapter.NOTESDB, notesTemp);
        sis.putSerializable(DbAdapter.DAYSDB, daysTemp);
        sis.putSerializable(DbAdapter.PLANTEDDB, plantedTemp);
        sis.putSerializable(DbAdapter.CREATEDDB, createdTemp);
        sis.putSerializable("ldtl", lastdateTempLong);
        sis.putSerializable("ldtl2", lastdateTempLong2);
        
    }
	
	
	//1.4
 //   public void onConfigurationChanged(Configuration newConfig)
 //   {
 //   	super.onConfigurationChanged(newConfig);
 //   	setContentView(R.layout.editcreate);
 //   }
    
    
	
	
	
    //1.5
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, PHOTO_ID, 0, R.string.take_photo);
        menu.add(0, GALLERY_ID, 0, R.string.take_gallery);
        menu.add(0, DELETE_ID, 0, R.string.delete_plant);
        menu.add(0, TIME_ID, 0, R.string.insert_time);
         return true;
    }
    //1.6
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) 
    {
    	switch (menuItem.getItemId()) 
    	{
    	case DELETE_ID:
    		if( rowId > 0 )
    		{
    			myDbHelper.deletePlant(rowId);
    	//		myDbHelper.close();
    			Toast.makeText(EditCreatePlant.this, "Deleted Plant "+String.valueOf(rowId), Toast.LENGTH_SHORT).show();
    			setResult(RESULT_OK);
    			finish();
    			
	    	}
    		else
    			Toast.makeText(EditCreatePlant.this, "Unable to delete Plant", Toast.LENGTH_SHORT).show();
    		break;
    	case PHOTO_ID:
    		String prompt = "Take a picture for your plant";
			Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(Intent.createChooser(pictureIntent, prompt), CAMERA);
			break;
    	case GALLERY_ID:
    		String prompt2 = "Choose a picture for this plant";
            Intent pickPhoto = new Intent(Intent.ACTION_PICK);
            pickPhoto.setType("image/*");
            startActivityForResult(Intent.createChooser(pickPhoto, prompt2), GALLERY);
            break;
    	case TIME_ID:
    		myCalendar = Calendar.getInstance();
    		String originalText;
    		String newText = myCalendar.getTime().toLocaleString();
    		if(mecnotes.isFocused())
    		{
    			int startSelection = mecnotes.getSelectionStart();
    			int newStartSelection = startSelection + newText.length();
    			originalText = mecnotes.getText().toString();
    			newText = originalText.substring(0, startSelection) 
    					+ newText 
    					+ originalText.substring(mecnotes.getSelectionEnd());
    			mecnotes.setText(newText);
    			mecnotes.setSelection(newStartSelection);
    		}
    		else if(mecname.isFocused())
    		{

    			int startSelection = mecname.getSelectionStart();
    			int newStartSelection = startSelection + newText.length();
    			originalText = mecname.getText().toString();
    			newText = originalText.substring(0, startSelection) 
    					+ newText 
    					+ originalText.substring(mecname.getSelectionEnd());
    			mecname.setText(newText);
    			mecname.setSelection(newStartSelection);
    		}
    		else
    			Toast.makeText(EditCreatePlant.this, "Place select a place to insert the time", Toast.LENGTH_SHORT).show();
    		break;
    		
    	}
    	return super.onOptionsItemSelected(menuItem);
    }

    
    
	//2
	public void savePlant(View saveButton)
	{
		if( nextdateTempLong > Calendar.getInstance().getTimeInMillis() || nextdateTempLong < 0 )
		{
			String oldSmallestString, newSmallestString;
			long oldSmallestNextDate = 0; 
			long newSmallestNextDate = 0;
			oldSmallestString = myDbHelper.getSmallestValue(DbAdapter.NEXTDATEDB, 0);
			if (oldSmallestString != null)
			 oldSmallestNextDate = Long.parseLong( myDbHelper.getSmallestValue(DbAdapter.NEXTDATEDB, 0) );	
			saveData(1);
			newSmallestString = myDbHelper.getSmallestValue(DbAdapter.NEXTDATEDB, 0);
			if (newSmallestString != null)
			{
				newSmallestNextDate = Long.parseLong( newSmallestString );
			
				if( oldSmallestNextDate != newSmallestNextDate)
				{
					if( newSmallestNextDate >= System.currentTimeMillis() )
					{
						AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			        	Intent intent = new Intent(this, myService.class);
			        	PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
			        	alarmManager.set(AlarmManager.RTC_WAKEUP, newSmallestNextDate, pendingIntent);
			    	}
					else 
						Toast.makeText(EditCreatePlant.this, "Unable to setup the alarm ", Toast.LENGTH_SHORT).show();
				}
			}
	//		myDbHelper.close();
			setResult(RESULT_OK);
			finish();
			return;
		}
		else if (daysTemp == 0)
		{
			saveData(1);
	//		myDbHelper.close();
			setResult(RESULT_OK);
			finish();
			return;
		/*	
			AlertDialog.Builder myad =	new AlertDialog.Builder(this);
			myad.setMessage("The Next Watering Time has passed, Please pick a new time").setPositiveButton("OK", null);
			myad.show();
			*/
		}
		else
		{
			long nextdateTempLong2 = nextdateTempLong + Long.parseLong( String.valueOf(daysTemp) )*dayInMillisecond; 
			
			myCalendar.setTimeInMillis( nextdateTempLong2 );
		 	final int hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
		 	final int minuteTemp = myCalendar.get(Calendar.MINUTE);
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
		 	nextdateTempLong2 = myCalendar.getTimeInMillis();
		 	
		 	if(nextdateTempLong2 > Calendar.getInstance().getTimeInMillis())
		 	{
		 		nextdateTempLong = nextdateTempLong2;
		 		nextdateTemp = String.valueOf(nextdateTempLong);
		 		long oldSmallestNextDate = Long.parseLong( myDbHelper.getSmallestValue(DbAdapter.NEXTDATEDB, 0) );	
				saveData(1);
				long newSmallestNextDate = Long.parseLong( myDbHelper.getSmallestValue(DbAdapter.NEXTDATEDB, 0) );
				if( oldSmallestNextDate != newSmallestNextDate)
				{
					if( newSmallestNextDate >= System.currentTimeMillis() )
					{
						AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
				        Intent intent = new Intent(this, myService.class);
				        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
				        alarmManager.set(AlarmManager.RTC_WAKEUP, newSmallestNextDate, pendingIntent);
				    }
					else 
						Toast.makeText(EditCreatePlant.this, "Unable to setup the alarm ", Toast.LENGTH_SHORT).show();
				}
				Toast.makeText(EditCreatePlant.this, "Alarm date is shifted by one interval", Toast.LENGTH_SHORT).show();
		// 		myDbHelper.close();
				setResult(RESULT_OK);
				finish();
				return;
		 	}
		 	else
		 	{
		 		AlertDialog.Builder myad =	new AlertDialog.Builder(this);
				myad.setMessage("The Next Watering Time has passed even it is shifted by one interval. \n\nPlease pick a new time or specify the alarm date by clicking the Alarm Date button").setPositiveButton("OK", null);
				myad.show();
		 	}
		 	
		}
		return;
	}
	
	//2.1
	public void cancelPlant(View cancelButton)
	{
//		myDbHelper.close();
		Toast.makeText(EditCreatePlant.this, "Canceled", Toast.LENGTH_SHORT).show();
		setResult(RESULT_CANCELED);
		finish();
	}
	
	
	//2.2
	public void stopAlarm(View stopButton)
	{
		if(nextdateTempLong >= 0)
		{
			nextdateTempLong *= -1;
			nextdateTemp = String.valueOf(nextdateTempLong);
			mecstop.setText(R.string.enable_alarm);
			
			if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
		 		mecnextdate.setPaintFlags(paintFlagLine);
	 		else
	 			mecnextdate.setPaintFlags(paintFlag);
		}
		else
		{
			nextdateTempLong *= -1;
			nextdateTemp = String.valueOf(nextdateTempLong);
			mecstop.setText(R.string.stop_alarm);
			
			if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
		 		mecnextdate.setPaintFlags(paintFlagLine);
	 		else
	 			mecnextdate.setPaintFlags(paintFlag);
		}
	}
	
	//2.3
	public void skipAlarm(View skipButton)
	{
		if(nextdateTempLong >= 0)
		{
		
			myCalendar.setTimeInMillis( nextdateTempLong + Long.parseLong( String.valueOf(daysTemp) )*dayInMillisecond );
			final int hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
			final int minuteTemp = myCalendar.get(Calendar.MINUTE);
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
	 	
			if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
				mecnextdate.setPaintFlags(paintFlagLine);
			else
				mecnextdate.setPaintFlags(paintFlag);
			mecnextdate.setText(myCalendar.getTime().toLocaleString());
	 	
			nextdateTemp = String.valueOf(nextdateTempLong);
		}
		else
			Toast.makeText(EditCreatePlant.this, "The Alarm is stop, please enable it first", Toast.LENGTH_SHORT).show();
		
	}
	
	//2.4
	public void waterIt(View waterButton)
	{
		if(isWaterButtonClicked == 0 )
		{
			while ( nextdateTempLong <= Calendar.getInstance().getTimeInMillis() && nextdateTempLong >= 0 && daysTemp > 0)
			{
				myCalendar.setTimeInMillis( nextdateTempLong + Long.parseLong( String.valueOf(daysTemp) )*dayInMillisecond );
				final int hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
				final int minuteTemp = myCalendar.get(Calendar.MINUTE);
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
	 		 
				if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
					mecnextdate.setPaintFlags(paintFlagLine);
				else
					mecnextdate.setPaintFlags(paintFlag);
				mecnextdate.setText(myCalendar.getTime().toLocaleString());
 	
				nextdateTemp = String.valueOf(nextdateTempLong);
			}
			isWaterButtonClicked = 1;
		}
		
		if(lastdateTemp.equals(na))
		{
			if(wateredTemp == 0)
			{
				wateredTemp = 1;
				mecwatered.setBackgroundResource(R.drawable.water1);
				myCalendar = Calendar.getInstance();
				lastdateTempLong2 = myCalendar.getTimeInMillis();
				meclastdate.setText( myCalendar.getTime().toLocaleString() );
				
			}
			else if (wateredTemp ==1)
			{
				wateredTemp = 0;
				mecwatered.setBackgroundResource(R.drawable.water0);
				meclastdate.setText(na);
				lastdateTempLong2 = 0;
			}
			
		}
		else
		{
			if(wateredTemp == 0)
			{
				wateredTemp = 1;
				mecwatered.setBackgroundResource(R.drawable.water1);
				myCalendar = Calendar.getInstance();
				lastdateTempLong2 = myCalendar.getTimeInMillis();
				meclastdate.setText( myCalendar.getTime().toLocaleString() );
			}
			else if (wateredTemp == 2)
			{
				wateredTemp = 1;
				mecwatered.setBackgroundResource(R.drawable.water1);
				myCalendar = Calendar.getInstance();
				lastdateTempLong2 = myCalendar.getTimeInMillis();
				meclastdate.setText( myCalendar.getTime().toLocaleString() );
				
			}
			else if (wateredTemp == 1)
			{
				wateredTemp = 0;
				mecwatered.setBackgroundResource(R.drawable.water0);
				lastdateTempLong2 = lastdateTempLong;
				myCalendar.setTimeInMillis(lastdateTempLong);
				meclastdate.setText( myCalendar.getTime().toLocaleString() );
			}
		}
	}
	
	//2.5
	private void saveData(int isSaveButtonClicked)
	{
		
		nameTemp = mecname.getText().toString();
		
		if(lastdateTempLong2>0)
			lastdateTemp = String.valueOf(lastdateTempLong2);
		if(wateredTemp == 2)
			wateredTemp = 1;
		
		notesTemp = mecnotes.getText().toString();
 		
 		if(rowId > 0)
 		{
 			
 			if( myDbHelper.updatePlant(rowId, imageTemp, nameTemp, nextdateTemp, wateredTemp, lastdateTemp, notesTemp, daysTemp, plantedTemp, createdTemp) )
 				Toast.makeText(EditCreatePlant.this, "Plant Saved", Toast.LENGTH_SHORT).show();
 			else
 				Toast.makeText(EditCreatePlant.this, "Internal Error 001, Plant NOT Saved", Toast.LENGTH_SHORT).show();
 		}
 		else
		{
			myCalendar = Calendar.getInstance();
			createdTemp = String.valueOf(myCalendar.getTimeInMillis());
			long newId = myDbHelper.createPlant(imageTemp, nameTemp, nextdateTemp, wateredTemp, lastdateTemp, notesTemp, daysTemp, plantedTemp, createdTemp, na);		
 			
 			if (newId > 0)
 			{
 				rowId = newId;
 				Toast.makeText(EditCreatePlant.this, "Plant Saved", Toast.LENGTH_SHORT).show();
 			}
 			else
 				Toast.makeText(EditCreatePlant.this, "Internal Error 002, Plant NOT Saved", Toast.LENGTH_SHORT).show();
 		}
	}
	
	//3
	private void getDetail()
	{
		if ( rowId > 0 && saveTemplateData.equals("saveTemplateDataValue") )
		{
			setTitle("Edit Plant");
			Cursor myPlant = myDbHelper.getPlant(rowId);
			startManagingCursor(myPlant);
			imageTemp = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.IMAGEDB) );
			nameTemp = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.NAMEDB) );
			nextdateTemp = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.NEXTDATEDB) );
			wateredTemp = myPlant.getInt( myPlant.getColumnIndexOrThrow(DbAdapter.WATEREDDB) );
			lastdateTemp = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.LASTDATEDB) );
			notesTemp = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.NOTESDB) );
			daysTemp = myPlant.getInt( myPlant.getColumnIndexOrThrow(DbAdapter.DAYSDB) );
			plantedTemp = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.PLANTEDDB) );
			createdTemp = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.CREATEDDB) );
	/*		
		 	String ll = myPlant.getString( myPlant.getColumnIndexOrThrow(DbAdapter.LASTALARMDB) );
		 	if( !ll.equals(na))
		 	{ myCalendar.setTimeInMillis( Long.parseLong( ll ) );
		 	 
		 		ll = myCalendar.getTime().toLocaleString();
		 	}		
		*/	 
			nextdateTempLong = Long.parseLong(nextdateTemp);
			
			if(wateredTemp == 1)
				wateredTemp = 2;
			
	 		mecimage.setImageURI( Uri.parse(imageTemp) );
	 		if(mecimage.getDrawable() == null)
	 			mecimage.setImageURI( noImageUri2 );
	 		
	 		mecname.setText(nameTemp); 
	 		
	 		if(nextdateTempLong >= 0)
	 		{
	 			mecstop.setText(R.string.stop_alarm);
	 			myCalendar.setTimeInMillis(nextdateTempLong);
	 		}
	 		else
	 		{
	 			mecstop.setText(R.string.enable_alarm);
	 			myCalendar.setTimeInMillis(nextdateTempLong * -1);
	 		}
	 		
	 		if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
		 		mecnextdate.setPaintFlags(paintFlagLine);
	 		else
	 			mecnextdate.setPaintFlags(paintFlag);
			mecnextdate.setText( myCalendar.getTime().toLocaleString() );
	 		
	 		myHour = myCalendar.get(Calendar.HOUR_OF_DAY);
		 	myMinute = myCalendar.get(Calendar.MINUTE);
			setHour = myHour;
			setMinute = myMinute;
		 	mectime.setText( formatTime(myHour, myMinute) );
		 	
		 	if(wateredTemp == 1)
		 	{
		 		mecwatered.setBackgroundResource(R.drawable.water1);
		 	}
		 	else if(wateredTemp == 2)
		 	{
		 		mecwatered.setBackgroundResource(R.drawable.water2);
		 	}
		 	else
		 	{
		 		mecwatered.setBackgroundResource(R.drawable.water0);
		 	}
		 	
			if (lastdateTemp.equals(na))
				meclastdate.setText(na);
			else
			{
				lastdateTempLong = Long.parseLong(lastdateTemp);
				lastdateTempLong2 = lastdateTempLong;
				myCalendar.setTimeInMillis(lastdateTempLong);
				meclastdate.setText( String.valueOf(myCalendar.getTime().toLocaleString()) );
			}
			
			mecnotes.setText(notesTemp);
			
	//		mecnotes.setText(ll);
		
	 	 	
			mecdays.setText( String.valueOf(daysTemp) );
	 	 	
	 	 	if (plantedTemp.equals(na))
	 	 	{
	 	 		plantedTempLong = Calendar.getInstance().getTimeInMillis();
	 	 		mecplanted.setText(na);
	 	 	}
	 	 	else
	 	 	{
				plantedTempLong = Long.parseLong(plantedTemp);
	 	 //		myCalendar.setTimeInMillis(plantedTempLong);

		//		String dateString = myCalendar.getTime().toLocaleString();
		//		mecplanted.setText( dateString.substring(0, dateString.length()-11) );
				java.text.DateFormat df2 = android.text.format.DateFormat.getDateFormat(EditCreatePlant.this);
		 		mecplanted.setText( df2.format(new Date(plantedTempLong)) );
	 	 	}
	 	 	
	 	 	myCalendar.setTimeInMillis( Long.parseLong(createdTemp) );
	 	 	meccreated.setText( myCalendar.getTime().toLocaleString() );
							 		
			
		}
		else if ( rowId == 0 || saveTemplateData.equals("1") )
		{
			nextdateTempLong = Long.parseLong(nextdateTemp);
			mecimage.setImageURI( Uri.parse(imageTemp) );
			if(mecimage.getDrawable() == null)
	 			mecimage.setImageURI( noImageUri2 );
			
	 		mecname.setText(nameTemp); 
	 		
	 		if(nextdateTempLong >= 0)
	 		{
	 			mecstop.setText(R.string.stop_alarm);
	 			myCalendar.setTimeInMillis(nextdateTempLong);
	 		}
	 		else
	 		{
	 			mecstop.setText(R.string.enable_alarm);
	 			myCalendar.setTimeInMillis(nextdateTempLong * -1);
	 		}
	 		
	 		if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
		 		mecnextdate.setPaintFlags(paintFlagLine);
	 		else
	 			mecnextdate.setPaintFlags(paintFlag);
			mecnextdate.setText( myCalendar.getTime().toLocaleString() );
	 		
	 		myHour = myCalendar.get(Calendar.HOUR_OF_DAY);
		 	myMinute = myCalendar.get(Calendar.MINUTE);
			setHour = myHour;
			setMinute = myMinute;
		 	mectime.setText( formatTime(myHour, myMinute) );
		 	if(wateredTemp == 1)
		 	{
		 		mecwatered.setBackgroundResource(R.drawable.water1);
		 	}
		 	else if(wateredTemp == 2)
		 	{
		 		mecwatered.setBackgroundResource(R.drawable.water2);
		 	}
		 	else
		 	{
		 		mecwatered.setBackgroundResource(R.drawable.water0);
		 	}
		 	
		 	
		 	if(lastdateTempLong2>0)
		 	{
		 		myCalendar.setTimeInMillis(lastdateTempLong2);
				meclastdate.setText( String.valueOf(myCalendar.getTime().toLocaleString()) );
				
		 	}
		 	else 
				meclastdate.setText(na);
			
			mecnotes.setText(notesTemp);
	 	 	
			mecdays.setText( String.valueOf(daysTemp) );
	 	 	
	 	 	if (plantedTemp.equals(na))
	 	 	{
	 	 		plantedTempLong = Calendar.getInstance().getTimeInMillis();
	 	 		mecplanted.setText(na);
	 	 	}
	 	 	else
	 	 	{
				plantedTempLong = Long.parseLong(plantedTemp);
	 	 	//	myCalendar.setTimeInMillis(plantedTempLong);

			//	String dateString = myCalendar.getTime().toLocaleString();
			//	mecplanted.setText( dateString.substring(0, dateString.length()-11) );
				java.text.DateFormat df3 = android.text.format.DateFormat.getDateFormat(EditCreatePlant.this);
		 		mecplanted.setText(df3.format(new Date(plantedTempLong)));
	 	 	}
	 	 	
	 	 	
	 	 	if(saveTemplateData.equals("1") && rowId > 0)
	 	 	{
	 	 		setTitle("Edit Plant");
		 	 	myCalendar.setTimeInMillis( Long.parseLong(createdTemp) );
		 	 	meccreated.setText( myCalendar.getTime().toLocaleString() );
	 	 	}
	 	 	else if( saveTemplateData.equals("0") && rowId == 0 )
	 	 	{
	 	 		setTitle("Create Plant");
	 	 		meccreatedate.setVisibility(View.GONE);
				meccreated.setVisibility(View.GONE);
	 	 	}
	 	 		 	 	
		}
		else
		{
			setTitle("New Plant");
			imageTemp = noImageUri.toString(); 
			mecimage.setImageURI(noImageUri);
			if(mecimage.getDrawable() == null)
	 			mecimage.setImageURI( noImageUri2 );
			
			meclastdate.setText(na);
			mecwatered.setBackgroundResource(R.drawable.water0);
			mecdays.setText("1");
			lastdateTemp = na;
			wateredTemp = 0;
			daysTemp = 1;
			
			myCalendar = Calendar.getInstance();
			mecplanted.setText(na);
			plantedTemp = na;
			plantedTempLong = myCalendar.getTimeInMillis();
			
			myYear = myCalendar.get(Calendar.YEAR);
		 	myMonth = myCalendar.get(Calendar.MONTH);
		 	myDay = myCalendar.get(Calendar.DAY_OF_MONTH);
			myHour = myCalendar.get(Calendar.HOUR_OF_DAY);
		 	myMinute = myCalendar.get(Calendar.MINUTE);
		 	setHour = myHour;
			setMinute = myMinute;
			mectime.setText( formatTime(myHour, myMinute) );
			myCalendar.set(Calendar.SECOND, 0);
			myCalendar.setTimeInMillis(myCalendar.getTimeInMillis() + dayInMillisecond);
			final int hourTemp = myCalendar.get(Calendar.HOUR_OF_DAY);
		 	final int minuteTemp = myCalendar.get(Calendar.MINUTE);
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
			nextdateTemp = String.valueOf(nextdateTempLong);
			
			if(nextdateTempLong < Calendar.getInstance().getTimeInMillis())
		 		mecnextdate.setPaintFlags(paintFlagLine);
	 		else
	 			mecnextdate.setPaintFlags(paintFlag);
			mecnextdate.setText(myCalendar.getTime().toLocaleString());
			if(nextdateTempLong >= 0)
	 			mecstop.setText(R.string.stop_alarm);
	 		else
	 			mecstop.setText(R.string.enable_alarm);
	 		
						
			meccreatedate.setVisibility(View.GONE);
			meccreated.setVisibility(View.GONE);
		}
	}
	
	//4
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
        {
		case CAMERA:

            if (resultCode == Activity.RESULT_CANCELED) 
            {
            	Toast.makeText(EditCreatePlant.this, "Action Canceled", Toast.LENGTH_SHORT).show();
            } 
            else if (resultCode == Activity.RESULT_OK) 
            {
            	Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                saveAndSetImage(cameraImage);
            } 
            break;
		case GALLERY:
			if (resultCode == Activity.RESULT_CANCELED) 
			{ 
				Toast.makeText(EditCreatePlant.this, "Action Canceled", Toast.LENGTH_SHORT).show();
			} 
			else if (resultCode == Activity.RESULT_OK) 
			{
                Uri photoUri = data.getData();
                if (photoUri != null) 
                {
                    try 
                    {
                    	Bitmap galleryImage = Media.getBitmap(getContentResolver(), photoUri);
                        saveAndSetImage(galleryImage);
                    } 
                    catch (Exception e) 
                    {
                    //    Log.e(TAG, "save image with gallery picker failed.", e);
                    }
                }
            }

			break;
        }
    }
	
	
	
	//4.1
	private void saveAndSetImage(Bitmap bitmapFV)
	{
		String strPlantFilename = "imageFileName";
        
        if (bitmapFV != null) 
        {
            try 
            {
            	int thumbnailsize = getResources().getInteger(R.integer.thumbnail);
            	Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapFV, thumbnailsize, thumbnailsize, true);
            	if( imageTemp.equals(noImageUri.toString()) )
            		strPlantFilename = "plantImage" + String.valueOf(currentCalendarInMillis) + ".png";
            	else
            		strPlantFilename = imageTemp.substring( imageTemp.indexOf("plantImage") );
                
            	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            	{
            		File myDir = new File( Environment.getExternalStorageDirectory().getAbsolutePath(), "WaterPlants" );
                	myDir.mkdir();
            		File myFile = new File(myDir, strPlantFilename);
            	
            		try 
            		{
            			OutputStream opStream = new FileOutputStream(myFile);
            			scaledBitmap.compress(CompressFormat.PNG, 0, opStream);
            			opStream.flush();
            			opStream.close();
            		} 
            		catch (Exception e) 
            		{
            		//	Log.e(TAG, "Image compression and save failed.", e);
            		}
            		Uri myImageUri = Uri.fromFile(myFile);
            		mecimage.setImageURI(null);
            		mecimage.setImageURI(myImageUri);
            		imageTemp = myImageUri.toString();
            		return;
            	}
            	else
            	{

            		try 
                    {
                        scaledBitmap.compress(CompressFormat.PNG, 0, openFileOutput(strPlantFilename, MODE_PRIVATE));
                    } 
                    catch (Exception e) 
                    {
                    //	Log.e(TAG, "Image compression and save failed.", e);
                    }
            		Uri myImageUri2 = Uri.fromFile(new File(EditCreatePlant.this.getFilesDir(), strPlantFilename));
            		mecimage.setImageURI(null);
            		mecimage.setImageURI(myImageUri2);
            		imageTemp = myImageUri2.toString();
            		return;
            	}
                
            } 
            catch (Exception e) 
            {
            //    Log.e(TAG, "save image failed.", e);
            }
        }
        else
        	Toast.makeText(EditCreatePlant.this, "Unable to get the image", Toast.LENGTH_SHORT).show();
        return;
	}
	
	//5 (FV function variable)
	private String formatTime(int hourFV, int minuteFV)
	{
		String minuteHasZeroFV = ":";
		String amFV = " AM";
		String pmFV = " PM";
		String twelveFV = "12";
		if(minuteFV < 10)
			minuteHasZeroFV = ":0";
		
		if(hourFV > 12)
		{
			return String.valueOf(hourFV-12) + minuteHasZeroFV + String.valueOf(minuteFV) + pmFV;
			
		}
		else if(hourFV == 0)
		{
			return twelveFV + minuteHasZeroFV + String.valueOf(minuteFV) + amFV;
		}
		else if(hourFV < 12)
		{
			return String.valueOf(hourFV) + minuteHasZeroFV + String.valueOf(minuteFV) + amFV;
		}
		else if(hourFV == 12)
		{
			return twelveFV + minuteHasZeroFV + String.valueOf(minuteFV) + pmFV;
		}
		return null;
	}
	
	
	//6
	
	
}//
