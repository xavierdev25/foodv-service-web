package pe.ucv.foodv.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.ucv.foodv.dto.ApiResponse;
import pe.ucv.foodv.dto.CreateOrderRequest;
import pe.ucv.foodv.dto.OrderResponse;
import pe.ucv.foodv.model.entity.Order;
import pe.ucv.foodv.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Endpoints para gestión de pedidos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido con los items del carrito")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            OrderResponse order = orderService.createOrder(request);
            return ResponseEntity.ok(ApiResponse.success("Pedido creado exitosamente", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener pedidos del usuario", description = "Retorna todos los pedidos del usuario autenticado")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders() {
        try {
            List<OrderResponse> orders = orderService.getUserOrders();
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido específico por su ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long orderId) {
        try {
            OrderResponse order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(ApiResponse.success(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener pedidos por estado", description = "Retorna pedidos filtrados por estado (PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED)")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@PathVariable String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            List<OrderResponse> orders = orderService.getOrdersByStatus(orderStatus);
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Estado de pedido inválido"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Confirmar pago en efectivo", description = "Confirma el pago en efectivo de un pedido. Para pagos en efectivo, no se requiere payment simulator.")
    @PostMapping("/{orderId}/confirm-cash-payment")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmCashPayment(@PathVariable Long orderId) {
        try {
            OrderResponse order = orderService.confirmCashPayment(orderId);
            return ResponseEntity.ok(ApiResponse.success("Pago en efectivo confirmado exitosamente", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
