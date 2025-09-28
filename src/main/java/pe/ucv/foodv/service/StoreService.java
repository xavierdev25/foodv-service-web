package pe.ucv.foodv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.ucv.foodv.dto.StoreResponse;
import pe.ucv.foodv.model.entity.Store;
import pe.ucv.foodv.repository.StoreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;
    
    public List<StoreResponse> getAllStores() {
        return storeRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<StoreResponse> getStoresByType(Store.StoreType type) {
        return storeRepository.findByTypeAndIsActiveTrue(type).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        
        if (!store.getIsActive()) {
            throw new RuntimeException("La tienda no está disponible");
        }
        
        return convertToResponse(store);
    }
    
    private StoreResponse convertToResponse(Store store) {
        return new StoreResponse(
            store.getId(),
            store.getName(),
            store.getType().name(),
            store.getDescription(),
            store.getIsActive(),
            store.getCreatedAt()
        );
    }
}

