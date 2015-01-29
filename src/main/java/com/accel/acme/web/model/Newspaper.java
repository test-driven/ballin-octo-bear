package com.accel.acme.web.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "newspapers")
public class Newspaper implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String language;
    private String code;
    private Float ratePerUnit;
    private Date created_at;
    private Date updated_at;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "newspapersadvertisements", cascade = CascadeType.ALL)
    private Set<NewsPapersAdvertisements> newsPaperAdvertisement;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, length = 11)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "language", nullable = false, length = 32)
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Column(name = "code", nullable = false, length = 32, unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "ratePerUnit", nullable = false, length = 11)
    public Float getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(Float ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    @Column(name = "created_at", length = 32)
    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Column(name = "updated_at", length = 32)
    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

}
