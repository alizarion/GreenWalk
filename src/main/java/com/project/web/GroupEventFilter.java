package com.project.web;

import com.project.Helper;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.map.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 07/10/13
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class GroupEventFilter {

    private String country;

    private String city;

    private LatLng position;

    private Date fromDate;

    private Date toDate;

    private DateFormat formatter = new SimpleDateFormat("ddMMyyyy");

    public GroupEventFilter() {

    }

    public GroupEventFilter(final Map<String, String[]> params) {
        if (params.containsKey(getCityParam())) {
            this.setCity(params.get(getCityParam())[0]);
        }
        if (params.containsKey(getCountryParam())) {
            this.setCountry(params.get(getCountryParam())[0]);
        }
        if (params.containsKey(getLatLngParam())) {
            this.setPosition(Helper.
                    getLatLngFromSeparatedString(params.get(getLatLngParam())[0]));
        }
        if (params.containsKey(getLatLngParam())) {
            this.setPosition(Helper.
                    getLatLngFromSeparatedString(params.get(getLatLngParam())[0]));
        }

        try {
            if (params.containsKey(getFromDateParam())) {
                this.setFromDate(formatter.
                        parse(params.get(getFromDateParam())[0]));
            }
            if (params.containsKey(getToDateParam())) {
                this.setToDate(formatter.
                        parse(params.get(getToDateParam())[0]));
            }
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public void setCountry(String country) {
        if(!StringUtils.isEmpty(country)){
            this.country = country;
        }   else {
            this.country = null;
        }
    }

    public void setCity(String city) {
        if(!StringUtils.isEmpty(city)){
            this.city = city;
        }  else {
            this.city = null;
        }
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getCountry() {
        return country;
    }

    public Date getToDate() {
        return toDate;
    }



    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getCity() {
        return city;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setAddressPosition(String latlng) {
        this.setPosition(Helper.
                getLatLngFromSeparatedString(latlng));
    }


    public String getAddressPosition() {
        if (this.position!= null){
            return this.position.getLat() + ","
                    + this.position.getLng() ;
        }
        return null;
    }


    public final String getCityParam() {
        return "city";
    }


    public final String getFromDateParam() {
        return "from";
    }

    public final String getToDateParam() {
        return "to";
    }

    public final String getCountryParam() {
        return "country";
    }

    public final String getLatLngParam() {
        return "latlng";
    }

}
