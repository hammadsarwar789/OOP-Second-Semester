package Art;

import java.util.Scanner;

public class Main {
    private static ArtExhibitionManager manager = new ArtExhibitionManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load data from file at startup
        manager.loadFromFile();

        while (true) {
            System.out.println("\nArt Exhibition Management System");
            System.out.println("1. Add Art Piece");
            System.out.println("2. View All Art Pieces");
            System.out.println("3. Update Art Piece");
            System.out.println("4. Delete Art Piece");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addArtPiece();
                    break;
                case 2:
                    viewAllArtPieces();
                    break;
                case 3:
                    updateArtPiece();
                    break;
                case 4:
                    deleteArtPiece();
                    break;
                case 5:
                    // Save data to file before exiting
                    manager.saveToFile();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addArtPiece() {
        String id = manager.generateNextArtId();
        System.out.println("Generated Art ID: " + id);

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Artist Name: ");
        String artistName = scanner.nextLine();

        System.out.print("Enter Artist Bio: ");
        String bio = scanner.nextLine();

        String type;
        while (true) {
            System.out.print("Enter Type (Painting, Sculpture, Digital): ");
            type = scanner.nextLine();
            if (type.equalsIgnoreCase("Painting") || type.equalsIgnoreCase("Sculpture") || type.equalsIgnoreCase("Digital")) {
                break;
            } else {
                System.out.println("Invalid type. Please enter one of the following: Painting, Sculpture, Digital.");
            }
        }

        double price;
        while (true) {
            System.out.print("Enter Price: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid price.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value for the price.");
            }
        }

        Artist artist = new Artist(artistName, bio);
        ArtPiece artPiece = new ArtPiece(id, title, artist, type, price);
        manager.addArtPiece(artPiece);

        System.out.println("Art piece added successfully!");
    }

    private static void viewAllArtPieces() {
        System.out.println("\nAll Art Pieces:");
        for (ArtPiece artPiece : manager.getAllArtPieces()) {
            System.out.println(artPiece.getInfo());
            System.out.println("--------------------");
        }
    }

    private static void updateArtPiece() {
        System.out.print("Enter Art ID to update: ");
        String id = scanner.nextLine();

        ArtPiece artPiece = manager.getArtPieceById(id);
        if (artPiece == null) {
            System.out.println("Art piece not found.");
            return;
        }

        System.out.print("Enter New Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter New Artist Name: ");
        String artistName = scanner.nextLine();

        System.out.print("Enter New Artist Bio: ");
        String bio = scanner.nextLine();

        String type;
        while (true) {
            System.out.print("Enter New Type (Painting, Sculpture, Digital): ");
            type = scanner.nextLine();
            if (type.equalsIgnoreCase("Painting") || type.equalsIgnoreCase("Sculpture") || type.equalsIgnoreCase("Digital")) {
                break;
            } else {
                System.out.println("Invalid type. Please enter one of the following: Painting, Sculpture, Digital.");
            }
        }

        double price;
        while (true) {
            System.out.print("Enter New Price: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid price.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value for the price.");
            }
        }

        Artist artist = new Artist(artistName, bio);
        manager.updateArtPiece(id, title, artist, type, price);

        System.out.println("Art piece updated successfully!");
    }

    private static void deleteArtPiece() {
        System.out.print("Enter Art ID to delete: ");
        String id = scanner.nextLine();

        ArtPiece artPiece = manager.getArtPieceById(id);
        if (artPiece == null) {
            System.out.println("Art piece not found. Please enter a valid ID.");
            return;
        }

        manager.deleteArtPiece(id);
        System.out.println("Art piece deleted successfully!");
    }
}
