package com.diabetesapp.repositories;

import com.diabetesapp.model.DiabetesRecord;

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

}
