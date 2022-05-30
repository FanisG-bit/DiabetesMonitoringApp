package com.diabetesapp.resources;

import com.diabetesapp.model.DiabetesRecord;
import com.diabetesapp.repositories.DiabetesRecordsRepository;
import com.diabetesapp.service.LineChart;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

@Path("/diabetes-records")
public class DiabetesRecordsResource {

    @Inject
    DiabetesRecordsRepository diabetesRecordsRepository;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public DiabetesRecord get(@PathParam("id") int recordId) {
        return diabetesRecordsRepository.get(recordId);
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public void get(DiabetesRecord newRecord) {
        diabetesRecordsRepository.add(newRecord);
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public void update(DiabetesRecord newRecord) {
        diabetesRecordsRepository.update(newRecord);
    }

    @Path("/delete/{id}")
    @DELETE
    @RolesAllowed("ADMIN")
    public void delete(@PathParam("id") int recordId) {
        diabetesRecordsRepository.delete(recordId);
    }

    // Displaying all the above data over a (user-specified) time period.
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("PHYSICIAN")
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
    @RolesAllowed("PHYSICIAN")
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
    @RolesAllowed("PHYSICIAN")
    public double averageCarbIntake(@QueryParam("startingDate") Date startingDate,
                                    @QueryParam("endingDate") Date endingDate) {
        if(startingDate == null || endingDate == null) {
            return diabetesRecordsRepository.averageCarbIntake();
        } else {
            return diabetesRecordsRepository.averageCarbIntake(startingDate, endingDate);
        }
    }

    @Path("/chart")
    @PermitAll
    @GET
    @Produces("image/png")
    public Response getChart(@QueryParam("startingDate") Date startingDate,
                             @QueryParam("endingDate") Date endingDate,
                             @QueryParam("chartCase") String chartCase) {
        if(!chartCase.equals("bloodGlucoseSingle") && !chartCase.equals("carbonIntakeSingle") &&
                !chartCase.equals("bothInOne")) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("The chartCase query parameter could not be resolved.")
                    .build();
        }
        LineChart lineChart = new LineChart(diabetesRecordsRepository, startingDate, endingDate, chartCase);
        byte[] chartImagePNG = lineChart.getChartImagePNG();
        return Response
                .ok(chartImagePNG)
                .build();
    }

}