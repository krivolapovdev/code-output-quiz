package io.github.nellshark.codeoutputquiz.notificationservice.event;

public record EmailNotificationEvent(String to, String subject, String content) {}
