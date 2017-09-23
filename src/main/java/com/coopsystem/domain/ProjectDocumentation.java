package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.coopsystem.domain.enumeration.LifeCycle;

/**
 * A ProjectDocumentation.
 */
@Entity
@Table(name = "project_documentation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectDocumentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Column(name = "version")
    private Integer version;

    @Lob
    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle")
    private LifeCycle lifeCycle;

    @Column(name = "label")
    private String label;

    @Column(name = "data")
    private String data;

    @OneToOne
    @JoinColumn()
    private JUser lastAuthor;

    @ManyToOne
    private Project project;

    @ManyToOne
    private DocumentationCatalogue underCatalogue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ProjectDocumentation createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public ProjectDocumentation modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getVersion() {
        return version;
    }

    public ProjectDocumentation version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public ProjectDocumentation content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public ProjectDocumentation lifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getLabel() {
        return label;
    }

    public ProjectDocumentation label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public ProjectDocumentation data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JUser getLastAuthor() {
        return lastAuthor;
    }

    public ProjectDocumentation lastAuthor(JUser jUser) {
        this.lastAuthor = jUser;
        return this;
    }

    public void setLastAuthor(JUser jUser) {
        this.lastAuthor = jUser;
    }

    public Project getProject() {
        return project;
    }

    public ProjectDocumentation project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public DocumentationCatalogue getUnderCatalogue() {
        return underCatalogue;
    }

    public ProjectDocumentation underCatalogue(DocumentationCatalogue documentationCatalogue) {
        this.underCatalogue = documentationCatalogue;
        return this;
    }

    public void setUnderCatalogue(DocumentationCatalogue documentationCatalogue) {
        this.underCatalogue = documentationCatalogue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectDocumentation projectDocumentation = (ProjectDocumentation) o;
        if (projectDocumentation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectDocumentation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectDocumentation{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", version='" + version + "'" +
            ", content='" + content + "'" +
            ", lifeCycle='" + lifeCycle + "'" +
            ", label='" + label + "'" +
            ", data='" + data + "'" +
            '}';
    }
}
