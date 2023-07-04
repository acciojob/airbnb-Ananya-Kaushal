package com.driver.repository;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {
    HashMap<String,Hotel> storeHotel = new HashMap<>();

    HashMap<Integer, User> storeUser = new HashMap<>();

    HashMap<String,Booking> storeBooking = new HashMap<>();


    public boolean addHotel(Hotel hotel) {
        //  null values
        if(hotel == null || hotel.getHotelName() == null)return false;
        // duplicacy
        if(storeHotel.containsKey(hotel.getHotelName()))return false;
        storeHotel.put(hotel.getHotelName(), hotel);
        return true;
    }

    public Integer addUser(User user) {
        storeUser.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }


    public String getHotelWithMostFacilities() {
        String name = "";
        if(storeHotel.size() == 0)return name;
        int size = 0;
        for(Hotel hotel : storeHotel.values()){
            int nSize = hotel.getFacilities().size();
            if(nSize > size){
                size = nSize;
                name = hotel.getHotelName();
            }
            else if(nSize == size){
                if(name.compareTo(hotel.getHotelName()) > 0){
                    name = hotel.getHotelName();
                }
            }
        }
        return name;
    }

    public int bookARoom(Booking booking) {
        String hotel_name = booking.getHotelName();
        Hotel hotel = storeHotel.get(hotel_name);
        int no_of_Rooms = booking.getNoOfRooms();
        if(storeHotel.get(hotel_name).getAvailableRooms() < no_of_Rooms){
            return -1;
        }
        UUID uuid = UUID.randomUUID();
        String booking_id = uuid.toString();
        int amount_paid = no_of_Rooms * hotel.getPricePerNight();
        booking.setBookingId(booking_id);
        booking.setAmountToBePaid(amount_paid);
        storeBooking.put(booking_id,booking);

        return amount_paid;
    }


    public int getBookings(Integer aadharCard) {
        int no_of_bookings = 0;
        for(Booking booking : storeBooking.values()){
            if(booking.getBookingAadharCard() == aadharCard){
                no_of_bookings += booking.getNoOfRooms();
            }
        }
        return no_of_bookings;
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = storeHotel.get(hotelName);
        HashSet<Facility> dummyFacility = new HashSet<>();
        for(Facility facility : newFacilities){
            dummyFacility.add(facility);
        }

        for(Facility facility : hotel.getFacilities()){
            dummyFacility.add(facility);
        }

        List<Facility> updatedFacility = new ArrayList<>(dummyFacility);
        hotel.setFacilities(updatedFacility);
        return hotel;
    }
}
