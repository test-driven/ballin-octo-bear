package com.accel.acme.web.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "advertisements")
public class Advertisement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String title;
    private String description;
    private String referenceId;
    private Date created_at;
    private Date updated_at;
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

    @Column(name = "title", nullable = false, length = 64)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", nullable = false, length = 254)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "referenceid", nullable = false, length = 32, unique = true)
    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Column(name = "created_at", length = 32)
    public Date getCreated_at() {
        return created_at;
    }

    @PreUpdate
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Column(name = "updated_at", length = 32)
    public Date getUpdated_at() {
        return updated_at;
    }

    @PreUpdate
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @OneToMany(mappedBy = "advertisement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<NewsPapersAdvertisements> getNewsPaperAdvertisement() {
        return newsPaperAdvertisement;
    }

    public void setNewsPaperAdvertisement(Set<NewsPapersAdvertisements> newsPaperAdvertisement) {
        this.newsPaperAdvertisement = newsPaperAdvertisement;
    }

}
