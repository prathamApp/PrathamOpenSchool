package com.example.pef.prathamopenschool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CrlDashboard extends AppCompatActivity {

    Context sessionContex;
    ScoreDBHelper scoreDBHelper;
    PlayVideo playVideo;
    boolean timer;
    TextView tv_version_code;
    static String CreatedBy, currentAdmin;
    public static Boolean transferFlag = false;
    static String deviceID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crl_dashboard);

        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Hide Actionbar
        getSupportActionBar().hide();

        // Displaying Version Code of App
        tv_version_code = (TextView) findViewById(R.id.tv_Version);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            int verCode = pInfo.versionCode;
            tv_version_code.setText(String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Execute File checking on diff thread
        new fileChecker().execute();

        MainActivity.sessionFlg = false;
        sessionContex = this;
        playVideo = new PlayVideo();

        Intent i = getIntent();
        CreatedBy = i.getStringExtra("CreatedBy"); // Created by is CRLID

        currentAdmin = i.getStringExtra("UserName");
        // Toast.makeText(this, "Welcome " + currentAdmin, Toast.LENGTH_SHORT).show();
    }

    public void gotoTabReportActivity(View view) {
        Intent intent = new Intent(CrlDashboard.this, AssessmentCrlDashBoardView.class);
        intent.putExtra("fromActivity", "crl");
        startActivity(intent);
    }

    // Mandatory File Check
    private class fileChecker extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Runs on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                // Runs on the background thread
                // Check important Folders existance
                checkPOSInternalStructure("ReceivedContent");
                checkPOSInternalStructure("sharableContent");
                checkPOSInternalStructure("receivedUsage");
                checkPOSInternalStructure("StudentProfiles");
                checkPOSInternalStructure("transferredUsage");
                checkPOSInternalStructure("pushedUsage");
                //checkPOSInternalStructure("databaseBackup");

            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            // Runs on the UI thread

        }

    }


    // Copy Avatars from Drawables
    private void copyB1() throws IOException {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.b1);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.POSinternal/StudentProfiles/Boys/";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(path, "1.png");
        FileOutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();


    }

    private void copyB2() throws IOException {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.b2);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.POSinternal/StudentProfiles/Boys/";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(path, "2.png");
        FileOutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();


    }

    private void copyB3() throws IOException {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.b3);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.POSinternal/StudentProfiles/Boys/";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(path, "3.png");
        FileOutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();


    }

    private void copyG1() throws IOException {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.g1);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.POSinternal/StudentProfiles/Girls/";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(path, "1.png");
        FileOutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();


    }

    private void copyG2() throws IOException {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.g2);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.POSinternal/StudentProfiles/Girls/";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(path, "2.png");
        FileOutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();


    }

    private void copyG3() throws IOException {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.g3);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.POSinternal/StudentProfiles/Girls/";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(path, "3.png");
        FileOutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();


    }

    // Copy Function to Copy file
    public static void copy(File src, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);

            // buffer size 1K
            byte[] buf = new byte[1024];

            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            is.close();
            os.close();
        }
    }


    public void fileCutPaste(File toMove, String destFolder) {
        try {
            File destinationFolder = new File(destFolder);
            File destinationFile = new File(destFolder + "/" + toMove.getName());
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
            }
            FileInputStream fileInputStream = new FileInputStream(toMove);
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);

            int bufferSize;
            byte[] bufffer = new byte[512];
            while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                fileOutputStream.write(bufffer, 0, bufferSize);
            }
            toMove.delete();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Creates folders if dont exists
    private void checkPOSInternalStructure(String folderName) {

        File folder = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/" + folderName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();

            if (folderName.equals("pushedUsage")) {

                try {
                    String path = Environment.getExternalStorageDirectory().toString() + "/.POSinternal/receivedUsage";
                    File receivedUsageDir = new File(path);

                    String destFolder = Environment.getExternalStorageDirectory() + "/.POSinternal/pushedUsage";

                    File[] files = receivedUsageDir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().contains("pushNewDataToServer")) {
                            fileCutPaste(files[i], destFolder);
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            } else if (folderName.equals("databaseBackup")) {

                // Backup Database
                try {

                    File from = new File(Environment.getExternalStorageDirectory() + "/PrathamTabDB.db");
                    File to = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/databaseBackup/PrathamTabDB.db");
                    copy(from, to);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (folderName.equals("StudentProfiles")) {

                // Copy Images
                try {
                    copyB1();
                    copyB2();
                    copyB3();
                    copyG1();
                    copyG2();
                    copyG3();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } else {
            // Do something else on failure
            // Check Student profiles files
            File boys = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Boys/");
            File b1 = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Boys/1.png");
            File b2 = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Boys/2.png");
            File b3 = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Boys/3.png");

            File girls = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Girls/");
            File g1 = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Girls/1.png");
            File g2 = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Girls/2.png");
            File g3 = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Girls/3.png");

            if (!boys.exists() || !b1.exists() || !b2.exists() || !b3.exists() || !girls.exists() || !g1.exists() || !g2.exists() || !g3.exists()) {

                // Copy Images
                try {
                    copyB1();
                    copyB2();
                    copyB3();
                    copyG1();
                    copyG2();
                    copyG3();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void goToCrlAddEditScreen(View view) {

        Intent goToAddEdit = new Intent(CrlDashboard.this, CrlAddEditScreen.class);
        startActivity(goToAddEdit);

    }


    public void AssignGroups(View view) {

        Intent intent = new Intent(CrlDashboard.this, AssignGroups.class);
        startActivity(intent);

    }


    public void goToCrlPullPushTransferUsageScreen(View view) {

        Intent intent = new Intent(CrlDashboard.this, CrlPullPushTransferUsageScreen.class);
        startActivity(intent);

    }


    public void goToCrlShareReceiveProfiles(View view) {

        Intent intent = new Intent(CrlDashboard.this, CrlShareReceiveProfiles.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MultiPhotoSelectActivity.pauseFlg) {
            MultiPhotoSelectActivity.cd.cancel();
            MultiPhotoSelectActivity.pauseFlg = false;
            MultiPhotoSelectActivity.duration = MultiPhotoSelectActivity.timeout;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

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
                MainActivity.sessionFlg = true;
                if (!CardAdapter.vidFlg) {
                    scoreDBHelper = new ScoreDBHelper(sessionContex);
                    playVideo.calculateEndTime(scoreDBHelper);
                    BackupDatabase.backup(sessionContex);
                    finishAffinity();
                }
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
