package com.example.pef.prathamopenschool;

/**
 * Created by Varun Anand on 10-Aug-2015.
 */
public final class DatabaseInitialization {

    // to prevent instantiation of a static class
    private DatabaseInitialization() {
    }

    // Constants needed for UserType model
    public static final String CreateUserTypeTable = "CREATE TABLE UserType (UserTypeID INTEGER PRIMARY KEY, UserTypeName TEXT NOT NULL, IsActive INTEGER DEFAULT 1);";
    public static final String InsertIndividualUserType = "INSERT INTO UserType(UserTypeID, UserTypeName) values (100,'Individual');";
    public static final String InsertGroupUserType = "INSERT INTO UserType(UserTypeID, UserTypeName) values (200,'Group');";
    public static final String InsertAdminUserType = "INSERT INTO UserType(UserTypeID, UserTypeName) values (300, 'Admin');";
    public static final String InsertSuperAdminUserType = "INSERT INTO UserType(UserTypeID, UserTypeName) values (400, 'Super Admin');";

    // Constants needed for User entity
    public static final String CreateUserTable = "CREATE TABLE Users (UserID TEXT PRIMARY KEY, StudentID TEXT NULL, Username TEXT NOT NULL, Password TEXT NOT NULL, UserTypeID INTEGER NOT NULL, GroupID Text NOT NULL, DeviceID INTEGER NOT NULL, CreatedOn TEXT NOT NULL, DeletedOn TEXT NULL, IsActive INTEGER DEFAULT 1)";
    public static final String InsertSuperAdminUser = "INSERT INTO USERS (UserID, Username, Password, UserTypeID, CreatedOn) VALUES ('468181DF-88AC-4EA1-AA51-F895DC6446F0', 'super', 'super', '728E11F1-50BE-4F11-82F3-37C7C7BC0299','%s')";
    public static final String InsertAdminUser = "INSERT INTO USERS (UserID, Username, Password, UserTypeID, CreatedOn) VALUES ('1780EBAD-9B3B-4917-BABA-01BFD07AAB67', 'admin', 'admin', '93133528-17EC-459B-A6A8-2BC74F570188','%s')";

    // Constants needed for Score entity / Tracking information
    //public static final String CreateScoreTable = "CREATE TABLE Scores (SessionID TEXT NOT NULL, PlayerID INTEGER, ResourceID text NOT NULL, QuestionID INTEGER NOT NULL, ScoredMarks integer NOT NULL, TotalMarks integer NOT NULL, StartDateTime TEXT NOT NULL, EndDateTime TEXT, Level INTEGER DEFAULT 1, DeviceId TEXT NOT NULL);";

    /******************************************Above code will be used for adding deviceId to score table***********************************************************/
    public static final String CreateScoreTable = "CREATE TABLE Scores (SessionID TEXT NOT NULL, GroupID TEXT, DeviceID TEXT NOT NULL, ResourceID text NOT NULL, QuestionID INTEGER NOT NULL, ScoredMarks integer NOT NULL, TotalMarks integer NOT NULL, StartDateTime TEXT NOT NULL, EndDateTime TEXT, Level INTEGER DEFAULT 1);";
    public static final String CreateAssessmentScoreTable = "CREATE TABLE AssessmentScores (aSessionID TEXT NOT NULL, aGroupID TEXT, aDeviceID TEXT NOT NULL, aResourceID text NOT NULL, aQuestionID INTEGER NOT NULL, aScoredMarks integer NOT NULL, aTotalMarks integer NOT NULL, aStartDateTime TEXT NOT NULL, aEndDateTime TEXT, aLevel INTEGER DEFAULT 1, aLessonSession TEXT NOT NULL);";
    // public static final String CreateScoreTable = "CREATE TABLE Scores (SessionID TEXT NOT NULL, PlayerID TEXT, ResourceID INTEGER NOT NULL, QuestionID INTEGER NOT NULL,ScoredMarks INT NOT NULL,StartDateTime TEXT NOT NULL, EndDateTime TEXT,Level INTEGER DEFAULT 1);";
    // Constants needed for Session entity
    public static final String CreateSessionTable = "CREATE TABLE Session(SessionID TEXT NOT NULL, UserID TEXT NOT NULL);";

