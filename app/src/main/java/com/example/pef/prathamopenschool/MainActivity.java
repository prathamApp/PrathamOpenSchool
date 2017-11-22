package com.example.pef.prathamopenschool;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView, horizontalrecyclerView;
    private CardAdapter adapter;
    private List<Card> cardList;

    ArrayList<String> resIdArray = new ArrayList<String>();
    ArrayList<String> resPathArray = new ArrayList<String>();

    public static String fpath, lessonSession, startTime, resourceId;
    String newNodeList, myNodeTitle;
    Utility utility;
    JSONArray contentNavigate;
    boolean mainFlag, loadFlg = false, isBound = false;
    Intent i;
    public static TextToSp ttspeech;
    int currentResCnt = -1;

    static Context mContext, sessionContex;
    ScoreDBHelper scoreDBHelper;
    PlayVideo playVideo;
    boolean timer;
    TextView bread_crums;
    private HashMap<String, String> navigateJson;
    static boolean sessionFlg = false;
    static boolean LangFlag = false;
    boolean destroyed = false;
    Utility Util;

    // Aaj Ka Sawaal
    TextView tv_Que, tv_result;
    RadioGroup rg_Opt;
    RadioButton rb_Opt1, rb_Opt2, rb_Opt3, rb_Opt4, selectedOption;
    Button btn_Submit, btn_Skip;
    AttendanceDBHelper attendanceDBHelper;
    String QueId, Question, QuestionType, Subject, Option1, Option2, Option3, Option4, Answer, resourceType, resourcePath, programLanguage;
    String aajKaSawaalStartTime;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util = new Utility();
        attendanceDBHelper = new AttendanceDBHelper(MainActivity.this);

        // Get Selected Group ID
        Intent i = getIntent();
        String selectedGroupId = i.getStringExtra("selectedGroupId");
        String aajKaSawalPlayed = i.getStringExtra("aajKaSawalPlayed");

        // Decide the next screen depending on aajKaSawalPlayed status
        // Flag Played if Submit

        // Aaj Ka Sawaal Played
        if (aajKaSawalPlayed != null && aajKaSawalPlayed.equals("1")) {

        }
        // Aaj Ka Sawaal NOT Played
        else if (aajKaSawalPlayed != null && aajKaSawalPlayed.equals("0")) {

            // Update updateTrailerCountbyGroupID to 1 if played
            StatusDBHelper updateTrailerCount = new StatusDBHelper(MainActivity.this);
            updateTrailerCount.updateTrailerCountbyGroupID(1, selectedGroupId);
            BackupDatabase.backup(MainActivity.this);

            final MediaPlayer correct = MediaPlayer.create(MainActivity.this, R.raw.correct);
            final MediaPlayer wrong = MediaPlayer.create(MainActivity.this, R.raw.wrong);
            final Dialog resultDialog = new Dialog(MainActivity.this);
            final Dialog dialog = new Dialog(MainActivity.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_aaj_ka_sawaal);

            // Memory Allocation
            tv_Que = dialog.findViewById(R.id.tv_question);
            tv_result = dialog.findViewById(R.id.tv_result);

            rg_Opt = dialog.findViewById(R.id.rg_options);

            rb_Opt1 = dialog.findViewById(R.id.rb_O1);
            rb_Opt2 = dialog.findViewById(R.id.rb_O2);
            rb_Opt3 = dialog.findViewById(R.id.rb_O3);
            rb_Opt4 = dialog.findViewById(R.id.rb_O4);

            btn_Submit = dialog.findViewById(R.id.btn_submit);
            btn_Skip = dialog.findViewById(R.id.btn_skip);


            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(800, 450);
            dialog.show();


            // Set the Question & Options from json
            try {

                aajKaSawaalStartTime = Util.GetCurrentDateTime();
                // Load Json in Array
                JSONArray queJsonArray = new JSONArray(loadQueJSONFromAsset());

                // Generate Random Que No
                Random r = new Random();
                int Low = 0;
                int High = queJsonArray.length();
                int randomQuestion = r.nextInt(High - Low) + Low;

                // Get Question Details
                JSONObject qObj = queJsonArray.getJSONObject(randomQuestion);
                QueId = qObj.getString("QueId");
                Question = qObj.getString("Question");
                QuestionType = qObj.getString("QuestionType");
                Subject = qObj.getString("Subject");
                Option1 = qObj.getString("Option1");
                Option2 = qObj.getString("Option2");
                Option3 = qObj.getString("Option3");
                Option4 = qObj.getString("Option4");
                Answer = qObj.getString("Answer");
                resourceId = qObj.getString("resourceId");
                resourceType = qObj.getString("resourceType");
                resourcePath = qObj.getString("resourcePath");
                programLanguage = qObj.getString("programLanguage");

                // Set Question
                tv_Que.setText(Question);
                rb_Opt1.setText(Option1);
                rb_Opt2.setText(Option2);
                rb_Opt3.setText(Option3);
                rb_Opt4.setText(Option4);


            } catch (Exception e) {
                e.getMessage();
            }


            // Skip Button Action
            btn_Skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            // Submit Button Action
            btn_Submit.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                    // Flag Played if Submit
                    StatusDBHelper sdbh = new StatusDBHelper(MainActivity.this);
                    sdbh.UpdateAlarm("aajKaSawalPlayed", "1");
                    BackupDatabase.backup(MainActivity.this);

                    // Score Table Entry
                    boolean enterScore = false;
                    ScoreDBHelper score = new ScoreDBHelper(MainActivity.this);
                    Score sc = new Score();

                    // Disable buttons after selection
                    btn_Submit.setClickable(false);
                    btn_Skip.setClickable(false);
                    rb_Opt1.setClickable(false);
                    rb_Opt2.setClickable(false);
                    rb_Opt3.setClickable(false);
                    rb_Opt4.setClickable(false);

                    // get selected radio button from radioGroup
                    int selectedId = rg_Opt.getCheckedRadioButtonId();
                    // find the radio button by returned id
                    selectedOption = (RadioButton) dialog.findViewById(selectedId);
                    String option = selectedOption.getText().toString();

                    boolean answer = false;
                    sc.SessionID = MultiPhotoSelectActivity.sessionId;
                    sc.ResourceID = resourceId;
                    sc.QuestionId = Integer.parseInt(QueId);
                    sc.TotalMarks = 10;
                    sc.Level = 99;
                    sc.StartTime = aajKaSawaalStartTime;
                    sc.EndTime = Util.GetCurrentDateTime();
                    String gid;
                    gid = MultiPhotoSelectActivity.selectedGroupId;
                    if (gid.contains(","))
                        gid = gid.split(",")[0];
                    sc.GroupID = gid;
                    String deviceId = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    sc.DeviceID = deviceId.equals(null) ? "0000" : deviceId;


                    if (option.equals(Answer)) {
                        tv_result.setText("Correct Answer !!!");
                        tv_result.setTextColor(Color.GREEN);

                        resultDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        resultDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        resultDialog.setContentView(getLayoutInflater().inflate(R.layout.correct_image_layout
                                , null));

                        resultDialog.show();

                        correct.start();

                        //tv_result.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer, 0, 0, 0);
                        sc.ScoredMarks = 10;
                    } else if (answer == false) {


                        tv_result.setText("Wrong Answer !!!\nCorrect Answer is " + Answer + " !!!");
                        tv_result.setTextColor(Color.RED);


                        resultDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        resultDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        resultDialog.setContentView(getLayoutInflater().inflate(R.layout.wrong_image_layout
                                , null));
                        resultDialog.show();
                        //tv_result.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wrong_answer, 0);

                        wrong.start();

                        sc.ScoredMarks = 0;

                    }
                    enterScore = score.Add(sc);
                    BackupDatabase.backup(MainActivity.this);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 2500ms
                            if (correct.isPlaying()) {
                                correct.stop();
                                correct.reset();
                                correct.release();
                            } else if (wrong.isPlaying()) {
                                wrong.stop();
                                wrong.reset();
                                wrong.release();
                            }
                            resultDialog.dismiss();
                            dialog.dismiss();
                        }
                    }, 5000);

                }
            });


        }
        // if Questions.json not present
        else if (aajKaSawalPlayed != null && aajKaSawalPlayed.equals("3")) {
            //Toast.makeText(MainActivity.this, "Aaj Ka Sawaal Json not Found !!!", Toast.LENGTH_SHORT).show();
        }
        // aaj Ka Sawaal is null
        else {
            //Toast.makeText(MainActivity.this, "Aaj Ka Sawaal Json not Found !!!", Toast.LENGTH_SHORT).show();
        }

        mContext = this;
        mainFlag = true;
        ttspeech = new TextToSp(mContext);
        sessionContex = this;
        utility = new Utility();
        startTime = "";
        playVideo = new PlayVideo();
        // Hide Actionbar
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        *///getSdCardPath();

        initCollapsingToolbar();

        newNodeList = getIntent().getStringExtra("nodeList");
        myNodeTitle = getIntent().getStringExtra("crums");

        if (newNodeList != null) {
            mainFlag = false;
        }

        recyclerView = (RecyclerView) findViewById(R.id.verticalGridLayout);


        GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridlayoutManager);

        cardList = new ArrayList<>();
        adapter = new CardAdapter(this, cardList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        bread_crums = (TextView) findViewById(R.id.bread_crums);
        String NavTitle = bread_crums.getText().toString();

        if ((myNodeTitle == null) || (NavTitle == null))
            bread_crums.setText("");
        else
            bread_crums.setText(NavTitle + " > " + myNodeTitle);


        ReadMyFile();

        try {
            Glide.with(this).load(R.drawable.topcover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
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


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    public void ReadMyFile() {
        try {
            if (newNodeList == null) {

//            File myJsonFile= new File(MainActivity.fpath+"Json/funNavigate.json");
                File myJsonFile = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Json/", "Config.json");
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

                // Getting data JSON Array nodes
//            JSONArray contentNavigate  = jsonObj.getJSONArray("funNavigate");
                contentNavigate = jsonObj.getJSONArray("nodelist");
//            JSONObject contentNavigate2 = jsonObj.getJSONObject("nodelist");

            } else {

                JSONArray jsnarray = new JSONArray(newNodeList);
                contentNavigate = jsnarray;
                // contentNavigate = findElementsChildren(contentNavigate.getJSONObject(""),newNodeId);
            }

            // looping through All nodes
            for (int i = 0; i < contentNavigate.length(); i++) {

                String notetitle;
                JSONObject c = contentNavigate.getJSONObject(i);

                Card card = new Card();
                card.nodeId = c.optString("nodeId");
                card.nodeType = c.optString("nodeType");
                String myNodeType = c.optString("nodeType").toString();
                card.nodeTitle = c.optString("nodeTitle").toString();
                notetitle = c.optString("nodeTitle").toString();
                card.nodeImage = splashScreenVideo.fpath + "Media/" + c.optString("nodeImage").toString();
                card.nodePhase = c.optString("nodePhase").toString();
                String phase = c.optString("nodePhase").toString();
                card.nodeAge = c.optString("nodeAge").toString();
                card.nodeDesc = c.optString("nodeDesc").toString();
                card.nodeKeywords = c.optString("nodeKeywords").toString();
                card.sameCode = c.optString("sameCode").toString();
                card.resourceId = c.optString("resourceId").toString();
                card.resourceType = c.optString("resourceType").toString();
                card.resourcePath = c.optString("resourcePath").toString();
                card.nodeList = c.optString("nodelist").toString();
                String tempNodeList = c.optString("nodelist").toString();

                String resourcePath = c.optString("resourcePath").toString();
                String resId = c.optString("resourceId").toString();
                String resourceType = c.optString("resourceType").toString();
                String assNodeDesc = c.optString("nodeDesc").toString();

                String numberOnly = phase.replaceAll("[^0-9]", "");

                cardList.add(card);

                if (myNodeType.equals("Resource") && assessmentLogin.assessmentFlg) {
                    loadFlg = true;
                    lessonSession = utility.GetUniqueID().toString();
                }

            }
            if (cardList.get(0).nodePhase.contains("phase")) {
                if (cardList.size() > 0) {
                    Collections.sort(cardList, new Comparator<Card>() {
                        @Override
                        public int compare(final Card object1, final Card object2) {
                            return object1.nodePhase.compareTo(object2.nodePhase);
                        }
                    });
                }
                Collections.reverse(cardList);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Adding few albums for testing
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (CardAdapter.pdfFlag) {
                CardAdapter.pdfFlag = false;
                PDFTracking.calculateEndTime();
                BackupDatabase.backup(getApplicationContext());
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mainFlag) {
            Intent main = new Intent(MainActivity.this, MultiPhotoSelectActivity.class);
            finish();
            startActivity(main);
        } else if (loadFlg) {
            loadFlg = false;
            Intent intent = new Intent(mContext, AssessmentResult.class);
            startActivity(intent);
            finish();
        } else {
            Runtime rs = Runtime.getRuntime();
            rs.freeMemory();
            rs.gc();
            rs.freeMemory();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        destroyed = false;
        if (MultiPhotoSelectActivity.pauseFlg) {
            MultiPhotoSelectActivity.cd.cancel();
            MultiPhotoSelectActivity.pauseFlg = false;
            MultiPhotoSelectActivity.duration = MultiPhotoSelectActivity.timeout;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroyed = false;

        MultiPhotoSelectActivity.pauseFlg = true;

        MultiPhotoSelectActivity.cd = new CountDownTimer(MultiPhotoSelectActivity.duration, 1000) {
            //cd = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MultiPhotoSelectActivity.duration = millisUntilFinished;
                timer = true;
            }

            @Override
            public void onFinish() {
                timer = false;
                sessionFlg = true;

                if (!CardAdapter.vidFlg) {
                    scoreDBHelper = new ScoreDBHelper(sessionContex);
                    playVideo.calculateEndTime(scoreDBHelper);
                    BackupDatabase.backup(sessionContex);
                    try {
                        finishAffinity();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        sessionFlg = true;
        destroyed = true;
        super.onDestroy();
    }


}