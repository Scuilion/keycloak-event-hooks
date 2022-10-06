package com.masergy.keycloak.webhook;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.keycloak.events.EventType;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import com.masergy.keycloak.webhook.jpa.NotificationStatus;
import com.masergy.keycloak.webhook.jpa.Webhook;
import com.masergy.keycloak.webhook.jpa.WebhookNotification;

public class WebhookNotificationRepresentation {

    private String id;

    private String webhookId;

    private String realmId;

    private NotificationStatus notificationStatus;

    private Integer attempts;

    public WebhookNotificationRepresentation() {
    }

    public WebhookNotificationRepresentation(WebhookNotification notification) {
        id = notification.getId();
        webhookId = notification.getWebhookId();
        realmId = notification.getRealmId();
        notificationStatus = notification.getStatus();
        attempts = notification.getAttempts();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
}