    // Constants needed for Resource entity
    public static final String CreateResourceTable = "CREATE TABLE Resource(ResourceID INTEGER NOT NULL, ResourceTypeID INTEGER NOT NULL, ResourceName TEXT NOT NULL, CreatedOn TEXT NOT NULL, DeletedOn TEXT NULL, IsActive INTEGER DEFAULT 1);";

    // Constants needed for Resource Type entity
    public static final String CreateResourceTypeTable = "CREATE TABLE ResourceType(ResourceTypeID INTEGER NOT NULL, ResourceTypeName TEXT NOT NULL, IsActive INTEGER DEFAULT 1);";

    // Constants needed for Device entity
    public static final String CreateDeviceTable = "CREATE TABLE Device(DeviceID INTEGER NOT NULL, DeviceType TEXT NOT NULL, DeviceName TEXT NOT NULL);";

    // Constants needed for CRL entity
    public static final String CreateCRLTable = "CREATE TABLE CRL(CRLID TEXT PRIMARY KEY, FirstName TEXT NOT NULL, LastName TEXT NOT NULL, UserName TEXT NOT NULL, PassWord TEXT NOT NULL, ProgramId INTEGER NOT NULL, Mobile TEXT NOT NULL, State TEXT NOT NULL, Email TEXT NOT NULL , CreatedBy text, NewFlag boolean);";

    // Constants needed for Student
    public static final String CreateStudentTable = "CREATE TABLE Student (StudentID text PRIMARY KEY, FirstName text, MiddleName text, LastName text, Age int, Class int, UpdatedDate text, Gender text, GroupID text, CreatedBy text, NewFlag boolean, StudentUID text ,IsSelected boolean);";

    // Constants needed for Group
    public static final String CreateGroupTable = "CREATE TABLE Groups (GroupID text PRIMARY KEY,GroupCode text, GroupName text, UnitNumber text, DeviceID text, Responsible text, ResponsibleMobile text, VillageID integer, ProgramId integer, CreatedBy text, NewFlag boolean, VillageName text, SchoolName text);";

    // Constants needed for Village
    public static final String CreateVillageTable = "CREATE TABLE Village (VillageID integer PRIMARY KEY, VillageCode text, VillageName text, Block text, District text, State text, CRLID TEXT);";

    public static final String CreateLogTable = "CREATE TABLE Logs (LogID INTEGER PRIMARY KEY AUTOINCREMENT, CurrentDateTime TEXT NOT NULL, ExceptionMsg TEXT NOT NULL, ExceptionStackTrace TEXT NOT NULL, MethodName TEXT NOT NULL, Type TEXT NOT NULL, GroupId TEXT NOT NULL, DeviceId TEXT NOT NULL, LogDetail TEXT);";

    public static final String CreateStatusTable = "CREATE TABLE Status(key TEXT, value TEXT NOT NULL,trailerCount INTEGER default 0,oldTrailerCount INTEGER default 0)";
    public static final String InsertToStatus = "INSERT INTO Status(key,value) values('pullFlag','0'),('group1','0'),('group2','0'),('group3','0'),('group4','0'),('group5','0'),('state',''),('district',''),('block',''),('village',''),('jsonForNewVideos',''),('deviceId',''),('ActivatedDate',''),('ActivatedForGroups',''),('CRL','0'),('TabLanguage','English'),('AMAlarm','0'),('PMAlarm','0'),('aajKaSawalPlayed','0');";

    public static final String CreateAttendanceTable = "CREATE TABLE Attendance(SessionID TEXT NOT NULL, GroupID TEXT NOT NULL, PresentStudentIds TEXT NOT NULL)";

    public static final String CreateAserTable = "CREATE TABLE Aser(StudentId TEXT NOT NULL, ChildID text, TestType int, TestDate text, Lang int, Num int, OAdd boolean, OSub boolean, OMul boolean, ODiv boolean, WAdd boolean, WSub boolean, CreatedBy text, CreatedDate text, DeviceId text, FLAG boolean, GroupID TEXT );";

    // pullFlag INTEGER DEFAULT 0, group1 INTEGER DEFAULT 0, group2 INTEGER DEFAULT 0, state TEXT DEFAULT "", district TEXT DEFAULT "", block TEXT DEFAULT "", village TEXT DEFAULT ""

    //public static final String demoUser="INSERT INTO Users values(Util.GetUniqueID().toString(),1,'123','123',1,Util.GetUniqueID().toString(),1,'04/09/2015','',1)";


}
