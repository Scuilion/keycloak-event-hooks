package com.masergy.keycloak.webhook.rest;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import com.masergy.keycloak.webhook.WebhookNotificationRepresentation;
import com.masergy.keycloak.webhook.WebhookRepresentation;
import com.masergy.keycloak.webhook.jpa.Webhook;
import com.masergy.keycloak.webhook.jpa.WebhookNotification;

public class WebhookResource {

    private static final Logger log = Logger.getLogger(WebhookResource.class);

    private final KeycloakSession session;

    public WebhookResource(KeycloakSession session) {
        this.session = session;
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    protected RealmModel getRealm() {
        return session.getContext().getRealm();
    }

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    @NoCache
    public Response getAllWebhooks() {
        log.info("realm id: " + getRealm().getId());
        List<Webhook> webhooksEntities = getEntityManager().createNamedQuery("findByRealm", Webhook.class)
            .setParameter("realmId", getRealm().getId())
            .getResultList();

        List<WebhookRepresentation> result = new LinkedList<>();
        for (Webhook entity : webhooksEntities) {
            result.add(new WebhookRepresentation(entity));
        }
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @POST
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @NoCache
    public WebhookRepresentation createWebhook(@Valid WebhookRepresentation webhook) {

        log.info("create");
        //validate name doesn't have special chars
        //if (webhook.getName().length)

        if (webhook.getOperationType() != null && webhook.getResourceType() == null
            || webhook.getOperationType() == null && webhook.getResourceType() != null) {
            throw new BadRequestException("Admin event must have operationType and resourceType.");
        }
        if (webhook.getEventType() != null
            && (webhook.getOperationType() != null || webhook.getResourceType() != null)) {
            throw new BadRequestException("Events cannot include operationType or resourceType.");
        }

        if (StringUtils.isBlank(webhook.getCallbackUrl())) {
            throw new BadRequestException("WebhookUrl can not be empty.");
        }

        Webhook entity = new Webhook();
        String id = KeycloakModelUtils.generateId();
        entity.setId(id);
        entity.setRealmId(getRealm().getId());
        entity.setName(webhook.getName());
        entity.setEventType(webhook.getEventType());
        entity.setOperationType(webhook.getOperationType());
        entity.setResourceType(webhook.getResourceType());
        entity.setCallbackUrl(webhook.getCallbackUrl());

        getEntityManager().persist(entity);

        webhook.setId(id);
        return webhook;
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @NoCache
    public void deleteWebhook(@PathParam("id") String id) {
        //deleteWebhookById
        getEntityManager().createNamedQuery("deleteWebhookById")
            .setParameter("id", id)
            .executeUpdate();
    }

    @GET
    @Path("/notifications")
    @Produces({MediaType.APPLICATION_JSON})
    @NoCache
    public Response getAllNotifications() {
        List<WebhookNotification> notifications = getEntityManager().createNamedQuery("findNotificationsByRealm", WebhookNotification.class)
            .setParameter("realmId", getRealm().getId())
            .getResultList();

        List<WebhookNotificationRepresentation> result = new LinkedList<>();
        for (WebhookNotification entity : notifications) {
            result.add(new WebhookNotificationRepresentation(entity));
        }
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @GET
    @Path("/v3/api-docs")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSwaggerDoc() {
//        FileResourcesUtils app = new FileResourcesUtils();
//
//        //String fileName = "database.properties";
//        String fileName = "json/file1.json";
//
//        System.out.println("getResourceAsStream : " + fileName);
//        InputStream is = app.getFileFromResourceAsStream(fileName);
//        printInputStream(is);
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("META-INF/swagger.json");
        return Response.status(Response.Status.OK).entity(is).build();
    }

}
