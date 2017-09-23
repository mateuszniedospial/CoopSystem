package com.coopsystem.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.coopsystem.domain.*;
import com.coopsystem.domain.enumeration.SprintLifeCycle;
import com.coopsystem.domain.enumeration.TaskLifeCycle;
import com.coopsystem.repository.ReportRepository;
import com.coopsystem.repository.SprintRepository;
import com.coopsystem.repository.TaskRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dariusz Ł on 02.05.2017.
 */
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private  final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;

    public ReportService(ReportRepository reportRepository, TaskRepository taskRepository, SprintRepository sprintRepository) {
        this.reportRepository = reportRepository;
        this.taskRepository = taskRepository;
        this.sprintRepository = sprintRepository;
    }

    @Async
    public void createNEWReport(Sprint sprint, Sprint previousSprint){
        if (previousSprint == null) {
            List<Sprint> previousSprints = sprintRepository.findSprintsByJGroupIdAndLifeCycleOrderByCreatedDateDesc(sprint.getJGroup().getId(), SprintLifeCycle.OUTDATED);
            if(!previousSprints.isEmpty()) {
                previousSprint = previousSprints.iterator().next();
            }
        }
        Report report = new Report();
        report.setCreatedDate(ZonedDateTime.now());
        report.setSprint(sprint);
        ReportContent reportContent = new ReportContent();
        reportContent.setEstimateStart(sprint.getSumOfEstimate());
        reportContent.setRemainingStart(sprint.getSumOfRemaining());
        reportContent.setSumOfEstimate(sprint.getSumOfEstimate());
        reportContent.setSumOfRemaining(sprint.getSumOfRemaining());
        reportContent.setSumOfWorklog(0f);
        reportContent.setPlotEntitiesRemaning(Lists.newArrayList());
        reportContent.setPreviousReportId(findPreviousReport(previousSprint));
        reportContent.setWorkLogPerUser(createWorkLogs(sprint));
        report.setData(reportContent.serializable());
        Report save = reportRepository.save(report);
        if(previousSprint != null){
            updatePreviousReportIfRequired(save, previousSprint);
        }
    }

    private void updatePreviousReportIfRequired(Report save, Sprint previousSprint) {
        Report report = reportRepository.findOneBySprintId(previousSprint.getId());
        if (report !=null) {
            ReportContent reportContent = ReportContent.deserializable(report.getData());
            reportContent.setNextReportId(save.getId());
            report.setData(reportContent.serializable());
            reportRepository.save(report);
        }
    }

    private Long findPreviousReport(Sprint previousSprint) {
        if (previousSprint ==null)
            return null;
        Report report = reportRepository.findOneBySprintId(previousSprint.getId());
        return (report==null)?null: report.getId();
    }

    private List<KeyValue<Long, Float>> createWorkLogs(Sprint sprint) {
        List<KeyValue<Long, Float>> result = Lists.newArrayList();
        sprint.getJGroup().getJusers().forEach(jUser -> {
            KeyValue keyValue = new KeyValue<>();
            keyValue.add(jUser.getId(), 0f);
            result.add(keyValue);
        } );
        return result;
    }

    @Async
    @Transactional
    public void updateWorkLogs(WorkLog workLog) {
        Sprint sprint = taskRepository.getOne(workLog.getTask().getId()).getSprint();
        JUser juser = workLog.getJuser();
        Report report = reportRepository.findOneBySprintId(sprint.getId());
        ReportContent reportContent = ReportContent.deserializable(report.getData());
        reportContent.getWorkLogPerUser().forEach((keyValue)->{
            if (keyValue.getKey().equals(juser.getId())){
                keyValue.setValue(keyValue.getValue() + workLog.getTimeInHour());
            }
        });
        reportContent.setSumOfWorklog(reportContent.getSumOfWorklog()+ workLog.getTimeInHour());
        report.setData(reportContent.serializable());
        reportRepository.save(report);
    }

    @Scheduled(fixedDelay = 24*60*60*1000)//24 hour
