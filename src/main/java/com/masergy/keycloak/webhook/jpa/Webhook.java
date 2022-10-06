package com.masergy.keycloak.webhook.jpa;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.keycloak.events.EventType;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

@Entity
@Table(name = "WEBHK_WEBHOOKS")
@NamedQueries({
    @NamedQuery(name = "findByRealm", query = "from Webhook where realmId = :realmId"),
    @NamedQuery(name = "findAdminEvents", query = "from Webhook where realmId = :realmId and operationType = :operationType and resourceType = :resourceType"),
    @NamedQuery(name = "findEvents", query = "from Webhook where realmId = :realmId and eventType = :eventType"),
    @NamedQuery(name = "deleteWebhookById", query = "delete from Webhook w where w.id=:id")
})
public class Webhook {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REALM_ID", nullable = false)
    private String realmId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EVENT_TYPE")
    private EventType eventType;

    @Column(name = "OPERATION_TYPE")
    private OperationType operationType;

    @Column(name = "RESOURCE_TYPE")
    private ResourceType resourceType;

    @Column(name = "CALLBACK_URL", nullable = false)
    private String callbackUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
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