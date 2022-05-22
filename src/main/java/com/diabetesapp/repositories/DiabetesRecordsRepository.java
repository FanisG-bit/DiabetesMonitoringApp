package com.diabetesapp.repositories;

import com.diabetesapp.model.DiabetesRecord;

import javax.ws.rs.QueryParam;
import java.sql.Date;
import java.util.List;

public interface DiabetesRecordsRepository {

    // view all
    List<DiabetesRecord> list();

    // view specific
    DiabetesRecord get(int recordId);

    // add new record
    void add(DiabetesRecord record, int userId);

    // update existing record
    void update(DiabetesRecord record);

    // delete existing record
    void delete(int recordId);

    List<DiabetesRecord> listSpecified(Date startingDate, Date endingDate);

}
