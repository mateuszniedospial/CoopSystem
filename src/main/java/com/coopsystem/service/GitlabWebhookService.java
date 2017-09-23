package com.coopsystem.service;

import com.coopsystem.domain.*;
import com.coopsystem.repository.*;
import org.gitlab4j.api.webhook.EventCommit;
import org.gitlab4j.api.webhook.PushEvent;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class GitlabWebhookService extends GithubWebhookService {

    public GitlabWebhookService(TaskRepository taskRepository, JcommitRepository commitRepository, UserRepository userRepository, JUserRepository jUserRepository) {
        super(taskRepository, commitRepository, userRepository, jUserRepository);
    }

    public void handle(PushEvent pushEvent) {
        for (EventCommit eventCommit : pushEvent.getCommits()) {
            createJCommit(eventCommit, eventCommit.getMessage());
        }
    }

    @Override
    protected void createCommitEntity(Set<Task> tasks, Object commit) {
        EventCommit eventCommit = (EventCommit) commit;
        if (tasks.size() > 0) {
            Jcommit jcommit = new Jcommit();
            jcommit.setUrl(eventCommit.getUrl());
            jcommit.setCommitDate(ZonedDateTime.ofInstant(eventCommit.getTimestamp().toInstant(), ZoneId.systemDefault()));
            jcommit.setModified(eventCommit.getModified().toString());
            jcommit.setRemoved(eventCommit.getRemoved().toString());
            jcommit.setAdded(eventCommit.getAdded().toString());
            jcommit.setTasks(tasks);
            jcommit.setMessage(eventCommit.getMessage());
            Optional<User> user = userRepository.findOneByEmail(eventCommit.getAuthor().getEmail());
            if (user.get() != null) {
                JUser juser = jUserRepository.findJUserUsingJhiUser(user.get().getId());
                jcommit.setJUser(juser);
            }
            commitRepository.save(jcommit);
        }
    }

}
