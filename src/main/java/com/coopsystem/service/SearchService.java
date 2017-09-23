package com.coopsystem.service;

import com.google.common.collect.ImmutableList;
import com.coopsystem.domain.Task;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.service.util.SearchParameter;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SearchService {
    private final TaskRepository taskRepository;

    public SearchService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> searchTask(SearchParameter searchParameter) {
        fillDate(searchParameter);
        if (searchParameter.getId() != null) {
            return ImmutableList.of(taskRepository.findOne(searchParameter.getId()));
        } else {
            return complexQuery(searchParameter);
        }
    }

    private List<Task> complexQuery(SearchParameter searchParameter) {
        convertSearchParameter(searchParameter);
        if (searchParameter.getAssignee() == null) {
            return taskRepository.search(
                searchParameter.getjGroupId(),
                searchParameter.getStatus(),
                searchParameter.getTitle(),
                searchParameter.getEnviroment(),
                searchParameter.getVersion(),
                searchParameter.getProject(),
                searchParameter.getPriority(),
                ZonedDateTime.ofInstant(searchParameter.getBeforeCreate().toInstant(), ZoneId.systemDefault()),
                ZonedDateTime.ofInstant(searchParameter.getAfterCreate().toInstant(), ZoneId.systemDefault()),
                ZonedDateTime.ofInstant(searchParameter.getBeforeModify().toInstant(), ZoneId.systemDefault()),
                ZonedDateTime.ofInstant(searchParameter.getAfterModify().toInstant(), ZoneId.systemDefault()),
                searchParameter.getDescription(),
                searchParameter.getComment());
        } else
            return taskRepository.searchWithAssignee(
                searchParameter.getjGroupId(),
                searchParameter.getStatus(),
                searchParameter.getTitle(),
                searchParameter.getEnviroment(),
                searchParameter.getVersion(),
                searchParameter.getProject(),
                searchParameter.getPriority(),
                searchParameter.getAssignee(),
                ZonedDateTime.ofInstant(searchParameter.getBeforeCreate().toInstant(), ZoneId.systemDefault()),
                ZonedDateTime.ofInstant(searchParameter.getAfterCreate().toInstant(), ZoneId.systemDefault()),
                ZonedDateTime.ofInstant(searchParameter.getBeforeModify().toInstant(), ZoneId.systemDefault()),
                ZonedDateTime.ofInstant(searchParameter.getAfterModify().toInstant(), ZoneId.systemDefault()),
                searchParameter.getDescription(),
                searchParameter.getComment());
    }


    private void convertSearchParameter(SearchParameter searchParameter) {
        searchParameter.setTitle(addPercentToString(searchParameter.getTitle()));
        searchParameter.setEnviroment(addPercentToString(searchParameter.getEnviroment()));
        searchParameter.setVersion(addPercentToString(searchParameter.getVersion()));
        searchParameter.setDescription(addPercentToString(searchParameter.getDescription()));
        searchParameter.setComment(addPercentToString(searchParameter.getComment()));

        if (searchParameter.getDescription() == null) {
            searchParameter.setDescription("%");
        }
    }

    public static Date parseDate(String date) {
        if (date == null) return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String addPercentToString(String input) {
        if (input != null) {
            return "%" + input + "%";
        }
        return null;
    }

    private void fillDate(SearchParameter searchParameter) {
        if (searchParameter.getBeforeModify() == null) {
            searchParameter.setBeforeModify(new Date());
        }
        if (searchParameter.getBeforeCreate() == null) {
            searchParameter.setBeforeCreate(new Date());
        }
        if (searchParameter.getAfterCreate() == null) {
            searchParameter.setAfterCreate(new Date(0));
        }
        if (searchParameter.getAfterModify() == null) {
            searchParameter.setAfterModify(new Date(0));
        }
    }
}
