package pe.ucv.foodv.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.ucv.foodv.dto.ApiResponse;
import pe.ucv.foodv.dto.StoreResponse;
import pe.ucv.foodv.model.entity.Store;
import pe.ucv.foodv.service.StoreService;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
@Tag(name = "Tiendas", description = "Endpoints para gestión de tiendas")
public class StoreController {
    
    @Autowired
    private StoreService storeService;
    
    @Operation(summary = "Obtener todas las tiendas", description = "Retorna una lista de todas las tiendas disponibles")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getAllStores() {
        try {
            List<StoreResponse> stores = storeService.getAllStores();
            return ResponseEntity.ok(ApiResponse.success(stores));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(summary = "Obtener tienda por ID", description = "Retorna una tienda específica por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@PathVariable Long id) {
        try {
            StoreResponse store = storeService.getStoreById(id);
            return ResponseEntity.ok(ApiResponse.success(store));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(summary = "Obtener tiendas por tipo", description = "Retorna tiendas filtradas por tipo (RESTAURANT, CAFE, FAST_FOOD, BAKERY)")
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getStoresByType(@PathVariable String type) {
        try {
            Store.StoreType storeType = Store.StoreType.valueOf(type.toUpperCase());
            List<StoreResponse> stores = storeService.getStoresByType(storeType);
            return ResponseEntity.ok(ApiResponse.success(stores));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Tipo de tienda inválido"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

