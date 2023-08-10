package coder.tp.facturacion.service;

import java.util.List;

import coder.tp.facturacion.entidad.*;
import coder.tp.facturacion.exception.StockInsuficienteException;
import coder.tp.facturacion.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComprobanteService {

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<Comprobante> findAll() {
        return this.comprobanteRepository.findAll();
    }

    @Transactional
    public Comprobante save(Comprobante comprobante) {
        // Asigno al cliente al Comprobante
        Integer clienteId = comprobante.getCliente().getId_cliente();
        Cliente clienteExistente = clienteRepository.getById(clienteId);

        if (clienteExistente != null) {
            comprobante.setCliente(clienteExistente);
        } else {
            throw new EntityNotFoundException("No se encontró el cliente con ID: " + clienteId);
        }

        Comprobante nuevoComprobante = comprobanteRepository.save(comprobante);

        float totalComprobante = 0.0f;

        for (Item item : comprobante.getItems()) {
            // Asigno el comprobante al item
            item.setComprobante(nuevoComprobante);

            // Asocio el producto al item (Producto ya existente en la tabla de Productos, relación unidireccional)
            Integer productoId = item.getProducto().getId_producto();
            Producto productoExistente = productoRepository.getById(productoId);

            if (productoExistente != null) {
                item.setProducto(productoExistente);

                // Calcula el precio_total del item
                float precioUnitario = productoExistente.getPrecio();
                int cantidadVendida = item.getCantidad();
                float precioTotal = precioUnitario * cantidadVendida;
                item.setPrecio_total(precioTotal);

                // Acumulo el precio total de los items
                totalComprobante += precioTotal;

                // Actualizo el stock
                Stock stock = stockRepository.findByProducto(productoExistente);
                int cantidadEnStock = stock.getCantidad();

                if (stock == null || cantidadEnStock < cantidadVendida) {
                    throw new StockInsuficienteException("Stock insuficiente para el producto: " + productoExistente.getDescripcion());
                }

                int nuevoStock = cantidadEnStock - cantidadVendida;
                stock.setCantidad(nuevoStock);
                stockRepository.save(stock);

            } else {
                // Manejo de error si no se encuentra el producto por su ID
                throw new EntityNotFoundException("No se encontró el producto con ID: " + productoId);
            }

            itemRepository.save(item);
        }

        // Asigna el precio_total del comprobante
        nuevoComprobante.setPrecio_total(totalComprobante);
        comprobanteRepository.save(nuevoComprobante);

        return nuevoComprobante;
    }

    public Comprobante findById(Integer id) {

        var opComprobante =  this.comprobanteRepository.findById(id);
        if (opComprobante.isPresent()) {
            return opComprobante.get();
        } else {
            System.out.println("Comprobante no existe");
            return null;
        }
    }
}