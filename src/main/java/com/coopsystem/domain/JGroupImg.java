package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JGroupImg.
 */
@Entity
@Table(name = "j_group_img")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JGroupImg implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "content_content_type", nullable = false)
    private String contentContentType;

    @OneToOne
    @JoinColumn(name = "j_group_id")
    @Where(clause = "j_group_id = id")
    private JGroup jgroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public JGroupImg content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public JGroupImg contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public JGroup getJGroup() {
        return jgroup;
    }

    public JGroupImg jGroup(JGroup jGroup) {
        this.jgroup = jGroup;
        return this;
    }

    public void setJGroup(JGroup jGroup) {
        this.jgroup = jGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JGroupImg jGroupImg = (JGroupImg) o;
        if (jGroupImg.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jGroupImg.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JGroupImg{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", contentContentType='" + contentContentType + "'" +
            '}';
    }
}
