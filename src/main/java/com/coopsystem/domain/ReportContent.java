package com.coopsystem.domain;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Dariusz Ł on 29.04.2017.
* This class not be store in db as table, because will be serializable and store as blob.
 */
public class ReportContent implements Serializable {
    private Float estimateStart;
    private Float remainingStart;
    private Float sumOfWorklog;
    private Float sumOfEstimate;
    private Float sumOfRemaining;
    private List<KeyValue<Long, Float>> workLogPerUser;
    private Set<Long> tasksIdDone;
    private Set<Long> tasksIdUnDone;
    private Set<Long> tasksIdAddedInSprint;
    private List<Task> tasksDone = Lists.newArrayList();//only in response, not persisted
    private List<Task> tasksUnDone = Lists.newArrayList();//only in response, not persisted
    private List<Task> tasksAddedInSprint = Lists.newArrayList() ;//only in response, not persisted
    private List<PlotEntity> plotEntitiesRemaning;
    private Long nextReportId;
    private Long previousReportId;

    public ReportContent() {
    }

    public Set<Long> getTasksIdAddedInSprint() {
        return tasksIdAddedInSprint;
    }

    public void setTasksIdAddedInSprint(Set<Long> tasksIdAddedInSprint) {
        this.tasksIdAddedInSprint = tasksIdAddedInSprint;
    }

    public Set<Long> getTasksIdUnDone() {
        return tasksIdUnDone;
    }

    public void setTasksIdUnDone(Set<Long> tasksIdUnDone) {
        this.tasksIdUnDone = tasksIdUnDone;
    }

    public Set<Long> getTasksIdDone() {
        return tasksIdDone;
    }




    public Float getSumOfRemaining() {
        return sumOfRemaining;
    }

    public void setSumOfRemaining(Float sumOfRemaining) {
        this.sumOfRemaining = sumOfRemaining;
    }

    public Float getSumOfEstimate() {
        return sumOfEstimate;
    }

    public void setSumOfEstimate(Float sumOfEstimate) {
        this.sumOfEstimate = sumOfEstimate;
    }

    public Float getSumOfWorklog() {
        return sumOfWorklog;
    }

    public void setSumOfWorklog(Float sumOfWorklog) {
        this.sumOfWorklog = sumOfWorklog;
    }

    public Float getRemainingStart() {
        return remainingStart;
    }

    public void setRemainingStart(Float remainingStart) {
        this.remainingStart = remainingStart;
    }

    public Float getEstimateStart() {
        return estimateStart;
    }

    public void setEstimateStart(Float estimateStart) {
        this.estimateStart = estimateStart;
    }

    public void setTasksIdDone(Set<Long> tasksIdDone) {
        this.tasksIdDone = tasksIdDone;
    }

    public List<PlotEntity> getPlotEntitiesRemaning() {
        return plotEntitiesRemaning;
    }

    public void setPlotEntitiesRemaning(List<PlotEntity> plotEntitiesRemaning) {
        this.plotEntitiesRemaning = plotEntitiesRemaning;
    }


    public byte[] serializable() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ReportContent deserializable(byte[] date) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(date);
            ReportContent reportContent = null;
            reportContent = (ReportContent) new ObjectInputStream(bais).readObject();
            return  reportContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<KeyValue<Long, Float>> getWorkLogPerUser() {
        return workLogPerUser;
    }

    public void setWorkLogPerUser(List<KeyValue<Long, Float>> workLogPerUser) {
        this.workLogPerUser = workLogPerUser;
    }

    public List<Task> getTasksDone() {
        return tasksDone;
    }

    public void setTasksDone(List<Task> tasksDone) {
        this.tasksDone = tasksDone;
    }

    public List<Task> getTasksUnDone() {
        return tasksUnDone;
    }

    public void setTasksUnDone(List<Task> tasksUnDone) {
        this.tasksUnDone = tasksUnDone;
    }

    public List<Task> getTasksAddedInSprint() {
        return tasksAddedInSprint;
    }

    public void setTasksAddedInSprint(List<Task> tasksAddedInSprint) {
        this.tasksAddedInSprint = tasksAddedInSprint;
    }

    public Long getPreviousReportId() {
        return previousReportId;
    }

    public void setPreviousReportId(Long previousReportId) {
        this.previousReportId = previousReportId;
    }

    public Long getNextReportId() {
        return nextReportId;
    }

    public void setNextReportId(Long nextReportId) {
        this.nextReportId = nextReportId;
    }
}
