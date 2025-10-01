package pe.ucv.foodv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.ucv.foodv.dto.CreateOrderRequest;
import pe.ucv.foodv.dto.OrderItemResponse;
import pe.ucv.foodv.dto.OrderResponse;
import pe.ucv.foodv.model.entity.*;
import pe.ucv.foodv.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Carrito vacío"));
        
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }
        
        // Verificar stock y crear pedido
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDIENTE);
        order.setPabellon(request.getPabellon());
        order.setPiso(request.getPiso());
        order.setSalon(request.getSalon());
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNotes(request.getNotes());
        order.setTotal(cart.getTotal());
        
        Order savedOrder = orderRepository.save(order);
        
        // Crear items del pedido y actualizar stock
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItemRepository.save(orderItem);
            
            // Actualizar stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        // Limpiar carrito
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
        
        return convertToResponse(savedOrder);
    }
    
    public List<OrderResponse> getUserOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        User user = getCurrentUser();
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permisos para ver este pedido");
        }
        
        return convertToResponse(order);
    }
    
    public List<OrderResponse> getOrdersByStatus(Order.OrderStatus status) {
        User user = getCurrentUser();
        return orderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(user.getId(), status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    private OrderResponse convertToResponse(Order order) {
        List<OrderItemResponse> items = orderItemRepository.findByOrderId(order.getId()).stream()
                .map(this::convertItemToResponse)
                .collect(Collectors.toList());
        
        return new OrderResponse(
            order.getId(),
            order.getUser().getId(),
            order.getStatus().name(),
            order.getTotal(),
            order.getPabellon(),
            order.getPiso(),
            order.getSalon(),
            order.getDeliveryMethod(),
            order.getPaymentMethod(),
            order.getNotes(),
            items,
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
    
    private OrderItemResponse convertItemToResponse(OrderItem item) {
        return new OrderItemResponse(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getProduct().getImageUrl(),
            item.getQuantity(),
            item.getUnitPrice(),
            item.getSubtotal()
        );
    }
}

