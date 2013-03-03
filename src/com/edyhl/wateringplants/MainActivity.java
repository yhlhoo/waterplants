package com.edyhl.wateringplants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.ads.AdRequest;
//import com.google.ads.AdView;

//import com.google.ads.Ad;
//import com.google.ads.AdListener;
//import com.google.ads.AdRequest;
//import com.google.ads.AdView;
//import com.google.ads.AdRequest.ErrorCode;


public class MainActivity extends ListActivity {
	
	public static final String PLANTPREFERENCE = "WateringPlantsPrefs";
	public static final String SOUNDCHECKKEY = "SoundCheckKey";
	public static final String VIBRATIONCHECKKEY = "VibrationCheckKey";
	public static final String IGNOREMINUTESKEY = "IgnoreMinutesKey";
	public static final String SORTCOLUMNKEY = "SortColumnKey";
	public static final String ORDERTYPEKEY = "OrderTypeKey";
	public static final String ALARMRANGEMINUSKEY = "AlarmRangeMinusKey";
	public static final String ALARMRANGEPLUSKEY = "AlarmRangePlusKey";
	SharedPreferences mWateringPlants;
	private long ignoreMinutes;
	private int sortColumn = 1;
	private int orderType = 1;
	
	RadioGroup rg1, rg2;
	RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9;
	
	
	private static final int EDITCREATEPLANT = 0;
//	private static final int SETIMAGE = 3;
	
	private long imageFileId;
	private Calendar myCalendar1 = Calendar.getInstance();
	private Calendar myCalendar2 = Calendar.getInstance();
//	private final static long dayInMillisecond = 86400000;
	private String lastdateTempString2 = null;
	private View rowView;
	private Uri noImageUri2 = Uri.parse( "android.resource://com.edyhl.wateringplants/drawable/noimage2");
// 	private AdView adview;
		
	private DbAdapter mDbHelper;
	private Cursor mCursor;
//	private final Context myContext = this;
	private static final int CREATE_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST+1;
	private static final int EDIT_ID = Menu.FIRST+2;
	private static final int FULLVIEW_ID = Menu.FIRST+3;
	private static final int REFRESH_ID = Menu.FIRST+4;
	private static final int SETTING_ID = Menu.FIRST+5;
	private static final int WATERALL_ID = Menu.FIRST+6;
	private static final int SHARE_ID = Menu.FIRST+7;
	
	private int fullView = 1;
	
	public String TAG = "Mainactivity";
	
	
	//1
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWateringPlants = getSharedPreferences(PLANTPREFERENCE, Context.MODE_PRIVATE);
        ignoreMinutes = mWateringPlants.getLong(IGNOREMINUTESKEY, 0);
        
        
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
        rowView = findViewById(R.id.dummyView);

        updateNextDateAndSetAlarm();
        sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
        orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
        fillData(sortColumn, orderType);

