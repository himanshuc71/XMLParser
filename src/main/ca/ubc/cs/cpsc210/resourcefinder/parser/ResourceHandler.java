package ca.ubc.cs.cpsc210.resourcefinder.parser;

import ca.ubc.cs.cpsc210.resourcefinder.model.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

// Handler for XML resource parsing
public class ResourceHandler extends DefaultHandler {
    private ResourceRegistry registry;
    private StringBuilder accumulator;


    private String name, address, latstr, lonstr;
    private double lat;
    private double lon;
    private String webAddressStr;
    private URL webAddress;
    private String phone;
    private Set<Service> services = new HashSet<>();
    private ContactInfo contactInfo;
    private GeoPoint geoLocation;
    private Resource resource;

    // EFFECTS: constructs resource handler for XML parser
    public ResourceHandler(ResourceRegistry registry) {
        this.registry = registry;
        accumulator = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.toLowerCase().equals("resource")) {
            name = "";
            address = "";
            latstr = "";
            lonstr = "";
            lat = 0.0;
            lon = 0.0;
            webAddressStr = "";
            webAddress = null;
            phone = "";
            services = new HashSet<>();
            contactInfo = null;
            resource = null;
            geoLocation = null;
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.toLowerCase().equals("name")) {
            name = accumulator.toString().trim();
        } else if (qName.toLowerCase().equals("address")) {
            address = accumulator.toString().trim();
        } else if (qName.toLowerCase().equals("lat")) {
            latstr = accumulator.toString().trim();
            lat = Double.parseDouble(latstr);
        } else if (qName.toLowerCase().equals("lon")) {
            lonstr = accumulator.toString().trim();
            lon = Double.parseDouble(lonstr);
        } else if (qName.toLowerCase().equals("webaddress")) {
            webAddressStr = accumulator.toString().trim();
            try {
                webAddress = new URL(webAddressStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (qName.toLowerCase().equals("location")) {
            geoLocation = new GeoPoint(lat, lon);
        } else if (qName.toLowerCase().equals("phone")) {
            phone = accumulator.toString().trim();
        } else if (qName.toLowerCase().equals("service")) {
            if (accumulator.toString().toLowerCase().trim().equals("food")) {
                services.add(Service.FOOD);
            } else if (accumulator.toString().toLowerCase().trim().equals("counselling")) {
                services.add(Service.COUNSELLING);
            } else if (accumulator.toString().toLowerCase().trim().equals("youth services")) {
                services.add(Service.YOUTH);
            } else if (accumulator.toString().toLowerCase().trim().equals("senior services")) {
                services.add(Service.SENIOR);
            } else if (accumulator.toString().toLowerCase().trim().equals("legal advice")) {
                services.add(Service.LEGAL);
            } else if (accumulator.toString().toLowerCase().trim().equals("shelter")) {
                services.add(Service.SHELTER);
            }
        } else if (qName.toLowerCase().equals("resource")) {
            if (!name.equals("") && !address.equals("") && geoLocation != null
                    && webAddress != null && !phone.equals("") && services.size() != 0) {
                contactInfo = new ContactInfo(address, geoLocation, webAddress, phone);
                resource = new Resource(name, contactInfo);
                for (Service s : services) {
                    resource.addService(s);
                }
                registry.addResource(resource);
            }
        } else if (qName.toLowerCase().equals("resources")) {
            if (registry.getResources().size() == 0) {
                throw new SAXException();
            }
        }
        accumulator.setLength(0);

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        accumulator.append(ch, start, length);
    }
}
