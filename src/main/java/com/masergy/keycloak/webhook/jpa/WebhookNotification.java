package com.masergy.keycloak.webhook.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "WEBHK_NOTIFICATIONS")
@NamedQueries({
    @NamedQuery(name = "findNotificationsByRealm", query = "from WebhookNotification where realmId = :realmId")
})
public class WebhookNotification {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "WEBHOOK_ID", nullable = false)
    private String webhookId;

    @Column(name = "REALM_ID", nullable = false)
    private String realmId;

    @Column(name = "STATUS", nullable = false)
    private NotificationStatus status;

    @Column(name = "ATTEMPTS", nullable = false)
    private Integer attempts;

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

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
}