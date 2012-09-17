
package com.htong.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dtuStatusModel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dtuStatusModel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="commStatus" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="connStatus" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="dtuNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="heartBeatTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wellNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtuStatusModel", propOrder = {
    "commStatus",
    "connStatus",
    "dtuNum",
    "heartBeatTime",
    "wellNum"
})
public class DtuStatusModel {

    protected Boolean commStatus;
    protected Boolean connStatus;
    protected String dtuNum;
    protected String heartBeatTime;
    protected String wellNum;

    /**
     * Gets the value of the commStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommStatus() {
        return commStatus;
    }

    /**
     * Sets the value of the commStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommStatus(Boolean value) {
        this.commStatus = value;
    }

    /**
     * Gets the value of the connStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConnStatus() {
        return connStatus;
    }

    /**
     * Sets the value of the connStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConnStatus(Boolean value) {
        this.connStatus = value;
    }

    /**
     * Gets the value of the dtuNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDtuNum() {
        return dtuNum;
    }

    /**
     * Sets the value of the dtuNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDtuNum(String value) {
        this.dtuNum = value;
    }

    /**
     * Gets the value of the heartBeatTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeartBeatTime() {
        return heartBeatTime;
    }

    /**
     * Sets the value of the heartBeatTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeartBeatTime(String value) {
        this.heartBeatTime = value;
    }

    /**
     * Gets the value of the wellNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWellNum() {
        return wellNum;
    }

    /**
     * Sets the value of the wellNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWellNum(String value) {
        this.wellNum = value;
    }

}
