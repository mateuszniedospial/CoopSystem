package com.coopsystem.service;

import com.google.common.collect.Lists;
import com.coopsystem.domain.Sprint;
import com.coopsystem.domain.Task;
import com.coopsystem.domain.enumeration.SprintLifeCycle;
import com.coopsystem.domain.enumeration.TaskLifeCycle;
import com.coopsystem.repository.SprintRepository;
import com.coopsystem.repository.TaskRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dariusz Ł on 22.04.2017.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final TaskHistoryDiffService taskHistoryDiffService;
    private final ReportService reportService;
    private List<TaskLifeCycle> taskNotFinishedState = Arrays.asList(TaskLifeCycle.TODO,TaskLifeCycle.INPROGRESS,
        TaskLifeCycle.REVIEWED, TaskLifeCycle.TEST);
    private List<TaskLifeCycle> taskFinishedState = Arrays.asList(TaskLifeCycle.CLOSED,TaskLifeCycle.DONE);
    public  TaskService(TaskRepository taskRepository, SprintRepository sprintRepository, TaskHistoryDiffService taskHistoryDiffService, ReportService reportService){
        this.taskRepository = taskRepository;
        this.sprintRepository = sprintRepository;
        this.taskHistoryDiffService = taskHistoryDiffService;
        this.reportService = reportService;
    }

    public enum TaskAction {
        ADD_TO_SPRINT, REMOVE_FROM_SPRINT, CHANGE_SPRINT_MEMBER,CHANGE_LIFE_CYCLE_TO_DONE_OR_CLOSED, TASK_REOPEN
    }


    @Async
    @Transactional
    public  void updateSprintIfRequired(Task task, Task taskFromDB) {
        TaskAction taskAction = null;
        Sprint savedSprint=null;
        boolean isSprintStoped = isSprintStoped(task,taskFromDB);
        if(isTaskAddedToSprint(taskFromDB, task)){
            changeRemaining(taskFromDB, task, false);
            savedSprint = changeEstimate(taskFromDB, task, false);
            taskAction = TaskAction.ADD_TO_SPRINT;
        } else if(isTaskRemovedFromSprint(taskFromDB, task) && !isSprintStoped){
            changeRemaining(taskFromDB, task, true);
            savedSprint = changeEstimate(taskFromDB, task, true);
            taskAction = TaskAction.REMOVE_FROM_SPRINT;
        } else if (isTaskSprintMember(taskFromDB)) {
            changeEstimateInSprintIfRequired(taskFromDB, task);
            savedSprint = changeRemainigInSprintIfRequired(taskFromDB, task);
            taskAction = TaskAction.CHANGE_SPRINT_MEMBER;
        }
        if(isChangeLifeCycleToDoneOrClose(task, taskFromDB)){
            taskAction = TaskAction.CHANGE_LIFE_CYCLE_TO_DONE_OR_CLOSED;
            savedSprint  = task.getSprint();
        } else if(isTaskReopen(task,taskFromDB)){
            taskAction = TaskAction.TASK_REOPEN;
            savedSprint  = task.getSprint();
        }
        reportService.updateReport(savedSprint,taskAction,task);

    }

    private boolean isSprintStoped(Task task, Task taskFromDB) {
        return task.getSprint() == null && taskFromDB.getSprint().getLifeCycle().equals(SprintLifeCycle.OUTDATED);
    }

    private boolean isTaskReopen(Task task, Task taskFromDB) {
        TaskLifeCycle tLC = task.getLifeCycle();
        TaskLifeCycle oLC = taskFromDB.getLifeCycle();
        return  taskNotFinishedState.contains(tLC) &&  taskFinishedState.contains(oLC);
    }

    private boolean isChangeLifeCycleToDoneOrClose(Task task, Task taskFromDB) {
        TaskLifeCycle tLC = task.getLifeCycle();
        TaskLifeCycle oLC = taskFromDB.getLifeCycle();
        return  taskFinishedState.contains(tLC) &&  taskNotFinishedState.contains(oLC);
    }

    @Async
    @Transactional
    public  void updateTaskHistory(Task task, Task taskFromDB){
        taskHistoryDiffService.updateTaskHistory(task, taskFromDB);
    }

    private boolean isTaskAddedToSprint(Task taskFromDB, Task task) {
        return taskFromDB.getSprint() == null && task.getSprint() != null;
    }

    private boolean isTaskRemovedFromSprint(Task taskFromDB, Task task) {
        return taskFromDB.getSprint() != null && task.getSprint() == null;
    }

    private boolean isTaskSprintMember(Task taskFromDB) {
        return taskFromDB.getSprint() != null;
    }



    private Sprint changeRemainigInSprintIfRequired(Task taskFromDB, Task task) {
        String newTaskRem = task.getRemainingTime();
        if(newTaskRem != null) {
            if(isFieldChanged(newTaskRem, taskFromDB.getRemainingTime())){
                return changeRemaining(taskFromDB, task, false);
            }
        }
        return null;
    }

    private Sprint changeRemaining(Task taskFromDB, Task task, boolean isTaskRemovedFromSprint) {
        List<Task> tasksBySprintId = taskRepository.findTasksBySprintId(extractSprintId(task,taskFromDB));
        float sumOfRemaining = 0;
        for (Task t : tasksBySprintId) {
            if (!t.getId().equals(task.getId())){
                sumOfRemaining+=parseEstimateOrRemianigTime(t.getRemainingTime());
            }
        }
        if(!isTaskRemovedFromSprint)
            sumOfRemaining+=parseEstimateOrRemianigTime(task.getRemainingTime());
        Sprint sprint = sprintRepository.findOne(extractSprintId(task,taskFromDB));
        sprint.setSumOfRemaining(sumOfRemaining);
        return sprintRepository.save(sprint);
    }

    private Long extractSprintId(Task task, Task taskFromDB) {
        if(task.getSprint() != null) {
            return task.getSprint().getId();
        } else if(taskFromDB.getSprint().getId() != null) {
            return taskFromDB.getSprint().getId();
        }
        return 0l;
    }

    private Sprint changeEstimateInSprintIfRequired(Task taskFromDB, Task task) {
        String newTaskEstim = task.getEstimateTime();
        if(newTaskEstim != null) {
            if(isFieldChanged(newTaskEstim, taskFromDB.getEstimateTime())){
                return changeEstimate(taskFromDB, task, false);
            }
        }
        return null;
    }

    private Sprint changeEstimate(Task taskFromDB, Task task, boolean isTaskRemovedFromSprint) {
        List<Task> tasksBySprintId = taskRepository.findTasksBySprintId(extractSprintId(task,taskFromDB));
        float sumOfEstimate = 0;
        for (Task t : tasksBySprintId) {
            if (!t.getId().equals(task.getId())){
                sumOfEstimate+=parseEstimateOrRemianigTime(t.getEstimateTime());
            }
        }
        if(!isTaskRemovedFromSprint)
            sumOfEstimate+=parseEstimateOrRemianigTime(task.getEstimateTime());
        Sprint sprint = sprintRepository.findOne(extractSprintId(task,taskFromDB));
        sprint.setSumOfEstimate(sumOfEstimate);
        return sprintRepository.save(sprint);
    }


    public static float parseEstimateOrRemianigTime(String time) {
        if(time != null && !"?".equals(time)) {
            int commaIndex = time.indexOf(".");
            time = time.replaceAll("[^-?0-9]+", "");
            if(commaIndex == -1){
                return Float.parseFloat(time);
            }
            List<String> list = Arrays.asList(time.trim().split(""));
            float result = 0;
            int i = commaIndex-1;
            for (String el : list) {
                result+=Integer.parseInt(el) * Math.pow(10,i--);
            }
            return result;
        } else {
            return 0;
        }
    }

    public List<Task> getAllClosedOrDoneTaskFromLastSprintByJGroupId(Long jGroupId){
        return getTasksFromLastSprintByState(jGroupId, taskFinishedState);
    }

    @Transactional
    public List<Task> getAllTaskFromLastSprintByJGroupId(Long jGroupId){
        return getTasksFromLastSprintByState(jGroupId, taskNotFinishedState);
    }

    private List<Task> getTasksFromLastSprintByState(Long jGroupId, List<TaskLifeCycle> state) {
        List<Task> allJGroupTasks = taskRepository.findAllJGroupTasksWithEagerRelationships(jGroupId);
        List<Sprint> sprints = sprintRepository.findSprintsByJGroupIdAndLifeCycleOrderByCreatedDateDesc(jGroupId, SprintLifeCycle.OUTDATED);
        List<Task> result = Lists.newArrayList();
        if(sprints.iterator().hasNext()){
            Sprint lastSprint = sprints.iterator().next();
            //REFACTOR ME
            for (Task task : allJGroupTasks) {
                if(state.contains(task.getLifeCycle())){
                    Optional<Sprint> sprintOptional = task.getHistoric_sprints().stream().filter(it -> it.getId().equals(lastSprint.getId())).findFirst();
                    sprintOptional.ifPresent(sprint -> result.add(task));

                }
            }
        }
        return result;
    }

    private boolean isFieldChanged(String newValue, String oldValue) {
        return !newValue.equals(oldValue);
    }

    @Async
    public void removeTaskFromSprint(Long sprintID){
        taskRepository.findTasksBySprintId(sprintID).forEach(task -> {
            task.setSprint(null);
            taskRepository.save(task);
        });
    }
}
