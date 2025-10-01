package pe.ucv.foodv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.ucv.foodv.dto.ProductResponse;
import pe.ucv.foodv.model.entity.Product;
import pe.ucv.foodv.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<ProductResponse> getProductsByStore(Long storeId) {
        return productRepository.findByStoreIdAndIsActiveTrue(storeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<ProductResponse> getProductsByCategory(Product.ProductCategory category) {
        return productRepository.findByCategoryAndIsActiveTrue(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<ProductResponse> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<ProductResponse> searchProductsByStore(Long storeId, String searchTerm) {
        return productRepository.searchProductsByStore(storeId, searchTerm).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (!product.getIsActive()) {
            throw new RuntimeException("El producto no está disponible");
        }
        
        return convertToResponse(product);
    }
    
    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getImageUrl(),
            product.getCategory(),
            product.getIsActive(),
            product.getStore().getId(),
            product.getStore().getName(),
            product.getCreatedAt()
        );
    }
}

