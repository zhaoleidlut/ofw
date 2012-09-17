
package com.htong.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.htong.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetDTUStatusResponse_QNAME = new QName("http://ws.htong.com/", "getDTUStatusResponse");
    private final static QName _GetDTUStatus_QNAME = new QName("http://ws.htong.com/", "getDTUStatus");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.htong.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetDTUStatus }
     * 
     */
    public GetDTUStatus createGetDTUStatus() {
        return new GetDTUStatus();
    }

    /**
     * Create an instance of {@link DtuStatusModel }
     * 
     */
    public DtuStatusModel createDtuStatusModel() {
        return new DtuStatusModel();
    }

    /**
     * Create an instance of {@link GetDTUStatusResponse }
     * 
     */
    public GetDTUStatusResponse createGetDTUStatusResponse() {
        return new GetDTUStatusResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDTUStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.htong.com/", name = "getDTUStatusResponse")
    public JAXBElement<GetDTUStatusResponse> createGetDTUStatusResponse(GetDTUStatusResponse value) {
        return new JAXBElement<GetDTUStatusResponse>(_GetDTUStatusResponse_QNAME, GetDTUStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDTUStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.htong.com/", name = "getDTUStatus")
    public JAXBElement<GetDTUStatus> createGetDTUStatus(GetDTUStatus value) {
        return new JAXBElement<GetDTUStatus>(_GetDTUStatus_QNAME, GetDTUStatus.class, null, value);
    }

}
