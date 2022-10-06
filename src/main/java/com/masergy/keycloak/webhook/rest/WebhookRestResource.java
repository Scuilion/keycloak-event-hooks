package com.masergy.keycloak.webhook.rest;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import com.masergy.keycloak.webhook.jpa.Webhook;

public class WebhookRestResource {

    private static final Logger log = Logger.getLogger(WebhookRestResource.class);

    private final KeycloakSession session;
    private final AuthenticationManager.AuthResult auth;

    public WebhookRestResource(KeycloakSession session) {
        this.session = session;
        this.auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
    }

    private void checkRealmAdmin() {
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        } else if (auth.getToken().getRealmAccess() == null || !auth.getToken().getRealmAccess().isUserInRole("admin")) {
            throw new ForbiddenException("Does not have realm admin role");
        }
    }

    @Path("")
    public WebhookResource getAllWebhooks() {
        return new WebhookResource(session);
    }

}
