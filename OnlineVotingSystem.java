import java.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class OnlineVotingSystem {

    private JFrame frame;
    private int currentVoterId = -1;
    private String district = "YourDistrict";
    private int nextVoterId;
    private Color lightBlueColor = new Color(173, 216, 230);

    // Database connection details
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OnlineVotingSystem();
        });
    }

    // Constructor: Initializes the main frame and sets up the initial UI
    public OnlineVotingSystem() {
        frame = new JFrame("Online Voting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        initializeNextVoterId();
        showInitialScreen();

        frame.setVisible(true);
    }

    // Loads and resizes the background image
    private ImageIcon loadImageIcon(String filename, int width, int height) {
        ImageIcon icon = new ImageIcon(filename);
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    // Fetches the next voter ID from the database
    private int initializeNextVoterId() {
        String query = "SELECT MAX(voter_id) FROM voters";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                nextVoterId = rs.getInt(1) + 1;
            } else {
                nextVoterId = 1001;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            nextVoterId = 1001;
        }
        return nextVoterId;
    }

    // Sets the current voter ID after successful login
    public void setCurrentVoterId(int currentVoterId) {
        this.currentVoterId = currentVoterId;
    }

    // Clears the frame content
    private void clearFrame() {
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
    }

    // Shows initial screen with Login and Signup options
    private void showInitialScreen() {
        clearFrame();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(lightBlueColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("WELCOME TO ONLINE VOTING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(Color.GREEN);
        loginButton.addActionListener(e -> showLoginScreen());
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(loginButton, gbc);

        JButton signupButton = new JButton("SIGNUP");
        signupButton.setFont(new Font("Arial", Font.BOLD, 18));
        signupButton.setBackground(Color.ORANGE);
        signupButton.addActionListener(e -> showSignupScreen());
        gbc.gridx = 1;
        mainPanel.add(signupButton, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
    }

    // Shows login screen
    private void showLoginScreen() {
        clearFrame();

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(lightBlueColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("VOTER LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        JLabel voterIdLabel = new JLabel("Voter ID:");
        voterIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(voterIdLabel, gbc);

        JTextField voterIdField = new JTextField(15);
        gbc.gridx = 1;
        loginPanel.add(voterIdField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(Color.GREEN);
        loginBtn.addActionListener(e -> {
            int voterId = Integer.parseInt(voterIdField.getText());
            String password = new String(passwordField.getPassword());
            if (authenticateUser(voterId, password)) {
                currentVoterId = voterId;
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                showVoteButtons();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Voter ID or Password!");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(loginBtn, gbc);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showInitialScreen());
        gbc.gridx = 1;
        loginPanel.add(backBtn, gbc);

        frame.add(loginPanel, BorderLayout.CENTER);
        frame.revalidate();
    }

    // Authenticates user from database
    private boolean authenticateUser(int voterId, String password) {
        String query = "SELECT * FROM voters WHERE voter_id = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, voterId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Shows signup screen
    private void showSignupScreen() {
        clearFrame();

        JPanel signupPanel = new JPanel(new GridBagLayout());
        signupPanel.setBackground(lightBlueColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel titleLabel = new JLabel("VOTER REGISTRATION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        signupPanel.add(titleLabel, gbc);

        String[] fields = {"Full Name:", "Date of Birth (YYYY-MM-DD):", "Address:", "City:", "Password:", "Confirm Password:"};
        JTextField[] textFields = new JTextField[6];

        for (int i = 0; i < fields.length; i++) {
            JLabel label = new JLabel(fields[i]);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            signupPanel.add(label, gbc);

            if (i >= 4) {
                textFields[i] = new JPasswordField(20);
            } else {
                textFields[i] = new JTextField(20);
            }
            gbc.gridx = 1;
            signupPanel.add(textFields[i], gbc);
        }

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(Color.GREEN);
        registerBtn.addActionListener(e -> {
            String name = textFields[0].getText();
            String dob = textFields[1].getText();
            String address = textFields[2].getText();
            String city = textFields[3].getText();
            String password = textFields[4].getText();
            String confirmPassword = textFields[5].getText();

            if (password.equals(confirmPassword)) {
                if (registerUser(name, dob, address, city, password)) {
                    JOptionPane.showMessageDialog(frame, "Registration Successful! Your Voter ID is: " + nextVoterId);
                    showInitialScreen();
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration Failed!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        signupPanel.add(registerBtn, gbc);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showInitialScreen());
        gbc.gridx = 1;
        signupPanel.add(backBtn, gbc);

        frame.add(signupPanel, BorderLayout.CENTER);
        frame.revalidate();
    }

    // Registers new user in database
    private boolean registerUser(String name, String dob, String address, String city, String password) {
        String query = "INSERT INTO voters (voter_id, full_name, date_of_birth, address, city, password, has_voted) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, nextVoterId);
            pstmt.setString(2, name);
            pstmt.setDate(3, Date.valueOf(dob));
            pstmt.setString(4, address);
            pstmt.setString(5, city);
            pstmt.setString(6, password);
            pstmt.setString(7, "NO");
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                nextVoterId++;
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Displays the main menu with options for casting votes and other actions
    public void showVoteButtons() {
        clearFrame();

        JPanel votePanel = new JPanel(new GridBagLayout());
        votePanel.setBackground(lightBlueColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel welcomeLabel = new JLabel("WELCOME VOTER!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        votePanel.add(welcomeLabel, gbc);

        String[] parties = {"Party A - Lotus", "Party B - Elephant", "Party C - Hand", "NOTA"};
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.GRAY};

        for (int i = 0; i < parties.length; i++) {
            JButton partyBtn = new JButton(parties[i]);
            partyBtn.setFont(new Font("Arial", Font.BOLD, 16));
            partyBtn.setBackground(colors[i]);
            final int partyIndex = i + 1;
            partyBtn.addActionListener(e -> castVote(partyIndex));
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            votePanel.add(partyBtn, gbc);
        }

        JButton viewInfoBtn = new JButton("View My Information");
        viewInfoBtn.setBackground(Color.CYAN);
        viewInfoBtn.addActionListener(e -> viewVoterInfo());
        gbc.gridy = 5;
        votePanel.add(viewInfoBtn, gbc);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.addActionListener(e -> {
            currentVoterId = -1;
            showInitialScreen();
        });
        gbc.gridx = 1;
        votePanel.add(logoutBtn, gbc);

        frame.add(votePanel, BorderLayout.CENTER);
        frame.revalidate();
    }

    // Casts vote for selected party
    private void castVote(int partyId) {
        // Check if already voted
        String checkQuery = "SELECT has_voted FROM voters WHERE voter_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentVoterId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && "YES".equals(rs.getString("has_voted"))) {
                JOptionPane.showMessageDialog(frame, "You have already voted! You cannot vote again.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Cast vote
        String voteQuery = "INSERT INTO votes (voter_id, party_id, vote_date) VALUES (?, ?, CURRENT_DATE)";
        String updateQuery = "UPDATE voters SET has_voted = 'YES' WHERE voter_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement voteStmt = conn.prepareStatement(voteQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

                voteStmt.setInt(1, currentVoterId);
                voteStmt.setInt(2, partyId);
                voteStmt.executeUpdate();

                updateStmt.setInt(1, currentVoterId);
                updateStmt.executeUpdate();

                conn.commit();
                JOptionPane.showMessageDialog(frame, "Vote Cast Successfully! Thank you for voting.");
                showVoteButtons();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to cast vote!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Views voter information
    private void viewVoterInfo() {
        String query = "SELECT * FROM voters WHERE voter_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, currentVoterId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                StringBuilder info = new StringBuilder();
                info.append("=== VOTER INFORMATION ===\n");
                info.append("Voter ID: ").append(rs.getInt("voter_id")).append("\n");
                info.append("Full Name: ").append(rs.getString("full_name")).append("\n");
                info.append("Date of Birth: ").append(rs.getDate("date_of_birth")).append("\n");
                info.append("Address: ").append(rs.getString("address")).append("\n");
                info.append("City: ").append(rs.getString("city")).append("\n");
                info.append("Voting Status: ").append(rs.getString("has_voted")).append("\n");

                JTextArea textArea = new JTextArea(info.toString());
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Voter Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not retrieve voter information!");
        }
    }
}
