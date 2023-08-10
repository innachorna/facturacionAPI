package coder.tp.facturacion.service;

import java.util.List;

import coder.tp.facturacion.entidad.Producto;
import coder.tp.facturacion.entidad.Stock;
import coder.tp.facturacion.repository.ProductoRepository;
import coder.tp.facturacion.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private StockRepository stockRepository;


    public List<Producto> findAll() {
        return this.productoRepository.findAll();
    }

    public Producto save(Producto producto) {
        Producto nuevoProducto = productoRepository.save(producto);

        Stock stock = new Stock();
        stock.setProducto(nuevoProducto);
        // Inicialmente sin stock, actualizar la cantidad con el metodo PATCH.
        stockRepository.save(stock);

        return nuevoProducto;
    }

    public Producto findById(Integer id) {

        var opProducto =  this.productoRepository.findById(id);
        if (opProducto.isPresent()) {
            return opProducto.get();
        } else {
            System.out.println("Producto inexistente");
            return null;
        }
    }
}

