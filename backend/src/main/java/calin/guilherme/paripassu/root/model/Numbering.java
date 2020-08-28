package calin.guilherme.paripassu.root.model;

import java.sql.Timestamp;

public class Numbering {
    private long id;
    private String numberingCode;
    private String numberingType;
    private Timestamp dateTimeRequest;
    private Timestamp dateTimeServed;

    public Numbering(long id, int numbering, String numberingType, Timestamp dateTimeRequest){
        this.id = id;
        this.numberingType = numberingType;
        this.numberingCode = this.padLeft(String.valueOf(numbering), 4, "0");
        this.dateTimeRequest = dateTimeRequest;
    }

    public Numbering(long id, int numbering, String numberingType, Timestamp dateTimeRequest, Timestamp dateTimeServed){
        this.id = id;
        this.numberingType = numberingType;
        this.numberingCode = this.padLeft(String.valueOf(numbering), 4, "0");
        this.dateTimeRequest = dateTimeRequest;
        this.dateTimeServed = dateTimeServed;
    }

    public String getNumberingCode() {
        return numberingCode;
    }

    public String getNumberingType() {
        return numberingType;
    }

    public long getId() {
        return id;
    }

    public Timestamp getDateTimeRequest() {
        return dateTimeRequest;
    }

    public Timestamp getDateTimeServed() {
        return dateTimeServed;
    }

    public void setDateTimeServed(Timestamp dateTimeServed) {
        this.dateTimeServed = dateTimeServed;
    }

    private String padLeft(String string, int length, String character){
        String newPattern = "";

        for(int i = string.length(); i < length; i++){
            newPattern += character;
        }

        return newPattern + string;
    }
}
