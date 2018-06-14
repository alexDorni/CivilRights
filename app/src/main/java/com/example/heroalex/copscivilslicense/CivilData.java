package com.example.heroalex.copscivilslicense;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * Created by Hero Alex on 12/5/2017.
 */

public class CivilData{
    private String FirstName;
    private String GPSCoordonates;
    private String StatusPoint;



    private CivilData(CivilDataBuilder builder){
        this.FirstName = builder.FirstName;
        this.GPSCoordonates = builder.GPSCoordonates;
        this.StatusPoint = builder.StatusPoint;
    }


    public String getFirstName(){
        return FirstName;
    }
    public String getGPSCoordonates(){ return GPSCoordonates; }
    public String getStatusPoint() { return StatusPoint; }

    public static class CivilDataBuilder{
        private String FirstName;
        private String GPSCoordonates;
        private String StatusPoint;


        public  CivilDataBuilder firstName(String firstName){
            this.FirstName = firstName;
            return  this;
        }

        public CivilDataBuilder gps(String GPSCoordonates){
            this.GPSCoordonates = GPSCoordonates;
            return this;
        }

        public CivilDataBuilder status(String StatusPoint){
            this.StatusPoint = StatusPoint;
            return this;
        }

        public CivilData build() {
            return new CivilData(this);
        }


    }

}
