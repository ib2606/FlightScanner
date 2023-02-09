package job.task.kingict.Service;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.exceptions.ResponseException;
public enum AmadeusConnect {
    INSTANCE;
    private Amadeus amadeus;
    private AmadeusConnect() {
        this.amadeus = Amadeus
                .builder("1XBXHGELwXUkSpaHe7wX4RNFnZOj9aX0", "VoGC3XZ751tbanj6")
                .build();
    }
    public FlightOfferSearch[] flights(String from, String to, String departDate, String returnDate, String numberOfAdults, String currency) throws ResponseException {
        Params params;
        if(returnDate == null)
            params = Params.with("originLocationCode", from)
                    .and("destinationLocationCode", to)
                    .and("departureDate", departDate)
                    .and("adults", numberOfAdults == null ? 1 : numberOfAdults)
                    .and("currencyCode", currency)
                    .and("max", 20);
        else
            params = Params.with("originLocationCode", from)
                    .and("destinationLocationCode", to)
                    .and("departureDate", departDate)
                    .and("returnDate", returnDate)
                    .and("adults", numberOfAdults == null ? 1 : numberOfAdults)
                    .and("currencyCode", currency)
                    .and("max", 20);

        return amadeus.shopping.flightOffersSearch.get(params);
    }

}
