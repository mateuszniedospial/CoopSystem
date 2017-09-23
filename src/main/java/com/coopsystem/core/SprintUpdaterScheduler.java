package com.coopsystem.core;

import com.google.common.collect.Lists;
import com.coopsystem.domain.Sprint;
import com.coopsystem.domain.Task;
import com.coopsystem.domain.enumeration.SprintLifeCycle;
import com.coopsystem.repository.SprintRepository;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.service.ReportService;
import com.coopsystem.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Dariusz Ł on 21.04.2017.
 */
@Configuration
@EnableScheduling
public class SprintUpdaterScheduler {
    private static final Logger log = LoggerFactory.getLogger(SprintUpdaterScheduler.class);
    private final SprintRepository sprintRepository;
    private TaskService taskService;
    private TaskRepository taskRepository;
    private ReportService reportService;
    public SprintUpdaterScheduler(SprintRepository sprintRepository, TaskService taskService, TaskRepository taskRepository, ReportService reportService) {
        this.sprintRepository = sprintRepository;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.reportService = reportService;
    }

    @Scheduled(fixedDelay = 60*1000)
    public void closeSprintsIfRequired() {
        List<Sprint> depracatedSprints = sprintRepository.findByStopTimeBeforeAndLifeCycle(ZonedDateTime.now(), SprintLifeCycle.ACTIVE);
        depracatedSprints.forEach((sprint) -> {sprint.setLifeCycle(SprintLifeCycle.OUTDATED);});
        if(!depracatedSprints.isEmpty()){
            sprintRepository.save(depracatedSprints);
        }
        depracatedSprints.forEach(sprint -> {startNewDefaultSprintIfRequired(sprint);});
    }

    @Async
    @Transactional
    private void startNewDefaultSprintIfRequired(Sprint sprint) {
        Sprint saved = createNewSprint(sprint);
        float sumOfRemaining = 0;
        float sumOfEstimate = 0;
        List tasksToUpdate = Lists.newArrayList();
        for (Task t : taskService.getAllTaskFromLastSprintByJGroupId(sprint.getJGroup().getId())) {
            sumOfEstimate+=TaskService.parseEstimateOrRemianigTime(t.getEstimateTime());
            sumOfRemaining+=TaskService.parseEstimateOrRemianigTime(t.getRemainingTime());
            t.setSprint(saved);
            tasksToUpdate.add(t);
        }
        saved.setSumOfRemaining(sumOfRemaining);
        saved.setSumOfEstimate(sumOfEstimate);
        sprintRepository.save(saved);
        taskRepository.save(tasksToUpdate);
        taskService.getAllClosedOrDoneTaskFromLastSprintByJGroupId(sprint.getJGroup().getId()).forEach(task ->{
            task.setSprint(null);
            taskRepository.save(task);
        });
        reportService.createNEWReport(saved, sprint);
    }

    private Sprint createNewSprint(Sprint sprint) {
        Sprint newSprint = new Sprint();
        newSprint.setCreatedDate(ZonedDateTime.now());
        newSprint.setStartTime(ZonedDateTime.now());
        newSprint.setJGroup(sprint.getJGroup());
        newSprint.setLifeCycle(SprintLifeCycle.ACTIVE);
        newSprint.setDescription(sprint.getDescription());
        newSprint.setTitle(sprint.getTitle());
        newSprint.setDurationTime(sprint.getDurationTime());
        newSprint.setStopTime(ZonedDateTime.now().plusDays(sprint.getDurationTime()*7));
        return sprintRepository.save(newSprint);
    }


}
