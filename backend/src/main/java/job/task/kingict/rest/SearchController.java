package job.task.kingict.rest;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import job.task.kingict.DTOs.SearchDTO;
import job.task.kingict.Service.AmadeusConnect;
import job.task.kingict.Utils.CurrencyType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class SearchController {
    public static Map<SearchDTO, List<SearchDTO>> searchedFlights = new HashMap<>();

    @PostMapping("/search")
    public ResponseEntity<List<SearchDTO>> searchFlight(@RequestBody SearchDTO searchDTO) {
        if (searchDTO == null)
            return ResponseEntity.badRequest().body(null);

        SearchDTO.editDTO(searchDTO);

        if (searchDTO.getDepartureFrom() == null ||
                searchDTO.getArrivalTo() == null ||
                searchDTO.getDepartDate() == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        CurrencyType currencyType;
        try {
            if (searchDTO.getCurrency() != null)
                currencyType = CurrencyType.valueOf(searchDTO.getCurrency().toUpperCase());
            else
                currencyType = CurrencyType.EUR;
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        //Učitavanje lokalne pohrane
        try {
            Path path = Paths.get("src/main/resources/searchedFlights/data.properties");
            if (Files.size(path) > 1) {
                FileInputStream fileInputStream = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fileInputStream);
                //noinspection unchecked
                searchedFlights = (Map<SearchDTO, List<SearchDTO>>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Greška prilikom učitavanja datoteke");
        }


        if (searchedFlights.get(searchDTO) != null) {
            return ResponseEntity.ok(searchedFlights.get(searchDTO));
        }

        List<FlightOfferSearch> list;

        try {
            list = Arrays.asList(AmadeusConnect.INSTANCE.flights(searchDTO.getDepartureFrom(), searchDTO.getArrivalTo(), searchDTO.getDepartDate(),
                    searchDTO.getReturnDate(), String.valueOf(searchDTO.getNumberOfAdults()), currencyType.toString()));
        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body(null);
        }


        List<SearchDTO> searchDTOSResponse = new LinkedList<>();
        for (FlightOfferSearch flightOfferSearch : list) {
            String departureFrom, arrivalTo, departDate, returnDate = null, currency, numberOfTransfersOnDeparture, numberOfTransfersArrival = null;
            double price;
            int numberOfAdults;

            List<FlightOfferSearch.Itinerary> itineraryList = Arrays.stream(flightOfferSearch.getItineraries()).toList();

            currency = flightOfferSearch.getPrice().getCurrency();
            price = flightOfferSearch.getPrice().getTotal();
            numberOfAdults = flightOfferSearch.getNumberOfBookableSeats();
            var fromList = itineraryList.get(0);
            FlightOfferSearch.SearchSegment[] segment = fromList.getSegments();
            numberOfTransfersOnDeparture = String.valueOf(segment.length);
            departureFrom = SearchDTO.editIATA(segment[0].getDeparture().getIataCode());
            arrivalTo = SearchDTO.editIATA(segment[segment.length - 1].getArrival().getIataCode());

            departDate = segment[0].getDeparture().getAt();
            var returnList = itineraryList.size() == 2 ? itineraryList.get(1) : null;
            if (returnList != null) {
                FlightOfferSearch.SearchSegment[] arrivalSegment = returnList.getSegments();
                numberOfTransfersArrival = String.valueOf(arrivalSegment.length);
                returnDate = arrivalSegment[0].getDeparture().getAt();
            }

            searchDTOSResponse.add(new SearchDTO(departureFrom, arrivalTo, departDate, returnDate, numberOfAdults, currency, numberOfTransfersOnDeparture, numberOfTransfersArrival, price));

        }
        searchedFlights.put(searchDTO, searchDTOSResponse);
        //Spremanje pretrage
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src/main/resources/searchedFlights/data.properties"));
            objectOutputStream.writeObject(searchedFlights);
        } catch (IOException e) {
            System.err.println("Greška prilikom spremanja datoteke");
        }


        return ResponseEntity.ok(searchDTOSResponse);
    }


}

