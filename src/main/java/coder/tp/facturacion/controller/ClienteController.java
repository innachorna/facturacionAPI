package coder.tp.facturacion.controller;

import java.util.ArrayList;
import java.util.List;

import coder.tp.facturacion.dto.ClienteDTO;
import coder.tp.facturacion.dto.ComprobanteDTO;
import coder.tp.facturacion.dto.ItemDTO;
import coder.tp.facturacion.dto.ProductoDTO;
import coder.tp.facturacion.entidad.Cliente;
import coder.tp.facturacion.entidad.Comprobante;
import coder.tp.facturacion.entidad.Item;
import coder.tp.facturacion.entidad.Producto;
import coder.tp.facturacion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> clientes = clienteService.findAll();
        List<ClienteDTO> clientesDTO = new ArrayList<>();

        for (Cliente cliente : clientes) {
            clientesDTO.add(convertToDto(cliente));
        }

        if (clientesDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(clientesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> one(@PathVariable Integer id) {
        Cliente cliente = clienteService.findById(id);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToDto(cliente));
    }

    @PostMapping
    public ResponseEntity<?> newEntity(@RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente nuevoCliente = clienteService.save(cliente);
        ClienteDTO clienteGuardadoDTO = convertToDto(nuevoCliente);

        if (clienteGuardadoDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente existente");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardadoDTO);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Integer id, @RequestBody ClienteDTO clienteAmodificarDTO) {
        Cliente cliente = clienteService.findById(id);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        Cliente clienteModificado = modificarCliente(cliente, clienteAmodificarDTO);

        Cliente clienteGuardado = clienteService.save(clienteModificado);

        return ResponseEntity.ok(convertToDto(clienteGuardado));
    }

    private Cliente modificarCliente(Cliente cliente, ClienteDTO clienteAmodificarDTO) {
        cliente.setApellido(clienteAmodificarDTO.getApellido());
        cliente.setNombre(clienteAmodificarDTO.getNombre());
        cliente.setDni(clienteAmodificarDTO.getDni());
        cliente.setDomicilio(clienteAmodificarDTO.getDomicilio());
        return cliente;
    }

    private ClienteDTO convertToDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        List<Comprobante> comprobantes = cliente.getComprobantes();
        List<ComprobanteDTO> comprobanteDTOs = new ArrayList<>();

        if (comprobantes != null) {
            for (Comprobante comprobante : comprobantes) {
                comprobanteDTOs.add(new ComprobanteDTO(comprobante.getId_comprobante(), comprobante.getFecha(), comprobante.getPrecio_total()));
            }
        }

        return new ClienteDTO(cliente.getId_cliente(), cliente.getNombre(), cliente.getApellido(), cliente.getDni(), cliente.getDomicilio(), comprobanteDTOs);
    }

    private Cliente convertToEntity(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setId_cliente(clienteDTO.getIdCliente());
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellido(clienteDTO.getApellido());
        cliente.setDni(clienteDTO.getDni());
        cliente.setDomicilio(clienteDTO.getDomicilio());

        List<ComprobanteDTO> comprobanteDTOs = clienteDTO.getComprobantes();
        if (comprobanteDTOs != null && !comprobanteDTOs.isEmpty()) {
            List<Comprobante> comprobantes = new ArrayList<>();
            for (ComprobanteDTO comprobanteDTO : comprobanteDTOs) {
                Comprobante comprobante = new Comprobante();
                comprobante.setId_comprobante(comprobanteDTO.getId_comprobante());
                comprobante.setFecha(comprobanteDTO.getFecha());
                comprobante.setPrecio_total(comprobanteDTO.getPrecio_total());
                comprobante.setCliente(cliente);

                List<ItemDTO> itemDTOs = comprobanteDTO.getItems();
                if (itemDTOs != null && !itemDTOs.isEmpty()) {
                    List<Item> items = new ArrayList<>();
                    for (ItemDTO itemDTO : itemDTOs) {
                        Item item = new Item();
                        item.setCantidad(itemDTO.getCantidad());
                        item.setPrecio_total(itemDTO.getPrecioTotal());
                        item.setComprobante(comprobante);

                        ProductoDTO productoDTO = itemDTO.getProducto();
                        if (productoDTO != null) {
                            Producto producto = new Producto();
                            producto.setDescripcion(productoDTO.getDescripcion());
                            producto.setPrecio(productoDTO.getPrecio());
                            // Asignamos el producto al item
                            item.setProducto(producto);
                        }
                        items.add(item);
                    }
                    comprobante.setItems(items);
                }
                comprobantes.add(comprobante);
            }
            cliente.setComprobantes(comprobantes);
        }
        return cliente;
    }
}

