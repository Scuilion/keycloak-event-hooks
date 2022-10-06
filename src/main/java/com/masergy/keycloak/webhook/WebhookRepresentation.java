package com.masergy.keycloak.webhook;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.keycloak.events.EventType;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import com.masergy.keycloak.webhook.jpa.Webhook;

public class WebhookRepresentation {

    private String id;

    @NotEmpty
    private String name;

    private EventType eventType;

    private OperationType operationType;

    private ResourceType resourceType;

    @NotEmpty
    @NotNull
    private String callbackUrl;

    public WebhookRepresentation() {
    }

    public WebhookRepresentation(Webhook webhook) {
        id = webhook.getId();
        name = webhook.getName();
        eventType = webhook.getEventType();
        operationType = webhook.getOperationType();
        resourceType = webhook.getResourceType();
        callbackUrl = webhook.getCallbackUrl();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
