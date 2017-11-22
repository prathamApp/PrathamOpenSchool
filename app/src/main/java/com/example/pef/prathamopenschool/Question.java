package com.example.pef.prathamopenschool;


public class Question {

    public String QueId;
    public String Question;
    public String QuestionType;
    public String Subject;
    public String Option1;
    public String Option2;
    public String Option3;
    public String Option4;
    public String Answer;
    public String resourceId;
    public String resourceType;
    public String resourcePath;
    public String resourceName;
    public String programLanguage;

    public String getResourceName() {
        return resourceName;
    }

    @Override
    public String toString() {
        return "Question{" +
                "QueId='" + QueId + '\'' +
                ", Question='" + Question + '\'' +
                ", QuestionType='" + QuestionType + '\'' +
                ", Subject='" + Subject + '\'' +
                ", Option1='" + Option1 + '\'' +
                ", Option2='" + Option2 + '\'' +
                ", Option3='" + Option3 + '\'' +
                ", Option4='" + Option4 + '\'' +
                ", Answer='" + Answer + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourcePath='" + resourcePath + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", programLanguage='" + programLanguage + '\'' +
                '}';
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

}
