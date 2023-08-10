package coder.tp.facturacion.repository;

import coder.tp.facturacion.entidad.Producto;
import coder.tp.facturacion.entidad.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findByProducto(Producto producto);
}
