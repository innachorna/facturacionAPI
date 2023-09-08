package coder.tp.facturacion.service;

import java.util.ArrayList;
import java.util.List;
import coder.tp.facturacion.dto.ProductoDTO;
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

    public List<ProductoDTO> findAll() {
        List<Producto> productos = productoRepository.findAll();
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            ProductoDTO productoDTO = convertToDto(producto);
            productosDTO.add(productoDTO);
        }
        return productosDTO;
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public ProductoDTO newEntity(ProductoDTO productoDTO) {
        Producto producto = convertToEntity(productoDTO);

        // Verificar que el producto no exista en la base de datos
        String descripcion = producto.getDescripcion();
        if (productoRepository.existsByDescripcion(descripcion)) {
            return null;
        }

        Producto nuevoProducto = save(producto);
        // Inicialmente sin stock, cantidad en null
        generarStock(nuevoProducto);

        return convertToDto(nuevoProducto);
    }

    private void generarStock(Producto nuevoProducto) {
        Stock stock = new Stock();
        stock.setProducto(nuevoProducto);
        stockRepository.save(stock);
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

    public ProductoDTO one(Integer id) {
        Producto producto = findById(id);
        if (producto == null) {
            return null;
        }
        return convertToDto(producto);
    }

    private ProductoDTO convertToDto(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId_producto(producto.getId_producto());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setPrecio(producto.getPrecio());
        return productoDTO;
    }

    private Producto convertToEntity(ProductoDTO productoDTO) {
        if (productoDTO == null) {
            return null;
        }
        Producto producto = new Producto();
        producto.setId_producto(productoDTO.getId_producto());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        return producto;
    }
}

