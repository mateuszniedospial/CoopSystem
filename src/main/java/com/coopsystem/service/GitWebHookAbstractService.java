package com.coopsystem.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.coopsystem.domain.Task;
import com.coopsystem.repository.JUserRepository;
import com.coopsystem.repository.JcommitRepository;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class GitWebHookAbstractService {
    protected final TaskRepository taskRepository;
    protected final JcommitRepository commitRepository;
    protected static final String COMIT_PATTERN_1="#FIX";
    protected static final String COMIT_PATTERN_2="#ADD 121,125 Title,12,11 of task 1\n sample message 1";
    protected final UserRepository userRepository;
    protected final JUserRepository jUserRepository;

    protected GitWebHookAbstractService(TaskRepository taskRepository, JcommitRepository commitRepository, UserRepository userRepository, JUserRepository jUserRepository) {
        this.taskRepository = taskRepository;
        this.commitRepository = commitRepository;
        this.userRepository = userRepository;
        this.jUserRepository = jUserRepository;
    }

    protected void createJCommit(Object commit, String message) {
        List<String> taskIds = extractIdOfTasks(message);
        Set<Task> tasks = Sets.newHashSet();
        for (String taskId : taskIds) {
            Long taskIdNumerical = parseTaskId(taskId);
            if (taskId != null) {
                Task task = taskRepository.findOne(taskIdNumerical);
                if (task != null){
                    tasks.add(task);
                }
            }
        }
        createCommitEntity(tasks,commit);
    }

    abstract void createCommitEntity(Set<Task> tasks, Object commit);

    protected static List<String> extractIdOfTasks(String message) {
        List<String> result = Lists.newArrayList();
        String[] split = message.split("/", 2);
        String firstLine = split[0];
        String ids = "";
        boolean firstDigit = true;
        boolean lastDigitOccure = false;
        for (char c : firstLine.toCharArray()) {
            if(Character.isDigit(c) && !lastDigitOccure){
                ids+=c;
                firstDigit = false;
            }
            if (c==",".charAt(0) && !lastDigitOccure){
                ids+=c;
            }
            if (!firstDigit && c!=",".charAt(0)&&!Character.isDigit(c)){
                lastDigitOccure=true;
            }
        }

        result = Arrays.asList(ids.split(","));
        System.out.print(result);
        return result;
    }


    protected Long parseTaskId(String taskId) {
        try {
            return Long.parseLong(taskId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

}
