package com.diabetesapp.repositories;

import com.diabetesapp.model.DiabetesRecord;
import java.sql.Date;
import java.util.List;

public interface DiabetesRecordsRepository {

    // view all
    List<DiabetesRecord> list();

    // view specific
    DiabetesRecord get(int recordId);

    // add new record
    void add(DiabetesRecord record);

    // update existing record
    void update(DiabetesRecord record);

    // delete existing record
    void delete(int recordId);

    List<DiabetesRecord> listSpecified(Date startingDate, Date endingDate);

    double averageBloodGlucose(Date startingDate, Date endingDate);

    double averageBloodGlucose();

    double averageCarbIntake(Date startingDate, Date endingDate);

    double averageCarbIntake();

}
