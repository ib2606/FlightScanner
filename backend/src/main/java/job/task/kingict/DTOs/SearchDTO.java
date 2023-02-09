package job.task.kingict.DTOs;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchDTO implements Serializable {

    private String departureFrom;
    private String arrivalTo;
    private String departDate;
    private String returnDate;
    private int numberOfAdults;
    private String currency;
    private String numberOfTransfersOnDeparture;
    private String numberOfTransfersOnArrival;
    private double price;
    static final Map<String, String> CityToIATA = new HashMap<>();

    static {
        Path path = Paths.get("src/main/resources/IATA_Codes/iata_codes.txt");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Error while opening IATA codes");
            System.exit(-1);
        }
        for(int i = 0; i+1 < lines.size(); i++){
            String key = lines.get(i).toLowerCase().trim();
            String value = lines.get(++i).toUpperCase().trim();
            CityToIATA.putIfAbsent(key, value);
        }

    }

    public SearchDTO() {
    }

    public SearchDTO(String departureFrom, String arrivalTo, String departDate, String returnDate, int numberOfAdults, String currency) {
        this.setDepartureFrom(departureFrom);
        this.setArrivalTo(arrivalTo);
        this.setDepartDate(departDate);
        this.setReturnDate(returnDate);
        this.numberOfAdults = numberOfAdults;
        this.currency = currency;
    }

    public SearchDTO(String departureFrom, String arrivalTo, String departDate, String returnDate, int numberOfAdults, String currency, String numberOfTransfersOnDeparture, String numberOfTransfersOnArrival, double price) {
        this.departureFrom = departureFrom;
        this.arrivalTo = arrivalTo;
        this.departDate = departDate;
        this.returnDate = returnDate;
        this.numberOfAdults = numberOfAdults;
        this.currency = currency;
        this.numberOfTransfersOnDeparture = numberOfTransfersOnDeparture;
        this.numberOfTransfersOnArrival = numberOfTransfersOnArrival;
        this.price = price;
    }

    public SearchDTO(String departureFrom, String arrivalTo, String departDate, String returnDate, int numberOfAdults, String currency, String numberOfTransfersOnDeparture, double price) {
        this.departureFrom = departureFrom;
        this.arrivalTo = arrivalTo;
        this.departDate = departDate;
        this.returnDate = returnDate;
        this.numberOfAdults = numberOfAdults;
        this.currency = currency;
        this.numberOfTransfersOnDeparture = numberOfTransfersOnDeparture;
        this.price = price;

    }

    public String getDepartureFrom() {
        return departureFrom;
    }

    public void setDepartureFrom(String departureFrom) {
        if(departureFrom != null) {
            String iataCode = CityToIATA.get(departureFrom.trim().toLowerCase());
            if (iataCode != null)
                this.departureFrom = iataCode;
            else if (CityToIATA.containsValue(departureFrom.trim().toUpperCase()))
                this.departureFrom = departureFrom.trim().toUpperCase();
        }
        else
            this.departureFrom = null;
    }

    public String getArrivalTo() {
        return arrivalTo;
    }

    public void setArrivalTo(String arrivalTo) {
        if(arrivalTo != null) {
            String iataCode = CityToIATA.get(arrivalTo.trim().toLowerCase());
            if (iataCode != null)
                this.arrivalTo = iataCode;
            else if (CityToIATA.containsValue(arrivalTo.trim().toUpperCase()))
                this.arrivalTo = arrivalTo.trim().toUpperCase();
        }
        else
            this.arrivalTo = null;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        if(departDate != null) {
            if (departDate.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})"))
                this.departDate = departDate;
        }
        else
            this.departDate = null;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        if(returnDate != null && returnDate.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})"))
            this.returnDate = returnDate;
        else
            this.returnDate = null;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNumberOfTransfersOnDeparture() {
        return numberOfTransfersOnDeparture;
    }

    public void setNumberOfTransfersOnDeparture(String numberOfTransfersOnDeparture) {
        this.numberOfTransfersOnDeparture = numberOfTransfersOnDeparture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNumberOfTransfersOnArrival() {
        return numberOfTransfersOnArrival;
    }

    public void setNumberOfTransfersOnArrival(String numberOfTransfersOnArrival) {
        this.numberOfTransfersOnArrival = numberOfTransfersOnArrival;
    }

    public static void editDTO(SearchDTO old){
        old.setDepartureFrom(old.getDepartureFrom());
        old.setArrivalTo(old.getArrivalTo());
        old.setDepartDate(old.getDepartDate());
        old.setReturnDate(old.getReturnDate());
    }

    public static String editIATA(String iata){
        for(Map.Entry<String, String> entry : CityToIATA.entrySet()){
            if(entry.getValue().equals(iata)){
                iata = entry.getKey().toUpperCase() + " (" + iata +")";
                break;
            }
        }
        return iata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchDTO searchDTO = (SearchDTO) o;
        return numberOfAdults == searchDTO.numberOfAdults && departureFrom.equals(searchDTO.departureFrom) && arrivalTo.equals(searchDTO.arrivalTo) && departDate.equals(searchDTO.departDate) && Objects.equals(returnDate, searchDTO.returnDate) && Objects.equals(currency, searchDTO.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureFrom, arrivalTo, departDate, returnDate, numberOfAdults, currency);
    }

    @Override
    public String toString() {
        return "SearchDTO{" +
                "departureFrom='" + departureFrom + '\'' +
                ", arrivalTo='" + arrivalTo + '\'' +
                ", departDate='" + departDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", numberOfAdults=" + numberOfAdults +
                '}';
    }

}
