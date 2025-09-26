package com.GQT.corejava.Swings;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// -------------------- Product Class --------------------
class Product {
    String name;
    double price;
    String description;

    Product(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String toString() {
        return name + ("₹" + price + " "+ description);
    }
}

// -------------------- SubCategory Class --------------------
class SubCategory {
    String name;
    ArrayList<Product> products;

    SubCategory(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product p) {
        products.add(p);
    }

    public String toString() {
        return name;
    }
}

// -------------------- Category Class --------------------
class Category {
    String name;
    ArrayList<SubCategory> subCategories;

    Category(String name) {
        this.name = name;
        this.subCategories = new ArrayList<>();
    }

    public void addSubCategory(SubCategory sc) {
        subCategories.add(sc);
    }

    public String toString() {
        return name;
    }
}

// -------------------- Main ECommerceApp Class --------------------
public class Ecommerce {
    static ArrayList<Category> categories = new ArrayList<>();
    static ArrayList<Product> cart = new ArrayList<>();

    public static void main(String[] args) {
        setupData(); // load categories, subcategories, products
        SwingUtilities.invokeLater(() -> showUserForm());
    }

    // -------------------- User Form (Login/Registration) --------------------
    private static void showUserForm() {
        JFrame userFrame = new JFrame("User Registration");
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userFrame.setLayout(new BorderLayout()); // main layout

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(25);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(25);
        JLabel contactLabel = new JLabel("Contact:");
        JTextField contactField = new JTextField(25);
        JButton submitBtn = new JButton("Submit");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(contactLabel);
        formPanel.add(contactField);
        formPanel.add(new JLabel()); // spacer
        formPanel.add(submitBtn);

        // Put formPanel inside another panel with FlowLayout (to center it)
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        centerPanel.add(formPanel);

        // Add to frame
        userFrame.add(centerPanel, BorderLayout.CENTER);

        userFrame.setSize(600, 400);          // main window size
        userFrame.setLocationRelativeTo(null); // center window on screen
        userFrame.setVisible(true);

        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(userFrame, "Please fill all fields!");
            } else {
                JOptionPane.showMessageDialog(userFrame, "Welcome " + name + "!");
                userFrame.dispose();
                showEcommerceApp();
            }
        });
    }


    private static void showEcommerceApp() {
        JFrame frame = new JFrame("E-Commerce App");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Category, SubCategory, Product dropdowns
        JComboBox<Category> categoryBox = new JComboBox<>(categories.toArray(new Category[0]));
        JComboBox<SubCategory> subCategoryBox = new JComboBox<>();
        JComboBox<Product> productBox = new JComboBox<>();

        JButton addToCart = new JButton("Add to Cart");
        JButton viewCart = new JButton("View Cart");
        JButton checkout = new JButton("Checkout");

        frame.add(new JLabel("Category:"));
        frame.add(categoryBox);
        frame.add(new JLabel("SubCategory:"));
        frame.add(subCategoryBox);
        frame.add(new JLabel("Products:"));
        frame.add(productBox);
        frame.add(addToCart);
        frame.add(viewCart);
        frame.add(checkout);

        // Load SubCategories when Category changes
        categoryBox.addActionListener(e -> {
            Category selected = (Category) categoryBox.getSelectedItem();
            subCategoryBox.removeAllItems();
            productBox.removeAllItems();
            if (selected != null) {
                for (SubCategory sc : selected.subCategories) {
                    subCategoryBox.addItem(sc);
                }
            }
        });

        // Load Products when SubCategory changes
        subCategoryBox.addActionListener(e -> {
            SubCategory selected = (SubCategory) subCategoryBox.getSelectedItem();
            productBox.removeAllItems();
            if (selected != null) {
                for (Product p : selected.products) {
                    productBox.addItem(p);
                }
            }
        });

        // Add to Cart
        addToCart.addActionListener(e -> {
            Product selected = (Product) productBox.getSelectedItem();
            if (selected != null) {
                cart.add(selected);
                JOptionPane.showMessageDialog(frame, selected.name + " added to cart!");
            }
        });

        // View Cart
        viewCart.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Your cart is empty!");
                return;
            }
            StringBuilder sb = new StringBuilder("Your Cart:\n");
            double total = 0;
            for (Product p : cart) {
                sb.append(p.name).append(" - ₹").append(p.price).append("\n");
                total += p.price;
            }
            sb.append("\nTotal: ₹").append(total);
            JOptionPane.showMessageDialog(frame, sb.toString());
        });

        // Checkout
        checkout.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Cart is empty! Add items before checkout.");
            } else {
                double total = 0;
                for (Product p : cart) total += p.price;
                JOptionPane.showMessageDialog(frame, "Order placed successfully!\nTotal Amount: ₹" + total + "\nThank you for shopping!");
                cart.clear();
            }
        });

        frame.setVisible(true);
    }

    // -------------------- Setup Data --------------------
    private static void setupData() {
        // Category 1: Electronics
        Category electronics = new Category("Electronics");

        SubCategory mobiles = new SubCategory("Mobiles");
        mobiles.addProduct(new Product("iPhone 14", 80000, "Apple smartphone"));
        mobiles.addProduct(new Product("Samsung Galaxy S22", 65000, "Samsung flagship"));
        mobiles.addProduct(new Product("OnePlus 10", 55000, "OnePlus premium phone"));
        mobiles.addProduct(new Product("Xiaomi Note 12", 20000, "Budget phone"));
        mobiles.addProduct(new Product("Realme Narzo", 18000, "Affordable phone"));

        SubCategory laptops = new SubCategory("Laptops");
        laptops.addProduct(new Product("MacBook Pro", 120000, "Apple laptop"));
        laptops.addProduct(new Product("Dell XPS", 100000, "Dell premium laptop"));
        laptops.addProduct(new Product("HP Pavilion", 60000, "HP midrange laptop"));
        laptops.addProduct(new Product("Lenovo ThinkPad", 75000, "Business laptop"));
        laptops.addProduct(new Product("Acer Aspire", 40000, "Budget laptop"));

        SubCategory tvs = new SubCategory("Televisions");
        tvs.addProduct(new Product("Sony Bravia", 70000, "4K Smart TV"));
        tvs.addProduct(new Product("Samsung QLED", 85000, "Premium TV"));
        tvs.addProduct(new Product("LG OLED", 90000, "High-end OLED TV"));
        tvs.addProduct(new Product("TCL Smart TV", 35000, "Budget TV"));
        tvs.addProduct(new Product("Mi TV", 25000, "Affordable Smart TV"));

        SubCategory accessories = new SubCategory("Accessories");
        accessories.addProduct(new Product("Headphones", 2000, "Wireless headphones"));
        accessories.addProduct(new Product("Smartwatch", 5000, "Fitness tracking watch"));
        accessories.addProduct(new Product("Keyboard", 1500, "Mechanical keyboard"));
        accessories.addProduct(new Product("Mouse", 800, "Wireless mouse"));
        accessories.addProduct(new Product("Power Bank", 1200, "10000mAh power bank"));

        electronics.addSubCategory(mobiles);
        electronics.addSubCategory(laptops);
        electronics.addSubCategory(tvs);
        electronics.addSubCategory(accessories);

        // Category 2: Fashion
        Category fashion = new Category("Fashion");

        SubCategory mens = new SubCategory("Men's Wear");
        mens.addProduct(new Product("T-Shirt", 800, "Cotton T-Shirt"));
        mens.addProduct(new Product("Jeans", 1500, "Slim fit jeans"));
        mens.addProduct(new Product("Jacket", 3000, "Leather jacket"));
        mens.addProduct(new Product("Shoes", 2500, "Sports shoes"));
        mens.addProduct(new Product("Watch", 5000, "Analog watch"));

        SubCategory womens = new SubCategory("Women's Wear");
        womens.addProduct(new Product("Saree", 2000, "Silk saree"));
        womens.addProduct(new Product("Dress", 3000, "Party dress"));
        womens.addProduct(new Product("Handbag", 1500, "Leather handbag"));
        womens.addProduct(new Product("Heels", 2000, "High heels"));
        womens.addProduct(new Product("Jewellery Set", 5000, "Gold plated set"));

        SubCategory kids = new SubCategory("Kids Wear");
        kids.addProduct(new Product("Shirt", 600, "Cotton shirt"));
        kids.addProduct(new Product("Shorts", 500, "Casual shorts"));
        kids.addProduct(new Product("School Bag", 1000, "Waterproof bag"));
        kids.addProduct(new Product("Sneakers", 1200, "Sports sneakers"));
        kids.addProduct(new Product("Jacket", 1500, "Winter jacket"));

        SubCategory accessoriesFashion = new SubCategory("Accessories");
        accessoriesFashion.addProduct(new Product("Sunglasses", 1000, "UV protected"));
        accessoriesFashion.addProduct(new Product("Belt", 700, "Leather belt"));
        accessoriesFashion.addProduct(new Product("Cap", 300, "Baseball cap"));
        accessoriesFashion.addProduct(new Product("Scarf", 500, "Wool scarf"));
        accessoriesFashion.addProduct(new Product("Wallet", 800, "Leather wallet"));

        fashion.addSubCategory(mens);
        fashion.addSubCategory(womens);
        fashion.addSubCategory(kids);
        fashion.addSubCategory(accessoriesFashion);

        // Category 3: Books
        Category books = new Category("Books");

        SubCategory fiction = new SubCategory("Fiction");
        fiction.addProduct(new Product("Harry Potter", 1200, "Fantasy novel"));
        fiction.addProduct(new Product("Lord of the Rings", 1500, "Epic fantasy"));
        fiction.addProduct(new Product("The Alchemist", 500, "Inspirational novel"));
        fiction.addProduct(new Product("Game of Thrones", 1800, "Fantasy saga"));
        fiction.addProduct(new Product("Sherlock Holmes", 800, "Detective stories"));

        SubCategory nonFiction = new SubCategory("Non-Fiction");
        nonFiction.addProduct(new Product("Sapiens", 700, "History of mankind"));
        nonFiction.addProduct(new Product("Atomic Habits", 600, "Self-help book"));
        nonFiction.addProduct(new Product("Educated", 750, "Memoir"));
        nonFiction.addProduct(new Product("Becoming", 1000, "Biography of Michelle Obama"));
        nonFiction.addProduct(new Product("Ikigai", 550, "Japanese philosophy"));

        SubCategory education = new SubCategory("Educational");
        education.addProduct(new Product("Java Programming", 900, "Programming guide"));
        education.addProduct(new Product("Data Structures", 850, "Computer science book"));
        education.addProduct(new Product("Mathematics", 700, "Algebra & calculus"));
        education.addProduct(new Product("Physics", 950, "Fundamentals of physics"));
        education.addProduct(new Product("Chemistry", 800, "Organic chemistry"));

        SubCategory comics = new SubCategory("Comics");
        comics.addProduct(new Product("Marvel Avengers", 500, "Superhero comic"));
        comics.addProduct(new Product("DC Batman", 450, "Dark Knight comic"));
        comics.addProduct(new Product("Spiderman", 400, "Friendly neighborhood Spiderman"));
        comics.addProduct(new Product("Dragon Ball Z", 600, "Manga series"));
        comics.addProduct(new Product("Naruto", 550, "Anime manga"));

        books.addSubCategory(fiction);
        books.addSubCategory(nonFiction);
        books.addSubCategory(education);
        books.addSubCategory(comics);

        // Category 4: Home & Kitchen
        Category home = new Category("Home & Kitchen");

        SubCategory furniture = new SubCategory("Furniture");
        furniture.addProduct(new Product("Sofa", 20000, "3-seater sofa"));
        furniture.addProduct(new Product("Dining Table", 15000, "Wooden table"));
        furniture.addProduct(new Product("Bed", 25000, "Queen size bed"));
        furniture.addProduct(new Product("Chair", 2000, "Office chair"));
        furniture.addProduct(new Product("Bookshelf", 5000, "Wooden bookshelf"));

        SubCategory appliances = new SubCategory("Appliances");
        appliances.addProduct(new Product("Washing Machine", 30000, "Front load"));
        appliances.addProduct(new Product("Refrigerator", 35000, "Double door"));
        appliances.addProduct(new Product("Microwave", 8000, "Convection oven"));
        appliances.addProduct(new Product("Mixer Grinder", 4000, "Kitchen grinder"));
        appliances.addProduct(new Product("Air Conditioner", 40000, "Split AC"));

        SubCategory decor = new SubCategory("Decor");
        decor.addProduct(new Product("Wall Clock", 1500, "Antique design"));
        decor.addProduct(new Product("Lamp", 1200, "Table lamp"));
        decor.addProduct(new Product("Painting", 2500, "Canvas art"));
        decor.addProduct(new Product("Vase", 800, "Ceramic vase"));
        decor.addProduct(new Product("Curtains", 1800, "Cotton curtains"));

        SubCategory kitchen = new SubCategory("Kitchen");
        kitchen.addProduct(new Product("Cooker", 2500, "Pressure cooker"));
        kitchen.addProduct(new Product("Dinner Set", 3000, "Ceramic dinner set"));
        kitchen.addProduct(new Product("Frying Pan", 1200, "Non-stick pan"));
        kitchen.addProduct(new Product("Blender", 3500, "Hand blender"));
        kitchen.addProduct(new Product("Kettle", 1500, "Electric kettle"));

        home.addSubCategory(furniture);
        home.addSubCategory(appliances);
        home.addSubCategory(decor);
        home.addSubCategory(kitchen);

        // Add all categories to main list
        categories.add(electronics);
        categories.add(fashion);
        categories.add(books);
        categories.add(home);
    }
}
