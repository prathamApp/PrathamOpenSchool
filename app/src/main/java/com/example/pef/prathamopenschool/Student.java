package com.example.pef.prathamopenschool;

// change student id  int to String
// change grp id int to String
public class Student {
    public String StudentID;
    public String FirstName;
    public String MiddleName;
    public int Age;
    public int Class;
    public String UpdatedDate;
    public String LastName;
    public String Gender;
    public String GroupID;
    public String CreatedBy;
    Boolean newStudent;
    public String StudentUID;
    Boolean IsSelected;
    public String studentPhotoPath;
    public String sharedBy;
    public String SharedAtDateTime;
    public String appVersion;
    public String appName;
    public String CreatedOn;

    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getSharedAtDateTime() {
        return SharedAtDateTime;
    }

    public void setSharedAtDateTime(String sharedAtDateTime) {
        SharedAtDateTime = sharedAtDateTime;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    Student() {
    }

    Student(String Firstname, String studentImgPath) {
        this.FirstName = Firstname;
        this.studentPhotoPath = studentImgPath;
    }

}