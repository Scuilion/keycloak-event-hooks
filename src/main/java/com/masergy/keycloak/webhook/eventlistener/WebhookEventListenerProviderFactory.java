package com.masergy.keycloak.webhook.eventlistener;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

public class WebhookEventListenerProviderFactory implements EventListenerProviderFactory, ServerInfoAwareProviderFactory {

    private String kcWebhookEndpoint;
    private HttpClient httpClient;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new WebhookEventListenerProvider(keycloakSession);
    }

    @Override
    public String getId() {
        return "webhook-event-listener";
    }

    @Override
    public void init(Config.Scope scope) {
        kcWebhookEndpoint = System.getenv("KC_WEBHOOK_ENDPOINT");
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
    }

    @Override
    public void close() {

    }

    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("endpoint", kcWebhookEndpoint);
        return ret;
    }
}