package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.coopsystem.domain.enumeration.LifeCycle;

/**
 * A DocumentationHistory.
 */
@Entity
@Table(name = "documentation_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentationHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "version")
    private Integer version;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle")
    private LifeCycle lifeCycle;

    @ManyToOne
    private ProjectDocumentation projectDocumentation;

    @OneToOne
    @JoinColumn()
    private JUser lastAuthor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public DocumentationHistory createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public DocumentationHistory content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getVersion() {
        return version;
    }

    public DocumentationHistory version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public DocumentationHistory modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public DocumentationHistory lifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public ProjectDocumentation getProjectDocumentation() {
        return projectDocumentation;
    }

    public DocumentationHistory projectDocumentation(ProjectDocumentation projectDocumentation) {
        this.projectDocumentation = projectDocumentation;
        return this;
    }

    public void setProjectDocumentation(ProjectDocumentation projectDocumentation) {
        this.projectDocumentation = projectDocumentation;
    }

    public JUser getLastAuthor() {
        return lastAuthor;
    }

    public DocumentationHistory lastAuthor(JUser jUser) {
        this.lastAuthor = jUser;
        return this;
    }

    public void setLastAuthor(JUser jUser) {
        this.lastAuthor = jUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentationHistory documentationHistory = (DocumentationHistory) o;
        if (documentationHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, documentationHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DocumentationHistory{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", content='" + content + "'" +
            ", version='" + version + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", lifeCycle='" + lifeCycle + "'" +
            '}';
    }
}
