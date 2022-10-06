package com.masergy.keycloak.webhook.rest;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class WebhookResourceProvider implements RealmResourceProvider {

    private static final Logger log = Logger.getLogger(WebhookResourceProvider.class);

    private final KeycloakSession session;

    public WebhookResourceProvider(final KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        try {
            return new WebhookRestResource(session);
        } catch (Exception e) {
            log.error("failed");
        }
        return null;

    }

    @Override
    public void close() {

    }

}