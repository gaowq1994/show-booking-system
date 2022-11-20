package com.codingtest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Show Booking System
 */
public final class App {
    private App() {
    }

    /**
     * Main program
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        List<Show> showList = new ArrayList<>();
        List<Ticket> ticketList = new ArrayList<>();

        Scanner userInput = new Scanner(System.in);

        while (true) {

            System.out.println("\nPlease enter User Type: ");

            String userType = userInput.nextLine();

            if (userType.equalsIgnoreCase("Admin") || userType.equalsIgnoreCase("Buyer")) {
                displayCommandList(userType);

                String command = new String();
                while (!(command.equalsIgnoreCase("Exit"))) {

                    System.out.println("\nPlease enter Command: ");
                    command = userInput.nextLine();

                    showList = evaluateCommand(userType, command, showList, ticketList);
                }
            }
            else if (userType.equalsIgnoreCase("Exit")) {
                break;
            }
            else {
                System.out.println("Invalid User Type");
            }
        }
        userInput.close();
    }

    private static void displayCommandList(String userType) {
        if (userType.equalsIgnoreCase("Admin")) {
            System.out.println("Available Commands: \nSetup <Show Number> <Number of Rows> <Number of seats per row> <Cancellation window in minutes> \nView <Show Number> \nExit");
        }
        else if (userType.equalsIgnoreCase("Buyer")) {
            System.out.println("Available Commands: \nAvailability <Show Number> \nBook <Show Number> <Phone#> <Comma separated list of seats> \nCancel  <Ticket#>  <Phone#> \nExit");
        }
    }

    private static List<Show> evaluateCommand(String userType, String command, List<Show> showList, List<Ticket> ticketList) {
        String commandPart[] = command.split(" ");
        if (userType.equalsIgnoreCase("Admin")) {
            if (commandPart[0].equalsIgnoreCase("Setup")) {
                int showNumber = Integer.parseInt(commandPart[1]);
                int rows = Integer.parseInt(commandPart[2]);
                int cols = Integer.parseInt(commandPart[3]);
                int cancellationWindow = Integer.parseInt(commandPart[4]);
                if (rows > 26) {
                    System.out.println("Number of Rows exceed maximum of 26.\n");
                }
                else if (cols > 10) {
                    System.out.println("Number of seats per row exceed maximum of 10.\n");
                }
                else {
                    //check for existing show number
                    if (getShow(showList, showNumber) == null) {
                        Show show = setupShow(showNumber, rows, cols, cancellationWindow);
                        showList.add(show);
                    }
                    else {
                        System.out.println("Show already set up. Please use another show number.\n");
                    }
                }
            }
            else if (commandPart[0].equalsIgnoreCase("View")) {
                Show show = getShow(showList, Integer.parseInt(commandPart[1]));
                displayShowDetails(show, ticketList);
            }
            else if (commandPart[0].equalsIgnoreCase("Exit")) {
            }
            else {
                System.out.println("Invalid command.");
            }
        }
        else if (userType.equalsIgnoreCase("Buyer")) {
            if (commandPart[0].equalsIgnoreCase("Availability")) {
                Show show = getShow(showList, Integer.parseInt(commandPart[1]));
                displaySeatAvailability(show);
            }
            else if (commandPart[0].equalsIgnoreCase("Book")) {
                int showNumber = Integer.parseInt(commandPart[1]);
                int phoneNo = Integer.parseInt(commandPart[2]);
                String seatNumbers = commandPart[3];
                if (isDuplicatePhoneNo(ticketList, phoneNo)) {
                    System.out.println("Phone # already in use. Only one booking per phone # allowed.\n");
                }
                else {
                    int ticketNumber = ticketList.size() + 1;
                    showList = bookShow(showList, showNumber, ticketNumber, seatNumbers);
                    Ticket ticket = new Ticket();
                    ticket.number = ticketNumber;
                    ticket.showNumber = showNumber;
                    ticket.phoneNo = phoneNo;
                    ticket.seatNumbers = seatNumbers;
                    ticket.date = LocalDateTime.now();
                    ticketList.add(ticket);
                    System.out.println("Ticket #" + ticketNumber + " booked.\n");
                }
            }
            else if (commandPart[0].equalsIgnoreCase("Cancel")) {
                int ticketNo = Integer.parseInt(commandPart[1]);
                // int phoneNo = Integer.parseInt(commandPart[2]);
                showList = cancelTicket(showList, ticketList, ticketNo);
            }
            else if (commandPart[0].equalsIgnoreCase("Exit")) {
            }
            else {
                System.out.println("Invalid command.");
            }
        }
        return showList;
    }

    private static List<Show> cancelTicket(List<Show> showList, List<Ticket> ticketList, int ticketNo) {
        Ticket ticket = getTicket(ticketList, ticketNo);
        int showNumber = ticket.showNumber;
        Show show = getShow(showList, showNumber);
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(ticket.date, currentTime);
        if (duration.toMinutes() > show.cancellationWindow) {
            System.out.println("Cancellation window of " + show.cancellationWindow + " minutes exceeded. Cancellation is not allowed.\n");
            return showList;
        }
        else {
            ticketList.remove(ticket);
            for (Show showSelected : showList) {
                if (showSelected.number == showNumber) {
                    showSelected.ticketNumbers.remove(ticketNo);
                }
            }
            System.out.println("Ticket #" + ticketNo + " cancelled.\n");

            return showList;
        }
    }

    private static void displayShowDetails(Show show, List<Ticket> ticketList) {
        System.out.println("Show number: " + show.number);
        for (Ticket ticket : ticketList) {
            if (ticket.showNumber == show.number) {
                System.out.println("Ticket #: " + ticket.number);
                System.out.println("Phone #: " + ticket.phoneNo);
                System.out.println("Seat Numbers: " + ticket.seatNumbers);
            }
        }
    }

    private static List<String> parseSeatNumbers(String seatNumbers) {
        String seatList[] = seatNumbers.split(",");

        return Arrays.asList(seatList);
    }

    private static Show setupShow(int showNumber, int rows, int cols, int cancellationWindow) {
        Show show = new Show();
        show.number = showNumber;
        show.rows = rows;
        show.cols = cols;
        show.cancellationWindow = cancellationWindow;
        show.seats = new ArrayList<>();

        char seatRow = 'A';
        int seatCol = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Seat seat = new Seat();
                seat.row = seatRow;
                seat.col = seatCol;
                seat.isBooked = false;
                show.seats.add(seat);
                seatCol++;
            }
            seatRow++;
            seatCol = 1;
        }
        return show;
    }

    /**
     * Get show based on show number
     * @param showList The list of shows.
     * @param showNumber The show number.
     * @return The show requested.
     */
    private static Show getShow(List<Show> showList, int showNumber) {
        Show viewedShow = null;
        for (Show show : showList) {
            if (show.number == showNumber) {
                viewedShow = show;
            }
        }
        return viewedShow;
    }

