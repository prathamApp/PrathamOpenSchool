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

    Student(){}

    Student(String Firstname, String studentImgPath){
        this.FirstName = Firstname;
        this.studentPhotoPath = studentImgPath;
    }

}