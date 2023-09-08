package coder.tp.facturacion.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import coder.tp.facturacion.dto.ClienteDTO;
import coder.tp.facturacion.dto.ComprobanteDTO;
import coder.tp.facturacion.dto.ItemDTO;
import coder.tp.facturacion.dto.ProductoDTO;
import coder.tp.facturacion.entidad.*;
import coder.tp.facturacion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
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

    public List<ComprobanteDTO> findAll() {
        List<Comprobante> comprobantes = comprobanteRepository.findAll();
        List<ComprobanteDTO> comprobantesDTO = new ArrayList<>();

        for (Comprobante comprobante : comprobantes) {
            comprobantesDTO.add(convertToDTO(comprobante));
        }
        return comprobantesDTO;
    }

    public Map<String, Object> newEntity(ComprobanteDTO comprobanteDTO) {
        Map<String, Object> response = new HashMap<>();
        Comprobante comprobante = convertToEntity(comprobanteDTO);

        Cliente cliente = comprobante.getCliente();
        Boolean existeCliente = existeCliente(cliente);
        Boolean existenProductos = existenProductos(comprobante.getItems());
        Boolean existeStock = existeStock(comprobante.getItems());

        if (existeCliente && existenProductos && existeStock) {

            // Asigno al cliente al Comprobante
            Integer clienteId = cliente.getId_cliente();
            Cliente clienteExistente = clienteRepository.getById(clienteId);

            comprobante.setCliente(clienteExistente);

            Comprobante nuevoComprobante = save(comprobante);

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

            // Asigna el precio_total al comprobante
            nuevoComprobante.setPrecio_total(totalComprobante);
            save(nuevoComprobante);

            ComprobanteDTO nuevoComprobanteDTO = convertToDTO(nuevoComprobante);
            response.put("mensaje", "Comprobante creado exitosamente");
            response.put("comprobanteDTO", nuevoComprobanteDTO);

            return response;
        } else {
            if (!existeCliente) {
                response.put("mensaje", "Cliente inexistente");
            } else if (!existenProductos) {
                response.put("mensaje", "Producto inexistente");
            } else if (!existeStock) {
                response.put("mensaje", "Stock insuficiente");
            }
            response.put("comprobanteDTO", null);

            return response;
        }
    }

    @Transactional
    public Comprobante save(Comprobante comprobante) {
        return comprobanteRepository.save(comprobante);
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
        } catch (HttpServerErrorException.ServiceUnavailable e) {
            e.printStackTrace();
            Date fecha = new Date();
            System.out.println("Fecha java EXC: " + fecha);
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

    public ComprobanteDTO one(Integer id) {
        Comprobante comprobante = findById(id);

        if (comprobante == null) {
            return null;
        }
        return convertToDTO(comprobante);
    }

    private ComprobanteDTO convertToDTO(Comprobante comprobante) {
        if (comprobante == null) {
            return null;
        }

        ComprobanteDTO dto = new ComprobanteDTO();
        dto.setId_comprobante(comprobante.getId_comprobante());
        dto.setFecha(comprobante.getFecha());
        dto.setPrecio_total(comprobante.getPrecio_total());

        Cliente cliente = comprobante.getCliente();
        if (cliente != null) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setIdCliente(cliente.getId_cliente());
            clienteDTO.setNombre(cliente.getNombre());
            clienteDTO.setApellido(cliente.getApellido());
            clienteDTO.setDni(cliente.getDni());
            clienteDTO.setDomicilio(cliente.getDomicilio());
            dto.setCliente(clienteDTO);
        }

        List<Item> items = comprobante.getItems();
        if (items != null) {
            List<ItemDTO> itemDTOs = new ArrayList<>();
            for (Item item : items) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setId_item(item.getId_item());
                itemDTO.setCantidad(item.getCantidad());
                itemDTO.setPrecioTotal(item.getPrecio_total());

                Producto producto = item.getProducto();
                if (producto != null) {
                    ProductoDTO productoDTO = new ProductoDTO();
                    productoDTO.setId_producto(producto.getId_producto());
                    productoDTO.setDescripcion(producto.getDescripcion());
                    productoDTO.setPrecio(producto.getPrecio());
                    itemDTO.setProducto(productoDTO);
                }

                itemDTOs.add(itemDTO);
            }
            dto.setItems(itemDTOs);
        }
        return dto;
    }

    private Comprobante convertToEntity(ComprobanteDTO comprobanteDTO) {
        if (comprobanteDTO == null) {
            return null;
        }

        Comprobante comprobante = new Comprobante();
        comprobante.setId_comprobante(comprobanteDTO.getId_comprobante());
        comprobante.setFecha(comprobanteDTO.getFecha());
        comprobante.setPrecio_total(comprobanteDTO.getPrecio_total());

        ClienteDTO clienteDTO = comprobanteDTO.getCliente();
        if (clienteDTO != null) {
            Cliente cliente = new Cliente();
            cliente.setId_cliente(clienteDTO.getIdCliente());
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setApellido(clienteDTO.getApellido());
            cliente.setDni(clienteDTO.getDni());
            cliente.setDomicilio(clienteDTO.getDomicilio());
            comprobante.setCliente(cliente);
        }

        List<ItemDTO> itemDTOs = comprobanteDTO.getItems();
        if (itemDTOs != null) {
            List<Item> items = new ArrayList<>();
            for (ItemDTO itemDTO : itemDTOs) {
                Item item = new Item();
                item.setId_item(itemDTO.getId_item());
                item.setCantidad(itemDTO.getCantidad());
                item.setPrecio_total(itemDTO.getPrecioTotal());

                ProductoDTO productoDTO = itemDTO.getProducto();
                if (productoDTO != null) {
                    Producto producto = new Producto();
                    producto.setId_producto(productoDTO.getId_producto());
                    producto.setDescripcion(productoDTO.getDescripcion());
                    producto.setPrecio(productoDTO.getPrecio());
                    item.setProducto(producto);
                }
                items.add(item);
            }
            comprobante.setItems(items);
        }
        return comprobante;
    }

}