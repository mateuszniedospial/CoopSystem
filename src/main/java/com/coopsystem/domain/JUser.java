package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JUser.
 */
@Entity
@Table(name = "j_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(name = "jhi_j_user_id")
    private User user;

    @OneToOne
    @JoinColumn()
    private SprintJUserPrivilege sprintJUserPrivilege;

    @ManyToOne
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public JUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SprintJUserPrivilege getSprintJUserPrivilege() {
        return sprintJUserPrivilege;
    }

    public JUser sprintJUserPrivilege(SprintJUserPrivilege sprintJUserPrivilege) {
        this.sprintJUserPrivilege = sprintJUserPrivilege;
        return this;
    }

    public void setSprintJUserPrivilege(SprintJUserPrivilege sprintJUserPrivilege) {
        this.sprintJUserPrivilege = sprintJUserPrivilege;
    }

    public Task getTask() {
        return task;
    }

    public JUser task(Task task) {
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
        JUser jUser = (JUser) o;
        if (jUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JUser{" +
            "id=" + id +
            ", user='" + user + "'" +
            '}';
    }
}
