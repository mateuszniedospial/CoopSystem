package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WorkLog.
 */
@Entity
@Table(name = "work_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Column(name = "start_work")
    private ZonedDateTime startWork;

    @Column(name = "stop_work")
    private ZonedDateTime stopWork;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "time_in_hour", nullable = false)
    private Integer timeInHour;

    @OneToOne
    private JUser juser;

    @ManyToOne
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public WorkLog createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public WorkLog modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public ZonedDateTime getStartWork() {
        return startWork;
    }

    public WorkLog startWork(ZonedDateTime startWork) {
        this.startWork = startWork;
        return this;
    }

    public void setStartWork(ZonedDateTime startWork) {
        this.startWork = startWork;
    }

    public ZonedDateTime getStopWork() {
        return stopWork;
    }

    public WorkLog stopWork(ZonedDateTime stopWork) {
        this.stopWork = stopWork;
        return this;
    }

    public void setStopWork(ZonedDateTime stopWork) {
        this.stopWork = stopWork;
    }

    public String getDescription() {
        return description;
    }

    public WorkLog description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeInHour() {
        return timeInHour;
    }

    public WorkLog timeInHour(Integer timeInHour) {
        this.timeInHour = timeInHour;
        return this;
    }

    public void setTimeInHour(Integer timeInHour) {
        this.timeInHour = timeInHour;
    }

    public JUser getJuser() {
        return juser;
    }

    public WorkLog juser(JUser jUser) {
        this.juser = jUser;
        return this;
    }

    public void setJuser(JUser jUser) {
        this.juser = jUser;
    }

    public Task getTask() {
        return task;
    }

    public WorkLog task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkLog workLog = (WorkLog) o;
        if (workLog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkLog{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", startWork='" + startWork + "'" +
            ", stopWork='" + stopWork + "'" +
            ", description='" + description + "'" +
            ", timeInHour='" + timeInHour + "'" +
            '}';
    }
}
