package pe.ucv.foodv.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.ucv.foodv.dto.AddToCartRequest;
import pe.ucv.foodv.dto.ApiResponse;
import pe.ucv.foodv.dto.CartResponse;
import pe.ucv.foodv.dto.UpdateCartItemRequest;
import pe.ucv.foodv.service.CartService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
@Tag(name = "Carrito de Compras", description = "Endpoints para gestión del carrito de compras")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Operation(summary = "Obtener carrito", description = "Obtiene el carrito de compras del usuario autenticado")
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        try {
            CartResponse cart = cartService.getCart();
            return ResponseEntity.ok(ApiResponse.success(cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(summary = "Agregar producto al carrito", description = "Agrega un producto al carrito de compras")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        try {
            CartResponse cart = cartService.addToCart(request);
            return ResponseEntity.ok(ApiResponse.success("Producto agregado al carrito", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(summary = "Actualizar item del carrito", description = "Actualiza la cantidad de un item en el carrito")
    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
        try {
            CartResponse cart = cartService.updateCartItem(itemId, request);
            return ResponseEntity.ok(ApiResponse.success("Item actualizado", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(summary = "Eliminar item del carrito", description = "Elimina un item específico del carrito")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(@PathVariable Long itemId) {
        try {
            CartResponse cart = cartService.removeFromCart(itemId);
            return ResponseEntity.ok(ApiResponse.success("Item eliminado del carrito", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(summary = "Vaciar carrito", description = "Elimina todos los items del carrito")
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<CartResponse>> clearCart() {
        try {
            CartResponse cart = cartService.clearCart();
            return ResponseEntity.ok(ApiResponse.success("Carrito vaciado", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

