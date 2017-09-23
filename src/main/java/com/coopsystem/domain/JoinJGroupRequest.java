package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A JoinJGroupRequest.
 */
@Entity
@Table(name = "joinjgroup_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JoinJGroupRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @ManyToOne
    private JGroup jGroup;

    @OneToOne
    @JoinColumn()
    private JUser whoRequested;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public JoinJGroupRequest description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public JoinJGroupRequest createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public JGroup getJGroup() {
        return jGroup;
    }

    public JoinJGroupRequest jGroup(JGroup jGroup) {
        this.jGroup = jGroup;
        return this;
    }

    public void setJGroup(JGroup jGroup) {
        this.jGroup = jGroup;
    }

    public JUser getWhoRequested() {
        return whoRequested;
    }

    public JoinJGroupRequest whoRequested(JUser jUser) {
        this.whoRequested = jUser;
        return this;
    }

    public void setWhoRequested(JUser jUser) {
        this.whoRequested = jUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JoinJGroupRequest joinJGroupRequest = (JoinJGroupRequest) o;
        if (joinJGroupRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, joinJGroupRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JoinJGroupRequest{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            '}';
    }
}
