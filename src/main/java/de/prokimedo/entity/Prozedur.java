package de.prokimedo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author Bilel-PC
 */
@Entity
@Indexed
public class Prozedur implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
     @Field
    @Column(length = 70, nullable = true)
    private String title;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Field
    @Column(length = 70, nullable = true)
    private String autor;
        @Field
    @Column(columnDefinition="TEXT")
    private String uebersichtTxt;
    @Field
    @Column(columnDefinition="TEXT")
    private String uebersichtNot;
    @Field
    @Column(columnDefinition="TEXT")
    private String diagnostikTxt;
    @Field
    @Column(columnDefinition="TEXT")
    private String diagnostikNot;
    @Field
    @Column(columnDefinition="TEXT")
    private String therapieTxt;
    @Field
    @Column(columnDefinition="TEXT")
    private String therapieNot;
    @Field
    @Column(columnDefinition="TEXT")
    private String beratungTxt;
    @Field
    @Column(columnDefinition="TEXT")
    private String beratungNot;
    @Field
    @Column(columnDefinition="TEXT")
    private String notes;
    @OneToMany
    private List<Icd> listIcd;
    @OneToMany
    private List<Medikament> listMedikament;

    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUebersichtTxt() {
        return uebersichtTxt;
    }

    public void setUebersichtTxt(String uebersichtTxt) {
        this.uebersichtTxt = uebersichtTxt;
    }

    public String getUebersichtNot() {
        return uebersichtNot;
    }

    public void setUebersichtNot(String uebersichtNot) {
        this.uebersichtNot = uebersichtNot;
    }

    public String getDiagnostikTxt() {
        return diagnostikTxt;
    }

    public void setDiagnostikTxt(String diagnostikTxt) {
        this.diagnostikTxt = diagnostikTxt;
    }

    public String getDiagnostikNot() {
        return diagnostikNot;
    }

    public void setDiagnostikNot(String diagnostikNot) {
        this.diagnostikNot = diagnostikNot;
    }

    public String getTherapieTxt() {
        return therapieTxt;
    }

    public void setTherapieTxt(String therapieTxt) {
        this.therapieTxt = therapieTxt;
    }

    public String getTherapieNot() {
        return therapieNot;
    }

    public void setTherapieNot(String therapieNot) {
        this.therapieNot = therapieNot;
    }

    public String getBeratungTxt() {
        return beratungTxt;
    }

    public void setBeratungTxt(String beratungTxt) {
        this.beratungTxt = beratungTxt;
    }

    public String getBeratungNot() {
        return beratungNot;
    }

    public void setBeratungNot(String beratungNot) {
        this.beratungNot = beratungNot;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Medikament> getListMedikament() {
        return listMedikament;
    }

    public void setListMedikament(List<Medikament> listMedikament) {
        this.listMedikament = listMedikament;
    }

    public List<Icd> getListIcd() {
        return listIcd;
    }

    public void setListIcd(List<Icd> listIcd) {
        this.listIcd = listIcd;
    }
}