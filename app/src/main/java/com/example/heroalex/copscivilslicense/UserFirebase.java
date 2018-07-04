package com.example.heroalex.copscivilslicense;

/**
 * Created by Hero Alex on 6/21/2018.
 */

public class UserFirebase {

    private String name;
    private String uid;
    private String coordinates;
    private String status;

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public double[] getCoordinates() {
        String[] aux = coordinates.split(" ");
        if (aux.length > 1) {

            // se ia coordonatele string din firebase
            return new double[]{Double.parseDouble(aux[0]), Double.parseDouble(aux[1])};
        }
        return new double[] {0, 0};
    }

}
