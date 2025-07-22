package io.github.krivolapovdev.codeoutputquiz.authservice.event;

public record EmailNotificationEvent(String to, String subject, String content) {}
