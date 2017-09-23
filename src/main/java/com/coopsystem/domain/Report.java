package com.coopsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Lob
    @JsonIgnore
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    @ManyToOne
    private Sprint sprint;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private ReportContent reportContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Report createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public Report modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public byte[] getData() {
        return data;
    }

    public Report data(byte[] data) {
        this.data = data;
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public Report dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public Report sprint(Sprint sprint) {
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
        Report report = (Report) o;
        if (report.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Report{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", modifyDate='" + modifyDate + "'" +
            ", data='" + data + "'" +
            ", dataContentType='" + dataContentType + "'" +
            '}';
    }

    public ReportContent getReportContent() {
        return reportContent;
    }

    public void setReportContent(ReportContent reportContent) {
        this.reportContent = reportContent;
    }
}
