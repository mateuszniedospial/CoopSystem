package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SprintDocument.
 */
@Entity
@Table(name = "sprint_document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SprintDocument implements Serializable {

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
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
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

    public SprintDocument createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public SprintDocument modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getContent() {
        return content;
    }

    public SprintDocument content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public SprintDocument sprint(Sprint sprint) {
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
        SprintDocument sprintDocument = (SprintDocument) o;
        if (sprintDocument.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sprintDocument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SprintDocument{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", content='" + content + "'" +
            '}';
    }
}
