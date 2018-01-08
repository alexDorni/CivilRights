package com.example.heroalex.copscivilslicense;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * Created by Hero Alex on 12/5/2017.
 */

public class CivilData{
    private final String FistName;
    private final String LastName;
    private String Password;
    private String Gender;
    private String BloodType;
    private String CloseOneNumber;
    private String CloseOneName;
    private String GPSCoordonates;

    private CivilData(CivilDataBuilder builder){
        this.FistName = builder.FistName;
        this.LastName = builder.LastName;
        this.Password = builder.Password;
        this.Gender = builder.Gender;
        this.BloodType = builder.BloodType;
        this.CloseOneName = builder.CloseOneName;
        this.CloseOneNumber = builder.CloseOneNumber;
        this.GPSCoordonates = builder.GPSCoordonates;
    }


    public String getFistName(){
        return FistName;
    }
    public String getLastName(){
        return  LastName;
    }
    public String getPassword() { return Password; }
    public String getGender(){
                    return Gender;
    }
    public String getBloodType(){ return  BloodType; }
    public String getCloseOneNumber(){
        return CloseOneNumber;
    }
    public String getCloseOneName(){
        return CloseOneName;
    }
    public String getGPSCoordonates(){ return GPSCoordonates; }

    public static class CivilDataBuilder{
        private final String FistName;
        private final String LastName;
        private String Password;
        private String Gender;
        private String BloodType;
        private String CloseOneNumber;
        private String CloseOneName;
        private String GPSCoordonates;

        public CivilDataBuilder(String FistName, String LastName){
            this.FistName = FistName;
            this.LastName = LastName;
        }

        public  CivilDataBuilder password(String Password){
            this.Password = Password;
            return  this;
        }
        public CivilDataBuilder gender(String Gender){
            this.Gender = Gender;
            return this;
        }
        public CivilDataBuilder bloodType(String BloodType){
            this.BloodType = BloodType;
            return this;
        }
        public CivilDataBuilder closeOneName(String CloseOneName){
            this.CloseOneName = CloseOneName;
            return this;
        }
        public CivilDataBuilder closeOneNumber(String CloseOneNumber){
            this.CloseOneNumber = CloseOneNumber;
            return this;
        }
        public CivilDataBuilder gps(String GPSCoordonates){
            this.GPSCoordonates = GPSCoordonates;
            return this;
        }

        public CivilData build() {
            return new CivilData(this);
        }


    }

}