     /*
           adview = (AdView) findViewById(R.id.adView);
           AdRequest adrequest = new AdRequest();
           Set<String> keywords = new HashSet<String>();
           keywords.add("plant");
           keywords.add("garden");
           keywords.add("water");
           adrequest.setKeywords(keywords);
      //     adrequest.addKeyword("garden");
           adview.loadAd(adrequest);
     */
           
           
    }
    
    //1.1  
    @Override
	protected void onDestroy()
	{
    	mCursor.close();
    	mDbHelper.close();
		super.onDestroy();
	}



	//1.1
    public void updateNextDateAndSetAlarm()
    {
    	ignoreMinutes = mWateringPlants.getLong(IGNOREMINUTESKEY, 0);
    	//Toast.makeText(MainActivity.this, String.valueOf(ignoreMinutes), Toast.LENGTH_SHORT).show();
    	String nextAlarmTime = mDbHelper.getSmallestValue(DbAdapter.NEXTDATEDB, ignoreMinutes);
        if(nextAlarmTime != null)
        {
        	long when = Long.parseLong(nextAlarmTime);
        	if(when >= System.currentTimeMillis()-2000)
        		startAlarm1( when );
        	else 
        	{
        	}
        }
        else
        {
            Cursor allCursor = mDbHelper.getAllPlants(1, 1, "0");
        	if( allCursor.getCount() > 0 )
        		Toast.makeText(MainActivity.this, "Unable to setup the alarm ", Toast.LENGTH_SHORT).show();
        	allCursor.close();
        }
    }

    
    //1.2
    public void onConfigurationChanged(Configuration newConfig)
    {
    	super.onConfigurationChanged(newConfig);
    	setContentView(R.layout.activity_main);
    	
    }
    
    //2
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0, CREATE_ID, 0, R.string.create_plant);
   		menu.add(0, DELETE_ID, 0, R.string.delete_plant);
   		menu.add(0, EDIT_ID, 0, R.string.edit_plant);
   		menu.add(0, FULLVIEW_ID, 0, R.string.full_view0);
        menu.add(0, REFRESH_ID, 0, R.string.refresh);
        menu.add(0, SETTING_ID, 0, R.string.settings);
        menu.add(0, WATERALL_ID, 0, R.string.water_all);
        menu.add(0, SHARE_ID, 0, R.string.share_app);
        
        return true;
    }
    

    //2.1
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) 
    {
    	if (imageFileId > 0)
    	{
    		menu.getItem(DELETE_ID-1).setEnabled(true);
            menu.getItem(EDIT_ID-1).setEnabled(true);
    	}
    	else
        {
        	menu.getItem(DELETE_ID-1).setEnabled(false);
            menu.getItem(EDIT_ID-1).setEnabled(false);
        }
    	if(fullView == 1)
    		menu.getItem(FULLVIEW_ID-1).setTitle(R.string.full_view0);
    	else
    		menu.getItem(FULLVIEW_ID-1).setTitle(R.string.full_view1);
        return true;
    }
    
    //2.2
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) 
    {
    	switch (menuItem.getItemId()) 
    	{
    	case DELETE_ID:
    		String plantId =String.valueOf(imageFileId);
    		if(plantId.equals("")||plantId == null || imageFileId<1)
    			Toast.makeText(MainActivity.this, "NO Plant Selected", Toast.LENGTH_SHORT).show();
    		else if( mDbHelper.deletePlant(imageFileId) )
    		{
    			imageFileId=0;
    			sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
                orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
    			fillData(sortColumn, orderType);
	    		Toast.makeText(MainActivity.this, "Deleted Plant "+plantId, Toast.LENGTH_SHORT).show();
    		}
    		else
    			Toast.makeText(MainActivity.this, "Unable to delete Plant "+plantId, Toast.LENGTH_SHORT).show();
    		break;
    	case CREATE_ID:
    		Intent newPlant = new Intent(MainActivity.this, EditCreatePlant.class);
        	startActivityForResult(newPlant, EDITCREATEPLANT);
        	break;
    	case EDIT_ID:
    		String plantId2 =String.valueOf(imageFileId);
    		if(plantId2.equals("")||plantId2 == null || imageFileId<1)
    			Toast.makeText(MainActivity.this, "NO Plant Selected", Toast.LENGTH_SHORT).show();
    		else
    		{
    			Intent i = new Intent(MainActivity.this, EditCreatePlant.class);
             	i.putExtra(DbAdapter.IDDB, plantId2);
             	startActivityForResult(i, EDITCREATEPLANT);
    		}
    		break;
    	case SHARE_ID:
    		Intent intent = new Intent(Intent.ACTION_SEND);
        	intent.setType("text/plain");

        	intent.putExtra(Intent.EXTRA_SUBJECT, "Water Plants");
        	intent.putExtra(Intent.EXTRA_TEXT, "Check out this cool App \n\n https://play.google.com/store/apps/details?id=com.edyhl.wateringplants");

        	Intent chooser = Intent.createChooser(intent, "Tell a friend about Water Plants");
        	startActivity(chooser);
        	break;
        	
    	case REFRESH_ID:
    		updateNextDateAndSetAlarm();
    		sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
            orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
    		fillData(sortColumn, orderType);
    		Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
    		break;
    	case SETTING_ID:
    		showDialog(SETTING_ID);
    		break;
    	case WATERALL_ID:
    		if(mCursor != null)
    		{
    			String lastdateFV = String.valueOf( Calendar.getInstance().getTimeInMillis() );
    			if(mCursor.moveToFirst())
    			{
    				ContentValues cv = new ContentValues();
    				cv.put(DbAdapter.WATEREDDB, 1);
    				cv.put(DbAdapter.LASTDATEDB, lastdateFV);
    				mDbHelper.updateCV( Long.parseLong( mCursor.getString(0) ), cv );
    				while ( mCursor.moveToNext() )
    				{
    					ContentValues cv2 = new ContentValues();
        				cv2.put(DbAdapter.WATEREDDB, 1);
        				cv2.put(DbAdapter.LASTDATEDB, lastdateFV);
        				mDbHelper.updateCV( Long.parseLong( mCursor.getString(0) ), cv2 );
    				}
    				sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
    	            orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
    	    		fillData(sortColumn, orderType);
    			}
    		}
    		break;
    	case FULLVIEW_ID:
    		if(fullView == 1)
    			fullView = 0;
    		else if (fullView == 0)
    			fullView = 1;
    		sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
            orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
    		fillData(sortColumn, orderType);
    		
    		break;
    	}
    	return super.onOptionsItemSelected(menuItem);
    }

      
    //3
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITCREATEPLANT)
        {
        	sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
            orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
        	fillData(sortColumn, orderType);
        }
        else if (requestCode > 0)
        {
        	if (resultCode == Activity.RESULT_CANCELED) 
            {
            	Toast.makeText(MainActivity.this, "Action Canceled", Toast.LENGTH_SHORT).show();
            } 
            else if (resultCode == Activity.RESULT_OK) 
            {
            	String strPlantFilename = "imageFileName";	
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                int thumbnailsize = getResources().getInteger(R.integer.thumbnail);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(cameraImage, thumbnailsize, thumbnailsize, true);
                if (cameraImage != null) 
                {
                    try 
                    {
                    	strPlantFilename = mDbHelper.getImageUri(requestCode);
                    	if(strPlantFilename != null)
                    	{
                    		if( strPlantFilename.equals( Uri.parse("android.resource://com.edyhl.wateringplants/drawable/noimage").toString() ) )
                    			strPlantFilename = "plantImage" + String.valueOf( Calendar.getInstance().getTimeInMillis() ) + ".png";
                    		else
                    			strPlantFilename = strPlantFilename.substring( strPlantFilename.indexOf("plantImage") );
                    		 
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
                        		mDbHelper.updateImage( requestCode, myImageUri.toString() );
                        		sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
                                orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
                        		fillData(sortColumn, orderType);
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

                        		Uri myImageUri2 = Uri.fromFile(new File(MainActivity.this.getFilesDir(), strPlantFilename));
                        		mDbHelper.updateImage( requestCode, myImageUri2.toString() );
                        		sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
                                orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
                        		fillData(sortColumn, orderType);

                        	}

                    	}
                    	else
                    		Toast.makeText(MainActivity.this, "Unable to locate the old image", Toast.LENGTH_SHORT).show();
                    } 
                    catch (Exception e) 
                    {
                    //    Log.e(TAG, "save image with camera image failed.", e);
                    }
                }
                else
                	Toast.makeText(MainActivity.this, "Unable to get the image from camera", Toast.LENGTH_SHORT).show();
            }
        }
       
        	
        
    }
    
   
    //4
    @Override
    protected Dialog onCreateDialog( int id) {
    	switch(id)
    	{
    	case SETTING_ID:
    		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.settingsdialog, (ViewGroup) findViewById(R.id.mysettings));
            final EditText etignoreMinutes = (EditText) layout.findViewById(R.id.numMinutes);
            final EditText etalarmRange1 = (EditText) layout.findViewById(R.id.alarmrange1);
            final EditText etalarmRange2 = (EditText) layout.findViewById(R.id.alarmrange2);
            final long ignoreMinutesTemp = mWateringPlants.getLong(IGNOREMINUTESKEY, 1);
           
            
            final CheckBox checkbox1 = (CheckBox) layout.findViewById(R.id.soundcheck);
            final CheckBox checkbox2 = (CheckBox) layout.findViewById(R.id.vibrationcheck);
            if( mWateringPlants.getInt(SOUNDCHECKKEY, 0) == 1)
            	checkbox1.setChecked(true);
            else 
            	checkbox1.setChecked(false);
            
            if( mWateringPlants.getInt(VIBRATIONCHECKKEY, 1) == 1)
            	checkbox2.setChecked(true);
            else 
            	checkbox2.setChecked(false);
            	
            	
            checkbox1.setOnClickListener(new View.OnClickListener()
			{
				
				public void onClick(View v)
				{
					if(checkbox1.isChecked())
					{
						Editor editor = mWateringPlants.edit();
                    	editor.putInt(SOUNDCHECKKEY, 1);
                    	editor.commit();
					}
					else
					{
						Editor editor = mWateringPlants.edit();
                    	editor.putInt(SOUNDCHECKKEY, 0);
                    	editor.commit();
					}
				}
			});
            
            checkbox2.setOnClickListener(new View.OnClickListener()
			{
				
				public void onClick(View v)
				{
					if(checkbox2.isChecked())
					{
						Editor editor = mWateringPlants.edit();
                    	editor.putInt(VIBRATIONCHECKKEY, 1);
                    	editor.commit();
					}
					else
					{
						Editor editor = mWateringPlants.edit();
                    	editor.putInt(VIBRATIONCHECKKEY, 0);
                    	editor.commit();
					}
				}
			});
            
            rg1 = (RadioGroup) layout.findViewById(R.id.sortcolumn);
            rg2 = (RadioGroup) layout.findViewById(R.id.ordertype);
            rb1 = (RadioButton) layout.findViewById(R.id.sort1);
            rb2 = (RadioButton) layout.findViewById(R.id.sort2);
            rb3 = (RadioButton) layout.findViewById(R.id.sort3);
            rb4 = (RadioButton) layout.findViewById(R.id.sort4);
            rb5 = (RadioButton) layout.findViewById(R.id.sort5);
            rb6 = (RadioButton) layout.findViewById(R.id.sort6);
            rb7 = (RadioButton) layout.findViewById(R.id.sort7);
            rb8 = (RadioButton) layout.findViewById(R.id.asc);
            rb9 = (RadioButton) layout.findViewById(R.id.desc);
            
            sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
            orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
            switch (sortColumn)
            {
            case 1:
            	rg1.check(R.id.sort1);
            	break;
            case 2:
            	rg1.check(R.id.sort2);
            	break;
            case 3:
            	rg1.check(R.id.sort3);
            	break;
            case 4:
            	rg1.check(R.id.sort4);
            	break;
            case 5:
            	rg1.check(R.id.sort5);
            	break;
            case 6:
            	rg1.check(R.id.sort6);
            	break;
            case 7:
            	rg1.check(R.id.sort7);
            	break;
            }
            if(orderType == 2)
            	rg2.check(R.id.desc);
            else
            	rg2.check(R.id.asc);
            
            rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() 
            {
                public void onCheckedChanged(RadioGroup rg, int checkedId) 
                {
                    if (rb1.getId() == checkedId) 
                    {
                    	sortColumn = 1;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(SORTCOLUMNKEY, sortColumn);
                    	editor.commit();
                        return;
                    } 
                    else if (rb2.getId() == checkedId) 
                    {
                    	sortColumn = 2;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(SORTCOLUMNKEY, sortColumn);
                    	editor.commit();
                    	return;
                    } 
                    else if (rb3.getId() == checkedId) 
                    {
                    	sortColumn = 3;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(SORTCOLUMNKEY, sortColumn);
                    	editor.commit();
                        return;
                    }
                    else if (rb4.getId() == checkedId) 
                    {
                    	sortColumn = 4;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(SORTCOLUMNKEY, sortColumn);
                    	editor.commit();
                        return;
                    }
                    else if (rb5.getId() == checkedId) 
                    {
                    	sortColumn = 5;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(SORTCOLUMNKEY, sortColumn);
                    	editor.commit();
                        return;
                    }
                    else if (rb6.getId() == checkedId) 
                    {
                    	sortColumn = 6;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(SORTCOLUMNKEY, sortColumn);
                    	editor.commit();
                        return;
                    }
                    else if (rb7.getId() == checkedId) 
                    {
                    	sortColumn = 7;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(SORTCOLUMNKEY, sortColumn);
                    	editor.commit();
                        return;
                    }
                }
            });
         
            rg2.setOnCheckedChangeListener(new OnCheckedChangeListener() 
            {
                public void onCheckedChanged(RadioGroup rg, int checkedId) 
                {
                    if (rb8.getId() == checkedId) 
                    {
                    	orderType = 1;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(ORDERTYPEKEY, orderType);
                    	editor.commit();
                        return;
                    } 
                    else if (rb9.getId() == checkedId) 
                    {
                    	orderType = 2;
                    	Editor editor = mWateringPlants.edit();
                    	editor.putInt(ORDERTYPEKEY, orderType);
                    	editor.commit();
                    	return;
                    }
                }
            });

            
            
            
            ignoreMinutes = mWateringPlants.getLong(IGNOREMINUTESKEY, 0);
            etignoreMinutes.setText( String.valueOf(ignoreMinutes) );
            
            etignoreMinutes.addTextChangedListener(new TextWatcher() 
            {
               // @Override
                public void afterTextChanged(Editable s) 
                {
                	String minsTempStr= s.toString();
            			
            		if ( minsTempStr != null && !minsTempStr.equals("") )
            		{
            			ignoreMinutes = Long.parseLong(minsTempStr);
            			Editor editor = mWateringPlants.edit();
            			editor.putLong(IGNOREMINUTESKEY, ignoreMinutes);
            			editor.commit();
            		}
            		else
            		{
            			Editor editor = mWateringPlants.edit();
            			editor.putLong(IGNOREMINUTESKEY, ignoreMinutesTemp);
            			editor.commit();
            		//	Toast.makeText(MainActivity.this, "Minutes can not be empty", Toast.LENGTH_SHORT).show();
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
            });

            
            final long alarmRangeTemp1 = mWateringPlants.getLong(ALARMRANGEMINUSKEY, 10);
            final long alarmRangeTemp2 = mWateringPlants.getLong(ALARMRANGEPLUSKEY, 0);
            
            etalarmRange1.setText( String.valueOf( alarmRangeTemp1 ) );
            etalarmRange2.setText( String.valueOf( alarmRangeTemp2 ) );
            
            etalarmRange1.addTextChangedListener(new TextWatcher() 
            {
               // @Override
                public void afterTextChanged(Editable s) 
                {
                	String minsTempStr= s.toString();
            			
            		if ( minsTempStr != null && !minsTempStr.equals("") )
            		{
            			Editor editor = mWateringPlants.edit();
            			editor.putLong(ALARMRANGEMINUSKEY, Long.parseLong(minsTempStr));
            			editor.commit();
            		}
            		else
            		{
            			Editor editor = mWateringPlants.edit();
            			editor.putLong(ALARMRANGEMINUSKEY, alarmRangeTemp1);
            			editor.commit();
            		//	Toast.makeText(MainActivity.this, "Minutes can not be empty", Toast.LENGTH_SHORT).show();
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
            });
            
            
            etalarmRange2.addTextChangedListener(new TextWatcher() 
            {
               // @Override
                public void afterTextChanged(Editable s) 
                {
                	String minsTempStr= s.toString();
            			
            		if ( minsTempStr != null && !minsTempStr.equals("") )
            		{
            			Editor editor = mWateringPlants.edit();
            			editor.putLong(ALARMRANGEPLUSKEY, Long.parseLong(minsTempStr));
            			editor.commit();
            		}
            		else
            		{
            			Editor editor = mWateringPlants.edit();
            			editor.putLong(ALARMRANGEPLUSKEY, alarmRangeTemp2);
            			editor.commit();
            		//	Toast.makeText(MainActivity.this, "Minutes can not be empty", Toast.LENGTH_SHORT).show();
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
            });
            
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);
            builder.setTitle(R.string.settings);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                	
              //  	String stringFV = etignoreMinutes.getText().toString();
               // 	if ( stringFV != null && !stringFV.equals("") )
            	//	{
                		updateNextDateAndSetAlarm();
                		sortColumn = mWateringPlants.getInt(SORTCOLUMNKEY, 1);
                		orderType = mWateringPlants.getInt(ORDERTYPEKEY, 1);
                		fillData(sortColumn, orderType);
                		MainActivity.this.removeDialog(SETTING_ID);
            //		}
              //  	else
              //  	{
                //		Toast.makeText(MainActivity.this, "No Change to the Minutes Box", Toast.LENGTH_SHORT).show();
            //    	}
                }
            });

            AlertDialog settingsDialog = builder.create();
            return settingsDialog;

    		//break;
    	}
    	return null;
    	
    }
    
    
  
    
    
    
    
    
    //5
    private void fillData(int sortColumnFV, int orderTypeFV) 
    {
        myCalendar2 = Calendar.getInstance();
        String currentTimeInMillisFV = String.valueOf( myCalendar2.getTimeInMillis() );
        
  //      Cursor cc = mDbHelper.getAllPlants(sortColumnFV, orderTypeFV, currentTimeInMillisFV);
        
        Cursor cc;
        if(fullView == 0)
        {
        	
        	long alarmrange1 = mWateringPlants.getLong(ALARMRANGEMINUSKEY, 10);
        	long alarmrange2 = mWateringPlants.getLong(ALARMRANGEPLUSKEY, 0);
        	setTitle( "Water Plants: -" + String.valueOf(alarmrange1) + " to +" + String.valueOf(alarmrange2) + " minutes" );
        	cc = mDbHelper.getPlantsWithinMinutes(sortColumnFV, orderTypeFV, currentTimeInMillisFV, alarmrange1, alarmrange2);
        	if(cc.getCount() < 1)
        	{
        		TextView emptylist = (TextView) findViewById(android.R.id.empty);
    			emptylist.setText("No Plants in this range. \nBy setting the alarm time interval in the Settings popup box, you can view plants with the latest alarm time");
    		}
        }
        else
        {
        	setTitle("Water Plants - Full List");
        	cc = mDbHelper.getAllPlants(sortColumnFV, orderTypeFV, currentTimeInMillisFV);
        	if(cc.getCount() < 1)
        	{
        		TextView emptylist = (TextView) findViewById(android.R.id.empty);
    			emptylist.setText("No Data Yet, Click the menu button and then click New to Add Plants.\n\nLong Click on a row of data to edit that plant");
    		}
        }
        mCursor = cc;
        startManagingCursor(cc);
        
        String[] from = new String[] {DbAdapter.IDDB, DbAdapter.IMAGEDB, DbAdapter.NAMEDB, DbAdapter.DAYSDB, DbAdapter.NEXTDATEDB, DbAdapter.WATEREDDB};
         int[] to = new int[]{R.id.myrow20, R.id.myrow21, R.id.myrow22, R.id.myrow23, R.id.myrow24, R.id.button33};
         
		mySimpleCursorAdapter sca = new mySimpleCursorAdapter(this, R.layout.myrow, cc, from, to, 0);
        setListAdapter(sca);
    }
    
    //6
    private class mySimpleCursorAdapter extends SimpleCursorAdapter 
    {
    	//6.1
    	public mySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) 
    	{
    		super(context, layout, c, from, to, flags);
    	}
    	
    	//6.2
		@Override
        public void bindView(View view, Context context, Cursor cursor) 
		{
    		final ViewBinder binder = super.getViewBinder();
            final int count = mTo.length;
            final int[] from = mFrom;
            final int[] to = mTo;
            final TextView idView = (TextView) view.findViewById(to[0]);
            
            String nextDateTemp = cursor.getString(from[4]);
            long nextDateTempLong = Long.parseLong(nextDateTemp);
            if(nextDateTempLong >= 0)
            	myCalendar1.setTimeInMillis( nextDateTempLong );
            else
            	myCalendar1.setTimeInMillis( nextDateTempLong * -1);
            long timeDifference = myCalendar1.getTimeInMillis() - myCalendar2.getTimeInMillis();
            long numDaysLater = timeDifference / 60000 ;
            long numDaysLaterMin = numDaysLater % 60;
            long numDaysLaterHrs = numDaysLater / 60;
            numDaysLater = numDaysLaterHrs / 24;
            numDaysLaterHrs = numDaysLaterHrs % 24;
            if(myCalendar2.get(Calendar.MINUTE) + numDaysLaterMin >= 60)
            	numDaysLaterHrs +=1;
            if(myCalendar2.get(Calendar.HOUR_OF_DAY) + numDaysLaterHrs >= 24)
            	numDaysLater +=1;
            
            for (int i = 0; i < count; i++) {
                final View v = view.findViewById(to[i]);
                
                if (v != null) {
                	v.setOnLongClickListener(new View.OnLongClickListener() 
                	{ 
                		public boolean onLongClick(View v) 
                		{
                			rowView.setBackgroundDrawable(null);
                   	 	 	rowView = (View) v.getParent();
                   	 	 	rowView.setBackgroundResource(R.drawable.rowbackground);
                   	 	 	imageFileId = Long.parseLong( idView.getText().toString() );
        
                			final String rowId = idView.getText().toString();
                 			Intent i = new Intent(MainActivity.this, EditCreatePlant.class);
                 			i.putExtra(DbAdapter.IDDB, rowId);
                 			
                			startActivityForResult(i, EDITCREATEPLANT);
                			return true;
                		}
                	});
                	

                	if (i==1)
                	{
                		String text = cursor.getString(from[i]);
						if (text == null) {
							text = "";
						}

						if (v instanceof ImageView) {
							super.setViewImage((ImageView) v, text);
						} else {
							throw new IllegalStateException(
									v.getClass().getName()
											+ " is not a "
											+ " view that can be bounds by this SimpleCursorAdapter");
						}
						ImageView myiv = (ImageView) v;
						if(myiv.getDrawable() == null)
						{
							if (v instanceof ImageView) {
								super.setViewImage((ImageView) v, noImageUri2.toString());
							} else {
								throw new IllegalStateException(
										v.getClass().getName()
												+ " is not a "
												+ " view that can be bounds by this SimpleCursorAdapter");
							}
						}
						
						v.setOnClickListener(new View.OnClickListener() 
                        {
                        	public void onClick(View vdate)
                        	{
                        		rowView.setBackgroundDrawable(null);
                        		rowView = (View) vdate.getParent();
                       	 	 	rowView.setBackgroundResource(R.drawable.rowbackground);
                       	 	 	imageFileId = Long.parseLong( idView.getText().toString() );
                       	 	
                       	 	
                        		int imageFileIdInt = Integer.parseInt( idView.getText().toString() );
                             	String strAvatarPrompt = "Take your picture to store your plant!";
                                Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(Intent.createChooser(pictureIntent, strAvatarPrompt), imageFileIdInt);
                        	}
						} );
						
						
                	}
                	else if (i==3)
					{
						int numDays = cursor.getInt(from[i]);
						if(nextDateTempLong >= 0)
						{
							if(numDays == 1)
								super.setViewText((TextView) v, "Daily");
							else if(numDaysLater > 1)
								super.setViewText((TextView) v, String.valueOf(numDaysLater) + "\nDays \nLater");
							else if(numDaysLater == 1)
								super.setViewText((TextView) v, String.valueOf(numDaysLater) + "\nDay \nLater");
							else if(numDaysLater == 0 && timeDifference >= 0)
								super.setViewText((TextView) v, "Today");
							else if( numDaysLater <= 0 && timeDifference < 0 )
								super.setViewText((TextView) v, "Old");
							else super.setViewText((TextView) v, "Old.");
						}
						else
							super.setViewText((TextView) v, "Stop");
						v.setOnClickListener(new View.OnClickListener() 
                        {
                        	public void onClick(View vdate)
                        	{
                        		rowView.setBackgroundDrawable(null);
                        	 	rowView = (View) vdate.getParent();
                        	 	rowView.setBackgroundResource(R.drawable.rowbackground);
                        	 	imageFileId = Long.parseLong( idView.getText().toString() );
                        	 	
                        	}
						} );
					}
					else if (i==4)
					{
						String waterTime = cursor.getString(from[i]);
						long waterTimeLong = Long.parseLong(waterTime);
						if(waterTimeLong >= 0)
							myCalendar1.setTimeInMillis(waterTimeLong);
						else
							myCalendar1.setTimeInMillis( waterTimeLong * -1);

						super.setViewText( (TextView) v, formatTime( myCalendar1.get(Calendar.HOUR_OF_DAY), myCalendar1.get(Calendar.MINUTE) ) );
						v.setOnClickListener(new View.OnClickListener() 
                        {
                        	public void onClick(View vdate)
                        	{
                        		rowView.setBackgroundDrawable(null);
                        	 	 rowView = (View) vdate.getParent();
                        	 	 rowView.setBackgroundResource(R.drawable.rowbackground);
                        	 	imageFileId = Long.parseLong( idView.getText().toString() );
                        	}
						} );
						
						
					}
					else if (i==5)
					{
						final int isWatered =  mDbHelper.getIsWatered( Long.parseLong( idView.getText().toString() ) );
						TextView vHint = (TextView) v;
						vHint.setText(" ");
						
				 		String buttonValue = String.valueOf(isWatered);
					 	if(isWatered == 1)
					 		buttonValue = "2";
				 		vHint.setHint(buttonValue);
				 	
				 		if(buttonValue.equals("2"))
				 			vHint.setBackgroundResource(R.drawable.water2);
				 		else if(buttonValue.equals("1"))
				 		{
				 			vHint.setBackgroundResource(R.drawable.water1);
				 		}
				 		else
				 			vHint.setBackgroundResource(R.drawable.water0);
			
						v.setOnClickListener(new View.OnClickListener() 
                        {
                        	public void onClick(View vdate)
                        	{
                              	rowView.setBackgroundDrawable(null);
                              	rowView = (View) vdate.getParent();
                                rowView.setBackgroundResource(R.drawable.rowbackground);
                                imageFileId = Long.parseLong( idView.getText().toString() );

                        		TextView tv = (TextView) vdate;
                        
                        		String isWateredcs = tv.getHint().toString();
                        		if (isWateredcs.equals("1"))
                        		{
                        			if( lastdateTempString2 !=null )
                        				lastdateTempString2 = mDbHelper.updateLastDateWater( Long.parseLong( idView.getText().toString() ), lastdateTempString2, 0 );
                        			else
                        				Toast.makeText(MainActivity.this, "Unable to update the water date", Toast.LENGTH_SHORT).show();
                        		
                        			tv.setBackgroundResource(R.drawable.water0);
                        			tv.setHint("0");
                        		}
                        		else if (isWateredcs.equals("0"))
                        		{
                        			lastdateTempString2 = mDbHelper.updateLastDateWater( Long.parseLong( idView.getText().toString() ), String.valueOf( Calendar.getInstance().getTimeInMillis() ), 1 );
                        			tv.setBackgroundResource(R.drawable.water1);
                        			tv.setHint("1");
                        		}
                        		else if (isWateredcs.equals("2"))
                        		{
                        			lastdateTempString2 = mDbHelper.updateLastDateWater( Long.parseLong( idView.getText().toString() ), String.valueOf( Calendar.getInstance().getTimeInMillis() ), 1 );
                        
                        			tv.setBackgroundResource(R.drawable.water1);
                        			tv.setHint("1");
                        		}
                        		
                        	
                        	}
						} );
							
					}
                	else 
                	{
                	
                		
                		v.setOnClickListener(new View.OnClickListener() 
                        {
                        	public void onClick(View vdate)
                        	{
                       			rowView.setBackgroundDrawable(null);
                        	 	rowView = (View) vdate.getParent();
                        	 	rowView.setBackgroundResource(R.drawable.rowbackground);
                        	 	imageFileId = Long.parseLong( idView.getText().toString() );
                        	 	
                        	}
						} );
                		
                		
                		
						boolean bound = false;
						if (binder != null) {
							bound = binder.setViewValue(v, cursor, from[i]);
						}

						if (!bound) 
						{
							String text = cursor.getString(from[i]);
							if (text == null) {
								text = "";
							}

							if (v instanceof TextView) {
								setViewText((TextView) v, text);
							} else if (v instanceof ImageView) {
								super.setViewImage((ImageView) v, text);
							} else {
								throw new IllegalStateException(
										v.getClass().getName()
												+ " is not a "
												+ " view that can be bounds by this SimpleCursorAdapter");
							}
							
							
						}//
                	}
                }
                
                
                
                
            }
            
 
        }
		
		
    	//
    	
    }
    
    
    //7
    public void startAlarm1(long when) 
    {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, myService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
    }
 /*   
    //8
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
    {
		super.onListItemClick(l, v, position, id);
		 Toast.makeText(MainActivity.this, "    wwTODO: t onListItemClick " , Toast.LENGTH_SHORT).show();
	}
    
   */ 
    //9
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
    
    
    
    
}

	

























