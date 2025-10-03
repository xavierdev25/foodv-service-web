package pe.ucv.foodv.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.AssertTrue;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    // Eliminamos @NotBlank y marcamos como nullable en la BD
    @Column(name = "pabellon", nullable = true)
    private String pabellon;

    @Column(name = "piso", nullable = true)
    private String piso;

    @Column(name = "salon", nullable = true)
    private String salon;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method", nullable = false)
    private DeliveryMethod deliveryMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.PENDIENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @AssertTrue(message = "Pabellón, piso y salón son obligatorios para Mini Delivery")
    public boolean isLocationValid() {
        if (deliveryMethod == DeliveryMethod.MINI_DELIVERY) {
            return pabellon != null && !pabellon.isBlank()
                    && piso != null && !piso.isBlank()
                    && salon != null && !salon.isBlank();
        }
        return true; // RECOGIDA_TIENDA no requiere pabellon, piso ni salon
    }

    public enum OrderStatus {
        PENDIENTE, CONFIRMADO, EN_CAMINO, ENTREGADO, CANCELADO
    }

    public enum DeliveryMethod {
        RECOGIDA_TIENDA, MINI_DELIVERY
    }

    public enum PaymentMethod {
        EFECTIVO, YAPE, PLIN
    }
}

// @Entity
// @Table(name = "orders")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class Order {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// @ManyToOne(fetch = FetchType.LAZY)
// @JoinColumn(name = "user_id", nullable = false)
// private User user;

// @Enumerated(EnumType.STRING)
// @Column(name = "status", nullable = false)
// private OrderStatus status;

// @Column(name = "total", precision = 10, scale = 2, nullable = false)
// private BigDecimal total;

// @NotBlank(message = "El pabellón es obligatorio")
// @Column(name = "pabellon", nullable = false)
// private String pabellon;

// @NotBlank(message = "El piso es obligatorio")
// @Column(name = "piso", nullable = false)
// private String piso;

// @NotBlank(message = "El salón es obligatorio")
// @Column(name = "salon", nullable = false)
// private String salon;

// @Enumerated(EnumType.STRING)
// @Column(name = "delivery_method", nullable = false)
// private DeliveryMethod deliveryMethod;

// @Enumerated(EnumType.STRING)
// @Column(name = "payment_method", nullable = false)
// private PaymentMethod paymentMethod;

// @Column(name = "notes", length = 500)
// private String notes;

// @Column(name = "created_at")
// private LocalDateTime createdAt;

// @Column(name = "updated_at")
// private LocalDateTime updatedAt;

// @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch =
// FetchType.LAZY, orphanRemoval = true)
// private List<OrderItem> items;

// @PrePersist
// protected void onCreate() {
// createdAt = LocalDateTime.now();
// updatedAt = LocalDateTime.now();
// if (status == null) {
// status = OrderStatus.PENDIENTE;
// }
// }

// @PreUpdate
// protected void onUpdate() {
// updatedAt = LocalDateTime.now();
// }

// public enum OrderStatus {
// PENDIENTE, CONFIRMADO, EN_CAMINO, ENTREGADO, CANCELADO
// }

// public enum DeliveryMethod {
// RECOGIDA_TIENDA, MINI_DELIVERY
// }

// public enum PaymentMethod {
// EFECTIVO, YAPE, PLIN
// }
// }
