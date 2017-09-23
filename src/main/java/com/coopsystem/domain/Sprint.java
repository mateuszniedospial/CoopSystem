package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.coopsystem.domain.enumeration.SprintLifeCycle;

/**
 * A Sprint.
 */
@Entity
@Table(name = "sprint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle")
    private SprintLifeCycle lifeCycle;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "stop_time")
    private ZonedDateTime stopTime;

    @Column(name = "duration_time")
    private Integer durationTime;

    @Column(name = "sum_of_estimate")
    private Float sumOfEstimate;

    @Column(name = "sum_of_remaining")
    private Float sumOfRemaining;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "retrospective")
    private String retrospective;

    @OneToOne
    @JoinColumn(name = "j_group_id")
//    @Column(name = "j_group_id")
    private JGroup jgroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Sprint title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Sprint createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public Sprint modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public SprintLifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public Sprint lifeCycle(SprintLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public void setLifeCycle(SprintLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Sprint startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getStopTime() {
        return stopTime;
    }

    public Sprint stopTime(ZonedDateTime stopTime) {
        this.stopTime = stopTime;
        return this;
    }

    public void setStopTime(ZonedDateTime stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public Sprint durationTime(Integer durationTime) {
        this.durationTime = durationTime;
        return this;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public Float getSumOfEstimate() {
        return sumOfEstimate;
    }

    public Sprint sumOfEstimate(Float sumOfEstimate) {
        this.sumOfEstimate = sumOfEstimate;
        return this;
    }

    public void setSumOfEstimate(Float sumOfEstimate) {
        this.sumOfEstimate = sumOfEstimate;
    }

    public Float getSumOfRemaining() {
        return sumOfRemaining;
    }

    public Sprint sumOfRemaining(Float sumOfRemaining) {
        this.sumOfRemaining = sumOfRemaining;
        return this;
    }

    public void setSumOfRemaining(Float sumOfRemaining) {
        this.sumOfRemaining = sumOfRemaining;
    }

    public String getDescription() {
        return description;
    }

    public Sprint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRetrospective() {
        return retrospective;
    }

    public Sprint retrospective(String retrospective) {
        this.retrospective = retrospective;
        return this;
    }

    public void setRetrospective(String retrospective) {
        this.retrospective = retrospective;
    }

    public JGroup getJGroup() {
        return jgroup;
    }

    public Sprint jgroup(JGroup jgroup) {
        this.jgroup = jgroup;
        return this;
    }

    public void setJGroup(JGroup jgroup) {
        this.jgroup = jgroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sprint sprint = (Sprint) o;
        if (sprint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sprint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sprint{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", lifeCycle='" + lifeCycle + "'" +
            ", startTime='" + startTime + "'" +
            ", stopTime='" + stopTime + "'" +
            ", durationTime='" + durationTime + "'" +
            ", sumOfEstimate='" + sumOfEstimate + "'" +
            ", sumOfRemaining='" + sumOfRemaining + "'" +
            ", description='" + description + "'" +
            ", retrospective='" + retrospective + "'" +
            '}';
    }
}
