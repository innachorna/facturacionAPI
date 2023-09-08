package coder.tp.facturacion.service;


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
import coder.tp.facturacion.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> findAll() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDTO> clientesDTO = new ArrayList<>();
        for (Cliente cliente : clientes) {
            clientesDTO.add(convertToDto(cliente));
        }
        return clientesDTO;
    }

    public Cliente findById(Integer id) {

        var opCliente =  this.clienteRepository.findById(id);
        if (opCliente.isPresent()) {
            System.out.println("Cliente Existe");
            return opCliente.get();
        } else {
            System.out.println("Cliente no existe");
            return null;
        }
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public ClienteDTO one(Integer id) {
        Cliente cliente = findById(id);
        if (cliente == null) {
            return null;
        }
        return convertToDto(cliente);
    }

    public ClienteDTO newEntity(ClienteDTO clienteDTO) {
        Cliente cliente = convertToEntity(clienteDTO);

        // Verificar que el cliente no exista en la base de datos
        Integer dni = cliente.getDni();
        if (clienteRepository.existsByDni(dni)) {
            return null;
        }

        Cliente nuevoCliente = save(cliente);
        return convertToDto(nuevoCliente);
    }

    public ClienteDTO actualizarCliente(ClienteDTO clienteAmodificarDTO, Integer id) {
        Cliente cliente = findById(id);

        // Verificar que el cliente exista en la base de datos
        if (cliente == null) {
            return null;
        }

        Cliente clienteAmodificar = convertToEntity(clienteAmodificarDTO);
        Cliente clienteModificado = modificarCliente(cliente, clienteAmodificar);
        Cliente clienteGuardado = save(clienteModificado);

        return convertToDto(clienteGuardado);
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

    private Cliente modificarCliente(Cliente cliente, Cliente clienteAmodificar) {
        cliente.setApellido(clienteAmodificar.getApellido());
        cliente.setNombre(clienteAmodificar.getNombre());
        cliente.setDni(clienteAmodificar.getDni());
        cliente.setDomicilio(clienteAmodificar.getDomicilio());
        return cliente;
    }
}
