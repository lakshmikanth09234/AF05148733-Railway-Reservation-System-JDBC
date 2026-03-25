package app;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ReservationSystem rs = new ReservationSystem();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Register\n2.Login\n3.View Trains\n4.Book Ticket\n5.Cancel Ticket\n6.View Ticket\n7.Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1: rs.register(); break;
                case 2: rs.login(); break;
                case 3: rs.viewTrains(); break;
                case 4: rs.bookTicket(); break;
                case 5: rs.cancelTicket(); break;
                case 6: rs.viewTicket(); break;
                case 7:
                    sc.close();
                    System.exit(0);
                default: System.out.println("Invalid choice");
            }
        }
    }
}