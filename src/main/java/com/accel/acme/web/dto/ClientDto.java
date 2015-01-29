/**
 *
 */
package com.accel.acme.web.dto;

/**
 * @author salna.k
 */
public class ClientDto {

    private Integer id;
    private String contactName;
    private String contactNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

}
