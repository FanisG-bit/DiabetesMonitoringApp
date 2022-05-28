package com.diabetesapp.resources;

import com.diabetesapp.dao.UsersDAO;
import com.diabetesapp.model.User;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/operations")
public class ApplicationResource {

    @Inject
    UsersDAO usersDAO;

    @Path("/validateUser")
    @PermitAll
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean validateUser(User userCredentials) {
        return usersDAO.areCredentialsValid(userCredentials.getUsername()
                , userCredentials.getPassword());
    }

}