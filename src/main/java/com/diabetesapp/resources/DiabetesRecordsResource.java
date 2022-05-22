package com.diabetesapp.resources;

import com.diabetesapp.model.DiabetesRecord;
import com.diabetesapp.repositories.DiabetesRecordsRepository;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.util.List;

@Path("/diabetes-records")
public class DiabetesRecordsResource {

    @Inject
    DiabetesRecordsRepository diabetesRecordsRepository;

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiabetesRecord> list() {
        return diabetesRecordsRepository.list();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DiabetesRecord get(@PathParam("id") int recordId) {
        return diabetesRecordsRepository.get(recordId);
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void get(@QueryParam("userId") int userId, DiabetesRecord newRecord) {
        diabetesRecordsRepository.add(newRecord, userId);
    }

    @Path("/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void update(DiabetesRecord newRecord) {
        diabetesRecordsRepository.update(newRecord);
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") int recordId) {
        diabetesRecordsRepository.delete(recordId);
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiabetesRecord> listSpecified(@QueryParam("startingDate") Date startingDate,
                                              @QueryParam("endingDate") Date endingDate) {

    }

}
