package com.comicland.comicland;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ComicLandSystem {
    // Public ComicBook class
    public static class ComicBook {
        private String id;
        private String title;
        private double price;
        private int stock;
        private String imagePath;
        private boolean bestSelling;
        private String genre;

        // Constructor with bestSelling parameter
        public ComicBook(String id, String title, double price, int stock, String imagePath, boolean bestSelling, String genre) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.stock = stock;
            this.imagePath = imagePath;
            this.bestSelling = bestSelling;  // Initialize best-selling status
            this.genre = genre;
        }

        // Getter for bestSelling
        public boolean isBestSelling() {
            return bestSelling;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public void reduceStock(int quantity) {
            if (quantity <= stock) {
                stock -= quantity;
            } else {
                System.out.println("Insufficient stock for " + title);
            }
        }

        // Nested CartItem class
        public class CartItem {
            private ComicLandSystem.ComicBook comic;
            private int quantity;

            public CartItem(ComicLandSystem.ComicBook comic) {
                this.comic = comic;
                this.quantity = 1;  // Default quantity is 1
            }

            public ComicLandSystem.ComicBook getComic() {
                return comic;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public double getTotalPrice() {
                return comic.getPrice() * quantity;
            }
        }
    }

    // Inventory list
    private List<ComicBook> inventory = new ArrayList<>();

    public void initializeInventory() {
        inventory.add(new ComicBook("C001", "Spider-Man", 500.00, 12, "/images/spiderman.jpg", true, "Superhero"));
        inventory.add(new ComicBook("C002", "Batman", 350.00, 15, "/images/batman.jpg", true, "Superhero"));
        inventory.add(new ComicBook("C003", "Superman", 450.00, 24, "/images/superman.jpg", false, "Superhero"));
        inventory.add(new ComicBook("C004", "X-Men", 500.00, 45, "/images/xmen.jpg", true, "Superhero"));
        inventory.add(new ComicBook("C005", "Flashpoint", 250.00, 10, "/images/flashpoint.jpg", false, "Superhero"));
        inventory.add(new ComicBook("C006", "Fantastic Four", 500.00, 17, "/images/f4.jpg", false, "Superhero"));
        inventory.add(new ComicBook("C007", "DareDevil", 350.00, 21, "/images/daredevil.jpg", true, "Superhero"));
        inventory.add(new ComicBook("C008", "Teen Titans", 250.00, 14, "/images/teen.jpg", false, "Superhero"));
        inventory.add(new ComicBook("C009", "Injustice", 500.00, 13, "/images/injustice.jpg", true, "Superhero"));
        inventory.add(new ComicBook("C010", "Infinity Gauntlet", 300.00, 9, "/images/infinity.jpg", false, "Superhero"));
        inventory.add(new ComicBook("C011", "One Piece", 300.00, 4, "/images/op.jpg", true, "Adventure"));
        inventory.add(new ComicBook("C012", "Boruto: TBV", 400.00, 26, "/images/bor.jpg", false, "Adventure"));
        inventory.add(new ComicBook("C013", "Attack on Titan", 500.00, 18, "/images/aot.jpg", true, "Violence"));
        inventory.add(new ComicBook("C014", "My Hero Academia", 300.00, 11, "/images/mha.jpg", true, "Superhero"));
        inventory.add(new ComicBook("C015", "Black Clover", 300.00, 6, "/images/bc.jpg", false, "Violence"));
        inventory.add(new ComicBook("C016", "Jujutsu Kaisen", 350.00, 5, "/images/jjk.jpg", false, "Violence"));
        inventory.add(new ComicBook("C017", "Naruto Shippuden", 250.00, 15, "/images/naruto.jpg", true, "Violence"));
        inventory.add(new ComicBook("C018", "Demon Slayer", 250.00, 25, "/images/ds.jpg", true, "Violence"));
        inventory.add(new ComicBook("C019", "Fire Force", 300.00, 4, "/images/ff.jpg", false, "Action"));
        inventory.add(new ComicBook("C020", "Bleach", 250.00, 19, "/images/bleach.jpg", true, "Action"));
    }


    // Public method to return the inventory
    public List<ComicBook> getInventory() {
        return inventory;
    }
}
