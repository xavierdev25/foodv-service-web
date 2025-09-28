package pe.ucv.foodv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.ucv.foodv.dto.AddToCartRequest;
import pe.ucv.foodv.dto.CartItemResponse;
import pe.ucv.foodv.dto.CartResponse;
import pe.ucv.foodv.dto.UpdateCartItemRequest;
import pe.ucv.foodv.model.entity.*;
import pe.ucv.foodv.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public CartResponse getCart() {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        
        List<CartItemResponse> items = cartItemRepository.findByCartId(cart.getId()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return new CartResponse(cart.getId(), user.getId(), cart.getTotal(), items, cart.getUpdatedAt());
    }
    
    public CartResponse addToCart(AddToCartRequest request) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (!product.getIsActive()) {
            throw new RuntimeException("El producto no está disponible");
        }
        
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente");
        }
        
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }
        
        updateCartTotal(cart);
        return getCart();
    }
    
    public CartResponse updateCartItem(Long itemId, UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado"));
        
        if (item.getProduct().getStock() < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente");
        }
        
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        
        updateCartTotal(item.getCart());
        return getCart();
    }
    
    public CartResponse removeFromCart(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado"));
        
        Cart cart = item.getCart();
        cartItemRepository.delete(item);
        
        updateCartTotal(cart);
        return getCart();
    }
    
    public CartResponse clearCart() {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
        
        return getCart();
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotal(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });
    }
    
    private void updateCartTotal(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        BigDecimal total = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        cart.setTotal(total);
        cartRepository.save(cart);
    }
    
    private CartItemResponse convertToResponse(CartItem item) {
        return new CartItemResponse(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getProduct().getImageUrl(),
            item.getProduct().getPrice(),
            item.getQuantity(),
            item.getSubtotal()
        );
    }
}

