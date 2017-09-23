package com.coopsystem.service.util;

import java.util.Date;

/**
 * Created by Dariusz Ł on 23.05.2017.
 */
public class SearchParameter {
    private String title;
    private Long jGroupId;
    private Long id;
    private String version;
    private String description;
    private String enviroment;
    private String status;
    private String comment;
    private String priority;
    private String project;
    private Long assignee;
    private Date afterCreate;
    private Date beforeCreate;
    private Date afterModify;
    private Date beforeModify;

    public SearchParameter() {
    }


    public String getTitle() {
        return title;
    }

    public SearchParameter setTitle(String title) {
        this.title = title;
        return this;
    }

    public Long getId() {
        return id;
    }

    public SearchParameter setId(Long id) {
        this.id = id;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public SearchParameter setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SearchParameter setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getEnviroment() {
        return enviroment;
    }

    public SearchParameter setEnviroment(String enviroment) {
        this.enviroment = enviroment;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public SearchParameter setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public SearchParameter setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getPriority() {
        return priority;
    }

    public SearchParameter setPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public String getProject() {
        return project;
    }

    public SearchParameter setProject(String project) {
        this.project = project;
        return this;
    }

    public Long getAssignee() {
        return assignee;
    }

    public SearchParameter setAssignee(Long assignee) {
        this.assignee = assignee;
        return this;
    }

    public Date getAfterCreate() {
        return afterCreate;
    }

    public SearchParameter setAfterCreate(Date afterCreate) {
        this.afterCreate = afterCreate;
        return this;
    }

    public Date getBeforeCreate() {
        return beforeCreate;
    }

    public SearchParameter setBeforeCreate(Date beforeCreate) {
        this.beforeCreate = beforeCreate;
        return this;
    }

    public Date getAfterModify() {
        return afterModify;
    }

    public SearchParameter setAfterModify(Date afterModify) {
        this.afterModify = afterModify;
        return this;
    }

    public Date getBeforeModify() {
        return beforeModify;
    }

    public SearchParameter setBeforeModify(Date beforeModify) {
        this.beforeModify = beforeModify;
        return this;
    }

    public Long getjGroupId() {
        return jGroupId;
    }

    public SearchParameter setjGroupId(Long jGroupId) {
        this.jGroupId = jGroupId;
        return this;
    }
}
