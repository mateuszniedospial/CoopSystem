package com.coopsystem.service;

import com.coopsystem.domain.Task;
import com.coopsystem.domain.TaskHistory;
import com.coopsystem.domain.User;
import com.coopsystem.repository.JUserRepository;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.repository.UserRepository;
import com.coopsystem.web.socket.GreetingController;
import com.coopsystem.web.socket.HelloMessage;
import com.coopsystem.web.socket.notification.ChangeInTask;
import com.coopsystem.web.socket.notification.Notification;
import com.coopsystem.web.socket.notification.NotificationSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Dariusz Ł on 29.03.2017.
 */
@Service
@Transactional
public class TaskHistoryService {
    private final UserRepository userRepository;
    private  final JUserRepository jUserRepository;
    private final TaskRepository taskRepository;
    private final MailService mailService;
    private final GreetingController greetingController;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationSender notificationSender;

    public TaskHistoryService(UserRepository userRepository, JUserRepository jUserRepository, TaskRepository taskRepository, MailService mailService, GreetingController greetingController, SimpMessagingTemplate messagingTemplate, NotificationSender notificationSender) {
        this.userRepository = userRepository;
        this.jUserRepository = jUserRepository;
        this.taskRepository = taskRepository;
        this.mailService = mailService;
        this.greetingController = greetingController;
        this.messagingTemplate = messagingTemplate;
        this.notificationSender = notificationSender;
    }

    public String createContentForChangeEmail(TaskHistory taskHistory) {
        User changedUser = taskHistory.getModifiedJUser().getUser();
        StringBuilder builder = new StringBuilder();
        builder
            .append("<b>User ")
            .append(changedUser.getLogin())
            .append("</b><br>changed task:<br>")
            .append(taskHistory.getTask().getId())
            .append(":")
            .append(taskHistory.getTask().getTitle())
            .append("<br>changed: ")
            .append(taskHistory.getChangedName())
            .append("<br>from:<br>")
            .append(taskHistory.getOldContent())
            .append("<br>to:<br>")
            .append(taskHistory.getContent())
            .append("<br>Date:")
            .append(taskHistory.getCreated_date());
        return builder.toString();
    }

    private User getUser(Long jhiJUserId) {
        return userRepository.findOne(jhiJUserId);
    }

    @Async
    public void sendTaskUpdatedEmail(TaskHistory taskHistory) {
        sendNotifiaction(taskHistory);
        String content = createContentForChangeEmail(taskHistory);
        Task task = taskRepository.findOne(taskHistory.getTask().getId());
        User changedUser = taskHistory.getModifiedJUser().getUser();
        sendToWatchersIfRequired(content, task, changedUser);
        sendToAssigneeIfRequired(content, task, changedUser);
        sendToReporterIfRequired(content, task, changedUser);
    }

    public void sendNotifiaction(TaskHistory taskHistory) {
        HelloMessage helloMessage = new HelloMessage();
        helloMessage.setName(taskHistory.getChangedName());
        ChangeInTask changeInTask = new ChangeInTask();
        changeInTask.setChangeAuthor(taskHistory.getModifiedJUser().getUser().getLogin())
            .setChangedField(taskHistory.getChangedName())
            .setModifyDate(new Date())
            .setChangedField(taskHistory.getChangedName())
            .setTaskId(taskHistory.getTask().getId().toString())
            .setTaskTitle(taskHistory.getTask().getTitle())
            .setFrom(taskHistory.getOldContent())
            .setTo(taskHistory.getContent());
        Notification<ChangeInTask> notification = new Notification<>();
        notification.setContent(changeInTask);
        notificationSender.send(notification, "/topic/greetings/"+taskHistory.getModifiedJUser().getUser().getId());
    }

    private void sendToReporterIfRequired(String content, Task task, User changedUser) {
        if(task.getReporter().getUser().equals(changedUser.getId())){
            User reporter = task.getReporter().getUser();
            mailService.sendChangeTaskEmail(reporter,content);
        }
    }

    private void sendToAssigneeIfRequired(String content, Task task, User changedUser) {
        if(task.getAssignee() != null) {
            if(!task.getAssignee().getUser().equals(changedUser.getId())) {
                User assignee = task.getAssignee().getUser();
                mailService.sendChangeTaskEmail(assignee,content);
            }
        }
    }

    private void sendToWatchersIfRequired(String content, Task task, User changedUser) {
        task.getWatchers().forEach((jUser -> {
            User user = jUser.getUser();
            if(!changedUser.getId().equals(user.getId())){
                mailService.sendChangeTaskEmail(user,content);
            }
        }));
    }
}
