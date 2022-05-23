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

    // Displaying all the above data over a (user-specified) time period.
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiabetesRecord> list(@QueryParam("startingDate") Date startingDate,
                                     @QueryParam("endingDate") Date endingDate) {
        if(startingDate == null || endingDate == null) {
            return diabetesRecordsRepository.list();
        } else {
            return diabetesRecordsRepository.listSpecified(startingDate, endingDate);
        }
    }

    // Displaying the average daily blood glucose level over a (user- specified) period.
    @Path("/averageBloodGlucose")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public double averageBloodGlucose(@QueryParam("startingDate") Date startingDate,
                                    @QueryParam("endingDate") Date endingDate) {
        if(startingDate == null || endingDate == null) {
            return diabetesRecordsRepository.averageBloodGlucose();
        } else {
            return diabetesRecordsRepository.averageBloodGlucose(startingDate, endingDate);
        }
    }

    // Displaying the average carb intake over a (user-specified) period.
    @Path("/averageCarbIntake")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public double averageCarbIntake(@QueryParam("startingDate") Date startingDate,
                                      @QueryParam("endingDate") Date endingDate) {
        if(startingDate == null || endingDate == null) {
            return diabetesRecordsRepository.averageCarbIntake();
        } else {
            return diabetesRecordsRepository.averageCarbIntake(startingDate, endingDate);
        }
    }

}
