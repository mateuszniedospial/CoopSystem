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
 * A JGroup.
 */
@Entity
@Table(name = "j_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JGroup implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle", nullable = false)
    private LifeCycle lifeCycle;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "jgroup_juser",
               joinColumns = @JoinColumn(name="jgroups_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="jusers_id", referencedColumnName="id"))
    private Set<JUser> jusers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public JGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public JGroup description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public JGroup createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public JGroup modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public JGroup lifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public Set<JUser> getJusers() {
        return jusers;
    }

    public JGroup jusers(Set<JUser> jUsers) {
        this.jusers = jUsers;
        return this;
    }

    public JGroup addJuser(JUser jUser) {
        this.jusers.add(jUser);
        return this;
    }

    public JGroup removeJuser(JUser jUser) {
        this.jusers.remove(jUser);
        return this;
    }

    public void setJusers(Set<JUser> jUsers) {
        this.jusers = jUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JGroup jGroup = (JGroup) o;
        if (jGroup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JGroup{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", lifeCycle='" + lifeCycle + "'" +
            '}';
    }
}
