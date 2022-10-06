package com.masergy.keycloak.webhook.eventlistener;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import com.masergy.keycloak.webhook.jpa.NotificationStatus;
import com.masergy.keycloak.webhook.jpa.Webhook;
import com.masergy.keycloak.webhook.jpa.WebhookNotification;

public class WebhookEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(WebhookEventListenerProvider.class);

    private final KeycloakSession session;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public WebhookEventListenerProvider(final KeycloakSession keycloakSession) {
        this.session = keycloakSession;
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    protected RealmModel getRealm() {
        return session.getContext().getRealm();
    }

    @Override
    public void onEvent(Event event) {
        log.info("Event Occurred:" + toString(event));
        System.out.println("Event Occurred:" + toString(event));

        List<Webhook> webhooksEntities = getEntityManager().createNamedQuery("findEvents", Webhook.class)
            .setParameter("realmId", getRealm().getId())
            .setParameter("eventType", event.getType())
            .getResultList();
        if (webhooksEntities.isEmpty()) {
            return;
        }
        log.info("Found webhooks events: " + webhooksEntities);
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        log.info("Admin Event Occurred:" + toString(adminEvent));

        List<Webhook> webhooksEntities = getEntityManager().createNamedQuery("findAdminEvents", Webhook.class)
            .setParameter("realmId", getRealm().getId())
            .setParameter("operationType", adminEvent.getOperationType())
            .setParameter("resourceType", adminEvent.getResourceType())
            .getResultList();
        if (webhooksEntities.isEmpty()) {
            return;
        }
        log.info("Found admin webhooks events: " + webhooksEntities.size());
        for (final Webhook w : webhooksEntities) {
            if (postHook(w, adminEvent)) {
                createNotification(w, NotificationStatus.SUCCESS);
            } else {
                createNotification(w, NotificationStatus.ATTEMPTED);
            }
        }
    }

    private void createNotification(final Webhook w, NotificationStatus status) {
        WebhookNotification we = new WebhookNotification();
        we.setId(KeycloakModelUtils.generateId());
        we.setWebhookId(w.getId());
        we.setRealmId(w.getRealmId());
        we.setAttempts(1);
        we.setStatus(status);
        getEntityManager().persist(we);
    }

    private boolean postHook(Webhook w, final AdminEvent adminEvent) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(adminEvent);
        } catch (JsonProcessingException e) {
            log.info("failed to serialize object", e);
            return false;
        }
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(w.getCallbackUrl()))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.info(String.format("Failed hook name (%s). Hook name (%s), to endpoint (%s) failed: %s",
                w.getId(), w.getName(), w.getCallbackUrl(), e.getMessage()));
            return false;
        }
        return true;
    }

    @Override
    public void close() {

    }

    private String toString(Event event) {

        StringBuilder sb = new StringBuilder(String.format("type=%s, realmId=%s, clientId=%s, userId=%s, ipAddress=%s",
            event.getType(), event.getRealmId(), event.getClientId(), event.getUserId(), event.getIpAddress()));

        if (event.getError() != null) {
            sb.append(", error=%s");
            sb.append(event.getError());
        }

        if (event.getDetails() != null) {
            for (Map.Entry<String, String> e : event.getDetails().entrySet()) {
                sb.append(", ");
                sb.append(e.getKey());

                if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {
                    sb.append("=");
                    sb.append(e.getValue());
                } else {
                    sb.append("='");
                    sb.append(e.getValue());
                    sb.append("'");
                }
            }
        }
        return sb.toString();
    }


    private String toString(AdminEvent adminEvent) {

        var message = String.format("OperationType=%s, resourceType=%s, representation=%s, realmId=%s, clientId=%s, userId=%s, ipAddress=%s, resourcePath=%s",
            adminEvent.getOperationType(), adminEvent.getResourceType(), adminEvent.getRepresentation(), adminEvent.getRealmId(), adminEvent.getAuthDetails().getRealmId(),
            adminEvent.getAuthDetails().getClientId(), adminEvent.getAuthDetails().getUserId(), adminEvent.getAuthDetails().getIpAddress());

        if (adminEvent.getError() != null) {
            message += String.format(", error=%s", adminEvent.getError());
        }

        return message;
    }
}