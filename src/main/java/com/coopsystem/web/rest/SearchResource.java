package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.Task;
import com.coopsystem.service.SearchService;
import com.coopsystem.service.util.SearchParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchResource {
    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private final SearchService searchService;

    public SearchResource(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    @Timed
    public List<Task> searchTask(@RequestParam(value = "title", required = false)  String title,
                                                 @RequestParam(value = "jGroupId", required = false)  Long jGroupId,
                                                 @RequestParam(value = "id", required = false)  Long id,
                                                 @RequestParam(value = "version", required = false)  String version,
                                                 @RequestParam(value = "description", required = false)  String description,
                                                 @RequestParam(value = "enviroment", required = false)  String enviroment,
                                                 @RequestParam(value = "status", required = false)  String status,
                                                 @RequestParam(value = "comment", required = false)  String comment,
                                                 @RequestParam(value = "priority", required = false)  String priority,
                                                 @RequestParam(value = "project", required = false)  String project,
                                                 @RequestParam(value = "assignee", required = false)  Long assignne,
                                                 @RequestParam(value = "afterCreate", required = false)  String afterCreate,
                                                 @RequestParam(value = "beforeCreate", required = false)  String beforeCreate,
                                                 @RequestParam(value = "afterModify", required = false)  String afterModify,
                                                 @RequestParam(value = "beforeModify", required = false)  String beforeModify
                                                 ) {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setTitle(title)
            .setjGroupId(jGroupId)
            .setDescription(description)
            .setVersion(version)
            .setId(id)
            .setComment(comment)
            .setEnviroment(enviroment)
            .setStatus(status)
            .setPriority(priority)
            .setProject(project)
            .setAssignee(assignne)
            .setAfterCreate(SearchService.parseDate(afterCreate))
            .setBeforeCreate(SearchService.parseDate(beforeCreate))
            .setAfterModify(SearchService.parseDate(afterModify))
            .setBeforeModify(SearchService.parseDate(beforeModify));
        return searchService.searchTask(searchParameter);
    }

}
