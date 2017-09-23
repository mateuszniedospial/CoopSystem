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

import com.coopsystem.domain.enumeration.LifeCycle;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle")
    private LifeCycle lifeCycle;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "project_j_group",
               joinColumns = @JoinColumn(name="projects_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="j_groups_id", referencedColumnName="id"))
    private Set<JGroup> jgroups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Project createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public Project modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public Project lifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public Set<JGroup> getJGroups() {
        return jgroups;
    }

    public Project jGroups(Set<JGroup> jGroups) {
        this.jgroups = jGroups;
        return this;
    }

    public Project addJGroup(JGroup jGroup) {
        this.jgroups.add(jGroup);
        return this;
    }

    public Project removeJGroup(JGroup jGroup) {
        this.jgroups.remove(jGroup);
        return this;
    }

    public void setJGroups(Set<JGroup> jGroups) {
        this.jgroups = jGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", lifeCycle='" + lifeCycle + "'" +
            '}';
    }
}
