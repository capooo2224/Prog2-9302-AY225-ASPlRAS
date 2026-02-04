// Weiam Aspiras 25-0430-348

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class app {
    
    // ============================================
    // DESIGN: Main Components (Tinker with these)
    // ============================================
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, gradeField;
    private JButton addButton, deleteButton;
    
    // ============================================
    // DESIGN: Dark Theme Colors
    // ============================================
    private static final Color DARK_BG = new Color(30, 30, 35);
    private static final Color DARK_SECONDARY = new Color(45, 45, 52);
    private static final Color DARK_ACCENT = new Color(60, 60, 70);
    private static final Color HEADER_COLOR = new Color(128, 45, 55);
    private static final Color BUTTON_ADD_COLOR = new Color(46, 160, 95);
    private static final Color BUTTON_DELETE_COLOR = new Color(200, 60, 60);
    private static final Color TEXT_PRIMARY = new Color(240, 240, 245);
    private static final Color TEXT_SECONDARY = new Color(180, 180, 190);
    private static final Color TABLE_ROW_ALT = new Color(38, 38, 45);
    private static final Color SELECTION_COLOR = new Color(128, 45, 55, 100);
    
    // ============================================
    // DESIGN: Sizes (Change these to resize)
    // ============================================
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 550;
    private static final int FIELD_WIDTH = 140;
    private static final int FIELD_HEIGHT = 35;
    private static final int BUTTON_WIDTH = 90;
    private static final int BUTTON_HEIGHT = 35;
    
    // ============================================
    // DESIGN: Fonts (Change these to restyle)
    // ============================================
    private static final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Verdana", Font.PLAIN, 12);
    private static final Font TABLE_FONT = new Font("Verdana", Font.PLAIN, 12);
    private static final Font TABLE_HEADER_FONT = new Font("Verdana", Font.BOLD, 12);
    private static final Font BUTTON_FONT = new Font("Verdana", Font.BOLD, 12);
    
    // CSV file path
    private static final String CSV_FILE = "MOCK_DATA.csv";
    
    public app() {
        // ============================================
        // DESIGN: Frame Setup
        // ============================================
        frame = new JFrame("Records - [WEIAM D. ASPIRAS] [25-0430-348]");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setMinimumSize(new Dimension(650, 400)); // Prevent layout breaking
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().setBackground(DARK_BG);
        
        // Create panels
        createTitlePanel();
        createInputPanel();
        createTablePanel();
        
        // Load data from CSV
        loadCSVData();
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    // ============================================
    // DESIGN: Title Panel (Dark Header)
    // ============================================
    private void createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(DARK_SECONDARY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("📋 Student Record System");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_PRIMARY);
        
        titlePanel.add(titleLabel);
        frame.add(titlePanel, BorderLayout.NORTH);
    }
    
    // ============================================
    // DESIGN: Input Panel (Form Area)
    // ============================================
    private void createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(DARK_BG);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1: Labels and Fields
        gbc.gridy = 0;
        
        // ID
        gbc.gridx = 0;
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(LABEL_FONT);
        idLabel.setForeground(TEXT_SECONDARY);
        inputPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        idField = createStyledTextField();
        inputPanel.add(idField, gbc);
        
        // Name
        gbc.gridx = 2;
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setForeground(TEXT_SECONDARY);
        inputPanel.add(nameLabel, gbc);
        
        gbc.gridx = 3;
        nameField = createStyledTextField();
        inputPanel.add(nameField, gbc);
        
        // Grade
        gbc.gridx = 4;
        JLabel gradeLabel = new JLabel("Grade");
        gradeLabel.setFont(LABEL_FONT);
        gradeLabel.setForeground(TEXT_SECONDARY);
        inputPanel.add(gradeLabel, gbc);
        
        gbc.gridx = 5;
        gradeField = createStyledTextField();
        inputPanel.add(gradeField, gbc);
        
        // Row 2: Buttons (centered)
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 5, 0);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(DARK_BG);
        
        addButton = createStyledButton("+ Add", BUTTON_ADD_COLOR);
        addButton.addActionListener(e -> addRecord());
        
        deleteButton = createStyledButton("Delete", BUTTON_DELETE_COLOR);
        deleteButton.addActionListener(e -> deleteRecord());
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        inputPanel.add(buttonPanel, gbc);
        
        // Create a top container to hold title + input
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(DARK_BG);
        
        // Get the title panel and re-add to container
        Component[] components = frame.getContentPane().getComponents();
        for (Component c : components) {
            if (c instanceof JPanel) {
                JPanel p = (JPanel) c;
                if (p.getBackground().equals(DARK_SECONDARY)) {
                    frame.remove(p);
                    topContainer.add(p, BorderLayout.NORTH);
                    break;
                }
            }
        }
        topContainer.add(inputPanel, BorderLayout.SOUTH);
        
        frame.add(topContainer, BorderLayout.NORTH);
    }
    
    // ============================================
    // DESIGN: Styled Text Field
    // ============================================
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setFont(LABEL_FONT);
        field.setBackground(DARK_ACCENT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 90), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    // ============================================
    // DESIGN: Styled Button
    // ============================================
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // ============================================
    // DESIGN: Table Panel (Dark Themed)
    // ============================================
    private void createTablePanel() {
        String[] columns = {"Student ID", "Name", "Prelims Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        
        table = new JTable(tableModel);
        table.setFont(TABLE_FONT);
        table.setRowHeight(32);
        table.setBackground(DARK_SECONDARY);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(DARK_ACCENT);
        table.setSelectionBackground(SELECTION_COLOR);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(HEADER_COLOR);
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);  // Disable column dragging
        header.setResizingAllowed(false);    // Disable column resizing
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                if (isSelected) {
                    c.setBackground(SELECTION_COLOR);
                    c.setForeground(TEXT_PRIMARY);
                } else {
                    c.setBackground(row % 2 == 0 ? DARK_SECONDARY : TABLE_ROW_ALT);
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        });
        
        // Scroll pane styling
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(DARK_SECONDARY);
        scrollPane.setBackground(DARK_BG);
        
        frame.add(scrollPane, BorderLayout.CENTER);
    }
    
    // ============================================
    // FILE I/O: Load CSV Data with try-catch
    // ============================================
    private void loadCSVData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE));
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                String[] values = line.split(",");
                
                if (values.length >= 8) {
                    String id = values[0].trim();
                    String name = values[1].trim() + " " + values[2].trim();
                    
                    double lab1 = Double.parseDouble(values[3].trim());
                    double lab2 = Double.parseDouble(values[4].trim());
                    double lab3 = Double.parseDouble(values[5].trim());
                    double prelimExam = Double.parseDouble(values[6].trim());
                    double attendanceGrade = Double.parseDouble(values[7].trim());
                    
                    // Grade Calculation
                    double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
                    double classStanding = (attendanceGrade * 0.40) + (labWorkAverage * 0.60);
                    double prelimGrade = (prelimExam * 0.30) + (classStanding * 0.70);
                    
                    String grade = String.format("%.2f", prelimGrade);
                    tableModel.addRow(new Object[]{id, name, grade});
                }
            }
            reader.close();
            
        } catch (FileNotFoundException e) {
            showStyledMessage("CSV file not found: " + CSV_FILE + "\nStarting with empty table.", "File Not Found", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            showStyledMessage("Error reading file: " + e.getMessage(), "Read Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            showStyledMessage("Error parsing numbers in CSV: " + e.getMessage(), "Parse Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ============================================
    // CRUD: Create - Add new row to table
    // ============================================
    private void addRecord() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String grade = gradeField.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || grade.isEmpty()) {
            showStyledMessage("Please fill in all fields (ID, Name, Grade)", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        tableModel.addRow(new Object[]{id, name, grade});
        idField.setText("");
        nameField.setText("");
        gradeField.setText("");
    }
    
    // ============================================
    // CRUD: Delete - Remove selected row from table
    // ============================================
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            showStyledMessage("Please select a row to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame,
            "Are you sure you want to delete this record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
        }
    }
    
    // ============================================
    // HELPER: Styled message dialog
    // ============================================
    private void showStyledMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(frame, message, title, type);
    }
    
    // ============================================
    // MAIN: Entry point
    // ============================================
    public static void main(String[] args) {
        // Enable anti-aliasing for smoother fonts
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> new app());
    }
}
