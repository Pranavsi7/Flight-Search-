package com.searchflights.utility;

import com.searchflights.model.Flight;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Utility class for importing flight data from an XML file.
 * <p>
 * Parses a {@link MultipartFile} representing an XML file and extracts flight entries
 * defined under {@code <flight>} elements. Each valid XML node is converted into a
 * {@link com.searchflights.model.Flight} object.
 * </p>
 *
 * <p>
 * Expected XML structure for each flight:
 * <pre>{@code
 * <flights>
 *     <flight>
 *         <flightNumber>...</flightNumber>
 *         <departureLocation>...</departureLocation>
 *         <arrivalLocation>...</arrivalLocation>
 *         <departureTime>HH:mm</departureTime>
 *         <duration>...</duration>
 *         <fare>...</fare>
 *         <validTill>yyyy-MM-dd</validTill>
 *         <classType>...</classType>
 *     </flight>
 *     ...
 * </flights>
 * }</pre>
 * </p>
 *
 * <p>
 * This class uses Java DOM (Document Object Model) parsing. If no {@code <flight>} elements
 * are found or if the structure is invalid, an exception may be thrown.
 * </p>
 *
 * @author Pranav Singh
 * @see com.searchflights.model.Flight
 */

public class XMLImporter {
    public static List<Flight> parseXMLToFlights(MultipartFile file) throws Exception {
        List<Flight> flights = new ArrayList<>();
        try (InputStream input = file.getInputStream()) {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("flight");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    Flight flight = new Flight();
                    flight.setFlightNumber(getTagText(e, "flightNumber"));
                    flight.setArrivalLocation(getTagText(e, "arrivalLocation"));
                    flight.setDepartureLocation(getTagText(e, "departureLocation"));
                    flight.setFare(new BigDecimal(getTagText(e, "fare")));
                    String departureTimeStr = getTagText(e, "departureTime");
                    if (!departureTimeStr.isEmpty())
                        flight.setDepartureTime(LocalTime.parse(departureTimeStr));
                    flight.setDuration(getTagText(e, "duration"));
                    String validTillStr = getTagText(e, "validTill");
                    if (!validTillStr.isEmpty())
                        flight.setValidTill(LocalDate.parse(validTillStr));
                    flight.setFlightClass(getTagText(e, "classType"));
                    flights.add(flight);
                }
            }
        }
        return flights;
    }

    private static String getTagText(Element e, String tag) {
        NodeList nl = e.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).getTextContent() != null) {
            return nl.item(0).getTextContent().trim();
        }
        return "";
    }
}
