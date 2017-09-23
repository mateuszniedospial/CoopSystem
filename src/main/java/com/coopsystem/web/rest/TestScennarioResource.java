package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.service.TestScennrioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestScennarioResource {
    private final Logger log = LoggerFactory.getLogger(ReportResource.class);
    private final TestScennrioService testScennrioService;

    public TestScennarioResource(TestScennrioService testScennrioService) {
        this.testScennrioService = testScennrioService;
    }

    @GetMapping("/automateTest/createUser")
    @Timed
    public void createUsers(@RequestParam(value = "startIterator", required = false)  Long startIterator,
                            @RequestParam(value = "stopIterator", required = false)  Long stopIterator) {
        boolean ok = testScennrioService.createUsers(startIterator, stopIterator);
        log.debug("test users create :", ok);
    }

    @PutMapping("/automateTest/createGroup")
    @Timed
    public void createGroup(@RequestParam(value = "sizeOdGroup", required = false)  Long sizeOdGroup)  {
        boolean ok = testScennrioService.createGroup(sizeOdGroup);
        log.debug("test group create :", ok);
    }

    @GetMapping("/automateTest/createProjects")
    @Timed
    public void createProjects(@RequestParam(value = "numberOfGroup", required = false)  Long numberOfGroup)  {
        boolean ok = testScennrioService.createProjects(numberOfGroup);
        log.debug("test projects create :", ok);
    }

    @GetMapping("/automateTest/createUserGroupProjects")
    @Timed
    public void createUsersGroupsProjects(@RequestParam(value = "startIterator", required = false)  Long startIterator,
                               @RequestParam(value = "stopIterator", required = false)  Long stopIterator,
                               @RequestParam(value = "sizeOdGroup", required = false)  Long sizeOdGroup,
                               @RequestParam(value = "numberOfGroup", required = false)  Long numberOfGroup )  {
        boolean ok = testScennrioService.createUsers(startIterator, stopIterator);
        boolean ok1 = testScennrioService.createGroup(sizeOdGroup);
        boolean ok2 = testScennrioService.createProjects(numberOfGroup);
        log.debug("test projects create :", ok2);
    }

}
