package pe.ucv.foodv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.ucv.foodv.model.entity.Store;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
    List<Store> findByIsActiveTrue();
    
    List<Store> findByTypeAndIsActiveTrue(Store.StoreType type);
    
    Optional<Store> findByName(String name);
}

