/**
 *
 */
package com.accel.acme.web.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Salna.K
 */

@Entity
@Table(name = "newspapersadvertisements")
public class NewsPapersAdvertisements implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Newspaper newsPaper;
    private Advertisement advertisement;
    private Date published_at;
    private Integer numberOfUnits;
    private Date created_at;
    private Date updated_at;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, length = 11)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "newspaper")
    public Newspaper getNewsPaper() {
        return newsPaper;
    }

    public void setNewsPaper(Newspaper newsPaper) {
        this.newsPaper = newsPaper;
    }

    @ManyToOne
    @JoinColumn(name = "advertisement")
    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    @Column(name = "published_at", nullable = false, length = 32)
    public Date getPublished_at() {
        return published_at;
    }

    public void setPublished_at(Date published_at) {
        this.published_at = published_at;
    }

    @Column(name = "numberofunits", nullable = false, length = 32)
    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
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
