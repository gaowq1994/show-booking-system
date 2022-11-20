# show-booking-system

Use Case

•	Build a simple Java application for the use case to book a show. The program must take input from command line.\
•	The program would setup available seats per show, allow buyers to select 1 or more available seats and buy/cancel tickets.\
•	The application shall cater to the below 2 types of users & their requirements – (1) Admin and (2) Buyer

Admin – The users should be able to Setup and view the list of shows and seat allocations. 
Commands to be implemented for Admin :
1. Setup &lt;Show Number> &lt;Number of Rows> &lt;Number of seats per row> &lt;Cancellation window in minutes>   
(To setup the number of seats per show) 
2. View &lt;Show Number>     
(To display Show Number, Ticket#, Buyer Phone#, Seat Numbers allocated to the buyer)

Buyer – The users should be able retrieve list of available seats for a show, select 1 or more seats , buy and cancel tickets.  
Commands to be implemented for Buyer :
1. Availability &lt;Show Number>    
(To list all available seat numbers for a show. E,g A1, F4 etc)
2. Book &lt;Show Number> &lt;Phone#> &lt;Comma separated list of seats>  
(To book a ticket. This must generate a unique ticket # and display)
3.  Cancel &lt;Ticket#> &lt;Phone#>
(To cancel a ticket. See constraints in the section below)

Constraints:\
•	Assume max seats per row is 10 and max rows are 26. Example seat number A1,  H5 etc. The “Add” command for admin must ensure rows cannot be added beyond the upper limit of 26.\
•	After booking, User can cancel the seats within a time window of 2 minutes (configurable).   Cancellation after that is not allowed.\
•	Only one booking per phone# is allowed per show.
