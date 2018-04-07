package com.example.pef.prathamopenschool;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pef.prathamopenschool.gpsmodule.Interfaces.GpsTestListener;
import com.example.pef.prathamopenschool.gpsmodule.util.GpsTestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.example.pef.prathamopenschool.gpsmodule.util.GpsTestUtil.writeGnssMeasurementToLog;
import static com.example.pef.prathamopenschool.gpsmodule.util.GpsTestUtil.writeNavMessageToLog;
import static com.example.pef.prathamopenschool.gpsmodule.util.GpsTestUtil.writeNmeaToLog;

//import android.support.annotation.NonNull;

public class MultiPhotoSelectActivity extends AppCompatActivity implements LocationListener, GpsTestListener {

    public static DilogBoxForProcess dilog;
    //\public static TextToSp tts;
    public ImageAdapter imageAdapter;
    List<JSONArray> students;
    List<String> groupNames, assignedIds;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    StatusDBHelper statusDBHelper;
    StudentDBHelper studentDBHelper;
    GroupDBHelper groupDBHelper;
    TextView tv_title;
    static String presentStudents[];
    static String programID, language;
    static String selectedGroupName;
    static String selectedGroupId;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button next;
    Utility utility;
    ArrayList<JSONObject> attendenceData;
    static String sessionId;
    public static Boolean grpSelectFlag;
    public static String sessionStartTime, selectedGroupsScore = "";

    Context sessionContex;
    static CountDownTimer cd;
    static Long timeout = (long) 20000 * 60;
    static Long duration = timeout;
    boolean timer;
    String newNodeList;
    ScoreDBHelper scoreDBHelper;
    PlayVideo playVideo;
    public static RelativeLayout myView;
    static Boolean pauseFlg = false;
    public static String deviceID = "DeviceID";
    int aajKaSawalPlayed = 3;
    boolean doubleBackToExitPressedOnce = false;
    String checkQJson;

    // GPS
    private static final String TAG = "GpsTest";

    boolean mStarted;

    boolean mFaceTrueNorth;

    boolean mWriteGnssMeasurementToLog;

    boolean mLogNmea;

    boolean mWriteNmeaTimestampToLog;

    private LocationManager mLocationManager;

    private LocationProvider mProvider;

    /**
     * Android M (6.0.1) and below status and listener
     */
    private GpsStatus mLegacyStatus;

    private GpsStatus.Listener mLegacyStatusListener;

    private GpsStatus.NmeaListener mLegacyNmeaListener;

    /**
     * Android N (7.0) and above status and listeners
     */
    private GnssStatus mGnssStatus;

    private GnssStatus.Callback mGnssStatusListener;

    private GnssMeasurementsEvent.Callback mGnssMeasurementsListener; // For SNRs

    private OnNmeaMessageListener mOnNmeaMessageListener;

    private GnssNavigationMessage.Callback mGnssNavMessageListener;

    // Listeners for Fragments
    private ArrayList<GpsTestListener> mGpsTestListeners = new ArrayList<GpsTestListener>();

    private Location mLastLocation;

    private long minTime; // Min Time between location updates, in milliseconds

    private float minDistance; // Min Distance between location updates, in meters
    private MultiPhotoSelectActivity sInstance;
    private long mFixTime;
    SimpleDateFormat mDateFormat = new SimpleDateFormat("hh:mm:ss.SS a");
    private String mTtff;
//    TextView time;
//    TextView Lat_lng;

    Dialog gpsTimeDialog;
    TextView tv_msg, tv_msgBottom;
    boolean appName = false;
    StatusDBHelper s;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.group_select);
