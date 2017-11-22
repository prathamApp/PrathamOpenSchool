package com.example.pef.prathamopenschool;

/**
 * Created by PEF on 05/06/2017.
 */

public class Crl {
    public String CRLId;
    public String FirstName;
    public String LastName;
    public String UserName;
    public String Password;
    public int ProgramId;
    public String Mobile;
    public String State;
    public String Email;
    public String CreatedBy;
    Boolean newCrl;

    public String getCRLId() {
        return CRLId;
    }

    public void setCRLId(String CRLId) {
        this.CRLId = CRLId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getProgramId() {
        return ProgramId;
    }

    public void setProgramId(int programId) {
        ProgramId = programId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
