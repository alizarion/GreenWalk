package com.project.services;

import com.project.entities.Address;
import com.project.entities.Position;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.map.LatLng;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 13/12/12
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class AddressByLongLatHelper {

    private static final String GEOCODE_REQUEST_URL = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=false&";
    private static HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());


    /**
     * Method to get Position address of a latitude and longitud point
     * @return
     */
    public static Address getLongitudeLatitude(LatLng latLng) {
        Address address = new Address();
        address.getPosition().setLongitude(latLng.getLng());
        address.getPosition().setLatitude(latLng.getLat());
        try {
            StringBuilder urlBuilder = new StringBuilder(GEOCODE_REQUEST_URL);
            urlBuilder.append("latlng=").append(address.getPosition().getAsLatLng().getLat()).
                    append(",").
                    append(address.getPosition().getAsLatLng().getLng());

            URL url = new URL(urlBuilder.toString());
            URLConnection connection = url.openConnection();
            InputStream input = null;
            input = connection.getInputStream();

            final GetMethod getMethod = new GetMethod(urlBuilder.toString());
            try {
                Reader reader = new InputStreamReader(input,"UTF-8");

                int data = reader.read();
                char[] buffer = new char[1024];
                Writer writer = new StringWriter();
                while ((data = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, data);
                }

                String result = writer.toString();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setEncoding("UTF-8");
                is.setCharacterStream(new StringReader("<"+writer.toString().trim()));

                Document doc = db.parse(is);


                String city = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='locality']/long_name/text()");
                address.setCity(city);
                String country = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='country']/long_name/text()");
                address.setCountry(country);
                String zipCode = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='postal_code']/long_name/text()");
                address.setZipCode(zipCode);
                String streetNumber = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='street_number']/long_name/text()");
                address.setNumber(streetNumber);
                String route = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='route']/long_name/text()");
                address.setStreet(route);

                return address;

            } finally {
                getMethod.releaseConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Method to get Position for a requested address
     * @param request
     * @return
     */
    public static Address getRequestPosition(String request){
        Address address = new Address();
        try {
            StringBuilder urlBuilder = new StringBuilder(GEOCODE_REQUEST_URL);
            if (StringUtils.isNotBlank(request)) {
                urlBuilder.append("address=").append(URLEncoder.encode(request, "UTF-8"));
            }

            final GetMethod getMethod = new GetMethod(urlBuilder.toString());
            try {
                httpClient.executeMethod(getMethod);
                Reader reader = new InputStreamReader(getMethod.getResponseBodyAsStream(), getMethod.getResponseCharSet());

                int data = reader.read();
                char[] buffer = new char[1024];
                Writer writer = new StringWriter();
                while ((data = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, data);
                }

                String result = writer.toString();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setEncoding("UTF-8");
                is.setCharacterStream(new StringReader("<"+writer.toString().trim()));

                Document doc = db.parse(is);

                String strLatitude = getXpathValue(doc, "//GeocodeResponse/result/geometry/location/lat/text()");
                if (strLatitude!= null){
                    address.getPosition().setLatitude(Double.parseDouble(strLatitude));
                }
                String strLongtitude = getXpathValue(doc,"//GeocodeResponse/result/geometry/location/lng/text()");
                String city = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='locality']/long_name/text()");
                address.setCity(city);
                String country = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='country']/long_name/text()");
                address.setCountry(country);
                String zipCode = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='postal_code']/long_name/text()");
                address.setZipCode(zipCode);
                String streetNumber = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='street_number']/long_name/text()");
                address.setNumber(streetNumber);
                String route = getXpathValue(doc,"//GeocodeResponse/result/address_component[type/text()='route']/long_name/text()");
                address.setStreet(route);
                if (strLongtitude!= null){
                    address.getPosition().setLongitude(Double.parseDouble(strLongtitude));
                }



                return address;


            } finally {
                getMethod.releaseConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static String getXpathValue(Document doc, String strXpath) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(strXpath);
        String resultData = null;
        Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result4;
        for (int i = 0; i < nodes.getLength(); i++) {
            resultData = nodes.item(i).getNodeValue();
        }
        return resultData;
    }
}