package pe.ucv.foodv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.ucv.foodv.model.entity.*;
import pe.ucv.foodv.repository.*;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedStores();
        seedProducts();
    }
    
    private void seedUsers() {
        if (userRepository.count() == 0) {
            // Usuario administrador
            User admin = new User();
            admin.setName("Administrador");
            admin.setUsername("ADMIN001");
            admin.setEmail("admin@ucv.edu.pe");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.UserRole.ADMIN);
            userRepository.save(admin);
            
            // Usuarios de prueba
            User user1 = new User();
            user1.setName("Juan Pérez");
            user1.setUsername("JPEREZ001");
            user1.setEmail("juan.perez@ucv.edu.pe");
            user1.setPassword(passwordEncoder.encode("password123"));
            user1.setRole(User.UserRole.CLIENTE);
            userRepository.save(user1);
            
            User user2 = new User();
            user2.setName("María García");
            user2.setUsername("MGARCIA001");
            user2.setEmail("maria.garcia@ucv.edu.pe");
            user2.setPassword(passwordEncoder.encode("password123"));
            user2.setRole(User.UserRole.CLIENTE);
            userRepository.save(user2);
            
            System.out.println("Usuarios creados exitosamente");
        }
    }
    
    private void seedStores() {
        if (storeRepository.count() == 0) {
            // Tienda Listo (Minimarket)
            Store listo = new Store();
            listo.setName("Listo");
            listo.setType(Store.StoreType.MINIMARKET);
            listo.setDescription("Minimarket con productos básicos para estudiantes");
            listo.setIsActive(true);
            storeRepository.save(listo);
            
            // Tienda Fresco (Dulcería)
            Store fresco = new Store();
            fresco.setName("Fresco");
            fresco.setType(Store.StoreType.DULCERIA);
            fresco.setDescription("Dulcería con snacks y golosinas");
            fresco.setIsActive(true);
            storeRepository.save(fresco);
            
            // Emprendedor 1
            Store emprendedor1 = new Store();
            emprendedor1.setName("Café del Estudiante");
            emprendedor1.setType(Store.StoreType.EMPRENDEDOR);
            emprendedor1.setDescription("Café y snacks preparados por estudiantes");
            emprendedor1.setIsActive(true);
            storeRepository.save(emprendedor1);
            
            // Emprendedor 2
            Store emprendedor2 = new Store();
            emprendedor2.setName("Snacks Saludables");
            emprendedor2.setType(Store.StoreType.EMPRENDEDOR);
            emprendedor2.setDescription("Productos saludables y orgánicos");
            emprendedor2.setIsActive(true);
            storeRepository.save(emprendedor2);
            
            System.out.println("Tiendas creadas exitosamente");
        }
    }
    
    private void seedProducts() {
        if (productRepository.count() == 0) {
            // Obtener tiendas
            Store listo = storeRepository.findByName("Listo").orElse(null);
            Store fresco = storeRepository.findByName("Fresco").orElse(null);
            Store cafe = storeRepository.findByName("Café del Estudiante").orElse(null);
            Store snacks = storeRepository.findByName("Snacks Saludables").orElse(null);
            
            // Productos para Listo (Minimarket) - BEBIDAS
            if (listo != null) {
                createProduct("Agua 500ml", "Agua embotellada", new BigDecimal("2.50"), 100, listo, Product.ProductCategory.BEBIDAS);
                createProduct("Gaseosa 500ml", "Coca Cola 500ml", new BigDecimal("3.50"), 50, listo, Product.ProductCategory.BEBIDAS);
                createProduct("Café instantáneo", "Café instantáneo 3 en 1", new BigDecimal("2.00"), 25, listo, Product.ProductCategory.BEBIDAS);
                
                // SNACKS
                createProduct("Galletas", "Galletas de chocolate", new BigDecimal("1.50"), 30, listo, Product.ProductCategory.SNACKS);
                createProduct("Chicle", "Chicle de menta", new BigDecimal("0.50"), 200, listo, Product.ProductCategory.SNACKS);
            }
            
            // Productos para Fresco (Dulcería) - POSTRES
            if (fresco != null) {
                createProduct("Chocolate", "Chocolate de leche", new BigDecimal("3.00"), 40, fresco, Product.ProductCategory.POSTRES);
                createProduct("Caramelos", "Caramelos surtidos", new BigDecimal("1.00"), 100, fresco, Product.ProductCategory.POSTRES);
                createProduct("Gomitas", "Gomitas de frutas", new BigDecimal("2.50"), 35, fresco, Product.ProductCategory.SNACKS);
                createProduct("Helado", "Helado de vainilla", new BigDecimal("4.00"), 20, fresco, Product.ProductCategory.POSTRES);
            }
            
            // Productos para Café del Estudiante - BEBIDAS y SNACKS
            if (cafe != null) {
                createProduct("Café americano", "Café americano caliente", new BigDecimal("5.00"), 50, cafe, Product.ProductCategory.BEBIDAS);
                createProduct("Café con leche", "Café con leche caliente", new BigDecimal("6.00"), 40, cafe, Product.ProductCategory.BEBIDAS);
                createProduct("Sandwich de pollo", "Sandwich de pollo con vegetales", new BigDecimal("8.00"), 15, cafe, Product.ProductCategory.SNACKS);
                createProduct("Empanada", "Empanada de pollo", new BigDecimal("4.50"), 25, cafe, Product.ProductCategory.SNACKS);
            }
            
            // Productos para Snacks Saludables - BEBIDAS, SNACKS y POSTRES
            if (snacks != null) {
                createProduct("Ensalada de frutas", "Ensalada fresca de frutas", new BigDecimal("7.00"), 20, snacks, Product.ProductCategory.POSTRES);
                createProduct("Yogurt natural", "Yogurt natural con granola", new BigDecimal("5.50"), 30, snacks, Product.ProductCategory.POSTRES);
                createProduct("Barra energética", "Barra energética de avena", new BigDecimal("4.00"), 25, snacks, Product.ProductCategory.SNACKS);
                createProduct("Jugo natural", "Jugo de naranja natural", new BigDecimal("6.50"), 15, snacks, Product.ProductCategory.BEBIDAS);
            }
            
            System.out.println("Productos creados exitosamente");
        }
    }
    
    private void createProduct(String name, String description, BigDecimal price, Integer stock, Store store, Product.ProductCategory category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setStore(store);
        product.setCategory(category);
        product.setIsActive(true);
        productRepository.save(product);
    }
}

