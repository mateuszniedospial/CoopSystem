package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.service.GitlabWebhookService;
import org.gitlab4j.api.webhook.PushEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/gitlab")
public class GitLabWeebhookResource {
    private final GitlabWebhookService gitlabWebhookService;

    public GitLabWeebhookResource(GitlabWebhookService gitlabWebhookService) {
        this.gitlabWebhookService = gitlabWebhookService;
    }

    @PostMapping("/push-event")
    @Timed
    public void pushEvent(@RequestBody PushEvent pushEvent) throws URISyntaxException {
        gitlabWebhookService.handle(pushEvent);
        System.out.print(pushEvent);
    }
}
