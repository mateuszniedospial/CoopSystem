package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.coopsystem.domain.enumeration.TaskLifeCycle;

import com.coopsystem.domain.enumeration.TaskType;

import com.coopsystem.domain.enumeration.TaskPriority;
import org.hibernate.annotations.Where;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle")
    private TaskLifeCycle lifeCycle;

    @Column(name = "enviroment")
    private String enviroment;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TaskType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @Column(name = "version")
    private String version;

    @Column(name = "remaining_time")
    private String remainingTime;

    @NotNull
    @Column(name = "estimate_time", nullable = false)
    private String estimateTime;

    @OneToOne
    @JoinColumn()
    private Sprint sprint;

    @OneToOne
    @JoinColumn()
    private Task parent;

    @OneToOne
    @JoinColumn(name = "j_group_id")
    @Where(clause = "j_group_id = id")
    private JGroup jgroup;

    @OneToOne
    @JoinColumn()
    private JUser assignee;

    @OneToOne
    @JoinColumn()
    private JUser reporter;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Task task;

    @OneToMany(mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> children = new HashSet<>();

    @ManyToOne
    private JUser juser;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "task_watchers",
               joinColumns = @JoinColumn(name="tasks_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="watchers_id", referencedColumnName="id"))
    private Set<JUser> watchers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "task_historic_sprints",
               joinColumns = @JoinColumn(name="tasks_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="historic_sprints_id", referencedColumnName="id"))
    private Set<Sprint> historic_sprints = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Task createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public Task modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public TaskLifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public Task lifeCycle(TaskLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public void setLifeCycle(TaskLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getEnviroment() {
        return enviroment;
    }

    public Task enviroment(String enviroment) {
        this.enviroment = enviroment;
        return this;
    }

    public void setEnviroment(String enviroment) {
        this.enviroment = enviroment;
    }

    public String getTitle() {
        return title;
    }

    public Task title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskType getType() {
        return type;
    }

    public Task type(TaskType type) {
        this.type = type;
        return this;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public Task priority(TaskPriority priority) {
        this.priority = priority;
        return this;
    }



    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getVersion() {
        return version;
    }

    public Task version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public Task remainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
        return this;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public Task estimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
        return this;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public Task sprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Task getParent() {
        return parent;
    }

    public Task parent(Task task) {
        this.parent = task;
        return this;
    }

    public void setParent(Task task) {
        this.parent = task;
    }

    public JGroup getJGroup() {
        return jgroup;
    }

    public Task jgroup(JGroup jGroup) {
        this.jgroup = jGroup;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {

        return project;
    }

    public void setJGroup(JGroup jGroup) {
        this.jgroup = jGroup;
    }

    public JUser getAssignee() {
        return assignee;
    }

    public Task assignee(JUser jUser) {
        this.assignee = jUser;
        return this;
    }

    public void setAssignee(JUser jUser) {
        this.assignee = jUser;
    }

    public JUser getReporter() {
        return reporter;
    }

    public Task reporter(JUser jUser) {
        this.reporter = jUser;
        return this;
    }

    public void setReporter(JUser jUser) {
        this.reporter = jUser;
    }

    public Set<JUser> getWatchers() {
        return watchers;
    }

    public Task watchers(Set<JUser> jUsers) {
        this.watchers = jUsers;
        return this;
    }

    public Task addWatchers(JUser jUser) {
        this.watchers.add(jUser);
        jUser.setTask(this);
        return this;
    }

    public Task removeWatchers(JUser jUser) {
        this.watchers.remove(jUser);
        jUser.setTask(null);
        return this;
    }

    public void setWatchers(Set<JUser> jUsers) {
        this.watchers = jUsers;
    }

    public Task getTask() {
        return task;
    }

    public Task task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Set<Task> getChildren() {
        return children;
    }

    public Task children(Set<Task> tasks) {
        this.children = tasks;
        return this;
    }

    public Task addChild(Task task) {
        this.children.add(task);
        task.setTask(this);
        return this;
    }

    public Task removeChild(Task task) {
        this.children.remove(task);
        task.setTask(null);
        return this;
    }

    public void setChildren(Set<Task> tasks) {
        this.children = tasks;
    }

    public JUser getJuser() {
        return juser;
    }

    public Task juser(JUser jUser) {
        this.juser = jUser;
        return this;
    }

    public void setJuser(JUser jUser) {
        this.juser = jUser;
    }



    public Set<Sprint> getHistoric_sprints() {
        return historic_sprints;
    }



    public void setHistoric_sprints(Set<Sprint> sprints) {
        this.historic_sprints = sprints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        if (task.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", lifeCycle='" + lifeCycle + "'" +
            ", enviroment='" + enviroment + "'" +
            ", title='" + title + "'" +
            ", type='" + type + "'" +
            ", priority='" + priority + "'" +
            ", version='" + version + "'" +
            ", remainingTime='" + remainingTime + "'" +
            ", estimateTime='" + estimateTime + "'" +
            '}';
    }
}
