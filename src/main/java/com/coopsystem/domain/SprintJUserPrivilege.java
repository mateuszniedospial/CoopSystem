package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.coopsystem.domain.enumeration.SprintPrivilegeType;

/**
 * A SprintJUserPrivilege.
 */
@Entity
@Table(name = "sprint_j_user_privilege")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SprintJUserPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SprintPrivilegeType type;

    @OneToOne
    @JoinColumn()
    private Sprint sprint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public SprintJUserPrivilege createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public SprintJUserPrivilege modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public SprintPrivilegeType getType() {
        return type;
    }

    public SprintJUserPrivilege type(SprintPrivilegeType type) {
        this.type = type;
        return this;
    }

    public void setType(SprintPrivilegeType type) {
        this.type = type;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public SprintJUserPrivilege sprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SprintJUserPrivilege sprintJUserPrivilege = (SprintJUserPrivilege) o;
        if (sprintJUserPrivilege.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sprintJUserPrivilege.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SprintJUserPrivilege{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
