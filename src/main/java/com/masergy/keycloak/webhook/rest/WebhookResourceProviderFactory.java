package com.masergy.keycloak.webhook.rest;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class WebhookResourceProviderFactory implements RealmResourceProviderFactory {

    private static final Logger log = Logger.getLogger(WebhookResourceProviderFactory.class);

    public static final String ID = "webhook-resource";

    @Override
    public RealmResourceProvider create(KeycloakSession keycloakSession) {
        return new WebhookResourceProvider(keycloakSession);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void init(Config.Scope scope) {
        log.info("Initializing webhook resources");
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
    }

    @Override
    public void close() {

    }

}