package Art;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.UUID;

// Define a functional interface for row-based actions
@FunctionalInterface
interface RowAction {
    void perform(int row);
}

public class ArtExhibitionGUI extends JFrame {

    private JTextField idField, titleField, artistNameField, priceField;
    private JTextArea bioArea;
    private JComboBox<String> typeCombo;
    private JLabel qrCodeLabel;
    private JTextArea outputArea;
    private ArtExhibitionManager manager;
    private JDialog artListDialog;
    private JTable artTable;
    private DefaultTableModel tableModel;

    public ArtExhibitionGUI() {
        super("Art Exhibition Management System");
        manager = new ArtExhibitionManager();

        // Set up the main layout
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(147, 197, 114));
        setBorder();

        // Add title panel
        add(createTitlePanel(), BorderLayout.NORTH);

        // Add content panel
        add(createContentPanel(), BorderLayout.CENTER);

        // Add bottom panel
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Initialize the art list dialog
        initializeArtListDialog();

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setBorder() {
        getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(147, 197, 114)));
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(147, 197, 114));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel titleLabel = new JLabel("Art Exhibition Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize components
        idField = createStyledTextField();
        idField.setEditable(false);
        idField.setText(manager.generateNextArtId());

        titleField = createStyledTextField();
        artistNameField = createStyledTextField();
        bioArea = createStyledTextArea();
        typeCombo = new JComboBox<>(new String[]{"Select", "Painting", "Sculpture", "Digital"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeCombo.setBackground(Color.WHITE);

        priceField = createStyledTextField();
        priceField.setText("$ ");
        addPriceFieldFocusListener();

        // Initialize QR code label
        qrCodeLabel = new JLabel();
        qrCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qrCodeLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));

        // Add components to panel
        addComponent(panel, gbc, "Art ID:", idField, 0);
        addComponent(panel, gbc, "Title:", titleField, 1);
        addComponent(panel, gbc, "Artist Name:", artistNameField, 2);
        addComponent(panel, gbc, "Artist Bio:", new JScrollPane(bioArea), 3);
        addComponent(panel, gbc, "Type:", typeCombo, 4);
        addComponent(panel, gbc, "Price:", priceField, 5);
        addComponent(panel, gbc, "QR Code:", qrCodeLabel, 6);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(147, 197, 114));

        // Output area
        outputArea = new JTextArea(5, 30);
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(new Color(147, 197, 114), 1));
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(147, 197, 114));

        JButton addButton = createStyledButton("Add Art Piece");
        addButton.addActionListener(this::handleAddArtPiece);

        JButton viewAllButton = createStyledButton("View Art Collection");
        viewAllButton.addActionListener(e -> showArtList());

        buttonPanel.add(addButton);
        buttonPanel.add(viewAllButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addComponent(Container container, GridBagConstraints gbc, String labelText, Component component, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        container.add(createStyledLabel(labelText), gbc);

        gbc.gridx = 1;
        container.add(component, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea(3, 15);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(147, 197, 114), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return area;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(0, 0, 0));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 10));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void addPriceFieldFocusListener() {
        priceField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (priceField.getText().equals("$ ")) {
                    priceField.setCaretPosition(2);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (priceField.getText().isEmpty() || priceField.getText().equals("")) {
                    priceField.setText("$ ");
                } else if (!priceField.getText().startsWith("$ ")) {
                    if (priceField.getText().startsWith("$")) {
                        priceField.setText("$ " + priceField.getText().substring(1));
                    } else {
                        priceField.setText("$ " + priceField.getText());
                    }
                }
            }
        });
    }

    private void handleAddArtPiece(ActionEvent e) {
        String id = idField.getText().trim();
        String title = titleField.getText().trim();
        String artistName = artistNameField.getText().trim();
        String bio = bioArea.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        // Validate type selection
        if (type.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select an art type.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;

        try {
            String priceText = priceField.getText().trim().replace("$ ", "");
            if (priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid price.");
                return;
            }
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a valid number.");
            return;
        }

        Artist artist = new Artist(artistName, bio);
        ArtPiece art = new ArtPiece(id, title, artist, type, price);
        manager.addArtPiece(art);

        String info = art.getInfo();
        outputArea.setText("Added and QR code generated for:\n" + info);

        generateAndDisplayQRCode(info);

        // Clear fields and set next ID
        clearFields();
        idField.setText(manager.generateNextArtId());
    }

    private void clearFields() {
        titleField.setText("");
        artistNameField.setText("");
        bioArea.setText("");
        typeCombo.setSelectedIndex(0);  // Reset type selection to "Select"
        priceField.setText("$ "); // Reset price field with dollar sign and space
    }

    private void generateAndDisplayQRCode(String data) {
        try {
            String filename = "qr_" + UUID.randomUUID() + ".png";
            QRCodeGenerator.generateQRCode(data, filename);
            BufferedImage qrImage = ImageIO.read(new File(filename));
            qrCodeLabel.setIcon(new ImageIcon(qrImage));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "QR code generation failed: " + ex.getMessage());
        }
    }

    private void initializeArtListDialog() {
        artListDialog = new JDialog(this, "Art Collection Gallery", true);
        artListDialog.setLayout(new BorderLayout());

        // Add title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(230, 230, 250));
        JLabel titleLabel = new JLabel("Art Collection Gallery", SwingConstants.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel);
        artListDialog.add(titlePanel, BorderLayout.NORTH);

        // Initialize table with action columns
        String[] columns = {"ID", "Title", "Artist", "Type", "Price", "Update", "Delete"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 5; // Only update and delete buttons are editable
            }
        };
        artTable = new JTable(tableModel);

        // Make table non-editable except for action columns and add styling
        artTable.setDefaultEditor(Object.class, null);
        artTable.getTableHeader().setFont(new Font(artTable.getFont().getName(), Font.BOLD, 12));
        artTable.setRowHeight(25);
        artTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add action buttons to table
        artTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        artTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), this::handleUpdate));
        artTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        artTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), this::handleDelete));

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(artTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        artListDialog.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 255));
        JButton refreshButton = new JButton("Refresh List");
        JButton closeButton = new JButton("Close");

        refreshButton.addActionListener(e -> refreshArtList());
        closeButton.addActionListener(e -> artListDialog.setVisible(false));

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        artListDialog.add(buttonPanel, BorderLayout.SOUTH);

        artListDialog.setSize(800, 500);
        artListDialog.setLocationRelativeTo(this);
    }

    private void refreshArtList() {
        tableModel.setRowCount(0);
        List<ArtPiece> artPieces = manager.getAllArtPieces();

        for (ArtPiece art : artPieces) {
            Object[] row = {
                    art.getId(),
                    art.getTitle(),
                    art.getArtist().getName(),
                    art.getType(),
                    String.format("$%.2f", art.getPrice()),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(row);
        }
    }

    private void showArtList() {
        refreshArtList();
        artListDialog.setVisible(true);
    }

    private void handleUpdate(int row) {
        String id = (String) tableModel.getValueAt(row, 0);
        ArtPiece art = manager.getArtPieceById(id);
        if (art != null) {
            showEditDialog(art);
        }
    }

    private void handleDelete(int row) {
        String id = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(artListDialog,
                "Are you sure you want to delete the art piece with ID " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            manager.deleteArtPiece(id);
            refreshArtList();
            JOptionPane.showMessageDialog(artListDialog, "Art piece deleted successfully.");
        }
    }

    private void showEditDialog(ArtPiece art) {
        JDialog editDialog = new JDialog(this, "Edit Art Piece", true);
        editDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField titleFieldEdit = createStyledTextField();
        titleFieldEdit.setText(art.getTitle());
        JTextField artistNameFieldEdit = createStyledTextField();
        artistNameFieldEdit.setText(art.getArtist().getName());
        JTextArea bioAreaEdit = createStyledTextArea();
        bioAreaEdit.setText(art.getArtist().getBio());
        JComboBox<String> typeComboEdit = new JComboBox<>(new String[]{"Painting", "Sculpture", "Digital"});
        typeComboEdit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeComboEdit.setBackground(Color.WHITE);
        typeComboEdit.setSelectedItem(art.getType());
        JTextField priceFieldEdit = createStyledTextField();
        priceFieldEdit.setText(String.format("$ %.2f", art.getPrice()));
        addPriceFieldFocusListener(priceFieldEdit);

        addComponent(editDialog, gbc, "Title:", titleFieldEdit, 0);
        addComponent(editDialog, gbc, "Artist Name:", artistNameFieldEdit, 1);
        addComponent(editDialog, gbc, "Artist Bio:", new JScrollPane(bioAreaEdit), 2);
        addComponent(editDialog, gbc, "Type:", typeComboEdit, 3);
        addComponent(editDialog, gbc, "Price:", priceFieldEdit, 4);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        editDialog.add(buttonPanel, gbc);

        saveButton.addActionListener(e -> {
            String title = titleFieldEdit.getText().trim();
            String artistName = artistNameFieldEdit.getText().trim();
            String bio = bioAreaEdit.getText().trim();
            String type = (String) typeComboEdit.getSelectedItem();
            double price;

            try {
                String priceText = priceFieldEdit.getText().trim().replace("$ ", "");
                if (priceText.isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog, "Please enter a valid price.");
                    return;
                }
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editDialog, "Invalid price format. Please enter a valid number.");
                return;
            }

            Artist artistNew = new Artist(artistName, bio);
            manager.updateArtPiece(art.getId(), title, artistNew, type, price);
            refreshArtList();
            editDialog.dispose();
            JOptionPane.showMessageDialog(this, "Art piece updated successfully.");
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        editDialog.setSize(400, 400);
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    private void addPriceFieldFocusListener(JTextField field) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals("$ ")) {
                    field.setCaretPosition(2);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty() || field.getText().equals("")) {
                    field.setText("$ ");
                } else if (!field.getText().startsWith("$ ")) {
                    if (field.getText().startsWith("$")) {
                        field.setText("$ " + field.getText().substring(1));
                    } else {
                        field.setText("$ " + field.getText());
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ArtExhibitionGUI::new);
    }
}

// ButtonRenderer and ButtonEditor classes for table buttons
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private int row;
    private RowAction action;

    public ButtonEditor(JCheckBox checkBox, RowAction action) {
        super(checkBox);
        this.action = action;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            if (isPushed) {
                fireEditingStopped();
                action.perform(row);
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        this.row = row;
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}