    private static Ticket getTicket(List<Ticket> ticketList, int ticketNumber) {
        Ticket selectedTicket = null;
        for (Ticket ticket : ticketList) {
            if (ticket.number == ticketNumber) {
                selectedTicket = ticket;
            }
        }
        return selectedTicket;
    }

    private static boolean isDuplicatePhoneNo(List<Ticket> ticketList, int phoneNo) {
        for (Ticket ticket : ticketList) {
            if (ticket.phoneNo == phoneNo) {
                return true;
            }
        }
        return false;
    }

    private static void displaySeatAvailability(Show show) {
        for (Seat seat : show.seats) {
            if (seat.isBooked == false) {
                String seatRow=String.valueOf(seat.row);
                System.out.println(seatRow + seat.col);
            }
        }
    }

    private static List<Show> bookShow(List<Show> showList, int showNumber, int ticketNumber, String seatNumbers) {
        int index = 0;
        for (Show show : showList) {
            if (showNumber == show.number) {
                break;
            }
            index++;
        }

        showList.get(index).ticketNumbers.add(ticketNumber);

        List<Seat> seats = showList.get(index).seats;

        List<String> seatNumberList = parseSeatNumbers(seatNumbers);
        for (String seatNumber : seatNumberList) {
            char seatRow = seatNumber.charAt(0);
            int seatCol = Integer.parseInt(seatNumber.substring(1));
            for (Seat seat : seats) {
                if (seat.row == seatRow && seat.col == seatCol) {
                    if (seat.isBooked == false) {
                        seat.setIsBooked(true);
                    }
                    else {
                        System.out.println("Seat already booked.\n");
                        break;
                    }
                }
            }
        }
        showList.get(index).setSeats(seats);
        return showList;
    }
                
}
