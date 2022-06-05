package com.diabetesapp.security;

import com.diabetesapp.dao.UsersDAO;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.List;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    /*
     "The @Context annotation allows you to inject request/response context details into JAX-RS provider and resource
     classes."
     source -> https://stackoverflow.com/questions/20937362/what-objects-can-i-inject-using-the-context-annotation
    */
    @Context
    ResourceInfo resourceInfo;

    @Inject
    UsersDAO usersDAO;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

    }

   /* @Override
    public void filter(ContainerRequestContext requestContext) {
        Method requestedMethod = resourceInfo.getResourceMethod();
        *//* If resource method is annotated as PermitAll we don't do anything.
           Else we check for other security annotations. *//*
        if (!requestedMethod.isAnnotationPresent(PermitAll.class)) {
            // if resource method is annotated as DenyAll we block access.
            if (requestedMethod.isAnnotationPresent(DenyAll.class)) {
                // we abort the filter chain with a response that is being sent back to the client.
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity("Access to this resource is denied.")
                        .build());
            } else {
                // get headers sent by client.
                MultivaluedMap<String, String> headers = requestContext.getHeaders();
                // get authorization headers.
                List<String> authorization = headers.get("Authorization");
                *//* if authorization headers are not provided, then we again block access.
                based on the cw description this check is necessary
                        -> "<...> filter that ensures that a client request can only be served by a method
                if it encompasses an authentication header with a username and password"
                *//*
                if (authorization.isEmpty()) {
                    requestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity("Access to this resource is denied.")
                            .build());
                } else {
                    String usernamePasswordEncoded = authorization.get(0).replace("Basic ", "");
                    String usernamePasswordDecoded = new String(Base64.getDecoder().decode(usernamePasswordEncoded.getBytes()));
                    String[] splitUsernamePassword = usernamePasswordDecoded.split(":");
                    String username = splitUsernamePassword[0];
                    String password = splitUsernamePassword[1];
                    // find resource's annotated role and check whether user can access or not.
                    if (requestedMethod.isAnnotationPresent(RolesAllowed.class)) {
                        RolesAllowed rolesAllowed = requestedMethod.getAnnotation(RolesAllowed.class);
                        String[] rolesSetInResource = rolesAllowed.value();
                        String userRole = usersDAO.getUserRole(username, password);
                        if (userRole != null) {
                            // we take into consideration, that for this application a user can only have 1 role.
                            if (!userRole.equalsIgnoreCase(rolesSetInResource[0])) {
                                requestContext.abortWith(Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Access to this resource is denied.")
                                        .build());
                            }
                        } else {
                            requestContext.abortWith(Response
                                    .status(Response.Status.UNAUTHORIZED)
                                    .entity("Access to this resource is denied.")
                                    .build());
                        }
                    }
                }
            }
        }
    }*/
}