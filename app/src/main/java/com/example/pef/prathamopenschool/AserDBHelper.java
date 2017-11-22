package com.example.pef.prathamopenschool;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ameya on 19-Jun-17.
 */

public class AserDBHelper extends DBHelper {


    Cursor cursor;
    final String ERRORTABLENAME = "Logs";

    public AserDBHelper(Context context) {
        super(context);
        // db = this.getWritableDatabase();
        database = getWritableDatabase();
    }

    private void _PopulateLogValues(Exception ex, String method) {

        Logs logs = new Logs();

        logs.currentDateTime = Util.GetCurrentDateTime();
        logs.errorType = "Error";
        logs.exceptionMessage = ex.getMessage().toString();
        logs.exceptionStackTrace = ex.getStackTrace().toString();
        logs.methodName = method;
        logs.groupId = MultiPhotoSelectActivity.selectedGroupId;
        logs.deviceId = MultiPhotoSelectActivity.deviceID;

        contentValues.put("CurrentDateTime", logs.currentDateTime);
        contentValues.put("ExceptionMsg", logs.exceptionMessage);
        contentValues.put("ExceptionStackTrace", logs.exceptionStackTrace);
        contentValues.put("MethodName", logs.methodName);
        contentValues.put("Type", logs.errorType);
        contentValues.put("GroupId", logs.groupId);
        contentValues.put("DeviceId", logs.deviceId);

        contentValues.put("LogDetail", "AserLog");

        database.insert(ERRORTABLENAME, null, contentValues);
        database.close();
        BackupDatabase.backup(c);
    }


