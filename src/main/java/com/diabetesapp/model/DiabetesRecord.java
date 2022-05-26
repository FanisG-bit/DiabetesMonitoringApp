package com.diabetesapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiabetesRecord {

    private int diabetesRecordId;

    private double bloodGlucoseLevel;

    private double carbIntake;

    private double medicationDose;

    private Date dateRecorded;

    private int userId;

}
