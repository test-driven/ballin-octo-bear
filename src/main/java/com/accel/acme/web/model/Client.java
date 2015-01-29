package com.accel.acme.web.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jithin.Mohan
 */
@Entity
@Table(name = "clients")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, length = 11)
    private Integer id;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "phonenumber", nullable = false, length = 32, unique = true)
    private String phoneNumber;

    @Column(name = "created_at", length = 32)
    private Date created_at;

    @Column(name = "updated_at", length = 32)
    private Date updated_at;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @PreUpdate
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @PreUpdate
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

//    public Date getCreated_at() {
//        return created_at;
//    }
//
//    public Date getUpdated_at() {
//        return updated_at;
//    }
//

}
