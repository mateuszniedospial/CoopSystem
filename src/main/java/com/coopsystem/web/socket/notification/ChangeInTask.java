package com.coopsystem.web.socket.notification;

import java.util.Date;

/**
 * Created by Master on 14.05.2017.
 */

public class ChangeInTask {
    private String objectName ="ChangeInTask";
    private String taskId;
    private String taskTitle;
    private Date modifyDate;
    private String changeAuthor;
    private String changedField;
    private String from;
    private String to;

    public String getTaskId() {
        return taskId;
    }

    public ChangeInTask setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public ChangeInTask setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
        return this;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public ChangeInTask setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public String getChangeAuthor() {
        return changeAuthor;
    }

    public ChangeInTask setChangeAuthor(String changeAuthor) {
        this.changeAuthor = changeAuthor;
        return this;
    }

    public String getChangedField() {
        return changedField;
    }

    public ChangeInTask setChangedField(String changedField) {
        this.changedField = changedField;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public ChangeInTask setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public ChangeInTask setTo(String to) {
        this.to = to;
        return this;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
