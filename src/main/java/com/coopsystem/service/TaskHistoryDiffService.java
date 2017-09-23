package com.coopsystem.service;

import com.google.common.collect.Lists;
import com.coopsystem.domain.*;
import com.coopsystem.repository.JUserRepository;
import com.coopsystem.repository.TaskHistoryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Dariusz Ł on 27.04.2017.
 */
@Service
public class TaskHistoryDiffService {
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskHistoryService taskHistoryService;
    private final JUserRepository jUserRepository;

    List<String> taskFields = Arrays.asList("Title", "Estimate time", "Remaining Time", "Enviroment", "Life Cycle", "Priority",
        "Version", "Type", "Sprint", "Assignee", "Watchers", "Group", "");
    List<String> anotherFieldsRelatedToTask = Arrays.asList("Task Description", "Comment");


    public  TaskHistoryDiffService(TaskHistoryRepository taskHistoryRepository, TaskHistoryService taskHistoryService, JUserRepository jUserRepository){
        this.taskHistoryRepository=taskHistoryRepository;
        this.taskHistoryService = taskHistoryService;
        this.jUserRepository = jUserRepository;
    }

    @Async
    @Transactional
    public void createHistoryForComment(Comment comment) {
        JUser juser = jUserRepository.findOne(comment.getAuthor().getId());
        TaskHistory tH = new TaskHistory();
        tH.setCreated_date(ZonedDateTime.now());
        tH.setTask(comment.getTask());
        tH.setModifiedJUser(juser);
        tH.setChangedName("Comment");
        tH.setOldContent("");
        tH.setContent(comment.getContent());
        taskHistoryRepository.save(tH);
        taskHistoryService.sendNotifiaction(tH);
    }

    @Async
    @Transactional
    public void createHistoryForAttachment(Attachment attachment, String action) {
        TaskHistory tH = new TaskHistory();
        tH.setCreated_date(ZonedDateTime.now());
        tH.setTask(attachment.getTask());
        tH.setModifiedJUser(attachment.getAuthor());
        switch(action){
            case "add":
                tH.setChangedName("Add Attachment");
                tH.setOldContent("");
                tH.setContent(attachment.getName());
                break;
            case "remove":
                tH.setChangedName("Remove Attachment");
                tH.setOldContent(attachment.getName());
                tH.setContent("");
                break;
        }
        taskHistoryRepository.save(tH);
        taskHistoryService.sendNotifiaction(tH);
    }

    @Async
    @Transactional
    public void updateTaskCommentHistory(Comment oldComment, Comment newComment) {
        TaskHistory tH = new TaskHistory();
        tH.setCreated_date(ZonedDateTime.now());
        tH.setTask(oldComment.getTask());
        tH.setModifiedJUser(oldComment.getAuthor());
        fullfillHistory(tH, newComment.getContent(), oldComment.getContent(), anotherFieldsRelatedToTask.get(1));
        taskHistoryRepository.save(tH);
        taskHistoryService.sendNotifiaction(tH);
    }

    @Async
    @Transactional
    public void updateTaskDescriptionHistory(TaskDescription newTaskDescription, TaskDescription oldTaskDescription){
        TaskHistory tH = new TaskHistory();
        tH.setCreated_date(ZonedDateTime.now());
        tH.setTask(newTaskDescription.getTask());
        tH.setModifiedJUser(newTaskDescription.getTask().getJuser());
        fullfillHistory(tH,
            newTaskDescription.getContent() == null ? "" : newTaskDescription.getContent() ,
            oldTaskDescription.getContent() == null ? "" : oldTaskDescription.getContent(),
            anotherFieldsRelatedToTask.get(0));
        taskHistoryRepository.save(tH);
        taskHistoryService.sendNotifiaction(tH);

    }

