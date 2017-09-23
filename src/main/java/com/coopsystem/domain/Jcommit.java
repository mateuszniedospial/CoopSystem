package com.coopsystem.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A Jcommit.
 */
@Entity
@Table(name = "jcommit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Jcommit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "message")
    private String message;

    @JsonIgnore
    @Column(name = "modified")
    private String modified;

    @JsonIgnore
    @Column(name = "removed")
    private String removed;

    @JsonIgnore
    @Column(name = "added")
    private String added;

    @Column(name = "commit_date")
    private ZonedDateTime commitDate;

    @Column(name = "url")
    private String url;

    @OneToOne
    @JoinColumn()
    private JUser jUser;

    @JsonSerialize
    @JsonDeserialize
    @Transient
    private List<String> addedList;
    @JsonSerialize
    @JsonDeserialize
    @Transient
    private List<String> removedList;
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private List<String> modifiedList;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "jcommit_tasks",
        joinColumns = @JoinColumn(name="jcommits_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="tasks_id", referencedColumnName="id"))
    private Set<Task> tasks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Jcommit message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModified() {
        return modified;
    }

    public Jcommit modified(String modified) {
        this.modified = modified;
        return this;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getRemoved() {
        return removed;
    }

    public Jcommit removed(String removed) {
        this.removed = removed;
        return this;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }

    public String getAdded() {
        return added;
    }

    public Jcommit added(String added) {
        this.added = added;
        return this;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public ZonedDateTime getCommitDate() {
        return commitDate;
    }

    public Jcommit commitDate(ZonedDateTime commitDate) {
        this.commitDate = commitDate;
        return this;
    }

    public void setCommitDate(ZonedDateTime commitDate) {
        this.commitDate = commitDate;
    }

    public String getUrl() {
        return url;
    }

    public Jcommit url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JUser getJUser() {
        return jUser;
    }

    public Jcommit jUser(JUser jUser) {
        this.jUser = jUser;
        return this;
    }

    public void setJUser(JUser jUser) {
        this.jUser = jUser;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Jcommit tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Jcommit addTasks(Task task) {
        this.tasks.add(task);
        return this;
    }

    public Jcommit removeTasks(Task task) {
        this.tasks.remove(task);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jcommit jcommit = (Jcommit) o;
        if (jcommit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jcommit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Jcommit{" +
            "id=" + id +
            ", message='" + message + "'" +
            ", modified='" + modified + "'" +
            ", removed='" + removed + "'" +
            ", added='" + added + "'" +
            ", commitDate='" + commitDate + "'" +
            ", url='" + url + "'" +
            '}';
    }

    public List<String> getModifiedList() {
        return modifiedList;
    }

    public void setModifiedList(List<String> modifiedList) {
        this.modifiedList = modifiedList;
    }

    public List<String> getRemovedList() {
        return removedList;
    }

    public void setRemovedList(List<String> removedList) {
        this.removedList = removedList;
    }

    public List<String> getAddedList() {
        return addedList;
    }

    public void setAddedList(List<String> addedList) {
        this.addedList = addedList;
    }
}
