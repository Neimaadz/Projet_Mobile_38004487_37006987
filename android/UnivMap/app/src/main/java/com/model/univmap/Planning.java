package com.model.univmap;

public class Planning {

    private String Nom;
    private String Filiere;
    private String Enseignant;
    private String Hdebut;
    private String Hfin;
    private String Mdebut;
    private String Mfin;
    private String Salle;
    private String Latitude;
    private String Longitude;

    public Planning(){}


    public Planning(String nom, String filiere, String enseignant, String hDebut, String hFin, String mDebut, String mFin,
                    String salle, String latitude, String longitude){

        this.Nom = nom;
        this.Filiere = filiere;
        this.Enseignant = enseignant;
        this.Hdebut = hDebut;
        this.Hfin = hFin;
        this.Mdebut = mDebut;
        this.Mfin = mFin;
        this.Salle = salle;
        this.Latitude = latitude;
        this.Longitude = longitude;

    }

    public String getNom() {
        return this.Nom;
    }
    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getFiliere() {
        return this.Filiere;
    }
    public void setFiliere(String filiere) {
        this.Filiere = filiere;
    }

    public String getEnseignant() {
        return this.Enseignant;
    }
    public void setEnseignant(String enseignant) {
        this.Enseignant = enseignant;
    }

    public String getHdebut() {
        return this.Hdebut;
    }
    public void setHdebut(String hdebut) {
        this.Hdebut = hdebut;
    }

    public String getHfin() {
        return this.Hfin;
    }
    public void setHfin(String hfin) {
        this.Hfin = hfin;
    }

    public String getMdebut() {
        return this.Mdebut;
    }
    public void setMdebut(String mdebut) {
        this.Mdebut = mdebut;
    }

    public String getMfin() {
        return this.Mfin;
    }
    public void setMfin(String mfin) {
        this.Mfin = mfin;
    }

    public String getSalle() {
        return this.Salle;
    }
    public void setSalle(String salle) {
        this.Salle = salle;
    }

    public String getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongitude() {
        return this.Longitude;
    }
    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }
}