    // Check Data entry in Aser DB
    public boolean CheckChildIDExists(String uniqStdID, String ChildID, String GroupID) {
        try {
            database = getWritableDatabase();

            cursor = database.rawQuery("SELECT * FROM Aser WHERE StudentId != ? AND ChildID=? AND GroupID =?", new String[]{uniqStdID, ChildID, GroupID});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.close();
                    database.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "CheckChildIDExists");
            return false;
        }
    }


    public int GetBaselineCount(String GroupID) {
        try {
            database = getReadableDatabase();

            String count = "SELECT count(*) FROM Aser where GroupID = ? AND TestType = 0 ";
            Cursor mcursor = database.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {
                return icount;
            } else {
                return icount;
            }
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetBaselineCount");
            return 0;
        }
    }

    public int GetEndline1Count(String GroupID) {

        try {
            String count = "SELECT count(*) FROM Aser where GroupID = ? AND TestType = 1 ";
            database = getReadableDatabase();
            Cursor mcursor = database.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            mcursor.close();
            if (icount > 0) {
                return icount;
            } else {
                return icount;
            }
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetEndline1Count");
            return 0;
        }
    }

    public int GetEndline2Count(String GroupID) {

        try {
            database = getReadableDatabase();

            String count = "SELECT count(*) FROM Aser where GroupID = ? AND TestType = 2 ";
            Cursor mcursor = database.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {
                return icount;
            } else {
                return icount;
            }
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetEndline2Count");
            return 0;
        }
    }

    public int GetEndline3Count(String GroupID) {

        try {
            database = getReadableDatabase();

            String count = "SELECT count(*) FROM Aser where GroupID = ? AND TestType = 3 ";
            Cursor mcursor = database.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {
                return icount;
            } else {
                return icount;
            }
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetEndline3Count");
            return 0;
        }
    }

    public int GetEndline4Count(String GroupID) {

        try {
            database = getReadableDatabase();

            String count = "SELECT count(*) FROM Aser where GroupID = ? AND TestType = 4 ";
            Cursor mcursor = database.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {
                return icount;
            } else {
                return icount;
            }
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetEndline4Count");
            return 0;
        }
    }


    // Check Data entry in Aser DB
    public boolean CheckDataExists(String StudentId, int TestType) {

        try {

            database = getWritableDatabase();

            cursor = database.rawQuery("SELECT * FROM Aser WHERE StudentId=? AND TestType =?", new String[]{StudentId, String.valueOf(TestType)});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.close();
                    database.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "CheckDataExists");
            return false;
        }
    }

    // set Flag to false
    public void UpdateAserData(String ChildID, String testDate, int lang, int num, int oad, int osb, int oml, int odv, int wad, int wsb, String crtby, String crtdt, int isSelected, String studentID, int TstType) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("update Aser set ChildID='" + ChildID + "', TestDate='" + testDate + "', Lang = " + lang + ", Num = " + num + ", OAdd = " + oad + ", OSub = " + osb + ", OMul = " + oml + ", ODiv = " + odv + ", WAdd = " + wad + ", WSub = " + wsb + ", CreatedBy = '" + crtby + "', CreatedDate = '" + crtdt + "', FLAG =" + isSelected + "  WHERE StudentId='" + studentID + "' AND TestType =" + TstType + "", null);
            cursor.moveToFirst();
            database.close();
        } catch (Exception ex) {
            _PopulateLogValues(ex, "UpdateAserData");
        }
    }


    // Getting Aser Data based on Unique Student ID passed by universal child form
    public List<Aser> GetAllByStudentID(String StudentID, int testV) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from Aser where StudentId = ? AND TestType = ?", new String[]{StudentID, String.valueOf(testV)});

            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetAllByStudentID");
            return null;
        }
    }

    public boolean checkTableEmptyness() {
        try {
            database = getReadableDatabase();

            String count = "SELECT count(*) FROM ASER";
            Cursor mcursor = database.rawQuery(count, null);
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            _PopulateLogValues(ex, "checkTableEmptyness");
            return false;
        }
    }


    public void replaceData(Aser obj) {
        try {

            database = getWritableDatabase();

            contentValues.put("StudentId", obj.StudentId);
            contentValues.put("TestType", obj.TestType);
            contentValues.put("TestDate", obj.TestDate);
            contentValues.put("Lang", obj.Lang);
            contentValues.put("Num", obj.Num);
            contentValues.put("OAdd", obj.OAdd);
            contentValues.put("OSub", obj.OSub);
            contentValues.put("OMul", obj.OMul);
            contentValues.put("ODiv", obj.ODiv);
            contentValues.put("WAdd", obj.WAdd);
            contentValues.put("WSub", obj.WSub);
            contentValues.put("CreatedBy", obj.CreatedBy);
            contentValues.put("CreatedDate", obj.CreatedDate);
            contentValues.put("DeviceId", obj.DeviceId);
            contentValues.put("FLAG", obj.FLAG);

            database.replace("Aser", null, contentValues);
            database.close();
        } catch (Exception ex) {
            _PopulateLogValues(ex, "replaceData");

        }
    }

    public void insertData(Aser obj) {

        try {
            database = getWritableDatabase();

            contentValues.put("StudentId", obj.StudentId);
            contentValues.put("GroupID", obj.GroupID);
            contentValues.put("ChildID", obj.ChildID);
            contentValues.put("TestType", obj.TestType);
            contentValues.put("TestDate", obj.TestDate);
            contentValues.put("Lang", obj.Lang);
            contentValues.put("Num", obj.Num);
            contentValues.put("OAdd", obj.OAdd);
            contentValues.put("OSub", obj.OSub);
            contentValues.put("OMul", obj.OMul);
            contentValues.put("ODiv", obj.ODiv);
            contentValues.put("WAdd", obj.WAdd);
            contentValues.put("WSub", obj.WSub);
            contentValues.put("CreatedBy", obj.CreatedBy);
            contentValues.put("CreatedDate", obj.CreatedDate);
            contentValues.put("DeviceId", obj.DeviceId);
            contentValues.put("FLAG", obj.FLAG);


            database.insert("Aser", null, contentValues);
            database.close();
        } catch (Exception ex) {
            _PopulateLogValues(ex, "insertData");

        }
    }


    public List<Aser> GetAll() {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from Aser", null);

            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetAll");
            return null;
        }
    }

    public List<Aser> GetAllNewAserGroups() {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from Aser", null);

            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetAllNewAserGroups");
            return null;
        }
    }

    private List<Aser> _PopulateListFromCursor(Cursor cursor) {
        try {
            database = getWritableDatabase();
            List<Aser> aser_list = new ArrayList<Aser>();
            Aser aserObject;
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {

                aserObject = new Aser();

                aserObject.StudentId = cursor.getString(cursor.getColumnIndex("StudentId"));
                aserObject.ChildID = cursor.getString(cursor.getColumnIndex("ChildID"));
                aserObject.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                aserObject.TestType = cursor.getInt(cursor.getColumnIndex("TestType"));
                aserObject.TestDate = cursor.getString(cursor.getColumnIndex("TestDate"));
                aserObject.Lang = cursor.getInt((cursor.getColumnIndex("Lang")));
                aserObject.Num = cursor.getInt((cursor.getColumnIndex("Num")));
                aserObject.OAdd = cursor.getInt((cursor.getColumnIndex("OAdd")));
                aserObject.OSub = cursor.getInt((cursor.getColumnIndex("OSub")));
                //Boolean.valueOf(cursor.getString((cursor.getColumnIndex("OSub"))));
                aserObject.OMul = cursor.getInt((cursor.getColumnIndex("OMul")));
                aserObject.ODiv = cursor.getInt((cursor.getColumnIndex("ODiv")));
                aserObject.WAdd = cursor.getInt((cursor.getColumnIndex("WAdd")));
                aserObject.WSub = cursor.getInt((cursor.getColumnIndex("WSub")));
                aserObject.CreatedBy = cursor.getString((cursor.getColumnIndex("CreatedBy")));
                aserObject.CreatedDate = cursor.getString((cursor.getColumnIndex("CreatedDate")));
                aserObject.DeviceId = cursor.getString((cursor.getColumnIndex("DeviceId")));
                aserObject.FLAG = cursor.getInt((cursor.getColumnIndex("FLAG")));

                aser_list.add(aserObject);
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            return aser_list;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "populateListFromCursor");
            return null;
        }
    }


}
