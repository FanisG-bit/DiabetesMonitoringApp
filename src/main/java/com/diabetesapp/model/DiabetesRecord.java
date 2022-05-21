package com.diabetesapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DiabetesRecord {

    private int diabetesRecordId;

    private int bloodGlucoseLevel;

    private int carbIntake;

    private int medicationDose;

    private Date dateRecorded;

    private int userId;

}
