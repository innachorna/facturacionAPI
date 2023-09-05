package coder.tp.facturacion.controller;

import coder.tp.facturacion.dto.ClienteDTO;
import coder.tp.facturacion.dto.ComprobanteDTO;
import coder.tp.facturacion.dto.ItemDTO;
import coder.tp.facturacion.dto.ProductoDTO;
import coder.tp.facturacion.entidad.Cliente;
import coder.tp.facturacion.entidad.Comprobante;
import coder.tp.facturacion.entidad.Item;
import coder.tp.facturacion.entidad.Producto;
import coder.tp.facturacion.service.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comprobante")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    @GetMapping
    public ResponseEntity<List<ComprobanteDTO>> findAll() {
        List<Comprobante> comprobantes = comprobanteService.findAll();
        List<ComprobanteDTO> comprobantesDTO = new ArrayList<>();

        for (Comprobante comprobante : comprobantes) {
            comprobantesDTO.add(convertToDTO(comprobante));
        }

        if (comprobantesDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(comprobantesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobanteDTO> one(@PathVariable Integer id) {
        Comprobante comprobante = comprobanteService.findById(id);

        if (comprobante == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToDTO(comprobante));
    }

    @PostMapping
    public ResponseEntity<ComprobanteDTO> newEntity(@RequestBody ComprobanteDTO comprobanteDTO) {
        Comprobante comprobante = convertToEntity(comprobanteDTO);

        Comprobante nuevoComprobante = comprobanteService.save(comprobante);
        ComprobanteDTO nuevoComprobanteDTO = convertToDTO(nuevoComprobante);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComprobanteDTO);
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
