package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DocumentationCatalogue.
 */
@Entity
@Table(name = "documentation_catalogue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentationCatalogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "data")
    private String data;

    @OneToOne
    @JoinColumn
    private DocumentationCatalogue parent;

    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public DocumentationCatalogue label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public DocumentationCatalogue data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DocumentationCatalogue getParent() {
        return parent;
    }

    public DocumentationCatalogue parent(DocumentationCatalogue documentationCatalogue) {
        this.parent = documentationCatalogue;
        return this;
    }

    public void setParent(DocumentationCatalogue documentationCatalogue) {
        this.parent = documentationCatalogue;
    }

    public Project getProject() {
        return project;
    }

    public DocumentationCatalogue project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentationCatalogue documentationCatalogue = (DocumentationCatalogue) o;
        if (documentationCatalogue.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, documentationCatalogue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DocumentationCatalogue{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", data='" + data + "'" +
            '}';
    }
}