    @Async
    @Transactional
    public void updateTaskHistory(Task newT, Task oldT){
        TaskHistory tH = new TaskHistory();
        tH.setCreated_date(ZonedDateTime.now());
        tH.setTask(newT);
        tH.setModifiedJUser(newT.getJuser());
        if(isDiff(newT.getTitle(), oldT.getTitle())){
            fullfillHistory(tH,newT.getTitle(), oldT.getTitle(), taskFields.get(0));
        } else if(isDiff(newT.getEstimateTime(), oldT.getEstimateTime())){
            fullfillHistory(tH,newT.getEstimateTime(), oldT.getEstimateTime(), taskFields.get(1));
        } else if(isDiff(newT.getRemainingTime(), oldT.getRemainingTime())){
            fullfillHistory(tH,newT.getRemainingTime(), oldT.getRemainingTime(), taskFields.get(2));
        } else if (isDiff(newT.getEnviroment(), oldT.getEnviroment())){
            fullfillHistory(tH,newT.getEnviroment(), oldT.getEnviroment(), taskFields.get(3));
        } else if(isDiff(newT.getLifeCycle(), oldT.getLifeCycle())){
            fullfillHistory(tH,newT.getLifeCycle().toString(), oldT.getLifeCycle().toString(), taskFields.get(4));
        } else if(isDiff(newT.getPriority(), oldT.getPriority())){
            fullfillHistory(tH,newT.getPriority().toString(), oldT.getPriority().toString(), taskFields.get(5));
        } else if(isDiff(newT.getVersion(), oldT.getVersion())){
            fullfillHistory(tH,newT.getEstimateTime(), oldT.getEstimateTime(), taskFields.get(6));
        } else if(isDiff(newT.getType(), oldT.getType())){
            fullfillHistory(tH,newT.getType().toString(), oldT.getType().toString(), taskFields.get(7));
        } else if(isSprintFieldModified(newT.getSprint(), oldT.getSprint())){
            fullfillHistoryForSprint(tH,newT.getSprint(), oldT.getSprint(), taskFields.get(8));
        } else if(isAssigneFieldModified(newT.getAssignee(), oldT.getAssignee())){
            fullfillHistoryForAssigne(tH,newT.getAssignee(), oldT.getAssignee(), taskFields.get(9));
        } else if(isWatchersModified(newT.getWatchers(), oldT.getWatchers())){
            fullfillHistoryForWatchers(tH,newT.getWatchers(), oldT.getWatchers(), taskFields.get(10));
        } else if(isJGroupModified(newT.getJGroup(), oldT.getJGroup())){
            fullfillHistoryForJGroup(tH,newT.getJGroup(), oldT.getJGroup(), taskFields.get(11));
        }
        taskHistoryRepository.save(tH);
        taskHistoryService.sendNotifiaction(tH);

    }


    private void fullfillHistoryForWatchers(TaskHistory taskHistory, Set<JUser> watchers, Set<JUser> oldWatchers, String changeName) {
        String oldContent =(oldWatchers == null)? "": "calc";
        String newContent =(watchers == null)? "": "calc";
        if (oldContent.equals("calc")){
            oldContent = buildWatchersString(oldWatchers);
        }
        if (newContent.equals("calc")) {
            newContent = buildWatchersString(watchers);
        }
        fullfillHistory(taskHistory, newContent, oldContent,changeName);
    }

    private String buildWatchersString(Set<JUser> oldWatchers) {
        String result;
        StringBuilder builder = new StringBuilder();
        oldWatchers.forEach(it->{
            builder.append(it.getUser().getLogin()+",");
        });
        result=builder.toString();
        return result;
    }

    private void fullfillHistoryForJGroup(TaskHistory taskHistory, JGroup jGroup, JGroup oldJGroup, String changeName) {
        String oldContent =(oldJGroup == null)? "": oldJGroup.getId().toString();
        String newContent =(jGroup == null)? "": jGroup.getId().toString();
        fullfillHistory(taskHistory, newContent, oldContent,changeName);
    }

    private void fullfillHistoryForAssigne(TaskHistory taskHistory, JUser assignee, JUser oldAssigne, String changedName) {
        String oldContent =(oldAssigne == null)? "": oldAssigne.getUser().getLogin();
        String newContent =(assignee == null)? "": assignee.getUser().getLogin();
        fullfillHistory(taskHistory, newContent, oldContent,changedName);
    }


    private void fullfillHistoryForSprint(TaskHistory taskHistory, Sprint sprint, Sprint oldSprint, String changedName) {
        String oldContent =(oldSprint == null)? "": oldSprint.getId().toString();
        String newContent =(sprint == null)? "": sprint.getId().toString();
        fullfillHistory(taskHistory, newContent, oldContent,changedName);
    }

    private void fullfillHistory(TaskHistory taskHistory, String newContent, String oldContent, String changedName) {
        taskHistory.setOldContent(oldContent);
        taskHistory.setContent(newContent);
        taskHistory.setChangedName(changedName);
    }

    private boolean isJGroupModified(JGroup jGroup, JGroup oldJGroup) {
        if(isDiff(jGroup, oldJGroup)){
            return true;
        }
        return isDiff(jGroup.getId(), oldJGroup.getId());
    }

    private boolean isWatchersModified(Set<JUser> watchers, Set<JUser> oldWatchers) {
        if(isDiff(watchers, oldWatchers)){
            return true;
        } else if(watchers.size() != oldWatchers.size()){
            return true;
        } else {
            List<Long> oldWatchersId = Lists.newArrayList();
            oldWatchers.forEach(it->{oldWatchersId.add(it.getId());});
            for (JUser it : watchers) {
                if(!oldWatchers.contains(it.getId())){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAssigneFieldModified(JUser assignee, JUser oldassignee) {
        if(isDiff(assignee, oldassignee)){
            return true;
        }
        return isDiff(assignee.getId(), oldassignee.getId());
    }

    private boolean isSprintFieldModified(Sprint sprint, Sprint oldSprint) {
        if(isDiff(sprint, oldSprint)){
            return true;
        }
        if(sprint==null && oldSprint == null) return false;
        return isDiff(sprint.getId(), oldSprint.getId());
    }

    private boolean isDiff(Object newValue, Object oldValue) {
        if (newValue==null && oldValue==null) return false;
        if (newValue == null && oldValue != null || newValue != null && oldValue == null) return true;
        return !newValue.equals(oldValue);
    }

}
