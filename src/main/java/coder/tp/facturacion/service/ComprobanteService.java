package coder.tp.facturacion.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import coder.tp.facturacion.entidad.*;
import coder.tp.facturacion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    public List<Comprobante> findAll() {
        return this.comprobanteRepository.findAll();
    }

    @Transactional
    public Comprobante save(Comprobante comprobante) {

        Boolean existeCliente = existeCliente(comprobante.getCliente());

        Boolean existenProductos = existenProductos(comprobante.getItems());

        Boolean existeStock = existeStock(comprobante.getItems());

        if (existeCliente && existenProductos && existeStock) {

            // Asigno al cliente al Comprobante
            Integer clienteId = comprobante.getCliente().getId_cliente();
            Cliente clienteExistente = clienteRepository.getById(clienteId);

            comprobante.setCliente(clienteExistente);

            Comprobante nuevoComprobante = comprobanteRepository.save(comprobante);

            float totalComprobante = 0.0f;

            for (Item item : comprobante.getItems()) {
                // Asigno el comprobante al item
                item.setComprobante(nuevoComprobante);

                // Asocio el producto al item (Producto ya existente en la tabla de Productos, relación unidireccional)
                Integer productoId = item.getProducto().getId_producto();
                Producto productoExistente = productoRepository.getById(productoId);

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

                int nuevoStock = cantidadEnStock - cantidadVendida;
                stock.setCantidad(nuevoStock);
                stockRepository.save(stock);

                itemRepository.save(item);
            }

            // Fecha del comprobante
            Date fechaComprobante = obtenerFecha();
            nuevoComprobante.setFecha(fechaComprobante);


            // Asigna el precio_total del comprobante
            nuevoComprobante.setPrecio_total(totalComprobante);
            comprobanteRepository.save(nuevoComprobante);

            return nuevoComprobante;
        } else {
            return null;
        }
    }

    private Boolean existeStock(List<Item> items) {
        for (Item item : items) {
            Integer productoId = item.getProducto().getId_producto();
            Integer cantidadRequerida = item.getCantidad();

            // Busco el registro de Stock para el producto del comprobante
            Stock stock = stockRepository.findByProductoId(productoId);

            // Si no se encuentra el stock o la cantidad requerida es mayor que el stock disponible, retorna false
            if (stock == null || stock.getCantidad() == null || cantidadRequerida > stock.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    private Boolean existenProductos(List<Item> items) {
        for (Item item : items) {
            var idProducto = item.getProducto().getId_producto();
            var opProducto = this.productoRepository.findById(idProducto);
            if (opProducto.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Boolean existeCliente(Cliente cliente) {
        var opCliente = this.clienteRepository.findById(cliente.getId_cliente());
        return !opCliente.isEmpty();
    }

    private Date obtenerFecha() {
        // "2023-08-20T18:30Z"
        try {
            WorldClock worldClock = this.restTemplate.getForObject("http://worldclockapi.com/api/json/utc/now", WorldClock.class);
            String currentDateTime = worldClock.getCurrentDateTime();

            Date fecha = new SimpleDateFormat("yyyy-MM-dd'T'mm:ss'Z'").parse(currentDateTime);
            System.out.println("Fecha recuperada mediante API: " + fecha);
            return fecha;
        } catch (ParseException e) {
            e.printStackTrace();
            Date fecha = new Date();
            System.out.println("Fecha java: " + fecha);
            return fecha;
        }
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