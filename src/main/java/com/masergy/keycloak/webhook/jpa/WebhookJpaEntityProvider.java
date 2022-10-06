package com.masergy.keycloak.webhook.jpa;

import java.util.Collections;
import java.util.List;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

public class WebhookJpaEntityProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return Collections.singletonList(Webhook.class);
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/webhook-changelog.xml";
    }

    @Override
    public void close() {
    }

    @Override
    public String getFactoryId() {
        return WebhookJpaEntityProviderFactory.ID;
    }

}
