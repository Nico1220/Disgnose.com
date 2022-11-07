package org.haupt.chemicals.api.repository;

public interface SendingMail {
    boolean sendMail(String subject, String body);
}
