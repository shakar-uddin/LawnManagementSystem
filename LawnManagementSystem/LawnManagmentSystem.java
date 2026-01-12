import javax.swing.*;
import java.util.*;

// ---------------- LAWN CLASS ----------------
class Lawn {
    int number;
    String type;
    boolean isAvailable;

    public Lawn(int number, String type, boolean isAvailable) {
        this.number = number;
        this.type = type;
        this.isAvailable = isAvailable;
    }
}

// ---------------- BOOKING CLASS ----------------
class Booking {
    int id;
    String user;
    String location;
    Lawn lawn;
    String eventDate;
    String eventType;
    String status;
    String paymentStatus;

    public Booking(int id, String user, String location, Lawn lawn,
                   String eventDate, String eventType,
                   String status, String paymentStatus) {
        this.id = id;
        this.user = user;
        this.location = location;
        this.lawn = lawn;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }
}

// ---------------- USER ACCOUNT ----------------
class UserAccount {
    String email;
    String password;
    String role;

    public UserAccount(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

// ================= MAIN CLASS =================
public class LawnBookingGUI extends JFrame {

    static List<Lawn> lawnList = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static List<UserAccount> accounts = new ArrayList<>();
    static int nextBookingId = 501;

    JTextField emailField;
    JPasswordField passwordField;

    // ---------------- BUBBLE SORT ----------------
    public static void bubbleSortLawns() {
        for (int i = 0; i < lawnList.size() - 1; i++) {
            for (int j = 0; j < lawnList.size() - i - 1; j++) {
                if (lawnList.get(j).number > lawnList.get(j + 1).number) {
                    Lawn temp = lawnList.get(j);
                    lawnList.set(j, lawnList.get(j + 1));
                    lawnList.set(j + 1, temp);
                }
            }
        }
    }

    // ---------------- BINARY SEARCH ----------------
    public static Lawn binarySearchLawn(int lawnNumber) {
        int low = 0, high = lawnList.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (lawnList.get(mid).number == lawnNumber)
                return lawnList.get(mid);
            else if (lawnList.get(mid).number < lawnNumber)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return null;
    }

    // ---------------- LOGIN ----------------
    public LawnBookingGUI() {
        setTitle("Lawn Booking Login");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setBounds(30, 30, 80, 25);
        add(emailLbl);

        emailField = new JTextField();
        emailField.setBounds(100, 30, 200, 25);
        add(emailField);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(30, 70, 80, 25);
        add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 70, 200, 25);
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 110, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            for (UserAccount acc : accounts) {
                if (acc.email.equals(emailField.getText()) &&
                        acc.password.equals(new String(passwordField.getPassword()))) {

                    dispose();
                    if (acc.role.equalsIgnoreCase("Admin"))
                        new AdminMenu().setVisible(true);
                    else
                        new UserMenu(acc.email).setVisible(true);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid Credentials");
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ================= ADMIN MENU =================
    class AdminMenu extends JFrame {
        AdminMenu() {
            setTitle("Admin Panel");
            setSize(400, 300);
            setLayout(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JButton addLawn = new JButton("Add Lawn");
            JButton viewLawns = new JButton("View Lawns");
            JButton viewBookings = new JButton("View Bookings");
            JButton makeAvailable = new JButton("Mark Available");
            JButton exit = new JButton("Exit");

            addLawn.setBounds(100, 20, 200, 30);
            viewLawns.setBounds(100, 60, 200, 30);
            viewBookings.setBounds(100, 100, 200, 30);
            makeAvailable.setBounds(100, 140, 200, 30);
            exit.setBounds(100, 180, 200, 30);

            add(addLawn); add(viewLawns); add(viewBookings);
            add(makeAvailable); add(exit);

            // Add Lawn
            addLawn.addActionListener(e -> {
                try {
                    int num = Integer.parseInt(JOptionPane.showInputDialog("Lawn Number:"));
                    String type = JOptionPane.showInputDialog("Lawn Type:");
                    lawnList.add(new Lawn(num, type, true));
                    JOptionPane.showMessageDialog(this, "Lawn Added");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Input");
                }
            });

            // View Lawns
            viewLawns.addActionListener(e -> {
                bubbleSortLawns();
                StringBuilder sb = new StringBuilder();
                for (Lawn l : lawnList)
                    sb.append("Lawn ").append(l.number)
                            .append(" | ").append(l.type)
                            .append(" | Available: ").append(l.isAvailable).append("\n");

                JOptionPane.showMessageDialog(this,
                        sb.length() == 0 ? "No Lawns" : sb.toString());
            });

            // View Bookings
            viewBookings.addActionListener(e -> {
                StringBuilder sb = new StringBuilder();
                for (Booking b : bookings)
                    sb.append("ID: ").append(b.id)
                            .append(" | User: ").append(b.user)
                            .append(" | Lawn: ").append(b.lawn.number)
                            .append(" | Location: ").append(b.location)
                            .append("\n");

                JOptionPane.showMessageDialog(this,
                        sb.length() == 0 ? "No Bookings" : sb.toString());
            });

            // Mark Available
            makeAvailable.addActionListener(e -> {
                int num = Integer.parseInt(JOptionPane.showInputDialog("Lawn Number:"));
                bubbleSortLawns();
                Lawn l = binarySearchLawn(num);
                if (l != null) {
                    l.isAvailable = true;
                    JOptionPane.showMessageDialog(this, "Lawn Available Now");
                } else {
                    JOptionPane.showMessageDialog(this, "Lawn Not Found");
                }
            });

            exit.addActionListener(e -> System.exit(0));
            setLocationRelativeTo(null);
        }
    }

    // ================= USER MENU =================
    class UserMenu extends JFrame {
        UserMenu(String email) {
            setTitle("User Panel");
            setSize(400, 300);
            setLayout(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JButton book = new JButton("Book Lawn");
            JButton view = new JButton("My Bookings");
            JButton pay = new JButton("Payment Status");
            JButton exit = new JButton("Exit");

            book.setBounds(100, 20, 200, 30);
            view.setBounds(100, 60, 200, 30);
            pay.setBounds(100, 100, 200, 30);
            exit.setBounds(100, 140, 200, 30);

            add(book); add(view); add(pay); add(exit);

            book.addActionListener(e -> {
                bubbleSortLawns();
                ArrayList<Lawn> available = new ArrayList<>();
                for (Lawn l : lawnList)
                    if (l.isAvailable) available.add(l);

                if (available.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No Lawns Available");
                    return;
                }

                Lawn selected = available.get(0);

                String[] locations = {"City Lawn", "Royal Garden", "Green Palace"};
                String location = (String) JOptionPane.showInputDialog(
                        this, "Select Location", "Location",
                        JOptionPane.QUESTION_MESSAGE, null, locations, locations[0]);

                String date = JOptionPane.showInputDialog("Event Date:");
                String event = JOptionPane.showInputDialog("Event Type:");

                Booking b = new Booking(nextBookingId++, email, location,
                        selected, date, event, "Confirmed", "Unpaid");

                bookings.add(b);
                selected.isAvailable = false;

                JOptionPane.showMessageDialog(this, "Booking Confirmed!\nID: " + b.id);
            });

            view.addActionListener(e -> {
                StringBuilder sb = new StringBuilder();
                for (Booking b : bookings)
                    if (b.user.equals(email))
                        sb.append("ID: ").append(b.id)
                                .append("\nLocation: ").append(b.location)
                                .append("\nEvent: ").append(b.eventType)
                                .append("\nDate: ").append(b.eventDate)
                                .append("\n\n");

                JOptionPane.showMessageDialog(this,
                        sb.length() == 0 ? "No Bookings" : sb.toString());
            });

            pay.addActionListener(e -> {
                StringBuilder sb = new StringBuilder();
                for (Booking b : bookings)
                    if (b.user.equals(email))
                        sb.append("Booking ").append(b.id)
                                .append(" | Payment: ").append(b.paymentStatus).append("\n");

                JOptionPane.showMessageDialog(this,
                        sb.length() == 0 ? "No Payments" : sb.toString());
            });

            exit.addActionListener(e -> System.exit(0));
            setLocationRelativeTo(null);
        }
    }

    // ---------------- INITIAL DATA ----------------
    public static void initializeData() {
        lawnList.add(new Lawn(1, "Wedding Lawn", true));
        lawnList.add(new Lawn(2, "Party Lawn", true));
        lawnList.add(new Lawn(3, "Corporate Lawn", true));

        accounts.add(new UserAccount("admin@lawn.com", "admin123", "Admin"));
        accounts.add(new UserAccount("user@lawn.com", "user123", "User"));
    }

    public static void main(String[] args) {
        initializeData();
        SwingUtilities.invokeLater(LawnBookingGUI::new);
    }
}

