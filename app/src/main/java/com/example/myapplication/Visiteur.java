package com.example.myapplication;

public class Visiteur {
    String numVist;
    String nom;
    int nb_jours;
    int tarifJ;
    int tarif;
    int min;
    int max;
    public Visiteur(String numVist, String nom, int nb_jours, int tarifJ,int tarif) {
        this.numVist = numVist;
        this.nom = nom;
        this.nb_jours = nb_jours;
        this.tarifJ = tarifJ;
        this.tarif=tarif;
    }

    public Visiteur(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return "Visiteur [numVist=" + numVist + ", nom=" + nom + ", nb_jours=" + nb_jours + ", tarifJours=" + tarifJ + "]";
    }


    public Visiteur(String numVist, String nom, int nb_jours, int tarifJ) {
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTarif() {
        return tarif;
    }

    public void setTarif(int tarif) {
        this.tarif = tarif;
    }

    public String getNumVist() {
        return numVist;
    }

    public void setNumVist(String numVist) {
        this.numVist = numVist;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNb_jours() {
        return nb_jours;
    }

    public void setNb_jours(int nb_jours) {
        this.nb_jours = nb_jours;
    }

    public int getTarifJ() {
        return tarifJ;
    }

    public void setTarifJ(int tarifJ) {
        this.tarifJ = tarifJ;
    }
}
