package com.diabetesapp.resources;

import com.diabetesapp.dao.DiabetesRecordsDAO;
import com.diabetesapp.model.DiabetesRecord;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/diabetes-records")
public class DiabetesRecordsResource {

    @Inject
    DiabetesRecordsDAO diabetesRecordsDAO;

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiabetesRecord> list() {
        return diabetesRecordsDAO.list();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DiabetesRecord get(@PathParam("id") int recordId) {
        return diabetesRecordsDAO.get(recordId);
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void get(@QueryParam("userId") int userId, DiabetesRecord newRecord) {
        diabetesRecordsDAO.add(newRecord, userId);
    }

    @Path("/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void update(DiabetesRecord newRecord) {
        diabetesRecordsDAO.update(newRecord);
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") int recordId) {
        diabetesRecordsDAO.delete(recordId);
    }

}
