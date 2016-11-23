/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.lameduck;

import dk.dtu.imm.fastmoney.BankPortType;
import dk.dtu.imm.fastmoney.BankSecureService;
import dk.dtu.imm.fastmoney.types.AccountType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dtu.lameduck.model.Flight;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Simon
 */
@WebService(serviceName = "AirlineReservationService")
public class AirlineReservationService {
//    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankSecureService.wsdl")
    private BankPortType bankWebService;

    static HashMap<Integer, Flight> flights = new HashMap<>();
    static HashMap<Integer, List<String>> bookings = new HashMap<>();
    private DateFormat format;
    
    public AirlineReservationService() {
        format = new SimpleDateFormat("dd/MM/yyyy");
        bankWebService = new BankSecureService().getBankSecurePort();
        Flight f = new Flight("CPH", "NYC", "Aviator", "SAS", "28/11/2016 10:25:00", 
                "28/11/2016 21:30:00", 10, 3699);
        flights.put(f.getBookingNr(), f);
        f = new Flight("CPH", "NYC", "Aviator", "Norwegian", "28/11/2016 06:45:00", 
                "28/11/2016 17:15:00", 11, 1057);
        flights.put(f.getBookingNr(), f);
    }
    
    @WebMethod(operationName = "getFlights")
    public List<Flight> getFlights(@WebParam(name = "start") String start, 
            @WebParam(name = "destination") String destination, 
            @WebParam(name = "date") String date) throws ParseException {
        Date travelDate = format.parse(date);
        List<Flight> retur = new ArrayList<>();
        for (Flight f : flights.values()) {
            if (f.getStartAirport().equals(start) && f.getDestination().equals(destination) &&
                    f.getTakeoff().compareTo(travelDate) == 0);
                retur.add(f);
        }
        return retur;
    }

    /**
     * Web service operation
     * @param bookingNumber
     * @param creditcardNumber
     * @param creditcardName
     * @param expirationMonth
     * @param expirationYear
     */
    @WebMethod(operationName = "bookFlight")
    public boolean bookFlight(@WebParam(name = "bookingNumber") int bookingNumber, 
            @WebParam(name = "creditcardNumber") String creditcardNumber,
            @WebParam(name = "creditcardName") String creditcardName, 
            @WebParam(name = "expirationMonth") int expirationMonth,
            @WebParam(name = "expirationYear") int expirationYear) throws Exception {
        if (!flights.containsKey(bookingNumber))
            throw new Exception("Invalid booking number.");
        CreditCardInfoType creditcard;
        creditcard = createCreditCardInfo(creditcardNumber, creditcardName, expirationMonth, expirationYear);
        int price = flights.get(bookingNumber).getPrice();
        boolean validCreditCard = bankWebService.validateCreditCard(14, creditcard, price);
        if (!validCreditCard)
            throw new Exception("Not enough money on the credit card");
        AccountType account = new AccountType(); 
        account.setName("LameDuck"); account.setNumber("50208812");
        bankWebService.chargeCreditCard(14, creditcard, price, account);
        if (bookings.containsKey(bookingNumber))
            bookings.get(bookingNumber).add(creditcardNumber);
        else {
            List<String> l = new ArrayList<>();
            l.add(creditcardNumber);
            bookings.put(bookingNumber, l);
        }
        return true;
    }

    /**
     * Web service operation
     * @param bookingNumber
     * @param creditcardNumber
     * @param creditcardName
     * @param expirationMonth
     * @param expirationYear
     * @param price
     */
    @WebMethod(operationName = "cancelFlight")
    public boolean cancelFlight(@WebParam(name = "bookingNumber") int bookingNumber, 
            @WebParam(name = "creditcardNumber") String creditcardNumber,
            @WebParam(name = "creditcardName") String creditcardName, 
            @WebParam(name = "expirationMonth") int expirationMonth,
            @WebParam(name = "expirationYear") int expirationYear, 
            @WebParam(name = "price") int price) throws Exception {
        if (!bookings.containsKey(bookingNumber) && !bookings.get(bookingNumber).contains(creditcardNumber))
            throw new Exception("Flight is not booked with creditcard number: "+creditcardNumber);
        CreditCardInfoType creditcard;
        creditcard = createCreditCardInfo(creditcardNumber, creditcardName, expirationMonth, expirationYear);
        price = price / 2;
        boolean validCreditCard = bankWebService.validateCreditCard(14, creditcard, price);
        if (!validCreditCard)
            throw new Exception("Creditcard is not valid");
        AccountType account = new AccountType(); 
        account.setName("LameDuck"); account.setNumber("50208812");
        bankWebService.refundCreditCard(14, creditcard, price, account);
        bookings.get(bookingNumber).remove(creditcardNumber);
        return true;
    }
    
    private CreditCardInfoType createCreditCardInfo(String creditcardNumber, String creditcardName, int expirationMonth, 
            int expirationYear) {
        CreditCardInfoType creditcard = new CreditCardInfoType();
        creditcard.setName(creditcardName);
        creditcard.setNumber(creditcardNumber);
        CreditCardInfoType.ExpirationDate exDate = new CreditCardInfoType.ExpirationDate();
        exDate.setMonth(expirationMonth);
        exDate.setYear(expirationYear);
        creditcard.setExpirationDate(exDate);
        return creditcard;
    }
}
