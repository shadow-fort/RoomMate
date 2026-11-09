package com.example.roommate.databaseTest.buider;

import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.bookingAggregat.Period;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestBookingBuilder {

    private Long id = 12L;

    private String username = "alonzo";

    private String roomName = "Meeting";

    private int workplaceNumber = 2;

    private LocalDate date = LocalDate.of(2024,6, 5);


    private LocalTime startTime = LocalTime.of(11, 0);

    private LocalTime endTime = LocalTime.of(12, 0);

    public TestBookingBuilder withId(Long iD){
        this.id = iD;
        return this;
    }

    public TestBookingBuilder withUsername(String userName){
        this.username = userName;
        return this;
    }

    public TestBookingBuilder withRoomName(String room_name){
        this.roomName = room_name;
        return this;
    }

    public TestBookingBuilder withWorplaceNumber(int workplace){
        this.workplaceNumber = workplace;
        return this;
    }


    public TestBookingBuilder withDate(LocalDate daTe){
        this.date = daTe;
        return this;
    }

    public TestBookingBuilder withStartTime(LocalTime start){
        this.startTime = start;
        return this;
    }

    public TestBookingBuilder withEndTime(LocalTime end){
        this.endTime = end;
        return this;
    }
    public Booking build(){
        return new Booking(id, username, roomName,workplaceNumber, new Period(date, startTime, endTime));
    }
}
