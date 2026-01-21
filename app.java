// Import Swing libraries for GUI components
import javax.swing.*;
// Import AWT for layout management and styling
import java.awt.*;
// Import for date and time functionality
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// Import for generating unique E-Signature
import java.util.UUID;

/**
 * Attendance Tracker Application
 * A Java Swing application for tracking attendance with:
 * - Attendance Name field
 * - Course/Year field
 * - Time In (auto-generated from system time)
 * - E-Signature (programmatically generated)
 */
public class app {
    
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety for GUI
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    /**
     * Creates and displays the main GUI window
     */
    private static void createAndShowGUI() {
        // Create the main JFrame window with title
        JFrame frame = new JFrame("Attendance Tracker");
        frame.setSize(500, 350); // Set window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        frame.setLocationRelativeTo(null); // Center the window on screen
        
        // Create main panel with BorderLayout for organization
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        
        // Create title label at the top
        JLabel titleLabel = new JLabel("Attendance Tracking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set title font
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Create form panel using GridBagLayout for proper alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ===== Attendance Name Field =====
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.weightx = 0.3;
        formPanel.add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(20); // Text field with 20 columns
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; // Column 1
        gbc.gridy = 0; // Row 0
        gbc.weightx = 0.7;
        formPanel.add(nameField, gbc);
        
        // ===== Course/Year Field =====
        JLabel courseLabel = new JLabel("Course/Year:");
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        formPanel.add(courseLabel, gbc);
        
        JTextField courseField = new JTextField(20);
        courseField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; // Column 1
        gbc.gridy = 1; // Row 1
        formPanel.add(courseField, gbc);
        
        // ===== Time In Field (Auto-generated) =====
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 2; // Row 2
        formPanel.add(timeInLabel, gbc);
        
        JTextField timeInField = new JTextField(20);
        timeInField.setFont(new Font("Arial", Font.PLAIN, 14));
        timeInField.setEditable(false); // Make read-only since it's auto-generated
        timeInField.setBackground(new Color(240, 240, 240)); // Light gray background
        gbc.gridx = 1; // Column 1
        gbc.gridy = 2; // Row 2
        formPanel.add(timeInField, gbc);
        
        // Get current system date and time with proper formatting
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeIn = now.format(formatter); // Format the date/time nicely
        timeInField.setText(timeIn); // Set the time in field
        
        // ===== E-Signature Field (Programmatically Generated) =====
        JLabel eSignatureLabel = new JLabel("E-Signature:");
        eSignatureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 3; // Row 3
        formPanel.add(eSignatureLabel, gbc);
        
        JTextField eSignatureField = new JTextField(20);
        eSignatureField.setFont(new Font("Arial", Font.PLAIN, 12));
        eSignatureField.setEditable(false); // Make read-only since it's auto-generated
        eSignatureField.setBackground(new Color(240, 240, 240)); // Light gray background
        gbc.gridx = 1; // Column 1
        gbc.gridy = 3; // Row 3
        formPanel.add(eSignatureField, gbc);
        
        // Generate unique E-Signature using UUID
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature); // Set the e-signature field
        
        // Add form panel to the center of main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Submit button to confirm attendance
        JButton submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(e -> {
            // Validate that name and course fields are not empty
            if (nameField.getText().trim().isEmpty() || courseField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all required fields (Name and Course/Year).", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
            } else {
                // Show confirmation message with attendance details
                String message = "Attendance Recorded Successfully!\n\n" +
                    "Name: " + nameField.getText() + "\n" +
                    "Course/Year: " + courseField.getText() + "\n" +
                    "Time In: " + timeInField.getText() + "\n" +
                    "E-Signature: " + eSignatureField.getText();
                JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(submitButton);
        
        // Clear button to reset all fields
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.addActionListener(e -> {
            // Clear editable fields
            nameField.setText("");
            courseField.setText("");
            // Regenerate time and e-signature
            LocalDateTime currentTime = LocalDateTime.now();
            timeInField.setText(currentTime.format(formatter));
            eSignatureField.setText(UUID.randomUUID().toString());
        });
        buttonPanel.add(clearButton);
        
        // Add button panel to the bottom of main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        frame.add(mainPanel);
        
        // Make the frame visible
        frame.setVisible(true);
    }
}
