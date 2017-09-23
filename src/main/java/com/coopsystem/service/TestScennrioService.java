package com.coopsystem.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.coopsystem.domain.JGroup;
import com.coopsystem.domain.JUser;
import com.coopsystem.domain.Project;
import com.coopsystem.domain.User;
import com.coopsystem.domain.enumeration.LifeCycle;
import com.coopsystem.repository.JGroupRepository;
import com.coopsystem.repository.JUserRepository;
import com.coopsystem.repository.ProjectRepository;
import com.coopsystem.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TestScennrioService {
    private final UserRepository userRepository;
    private final JUserRepository jUserRepository;
    private final JGroupRepository jGroupRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    List<User> users = Lists.newArrayList();
    List<JGroup> jGroups = Lists.newArrayList();

    public TestScennrioService(UserRepository userRepository, JUserRepository jUserRepository, JGroupRepository jGroupRepository, UserService userService, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.jUserRepository = jUserRepository;
        this.jGroupRepository = jGroupRepository;
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    public boolean createGroup(Long sizeOdGroup) {
        Set<JUser> jUsers = Sets.newConcurrentHashSet();
        initUsersIfrequired();
        int counter = 0;
        int i = 0;
        for (User user : users) {
            JUser juser = jUserRepository.findJUserUsingJhiUser(user.getId());
            if (juser != null) {
                jUsers.add(juser);
                if (counter == sizeOdGroup) {
                    JGroup jGroup = new JGroup();
                    jGroup.setCreatedDate(ZonedDateTime.now());
                    jGroup.setDescription("Example group");
                    jGroup.setJusers(jUsers);
                    jGroup.setLifeCycle(LifeCycle.ACTIVE);
                    jGroup.setName("Example group nr: " + ++i);
                    jGroups.add(jGroupRepository.save(jGroup));
                    jUsers.clear();
                    counter = 0;
                }
                counter++;
            }
        }
        return true;
    }

    public void initUsersIfrequired() {
        if (users.size() == 0)
            users.addAll(userRepository.findAll());
    }

    public boolean createProjects(Long numberOfGroup) {
        initJGroupIfRequired();
        int counter = 0;
        int i = 0;
        Set<JGroup> jGroupsSet = Sets.newConcurrentHashSet();
        for (JGroup jGroup : jGroups) {
            jGroupsSet.add(jGroup);
            if (counter == numberOfGroup) {
                Project project = new Project();
                project.setName("Example project nr " + i++);
                project.setDescription("Example description");
                project.setLifeCycle(LifeCycle.ACTIVE);
                project.setCreatedDate(ZonedDateTime.now());
                project.setJGroups(jGroupsSet);
                projectRepository.save(project);
                jGroupsSet.clear();
                counter = 0;
            }
            counter++;
        }

        return true;
    }

    public void initJGroupIfRequired() {
        if (jGroups.size() == 0)
            jGroups.addAll(jGroupRepository.findAll());
    }


    public boolean createUsers(long startIter, long stopIter) {
        for (long i = startIter; i < stopIter; i++) {
            users.add(userService.createUser("user" + i, "12345", "Xuser" + i, "Yuser" + i, "Xuser" + i + "@example.com", null, null, true));
        }
        return true;
    }

    public void updateEmails() {

        userRepository.findAll().forEach(it -> {
            it.setEmail(it.getFirstName() + "@example.com");
            userRepository.save(it);
        });
    }
}
