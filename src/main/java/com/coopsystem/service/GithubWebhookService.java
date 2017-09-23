package com.coopsystem.service;

import com.coopsystem.domain.JUser;
import com.coopsystem.domain.Jcommit;
import com.coopsystem.domain.Task;
import com.coopsystem.domain.User;
import com.coopsystem.domain.github.Commit;
import com.coopsystem.domain.github.PushPayload;
import com.coopsystem.repository.JUserRepository;
import com.coopsystem.repository.JcommitRepository;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class GithubWebhookService extends GitWebHookAbstractService{


    public GithubWebhookService(TaskRepository taskRepository, JcommitRepository commitRepository, UserRepository userRepository, JUserRepository jUserRepository) {
        super(taskRepository, commitRepository, userRepository, jUserRepository);
    }

    public void handle(PushPayload pushEvent) {
        for (Commit commit : pushEvent.getCommits()) {
            createJCommit(commit, commit.getMessage());
        }
    }

    @Override
    protected void createCommitEntity(Set<Task> tasks, Object commit) {
        Commit commit1 = (Commit) commit;
        if (tasks.size() > 0) {
            Jcommit jcommit = new Jcommit();
            jcommit.setUrl(commit1.getUrl());
            jcommit.setCommitDate(ZonedDateTime.ofInstant(commit1.getTimestamp().toInstant(), ZoneId.systemDefault()));
            jcommit.setModified(commit1.getModified().toString());
            jcommit.setRemoved(commit1.getRemoved().toString());
            jcommit.setAdded(commit1.getAdded().toString());
            jcommit.setTasks(tasks);
            jcommit.setMessage(commit1.getMessage());
            Optional<User> user = userRepository.findOneByEmail(commit1.getAuthor().getEmail());
            if (user.get() != null) {
                JUser juser = jUserRepository.findJUserUsingJhiUser(user.get().getId());
                jcommit.setJUser(juser);
            }
            commitRepository.save(jcommit);
        }
    }




}
