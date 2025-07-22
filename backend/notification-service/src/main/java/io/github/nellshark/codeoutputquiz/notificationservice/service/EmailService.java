package io.github.nellshark.codeoutputquiz.notificationservice.service;

import reactor.core.publisher.Mono;

public interface EmailService {
  Mono<Void> sendEmail(String to, String subject, String text);
}
