package com.coopsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JUserImg.
 */
@Entity
@Table(name = "j_user_img")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JUserImg implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "content")
    private byte[] content;

    @Column(name = "content_content_type")
    private String contentContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private JUser juser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public JUserImg content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public JUserImg contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public JUser getJuser() {
        return juser;
    }

    public JUserImg juser(JUser jUser) {
        this.juser = jUser;
        return this;
    }

    public void setJuser(JUser jUser) {
        this.juser = jUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JUserImg jUserImg = (JUserImg) o;
        if (jUserImg.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jUserImg.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JUserImg{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", contentContentType='" + contentContentType + "'" +
            '}';
    }
}