//    @Scheduled(fixedDelay =60*1000)//1 minut - for test
    public void updateRemainingPlotDate(){
        List<Sprint> activeSprints = sprintRepository.findByLifeCycle(SprintLifeCycle.ACTIVE);
        activeSprints.forEach(sprint -> {
            updateRemainingPlotDataBySprint(sprint);
        });

    }

    @Async
    @Transactional
    private void updateRemainingPlotDataBySprint(Sprint sprint) {
        List<Task> sprintTasks = taskRepository.findTasksBySprintId(sprint.getId());
        Float sumOfRemainig = 0f;
        for (Task sprintTask : sprintTasks) {
            sumOfRemainig+= TaskService.parseEstimateOrRemianigTime(sprintTask.getRemainingTime());
        }
        Report report = reportRepository.findOneBySprintId(sprint.getId());
        if (report != null){
            ReportContent reportContent = ReportContent.deserializable(report.getData());
            List<PlotEntity> plotEntitiesRemaning = reportContent.getPlotEntitiesRemaning();
            if (plotEntitiesRemaning == null) {
                plotEntitiesRemaning = Lists.newArrayList();
            }
            plotEntitiesRemaning.add(new PlotEntity(new Date(), sumOfRemainig));
            report.setData(reportContent.serializable());
            reportRepository.save(report);
        }
    }

    public void updateReportIfSprintStarted(Sprint sprintReq, Sprint sprintDB ) {
        if(isSprintStartedNow(sprintDB,sprintReq)){
            Report report = reportRepository.findOneBySprintId(sprintDB.getId());
            ReportContent reportContent = ReportContent.deserializable(report.getData());
            report.setData(reportContent.serializable());
            reportRepository.save(report);
        }
    }

    private boolean isSprintStartedNow(Sprint sprintDB, Sprint sprintReq) {
        return sprintReq.getLifeCycle().equals(SprintLifeCycle.ACTIVE) && sprintDB.getLifeCycle().equals(SprintLifeCycle.FUTURE);
    }

    public void updateReport(Sprint modifiedSprint, TaskService.TaskAction taskAction, Task task){
        if(modifiedSprint != null){
            Report report = reportRepository.findOneBySprintId(modifiedSprint.getId());
            ReportContent reportContent = ReportContent.deserializable(report.getData());
            initListIfRequiered(reportContent);
            reportContent.setSumOfEstimate(modifiedSprint.getSumOfEstimate());
            reportContent.setSumOfRemaining(modifiedSprint.getSumOfRemaining());
            if(modifiedSprint.getLifeCycle().equals(SprintLifeCycle.FUTURE)){
                reportContent.setEstimateStart(modifiedSprint.getSumOfEstimate());
                reportContent.setRemainingStart(modifiedSprint.getSumOfRemaining());
            }

            switch (taskAction){
                case ADD_TO_SPRINT:
                    addToAddedToSprintListIfRequired(task, reportContent, modifiedSprint);
                    if(isTaskDone(task)){
                        reportContent.getTasksIdDone().add(task.getId());
                    } else {
                        reportContent.getTasksIdUnDone().add(task.getId());
                    }
                    break;
                case REMOVE_FROM_SPRINT:
                    remove(reportContent.getTasksIdAddedInSprint(), task);
                    remove(reportContent.getTasksIdDone(), task);
                    remove(reportContent.getTasksIdUnDone(), task);
                    break;
                case TASK_REOPEN:
                    remove(reportContent.getTasksIdDone(), task);
                    reportContent.getTasksIdUnDone().add(task.getId());
                    break;
                case CHANGE_LIFE_CYCLE_TO_DONE_OR_CLOSED:
                    remove(reportContent.getTasksIdUnDone(), task);
                    reportContent.getTasksIdDone().add(task.getId());
                    break;
            }
            report.setData(reportContent.serializable());
            reportRepository.save(report);
        }
    }

    private void addToAddedToSprintListIfRequired(Task task, ReportContent reportContent, Sprint modifiedSprint) {
        if(!modifiedSprint.getLifeCycle().equals(SprintLifeCycle.FUTURE)){
            reportContent.getTasksIdAddedInSprint().add(task.getId());
        }
    }

    private void remove(Collection list, Task task) {
        for (Iterator<Long> iterator = list.iterator(); iterator.hasNext();){
            Long id = iterator.next();
            if(id.equals(task.getId())){
                iterator.remove();
            }
        }
    }

    private void initListIfRequiered(ReportContent reportContent) {
        if (reportContent.getTasksIdAddedInSprint() == null) {
            reportContent.setTasksIdAddedInSprint(Sets.newHashSet());
        }
        if (reportContent.getTasksIdDone() == null) {
            reportContent.setTasksIdDone(Sets.newHashSet());
        }
        if (reportContent.getTasksIdUnDone() == null) {
            reportContent.setTasksIdUnDone(Sets.newHashSet());
        }
    }

    private boolean isTaskDone(Task task) {
        return task.getLifeCycle().equals(TaskLifeCycle.DONE) || task.getLifeCycle().equals(TaskLifeCycle.CLOSED);
    }

}
