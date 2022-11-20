package com.codingtest;

import java.util.ArrayList;
import java.util.List;

public class Show {
    int number;
    int rows;
    int cols;
    int cancellationWindow;
    List<Seat> seats;
    List<Integer> ticketNumbers = new ArrayList<>();

    public void setSeats(List<Seat> seats)  {
        this.seats = seats;
    }
}
