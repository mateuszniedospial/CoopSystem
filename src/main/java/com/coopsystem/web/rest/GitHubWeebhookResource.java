package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.github.PushPayload;
import com.coopsystem.service.GithubWebhookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/github")
public class GitHubWeebhookResource {
    private final GithubWebhookService githubWebhookService;

    public GitHubWeebhookResource(GithubWebhookService githubWebhookService) {
        this.githubWebhookService = githubWebhookService;
    }

    @PostMapping("/push-event")
    @Timed
    public void pushEvent(@RequestBody PushPayload pushEvent) throws URISyntaxException {
        githubWebhookService.handle(pushEvent);
        System.out.print(pushEvent);
    }
}
