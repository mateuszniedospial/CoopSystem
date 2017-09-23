package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TaskHistory.
 */
@Entity
@Table(name = "task_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime created_date;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "changed_name")
    private String changedName;

    @Lob
    @Column(name = "old_content")
    private String oldContent;

    @OneToOne
    @JoinColumn()
    private JUser modifiedJUser;

    @ManyToOne
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated_date() {
        return created_date;
    }

    public TaskHistory created_date(ZonedDateTime created_date) {
        this.created_date = created_date;
        return this;
    }

    public void setCreated_date(ZonedDateTime created_date) {
        this.created_date = created_date;
    }

    public String getContent() {
        return content;
    }

    public TaskHistory content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChangedName() {
        return changedName;
    }

    public TaskHistory changedName(String changedName) {
        this.changedName = changedName;
        return this;
    }

    public void setChangedName(String changedName) {
        this.changedName = changedName;
    }

    public String getOldContent() {
        return oldContent;
    }

    public TaskHistory oldContent(String oldContent) {
        this.oldContent = oldContent;
        return this;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public JUser getModifiedJUser() {
        return modifiedJUser;
    }

    public TaskHistory modifiedJUser(JUser jUser) {
        this.modifiedJUser = jUser;
        return this;
    }

    public void setModifiedJUser(JUser jUser) {
        this.modifiedJUser = jUser;
    }

    public Task getTask() {
        return task;
    }

    public TaskHistory task(Task task) {
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
        TaskHistory taskHistory = (TaskHistory) o;
        if (taskHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, taskHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskHistory{" +
            "id=" + id +
            ", created_date='" + created_date + "'" +
            ", content='" + content + "'" +
            ", changedName='" + changedName + "'" +
            ", oldContent='" + oldContent + "'" +
            '}';
    }
}
