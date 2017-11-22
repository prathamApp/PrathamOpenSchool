package com.example.pef.prathamopenschool;

// add created by id

public class Group {
    public String GroupID;
    public String GroupName; // AKA Unit Code
    public String UnitNumber;
    public String DeviceID;
    public String Responsible;
    public String ResponsibleMobile;
    public int VillageID;
    public String GroupCode;
    public int ProgramID;
    public String CreatedBy;
    Boolean newGroup;

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String VillageName;
    public String SchoolName;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }


    public String getGroupName() {
        return GroupName;
    }

    @Override
    public String toString() {
        return this.GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public int getVillageID() {
        return VillageID;
    }

    public void setVillageID(int villageID) {
        VillageID = villageID;
    }

    public int getProgramID() {
        return ProgramID;
    }

    public void setProgramID(int programID) {
        ProgramID = programID;
    }
}