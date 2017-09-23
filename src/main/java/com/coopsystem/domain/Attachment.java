package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.coopsystem.domain.enumeration.LifeCycle;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle")
    private LifeCycle lifeCycle;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "content_content_type", nullable = false)
    private String contentContentType;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Task task;

    @OneToOne
    @JoinColumn()
    private JUser author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Attachment createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public Attachment modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public Attachment lifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public byte[] getContent() {
        return content;
    }

    public Attachment content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public Attachment contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public String getName() {
        return name;
    }

    public Attachment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Task getTask() {
        return task;
    }

    public Attachment task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public JUser getAuthor() {
        return author;
    }

    public Attachment author(JUser jUser) {
        this.author = jUser;
        return this;
    }

    public void setAuthor(JUser jUser) {
        this.author = jUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attachment attachment = (Attachment) o;
        if (attachment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, attachment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", lifeCycle='" + lifeCycle + "'" +
            ", content='" + content + "'" +
            ", contentContentType='" + contentContentType + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