//        myView = (RelativeLayout) findViewById(R.id.my_layoutId);
//        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        // Check if location & gpstime is available
        s = new StatusDBHelper(MultiPhotoSelectActivity.this);
        boolean latitudeAvailable = false;
        boolean longitudeAvailable = false;
        boolean GPSDateTimeAvailable = false;

        boolean androidIDAvailable = false;
        boolean SerialIDAvailable = false;
        boolean apkVersion = false;

        latitudeAvailable = s.initialDataAvailable("Latitude");
        longitudeAvailable = s.initialDataAvailable("Longitude");
        GPSDateTimeAvailable = s.initialDataAvailable("GPSDateTime");

        androidIDAvailable = s.initialDataAvailable("AndroidID");
        SerialIDAvailable = s.initialDataAvailable("SerialID");

        apkVersion = s.initialDataAvailable("apkVersion");
        appName = s.initialDataAvailable("appName");

        if (androidIDAvailable == false) {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            String deviceID = "";
            deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            s.insertInitialData("AndroidID", deviceID);
        }

        if (SerialIDAvailable == false) {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            s.insertInitialData("SerialID", Build.SERIAL);
        }

        if (apkVersion == false) {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            PackageInfo pInfo = null;
            String verCode = "";
            try {
                pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                verCode = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            s.insertInitialData("apkVersion", verCode);
        } else {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            PackageInfo pInfo = null;
            String verCode = "";
            try {
                pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                verCode = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            s.Update("apkVersion", verCode);
        }

        // Timer Start
        // Todo get GPS DateTime & Location
//        if (latitudeAvailable == false || longitudeAvailable == false || GPSDateTimeAvailable == false) {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mProvider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        if (MyApplication.timer == null) {
            // Execute GPS Location & Time Dialog
            // GPS Signal Dialog
            gpsTimeDialog = new Dialog(this);
            gpsTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            gpsTimeDialog.setContentView(R.layout.customgpsdialog);
            gpsTimeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            GifView gifView = gpsTimeDialog.findViewById(R.id.gif_satellite);
            gifView.setGifResource(R.drawable.satellite);

            // Setting Dialog
            gpsTimeDialog.setCanceledOnTouchOutside(false);
            gpsTimeDialog.setCancelable(false);
            gpsTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            gpsTimeDialog.show();

            // GET GPS TIME
            sInstance = this;

            // execution of the app
            if (mProvider == null) {
                Log.e(TAG, "Unable to get GPS_PROVIDER");
                Toast.makeText(this, "gps_not_supported", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            gpsStart();

            /*           // if time more than minute then show " Go outside dialog " i.e set message
            tv_msg = gpsTimeDialog.findViewById(R.id.tv_msg);
 */
            // if time more than minute then show " Go outside dialog " i.e set message
            tv_msgBottom = gpsTimeDialog.findViewById(R.id.tv_msgBottom);
            tv_msgBottom.setVisibility(View.GONE);
            try {
                doAfterSomeTime();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } /*else {

            // GET GPS TIME
            sInstance = this;

            // execution of the app
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mProvider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
            if (mProvider == null) {
                Log.e(TAG, "Unable to get GPS_PROVIDER");
                Toast.makeText(this, "gps_not_supported", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            gpsStart();
        }*/
        startService(new Intent(this, WebViewService.class));

//        tv_title = (TextView) findViewById(R.id.tv_select);

        dilog = new DilogBoxForProcess();
        grpSelectFlag = true;
        attendenceData = new ArrayList<>();
        ActionBar actBar = getSupportActionBar();
        statusDBHelper = new StatusDBHelper(this);
        studentDBHelper = new StudentDBHelper(this);
        groupDBHelper = new GroupDBHelper(this);
        utility = new Utility();

        sessionId = utility.GetUniqueID().toString();
        sessionContex = this;
        playVideo = new PlayVideo();
        MainActivity.sessionFlg = false;
        if (assessmentLogin.assessmentFlg)
            newNodeList = getIntent().getStringExtra("nodeList");

        // Generate Unique Device ID
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public void doAfterSomeTime() {
        Runnable delayedTask = new Runnable() {
            @Override
            public void run() {
                if (gpsTimeDialog.isShowing()) {
                    tv_msgBottom.setVisibility(View.VISIBLE);
                }
            }
        };
        mainThreadHandler.postDelayed(delayedTask, 60000);
    }


    public void displayStudents() {
        String assignedGroupIDs[];
        students = new ArrayList<JSONArray>();
        groupNames = new ArrayList<String>();
        assignedIds = new ArrayList<String>();
        try {
            programID = getProgramId();

            if (programID.equals("1") || programID.equals("3") || programID.equals("4")) {
                tv_title.setText("Select Groups");
            } else {
                tv_title.setText("Select Units");
            }

            if (programID.equals("1") || programID.equals("2") || programID.equals("3") || programID.equals("4")) {
                next = (Button) findViewById(R.id.goNext);
                if (programID.equals("1") || programID.equals("3") || programID.equals("4")) {
                    next.setText("Select Groups");
                } else {
                    next.setText("Select Units");
                }
                assignedGroupIDs = statusDBHelper.getGroupIDs();
                if (!assignedGroupIDs[0].equals("")) {
                    for (int i = 0; i < assignedGroupIDs.length; i++) {
                        if (!assignedGroupIDs[i].equals("0")) {
                            students.add(studentDBHelper.getStudentsList(assignedGroupIDs[i]));
                            groupNames.add(groupDBHelper.getGroupById(assignedGroupIDs[i]));
                            assignedIds.add(assignedGroupIDs[i]);
                        }
                    }
                }
                next.setClickable(false);
                int groupCount = groupNames.size();
                if (groupNames.isEmpty()) {
                    if (programID.equals("1") || programID.equals("3") || programID.equals("4")) {
                        Toast.makeText(this, "Assign Groups First", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Assign Units First", Toast.LENGTH_LONG).show();
                    }
                } else {
                    next.setClickable(false);
                    radioGroup.setPadding(0, 50, 0, 0);
                    RadioButton rb;
                    for (int i = 0; i < groupCount; i++) {
                        rb = new RadioButton(this);

                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(8, 0, 0, 0);
                        rb.setLayoutParams(params);

                        rb.setHeight(160);
                        rb.setWidth(135);
                        rb.setTextSize(16);

                        // For HL
                        if (MultiPhotoSelectActivity.programID.equals("1") || MultiPhotoSelectActivity.programID.equals("3") || MultiPhotoSelectActivity.programID.equals("4")) {
                            rb.setBackgroundResource(R.drawable.groups);
                        } else if (MultiPhotoSelectActivity.programID.equals("2")) {
                            rb.setBackgroundResource(R.drawable.units);
                        }

                        rb.setPadding(0, 0, 2, 0);
                        rb.setId(i);
                        rb.setText(groupNames.get(i));
                        radioGroup.addView(rb);
                    }
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                // get selected radio button from radioGroup
                                int selectedId = radioGroup.getCheckedRadioButtonId();

                                if (selectedId == -1) {
                                    if (programID.equals("1") || programID.equals("3") || programID.equals("4")) {
                                        Toast.makeText(MultiPhotoSelectActivity.this, "Select atleast one group", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MultiPhotoSelectActivity.this, "Select atleast one unit", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // find the radiobutton by returned id
                                    radioButton = (RadioButton) findViewById(selectedId);
                                    radioButton.setBackgroundColor(getResources().getColor(R.color.selected));
                                    selectedGroupId = assignedIds.get(selectedId);
                                    selectedGroupName = (String) radioButton.getText();
                                    if (selectedGroupName.equals(null)) {
                                        Toast.makeText(MultiPhotoSelectActivity.this, "Assign Groups First", Toast.LENGTH_SHORT).show();
                                    } else {
                                        setSelectedStudents(selectedGroupName);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Invalid Program Id", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLanguage(Context c) {
        StatusDBHelper statdb;
        statdb = new StatusDBHelper(c);
        return statdb.getValue("TabLanguage");
    }

    public void setSelectedStudents(String groupName) throws IOException {
        grpSelectFlag = false;
        //selectedGroupId = groupDBHelper.getGroupIdByName(selectedGroupName);
        students = new ArrayList<JSONArray>();
        students.add(studentDBHelper.getStudentsList(selectedGroupId));
        setContentView(R.layout.layout_multi_photo_select);
        populateImagesFromGallery();
    }

    // Reading configuration Json From SDCard
    public String getProgramId() {

        String progIDString = null;
        try {
            File myJsonFile = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Json/Config.json");
            FileInputStream stream = new FileInputStream(myJsonFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

            JSONObject jsonObj = new JSONObject(jsonStr);
            progIDString = jsonObj.getString("programId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return progIDString;
    }


    public void setLanguage() {
        String langString = null;
        try {
            File myJsonFile = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Json/Config.json");
            FileInputStream stream = new FileInputStream(myJsonFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            JSONObject jsonObj = new JSONObject(jsonStr);
            langString = jsonObj.getString("programLanguage");

            StatusDBHelper sdb;
            sdb = new StatusDBHelper(MultiPhotoSelectActivity.this);
            sdb.Update("TabLanguage", langString);
            BackupDatabase.backup(MultiPhotoSelectActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btnChoosePhotosClick(View v) throws JSONException {

        sessionId = utility.GetUniqueID().toString();
        selectedGroupsScore = "";
        Attendance attendance = null;
        AttendanceDBHelper attendanceDBHelper = new AttendanceDBHelper(this);

        // If no student present in RI
        if (programID.equals("2") && students.get(0).length() == 0) {
            presentStudents = new String[1];
            presentStudents[0] = "RI";
            try {
                attendance = new Attendance();
                attendance.SessionID = sessionId;
                attendance.PresentStudentIds = "RI";
                attendance.GroupID = selectedGroupId;
                if (!selectedGroupsScore.contains(attendance.GroupID)) {
                    selectedGroupsScore += attendance.GroupID + ",";
                }
                attendanceDBHelper.Add(attendance);
                BackupDatabase.backup(this);


                /* -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- AAJ KA SAWAAL PLAYED ? -*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*- */

                // Decide the next screen depending on aajKaSawalPlayed status

                // Get Current Date
                String currentDate = utility.GetCurrentDate();

                // Get Currently Selected Group
                String currentlySelectedGroup = selectedGroupId;

                // Fetch records
                StatusDBHelper sdbh = new StatusDBHelper(sessionContex);
                List<String> AKSRecords = sdbh.getAKSRecordsOfSelectedGroup(currentlySelectedGroup);

                // Loop to get Aaj Ka Sawaal Played Status
                if (!AKSRecords.equals(null)) {

                    if (AKSRecords.size() == 0) {
                        // No Record Found
                        aajKaSawalPlayed = 0;
                    }

                    for (int i = 0; i < AKSRecords.size(); i++) {

                        if (AKSRecords.get(i).contains(currentDate)) {
                            // If Record Found i.e Aaj Ka Sawaal Played
                            aajKaSawalPlayed = 1;
                        } else {
                            // If Record NOT Found i.e Aaj Ka Sawaal Played
                            aajKaSawalPlayed = 0;
                        }
                    }

                }

                //set accordingly
                // int aajKaSawalPlayed = sdbh.getAajKaSawalPlayedStatus(selectedGroupId); //OLD LOGIC

                // if Questions.json not present
                JSONArray queJsonArray = null;
                try {
                    checkQJson = loadQueJSONFromAsset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (checkQJson == null) {
                    aajKaSawalPlayed = 3;
                }

                // Aaj Ka Sawaal Played
                if (aajKaSawalPlayed == 1) {
                    Intent main = new Intent(MultiPhotoSelectActivity.this, MainActivity.class);
                    if (assessmentLogin.assessmentFlg) {
                        main.putExtra("nodeList", newNodeList.toString());
                    }
                    main.putExtra("aajKaSawalPlayed", "1");
                    main.putExtra("selectedGroupId", selectedGroupId);
                    startActivity(main);
                    finish();
                }
                // Aaj Ka Sawaal NOT Played
                else if (aajKaSawalPlayed == 0) {

                    // Update updateTrailerCountbyGroupID to 1 if played
                    StatusDBHelper updateTrailerCount = new StatusDBHelper(MultiPhotoSelectActivity.this);
                    updateTrailerCount.updateTrailerCountbyGroupID(1, selectedGroupId);
                    BackupDatabase.backup(MultiPhotoSelectActivity.this);

                    Intent main = new Intent(MultiPhotoSelectActivity.this, MainActivity.class);
                    if (assessmentLogin.assessmentFlg) {
                        main.putExtra("nodeList", newNodeList.toString());
                    }
                    main.putExtra("aajKaSawalPlayed", "0");
                    main.putExtra("selectedGroupId", selectedGroupId);

                    startActivity(main);
                    finish();
                }

                // if Questions.json not present
                else if (aajKaSawalPlayed == 3) {

                    Intent main = new Intent(MultiPhotoSelectActivity.this, MainActivity.class);
                    if (assessmentLogin.assessmentFlg) {
                        main.putExtra("nodeList", newNodeList.toString());
                    }
                    main.putExtra("aajKaSawalPlayed", "3");
                    main.putExtra("selectedGroupId", selectedGroupId);


                    startActivity(main);
                    finish();
                }

                /*Intent main = new Intent(MultiPhotoSelectActivity.this, MainActivity.class);
                if (assessmentLogin.assessmentFlg)
                    main.putExtra("nodeList", newNodeList.toString());
                startActivity(main);
                finish();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Atleast one student is present in group/ unit
        else {
            ArrayList<JSONObject> selectedItems = imageAdapter.getCheckedItems();
            if (selectedItems.size() > 0) {

                presentStudents = new String[selectedItems.size()];
                try {
                    if (selectedItems != null && selectedItems.size() > 0) {
                        for (int i = 0; i < selectedItems.size(); i++) {
                            attendance = new Attendance();
                            attendance.SessionID = sessionId;
                            attendance.PresentStudentIds = selectedItems.get(i).getString("stdId");
                            presentStudents[i] = selectedItems.get(i).getString("stdId");
                            attendance.GroupID = selectedItems.get(i).getString("grpId");
                            if (!selectedGroupsScore.contains(attendance.GroupID)) {
                                selectedGroupsScore += attendance.GroupID + ",";
                            }
                            attendanceDBHelper.Add(attendance);
                        }
                        BackupDatabase.backup(this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                /* -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- AAJ KA SAWAAL PLAYED ? -*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*- */


                // Decide the next screen depending on aajKaSawalPlayed status

                // Get Current Date
                String currentDate = utility.GetCurrentDate();

                // Get Currently Selected Group
                String currentlySelectedGroup = selectedGroupId;

                // Fetch records
                StatusDBHelper sdbh = new StatusDBHelper(sessionContex);
                List<String> AKSRecords = sdbh.getAKSRecordsOfSelectedGroup(currentlySelectedGroup);

                // Loop to get Aaj Ka Sawaal Played Status
                if (!AKSRecords.equals(null)) {

                    if (AKSRecords.size() == 0) {
                        // No Record Found
                        aajKaSawalPlayed = 0;
                    }

                    for (int i = 0; i < AKSRecords.size(); i++) {

                        if (AKSRecords.get(i).contains(currentDate)) {
                            // If Record Found i.e Aaj Ka Sawaal Played
                            aajKaSawalPlayed = 1;
                        } else {
                            // If Record NOT Found i.e Aaj Ka Sawaal Played
                            aajKaSawalPlayed = 0;
                        }
                    }

                }

                //set accordingly
                // int aajKaSawalPlayed = sdbh.getAajKaSawalPlayedStatus(selectedGroupId); //OLD LOGIC

                // if Questions.json not present
                JSONArray queJsonArray = null;
                try {
                    checkQJson = loadQueJSONFromAsset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (checkQJson == null) {
                    aajKaSawalPlayed = 3;
                }

                if (aajKaSawalPlayed == 1) {
                    Intent main = new Intent(MultiPhotoSelectActivity.this, MainActivity.class);
                    if (assessmentLogin.assessmentFlg) {
                        main.putExtra("nodeList", newNodeList.toString());
                    }
                    main.putExtra("aajKaSawalPlayed", "1");
                    main.putExtra("selectedGroupId", selectedGroupId);


                    startActivity(main);
                    finish();
                } else if (aajKaSawalPlayed == 0) {

                    // Update updateTrailerCountbyGroupID to 1 if played
                    StatusDBHelper updateTrailerCount = new StatusDBHelper(MultiPhotoSelectActivity.this);
                    updateTrailerCount.updateTrailerCountbyGroupID(1, selectedGroupId);
                    BackupDatabase.backup(MultiPhotoSelectActivity.this);

                    Intent main = new Intent(MultiPhotoSelectActivity.this, MainActivity.class);
                    if (assessmentLogin.assessmentFlg) {
                        main.putExtra("nodeList", newNodeList.toString());
                    }
                    main.putExtra("aajKaSawalPlayed", "0");
                    main.putExtra("selectedGroupId", selectedGroupId);


                    startActivity(main);
                    finish();
                }
                // if Questions.json not present
                else if (aajKaSawalPlayed == 3) {

                    Intent main = new Intent(MultiPhotoSelectActivity.this, MainActivity.class);
                    if (assessmentLogin.assessmentFlg) {
                        main.putExtra("nodeList", newNodeList.toString());
                    }
                    main.putExtra("aajKaSawalPlayed", "3");
                    main.putExtra("selectedGroupId", selectedGroupId);

                    startActivity(main);
                    finish();
                }


            } else {
                Toast.makeText(this, "Please Select your Profile !!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Reading CRL Json From Internal Memory
    public String loadQueJSONFromAsset() {
        String queJsonStr = null;

        try {
            File queJsonSDCard = new File(splashScreenVideo.fpath + "AajKaSawaal/", "Questions.json");
            FileInputStream stream = new FileInputStream(queJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                queJsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return queJsonStr;
    }

    public void populateImagesFromGallery() throws IOException {
        if (!mayRequestGalleryImages()) {
            return;
        }

        ArrayList<JSONObject> imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }

    private boolean mayRequestGalleryImages() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            //promptStoragePermission();
            showPermissionRationaleSnackBar();
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
        }

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {

            case REQUEST_FOR_STORAGE_PERMISSION: {

                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        try {
                            populateImagesFromGallery();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                            showPermissionRationaleSnackBar();
                        } else {
                            Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;
            }
        }
    }

    private ArrayList<JSONObject> loadPhotosFromNativeGallery() throws IOException {
        ArrayList<JSONObject> imageUrls = new ArrayList<JSONObject>();
        JSONObject studentWithImgs;

        File folder = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/StudentProfiles");
        if (folder.exists()) {

            File photos[] = folder.listFiles();

            String fileNameWithExtension = "";//default fileName

            try {
                for (int j = 0; j < students.size(); j++) {//noOfGroups
                    for (int k = 0; k < students.get(j).length(); k++) {//noOfStudents
                        int flag = 0;
                        String stdid = students.get(j).getJSONObject(k).getString("studentId");
                        String stdName = students.get(j).getJSONObject(k).getString("studentName");
                        String grpId = students.get(j).getJSONObject(k).getString("groupId");
                        String gender = students.get(j).getJSONObject(k).getString("gender");
                        for (int i = 0; i < photos.length; i++) {//noOfImages
                            fileNameWithExtension = photos[i].getName();
                            String filePath = photos[i].getAbsolutePath();
                            String[] fileName = fileNameWithExtension.split("\\.");
                            if (fileName[0].equals(stdid)) {
                                flag = 1;
                                studentWithImgs = new JSONObject();
                                studentWithImgs.put("id", stdid);
                                studentWithImgs.put("name", stdName);
                                studentWithImgs.put("grpId", grpId);
                                studentWithImgs.put("imgPath", filePath);
                                imageUrls.add(studentWithImgs);
                                break;
                            }
                        }
                        if (flag == 0) {
                            studentWithImgs = new JSONObject();
                            studentWithImgs.put("id", stdid);
                            studentWithImgs.put("name", stdName);
                            studentWithImgs.put("grpId", grpId);
                            if ((gender.equals("M")) || (gender.equals("Male")))
                                studentWithImgs.put("imgPath", Environment.getExternalStorageDirectory() + "/.POSinternal/StudentProfiles/Boys/" + generateRandomNumber() + ".png");
                            else
                                studentWithImgs.put("imgPath", Environment.getExternalStorageDirectory() + "/.POSinternal/StudentProfiles/Girls/" + generateRandomNumber() + ".png");
                            imageUrls.add(studentWithImgs);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return imageUrls;
        } else {
            return null;
        }
    }

    private String generateRandomNumber() {
        Random rand = new Random();

        int n = rand.nextInt(3) + 1;
        return Integer.toString(n);
    }


    private void initializeRecyclerView(ArrayList<JSONObject> imageUrls) {
        imageAdapter = new ImageAdapter(this, imageUrls);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 8);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset2));
        recyclerView.setAdapter(imageAdapter);
    }

    private void showPermissionRationaleSnackBar() {
        Snackbar.make(findViewById(R.id.btn_continue), getString(R.string.permission_rationale),
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(MultiPhotoSelectActivity.this,
                        new String[]{READ_EXTERNAL_STORAGE},
                        REQUEST_FOR_STORAGE_PERMISSION);
            }
        }).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent goToAdminLogin = new Intent(MultiPhotoSelectActivity.this, AdminActivity.class);
//            finish();
            startActivity(goToAdminLogin);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (grpSelectFlag)
            super.onBackPressed();
        else {
//            finish();
//            startActivity(getIntent());
            setContentView(R.layout.group_select);
            onResume();
        }
    }
    /*@Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.group_select);
        myView = (RelativeLayout) findViewById(R.id.my_layoutId);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        tv_title = (TextView) findViewById(R.id.tv_select);
        next = (Button) findViewById(R.id.goNext);

        if (radioGroup.getChildCount() > 0) {
            radioGroup.removeAllViews();
        }
        displayStudents();
        setLanguage();

        // set Title according to program
        if (MultiPhotoSelectActivity.programID.equals("1"))
            setTitle("Pratham Digital - H Learning");
        else if (MultiPhotoSelectActivity.programID.equals("2"))
            setTitle("Pratham Digital - Read India");
        else if (MultiPhotoSelectActivity.programID.equals("3"))
            setTitle("Pratham Digital - Second Chance");
        else if (MultiPhotoSelectActivity.programID.equals("4"))
            setTitle("Pratham Digital - Pratham Institute");


        if (appName == false) {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            // app name
            if (MultiPhotoSelectActivity.programID.equals("1"))
                s.insertInitialData("appName", "Pratham Digital - H Learning");
            else if (MultiPhotoSelectActivity.programID.equals("2"))
                s.insertInitialData("appName", "Pratham Digital - Read India");
            else if (MultiPhotoSelectActivity.programID.equals("3"))
                s.insertInitialData("appName", "Pratham Digital - Second Chance");
            else if (MultiPhotoSelectActivity.programID.equals("4"))
                s.insertInitialData("appName", "Pratham Digital - Pratham Institute");

        } else {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            // app name
            if (MultiPhotoSelectActivity.programID.equals("1"))
                s.Update("appName", "Pratham Digital - H Learning");
            else if (MultiPhotoSelectActivity.programID.equals("2"))
                s.Update("appName", "Pratham Digital - Read India");
            else if (MultiPhotoSelectActivity.programID.equals("3"))
                s.Update("appName", "Pratham Digital - Second Chance");
            else if (MultiPhotoSelectActivity.programID.equals("4"))
                s.Update("appName", "Pratham Digital - Pratham Institute");

        }

        BackupDatabase.backup(MultiPhotoSelectActivity.this);


        // Check if location & gpstime is available
        StatusDBHelper s = new StatusDBHelper(MultiPhotoSelectActivity.this);
        boolean latitudeAvailable = false;
        boolean longitudeAvailable = false;
        boolean GPSTimeAvailable = false;

        latitudeAvailable = s.initialDataAvailable("Latitude");
        longitudeAvailable = s.initialDataAvailable("Longitude");
        GPSTimeAvailable = s.initialDataAvailable("GPSTime");

//        if (latitudeAvailable == false || longitudeAvailable == false || GPSTimeAvailable == false) {
        if (true) {
            addStatusListener();
            addNmeaListener();
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                promptEnableGps();
            }
            /**
             * Check preferences to see how these componenets should be initialized
             */
            checkTimeAndDistance(null);
            if (GpsTestUtil.isGnssStatusListenerSupported()) {
                addGnssMeasurementsListener();
            }
            if (GpsTestUtil.isGnssStatusListenerSupported()) {
                addNavMessageListener();
            }
        }


        // session
        duration = timeout;
        if (pauseFlg) {
            cd.cancel();
            pauseFlg = false;
        }


        if (!isMyServiceRunning(GPSLocationService.class)) {
            // Start Location Service
            startService(new Intent(this, GPSLocationService.class));
        } else {
            //Service Already runnung
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {

        try {
            mLocationManager.removeUpdates(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("destroyed", "------------- Multi Photo --------------- in Destroy");

/*        if (!CardAdapter.vidFlg) {
            scoreDBHelper = new ScoreDBHelper(sessionContex);
            playVideo.calculateEndTime(scoreDBHelper);
            BackupDatabase.backup(sessionContex);
            try {
                finishAffinity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // Remove status listeners
        removeStatusListener();
        removeNmeaListener();
        if (GpsTestUtil.isGnssStatusListenerSupported()) {
            removeNavMessageListener();
        }
        if (GpsTestUtil.isGnssStatusListenerSupported()) {
            removeGnssMeasurementsListener();
        }

        super.onPause();

        pauseFlg = true;

        cd = new CountDownTimer(duration, 1000) {
            //cd = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                duration = millisUntilFinished;
                timer = true;
            }

            @Override
            public void onFinish() {
                timer = false;
                MainActivity.sessionFlg = true;
                if (!CardAdapter.vidFlg) {
                    scoreDBHelper = new ScoreDBHelper(sessionContex);
                    playVideo.calculateEndTime(scoreDBHelper);
                    BackupDatabase.backup(sessionContex);
                    try {
                        finishAffinity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


//    // To Select only one radio button from Radio Group
//    public void oneRadioButtonClicked(View view) {
//
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_options);
//
//        switch (view.getId()) {
//
//
//            case R.id.rb_O1: {
//                rg.clearCheck();
//                rg.check(view.getId());
//                break;
//            }
//
//            case R.id.rb_O2: {
//                rg.clearCheck();
//                rg.check(view.getId());
//                break;
//            }
//
//            case R.id.rb_O3: {
//                rg.clearCheck();
//                rg.check(view.getId());
//                break;
//            }
//
//            case R.id.rb_O4: {
//                rg.clearCheck();
//                rg.check(view.getId());
//                break;
//            }
//
//        }
//    }

    // GPS TIME
    void addListener(GpsTestListener listener) {
        mGpsTestListeners.add(listener);
    }

    @SuppressLint("MissingPermission")
    public synchronized void gpsStart() {
        if (!mStarted) {
            mLocationManager
                    .requestLocationUpdates(mProvider.getName(), 5000, 0, this);
            mStarted = true;
        }
        for (GpsTestListener listener : mGpsTestListeners) {
            listener.gpsStart();
        }
    }

    public synchronized void gpsStop() {
        if (mStarted) {
            mLocationManager.removeUpdates(this);
            mStarted = false;
        }
        for (GpsTestListener listener : mGpsTestListeners) {
            listener.gpsStop();
        }
    }

    private int mSvCount, mPrns[], mConstellationType[], mUsedInFixCount;
    private float mSnrCn0s[], mSvElevations[], mSvAzimuths[];
    private boolean mHasEphemeris[], mHasAlmanac[], mUsedInFix[];


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSatelliteStatusChanged(GnssStatus status) {
        updateGnssStatus(status);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateGnssStatus(GnssStatus status) {
        mDateFormat.format(mFixTime);
//        time.setText(mDateFormat.format(mFixTime) + "");
        if (mPrns == null) {
            /**
             * We need to allocate arrays big enough so we don't overflow them.  Per
             * https://developer.android.com/reference/android/location/GnssStatus.html#getSvid(int)
             * 255 should be enough to contain all known satellites world-wide.
             */
            final int MAX_LENGTH = 255;
            mPrns = new int[MAX_LENGTH];
            mSnrCn0s = new float[MAX_LENGTH];
            mSvElevations = new float[MAX_LENGTH];
            mSvAzimuths = new float[MAX_LENGTH];
            mConstellationType = new int[MAX_LENGTH];
            mHasEphemeris = new boolean[MAX_LENGTH];
            mHasAlmanac = new boolean[MAX_LENGTH];
            mUsedInFix = new boolean[MAX_LENGTH];
        }

        final int length = status.getSatelliteCount();
        mSvCount = 0;
        mUsedInFixCount = 0;
        while (mSvCount < length) {
            int prn = status.getSvid(mSvCount);
            mPrns[mSvCount] = prn;
            mConstellationType[mSvCount] = status.getConstellationType(mSvCount);
            mSnrCn0s[mSvCount] = status.getCn0DbHz(mSvCount);
            mSvElevations[mSvCount] = status.getElevationDegrees(mSvCount);
            mSvAzimuths[mSvCount] = status.getAzimuthDegrees(mSvCount);
            mHasEphemeris[mSvCount] = status.hasEphemerisData(mSvCount);
            mHasAlmanac[mSvCount] = status.hasAlmanacData(mSvCount);
            mUsedInFix[mSvCount] = status.usedInFix(mSvCount);
            if (status.usedInFix(mSvCount)) {
                mUsedInFixCount++;
            }

            mSvCount++;
        }
    }

    @Deprecated
    private void updateLegacyStatus(GpsStatus status) {
        mDateFormat.format(mFixTime);
//        time.setText(mDateFormat.format(mFixTime) + "");
        Iterator<GpsSatellite> satellites = status.getSatellites().iterator();
        if (mPrns == null) {
            int length = status.getMaxSatellites();
            mPrns = new int[length];
            mSnrCn0s = new float[length];
            mSvElevations = new float[length];
            mSvAzimuths = new float[length];
            // Constellation type isn't used, but instantiate it to avoid NPE in legacy devices
            mConstellationType = new int[length];
            mHasEphemeris = new boolean[length];
            mHasAlmanac = new boolean[length];
            mUsedInFix = new boolean[length];
        }

        mSvCount = 0;
        mUsedInFixCount = 0;
        while (satellites.hasNext()) {
            GpsSatellite satellite = satellites.next();
            int prn = satellite.getPrn();
            mPrns[mSvCount] = prn;
            mSnrCn0s[mSvCount] = satellite.getSnr();
            mSvElevations[mSvCount] = satellite.getElevation();
            mSvAzimuths[mSvCount] = satellite.getAzimuth();
            mHasEphemeris[mSvCount] = satellite.hasEphemeris();
            mHasAlmanac[mSvCount] = satellite.hasAlmanac();
            mUsedInFix[mSvCount] = satellite.usedInFix();
            if (satellite.usedInFix()) {
                mUsedInFixCount++;
            }
            mSvCount++;
        }
    }

    @Override
    public void onGpsStatusChanged(int event, GpsStatus status) {
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                break;

            case GpsStatus.GPS_EVENT_FIRST_FIX:
                mTtff = GpsTestUtil.getTtffString(status.getTimeToFirstFix());
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                updateLegacyStatus(status);
                break;
        }
    }

    @Override
    public void onGnssFirstFix(int ttffMillis) {
        mTtff = GpsTestUtil.getTtffString(ttffMillis);
    }

    @Override
    public void onGnssStarted() {

    }

    @Override
    public void onGnssStopped() {

    }

    @Override
    public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) {

    }

    @Override
    public void onOrientationChanged(double orientation, double tilt) {

    }

    @Override
    public void onNmeaMessage(String message, long timestamp) {

    }

    private boolean sendExtraCommand(String command) {
        return mLocationManager.sendExtraCommand(LocationManager.GPS_PROVIDER, command, null);
    }

    private void addStatusListener() {
        if (GpsTestUtil.isGnssStatusListenerSupported()) {
            addGnssStatusListener();
        } else {
            addLegacyStatusListener();
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    private void addGnssStatusListener() {
        mGnssStatusListener = new GnssStatus.Callback() {
            @Override
            public void onStarted() {
                for (GpsTestListener listener : mGpsTestListeners) {
                    listener.onGnssStarted();
                }
            }

            @Override
            public void onStopped() {
                for (GpsTestListener listener : mGpsTestListeners) {
                    listener.onGnssStopped();
                }
            }

            @Override
            public void onFirstFix(int ttffMillis) {
                for (GpsTestListener listener : mGpsTestListeners) {
                    listener.onGnssFirstFix(ttffMillis);
                }
            }

            @Override
            public void onSatelliteStatusChanged(GnssStatus status) {
                mGnssStatus = status;

                // Stop progress bar after the first status information is obtained
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                for (GpsTestListener listener : mGpsTestListeners) {
                    listener.onSatelliteStatusChanged(mGnssStatus);
                }
            }
        };
        mLocationManager.registerGnssStatusCallback(mGnssStatusListener);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addGnssMeasurementsListener() {
        mGnssMeasurementsListener = new GnssMeasurementsEvent.Callback() {
            @Override
            public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) {
                for (GpsTestListener listener : mGpsTestListeners) {
                    listener.onGnssMeasurementsReceived(event);
                }
                if (mWriteGnssMeasurementToLog) {
                    for (GnssMeasurement m : event.getMeasurements()) {
                        writeGnssMeasurementToLog(m);
                    }
                }
            }

            @Override
            public void onStatusChanged(int status) {
                final String statusMessage;
                switch (status) {
                    case STATUS_LOCATION_DISABLED:
                        statusMessage = "gnss_measurement_status_loc_disabled";
                        break;
                    case STATUS_NOT_SUPPORTED:
                        statusMessage = "gnss_measurement_status_not_supported";
                        break;
                    case STATUS_READY:
                        statusMessage = "gnss_measurement_status_ready";
                        break;
                    default:
                        statusMessage = "gnss_status_unknown";
                }
                Log.d(TAG, "GnssMeasurementsEvent.Callback.onStatusChanged() - " + statusMessage);
            }
        };
        mLocationManager.registerGnssMeasurementsCallback(mGnssMeasurementsListener);
    }

    @SuppressLint("MissingPermission")
    private void addLegacyStatusListener() {
        mLegacyStatusListener = new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                mLegacyStatus = mLocationManager.getGpsStatus(mLegacyStatus);

                switch (event) {
                    case GpsStatus.GPS_EVENT_STARTED:
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED:
                        break;
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        break;
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        // Stop progress bar after the first status information is obtained
                        setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                        break;
                }
//                time.setText(mDateFormat.format(mFixTime) + "");
                for (GpsTestListener listener : mGpsTestListeners) {
                    listener.onGpsStatusChanged(event, mLegacyStatus);
                }
            }
        };
        mLocationManager.addGpsStatusListener(mLegacyStatusListener);
    }

    private void removeStatusListener() {
        if (GpsTestUtil.isGnssStatusListenerSupported()) {
            removeGnssStatusListener();
        } else {
            removeLegacyStatusListener();
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private void removeGnssStatusListener() {
        mLocationManager.unregisterGnssStatusCallback(mGnssStatusListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void removeGnssMeasurementsListener() {
        if (mLocationManager != null && mGnssMeasurementsListener != null) {
            mLocationManager.unregisterGnssMeasurementsCallback(mGnssMeasurementsListener);
        }
    }

    private void removeLegacyStatusListener() {
        if (mLocationManager != null && mLegacyStatusListener != null) {
            mLocationManager.removeGpsStatusListener(mLegacyStatusListener);
        }
    }

    private void addNmeaListener() {
        if (GpsTestUtil.isGnssStatusListenerSupported()) {
            addNmeaListenerAndroidN();
        } else {
            addLegacyNmeaListener();
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNmeaListenerAndroidN() {
        if (mOnNmeaMessageListener == null) {
            mOnNmeaMessageListener = new OnNmeaMessageListener() {
                @Override
                public void onNmeaMessage(String message, long timestamp) {
                    for (GpsTestListener listener : mGpsTestListeners) {
                        listener.onNmeaMessage(message, timestamp);
                    }
                    if (mLogNmea) {
                        writeNmeaToLog(message,
                                mWriteNmeaTimestampToLog ? timestamp : Long.MIN_VALUE);
                    }
                }
            };
        }
        mLocationManager.addNmeaListener(mOnNmeaMessageListener);
    }

    @SuppressLint("MissingPermission")
    private void addLegacyNmeaListener() {
        if (mLegacyNmeaListener == null) {
            mLegacyNmeaListener = new GpsStatus.NmeaListener() {
                @Override
                public void onNmeaReceived(long timestamp, String nmea) {
                    for (GpsTestListener listener : mGpsTestListeners) {
                        listener.onNmeaMessage(nmea, timestamp);
                    }
                    if (mLogNmea) {
                        writeNmeaToLog(nmea, mWriteNmeaTimestampToLog ? timestamp : Long.MIN_VALUE);
                    }
                }
            };
        }
        mLocationManager.addNmeaListener(mLegacyNmeaListener);
    }

    private void removeNmeaListener() {
        if (GpsTestUtil.isGnssStatusListenerSupported()) {
            if (mLocationManager != null && mOnNmeaMessageListener != null) {
                mLocationManager.removeNmeaListener(mOnNmeaMessageListener);
            }
        } else {
            if (mLocationManager != null && mLegacyNmeaListener != null) {
                mLocationManager.removeNmeaListener(mLegacyNmeaListener);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNavMessageListener() {
        if (mGnssNavMessageListener == null) {
            mGnssNavMessageListener = new GnssNavigationMessage.Callback() {
                @Override
                public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {
                    writeNavMessageToLog(event);
                }

                @Override
                public void onStatusChanged(int status) {
                    final String statusMessage;
                    switch (status) {
                        case STATUS_LOCATION_DISABLED:
                            statusMessage = "gnss_nav_msg_status_loc_disabled";
                            break;
                        case STATUS_NOT_SUPPORTED:
                            statusMessage = "gnss_nav_msg_status_not_supported";
                            break;
                        case STATUS_READY:
                            statusMessage = "gnss_nav_msg_status_ready";
                            break;
                        default:
                            statusMessage = "gnss_status_unknown";
                    }
                    Log.d(TAG, "GnssNavigationMessage.Callback.onStatusChanged() - " + statusMessage);
                }
            };
        }
        mLocationManager.registerGnssNavigationMessageCallback(mGnssNavMessageListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void removeNavMessageListener() {
        if (mLocationManager != null && mGnssNavMessageListener != null) {
            mLocationManager.unregisterGnssNavigationMessageCallback(mGnssNavMessageListener);
        }
    }

    /**
     * Ask the user if they want to enable GPS
     */
    private void promptEnableGps() {
        new AlertDialog.Builder(this)
                .setMessage("enable_gps")
                .setPositiveButton("enable",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton("later",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                )
                .show();
    }

    @SuppressLint("MissingPermission")
    private void checkTimeAndDistance(SharedPreferences settings) {
        if (mStarted) {
            mLocationManager
                    .requestLocationUpdates(mProvider.getName(), 5000, 0, this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkNavMessageOutput(SharedPreferences settings) {
    }


    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mFixTime = location.getTime();

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date gdate = new Date(location.getTime());
        String gpsDateTime = format.format(gdate);
//        Toast.makeText(MultiPhotoSelectActivity.this, "CurrentDateTime = " + CurrentDateTime + "\nGpsDateTime = " + gpsDateTime, Toast.LENGTH_SHORT).show();


//        Toast.makeText(MultiPhotoSelectActivity.this, "Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d("onLocationChanged:::", mFixTime + "");
        Log.d("onLocationChanged:::", location.hasAltitude() + "");
        Log.d("onLocationChanged:::", location.hasAccuracy() + "");
        Log.d("onLocationChanged:::", location.hasBearing() + "");
        Log.d("onLocationChanged:::", location.hasSpeed() + "");
        for (GpsTestListener listener : mGpsTestListeners) {
            listener.onLocationChanged(location);
        }
//        Toast.makeText(MultiPhotoSelectActivity.this, mDateFormat.format(mFixTime) + "", Toast.LENGTH_SHORT).show();

        // Check if location & gpstime is available
        StatusDBHelper s = new StatusDBHelper(MultiPhotoSelectActivity.this);
        boolean latitudeAvailable = false;
        boolean longitudeAvailable = false;
        boolean GPSDateTimeAvailable = false;

        latitudeAvailable = s.initialDataAvailable("Latitude");
        longitudeAvailable = s.initialDataAvailable("Longitude");
        GPSDateTimeAvailable = s.initialDataAvailable("GPSDateTime");

        if (latitudeAvailable == false) {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            s.insertInitialData("Latitude", String.valueOf(location.getLatitude()));
        }
        if (longitudeAvailable == false) {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            s.insertInitialData("Longitude", String.valueOf(location.getLongitude()));

        }
        if (GPSDateTimeAvailable == false) {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            s.insertInitialData("GPSDateTime", gpsDateTime);

            // Reset Timer
            MyApplication.resetTimer();

            MyApplication.startTimer();
        } else {
            s = new StatusDBHelper(MultiPhotoSelectActivity.this);
            s.Update("GPSDateTime", gpsDateTime);

            // Reset Timer
            MyApplication.resetTimer();

            MyApplication.startTimer();
        }

        BackupDatabase.backup(MultiPhotoSelectActivity.this);

        // if dialog is open then close
        if (gpsTimeDialog != null) {
            if (gpsTimeDialog.isShowing())
                gpsTimeDialog.dismiss();
        }

        // todo problem
        Log.d("before : ", "GetCurrentDateTime ");
        sessionStartTime = utility.GetCurrentDateTime(false);
        Log.d("beforeafter : ", "GetCurrentDateTime ");

        // stop getting location
        gpsStop();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        for (GpsTestListener listener : mGpsTestListeners) {
            listener.onStatusChanged(provider, status, extras);
        }
    }

    public void onProviderEnabled(String provider) {
        for (GpsTestListener listener : mGpsTestListeners) {
            listener.onProviderEnabled(provider);
        }
    }

    public void onProviderDisabled(String provider) {
        for (GpsTestListener listener : mGpsTestListeners) {
            listener.onProviderDisabled(provider);
        }
    }

}