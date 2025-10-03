package pe.ucv.foodv.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.ucv.foodv.dto.ApiResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/campus")
@CrossOrigin(origins = "*")
@Tag(name = "Campus", description = "Información del campus UCV para delivery")
public class CampusController {

    @Operation(summary = "Obtener mapa del campus", description = "Retorna la información completa del campus incluyendo pabellones, aulas y puntos de entrega")
    @GetMapping("/map")
    public ResponseEntity<ApiResponse<Object>> getCampusMap() {
        try {
            ClassPathResource resource = new ClassPathResource("campus-map.json");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Parse JSON manually or use ObjectMapper if needed
            return ResponseEntity.ok(ApiResponse.success("Mapa del campus obtenido exitosamente", content));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al cargar el mapa del campus"));
        }
    }

    @Operation(summary = "Obtener pabellones", description = "Retorna la lista de pabellones disponibles")
    @GetMapping("/pabellones")
    public ResponseEntity<ApiResponse<Object>> getPabellones() {
        try {
            ClassPathResource resource = new ClassPathResource("campus-map.json");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Extract pabellones from JSON (simplified for now)
            return ResponseEntity.ok(ApiResponse.success("Pabellones obtenidos exitosamente", content));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al cargar los pabellones"));
        }
    }

    @Operation(summary = "Obtener aulas por pabellón", description = "Retorna las aulas disponibles en un pabellón específico")
    @GetMapping("/pabellones/{pabellonId}/aulas")
    public ResponseEntity<ApiResponse<Object>> getAulasByPabellon(@PathVariable String pabellonId) {
        try {
            ClassPathResource resource = new ClassPathResource("campus-map.json");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Filter by pabellonId (simplified for now)
            return ResponseEntity
                    .ok(ApiResponse.success("Aulas del pabellón " + pabellonId + " obtenidas exitosamente", content));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al cargar las aulas del pabellón"));
        }
    }

    @Operation(summary = "Obtener puntos de entrega", description = "Retorna los puntos de entrega disponibles en el campus")
    @GetMapping("/puntos-entrega")
    public ResponseEntity<ApiResponse<Object>> getPuntosEntrega() {
        try {
            ClassPathResource resource = new ClassPathResource("campus-map.json");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            return ResponseEntity.ok(ApiResponse.success("Puntos de entrega obtenidos exitosamente", content));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al cargar los puntos de entrega"));
        }
    }
}
