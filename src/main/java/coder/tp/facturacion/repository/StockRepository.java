package coder.tp.facturacion.repository;

import coder.tp.facturacion.entidad.Producto;
import coder.tp.facturacion.entidad.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findByProducto(Producto producto);

    // Busqueda de Stock por id_producto
    @Query("SELECT s FROM Stock s WHERE s.producto.id_producto = :productoId")
    Stock findByProductoId(@Param("productoId") Integer productoId);
}
