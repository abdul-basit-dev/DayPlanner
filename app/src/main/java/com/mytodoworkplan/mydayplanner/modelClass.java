package com.mytodoworkplan.mydayplanner;

public class modelClass {
    String workTitle,workType,workContent,workTime,oneTimeDate,weekDay;
    int id;


    public modelClass(int id, String workTitle, String workType, String workContent, String workTime, String oneTimeDate, String weekDay) {
        this.id = id;
        this.workTitle = workTitle;
        this.workType = workType;
        this.workContent = workContent;
        this.workTime = workTime;
        this.oneTimeDate = oneTimeDate;
        this.weekDay = weekDay;
    }

    public modelClass(String workTitle, String oneTimeDate, String workContent, String workTime) {
        this.workTitle = workTitle;
        this.oneTimeDate = oneTimeDate;
        this.workContent = workContent;
        this.workTime = workTime;
    }
    public modelClass(String workTitle, String workContent, String workTime) {
        this.workTitle = workTitle;
        this.workType = workType;
        this.workContent = workContent;
        this.workTime = workTime;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getOneTimeDate() {
        return oneTimeDate;
    }

    public void setOneTimeDate(String oneTimeDate) {
        this.oneTimeDate = oneTimeDate;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
