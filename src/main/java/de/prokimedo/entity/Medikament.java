package de.prokimedo.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Medikament implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String bezeichnung;

    private String pzn;

    private String einheit;

    private String roteListe;

    private String inhaltsstoff;

    private String darr;

    public Medikament() {
    }

    public Medikament(String id, String bezeichnung, String pzn, String einheit, String roteListe, String darr, String inhaltsstoff) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.pzn = pzn;
        this.einheit = einheit;
        this.roteListe = roteListe;
        this.darr = darr;
        this.inhaltsstoff = inhaltsstoff;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getDarr() {
        return darr;
    }

    public void setDarr(String darr) {
        this.darr = darr;
    }

    public Medikament(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getPzn() {
        return pzn;
    }

    public void setPzn(String pzn) {
        this.pzn = pzn;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public String getRoteListe() {
        return roteListe;
    }

    public void setRoteListe(String roteListe) {
        this.roteListe = roteListe;
    }

    public String getInhaltsstoff() {
        return inhaltsstoff;
    }

    public void setInhaltsstoff(String inhaltsstoff) {
        this.inhaltsstoff = inhaltsstoff;
    }
}
