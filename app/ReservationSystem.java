package app;

import java.sql.*;
import java.util.Scanner;

public class ReservationSystem {

    Scanner sc = new Scanner(System.in);
    Connection con = DBConnection.getConnection();
    int userId = -1;

    // REGISTER
    public void register() {
        try {
            System.out.print("Enter name: ");
            String name = sc.next();
            System.out.print("Enter password: ");
            String pass = sc.next();

            String q = "INSERT INTO users(name,password) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, name);
            ps.setString(2, pass);
            ps.executeUpdate();

            System.out.println("Registered Successfully!");
        } catch (Exception e) {
            System.out.println("Error in registration");
        }
    }

    // LOGIN
    public void login() {
        try {
            System.out.print("Enter name: ");
            String name = sc.next();
            System.out.print("Enter password: ");
            String pass = sc.next();

            String q = "SELECT * FROM users WHERE name=? AND password=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, name);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
                System.out.println("Login Successful!");
            } else {
                System.out.println("Invalid login!");
            }

        } catch (Exception e) {
            System.out.println("Error in login");
        }
    }

    // VIEW TRAINS
    public void viewTrains() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM trains");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("train_id") + " | " +
                    rs.getString("train_name") + " | " +
                    rs.getString("source") + " -> " +
                    rs.getString("destination") + " | Seats: " +
                    rs.getInt("seats_available")
                );
            }
        } catch (Exception e) {
            System.out.println("Error fetching trains");
        }
    }

    // BOOK TICKET
    public void bookTicket() {
        if (userId == -1) {
            System.out.println("Login first!");
            return;
        }

        try {
            System.out.print("Enter train ID: ");
            int trainId = sc.nextInt();

            String check = "SELECT seats_available FROM trains WHERE train_id=?";
            PreparedStatement ps = con.prepareStatement(check);
            ps.setInt(1, trainId);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {

                System.out.print("Enter passenger name: ");
                String pname = sc.next();

                String insert = "INSERT INTO bookings(user_id,train_id,passenger_name,journey_date) VALUES(?,?,?,CURDATE())";
                PreparedStatement ps2 = con.prepareStatement(insert);
                ps2.setInt(1, userId);
                ps2.setInt(2, trainId);
                ps2.setString(3, pname);
                ps2.executeUpdate();

                String update = "UPDATE trains SET seats_available=seats_available-1 WHERE train_id=?";
                PreparedStatement ps3 = con.prepareStatement(update);
                ps3.setInt(1, trainId);
                ps3.executeUpdate();

                System.out.println("Ticket Booked!");

            } else {
                System.out.println("No seats available!");
            }

        } catch (Exception e) {
            System.out.println("Booking failed");
        }
    }

    // CANCEL TICKET
    public void cancelTicket() {
        try {
            System.out.print("Enter PNR: ");
            int pnr = sc.nextInt();

            String get = "SELECT train_id FROM bookings WHERE pnr=?";
            PreparedStatement ps = con.prepareStatement(get);
            ps.setInt(1, pnr);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int trainId = rs.getInt(1);

                String del = "DELETE FROM bookings WHERE pnr=?";
                PreparedStatement ps2 = con.prepareStatement(del);
                ps2.setInt(1, pnr);
                ps2.executeUpdate();

                String update = "UPDATE trains SET seats_available=seats_available+1 WHERE train_id=?";
                PreparedStatement ps3 = con.prepareStatement(update);
                ps3.setInt(1, trainId);
                ps3.executeUpdate();

                System.out.println("Ticket Cancelled!");
            } else {
                System.out.println("Invalid PNR");
            }

        } catch (Exception e) {
            System.out.println("Cancellation failed");
        }
    }

    // VIEW TICKET
    public void viewTicket() {
        try {
            System.out.print("Enter PNR: ");
            int pnr = sc.nextInt();

            String q = "SELECT * FROM bookings WHERE pnr=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, pnr);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("PNR: " + rs.getInt("pnr"));
                System.out.println("Passenger: " + rs.getString("passenger_name"));
                System.out.println("Train ID: " + rs.getInt("train_id"));
                System.out.println("Date: " + rs.getDate("journey_date"));
            } else {
                System.out.println("Ticket not found!");
            }

        } catch (Exception e) {
            System.out.println("Error viewing ticket");
        }
    }
}