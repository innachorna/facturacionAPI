package coder.tp.facturacion.controller;

import java.util.ArrayList;
import java.util.List;

import coder.tp.facturacion.dto.ClienteDTO;
import coder.tp.facturacion.dto.ComprobanteDTO;
import coder.tp.facturacion.entidad.Cliente;
import coder.tp.facturacion.entidad.Comprobante;
import coder.tp.facturacion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> findAll() {

        List<Cliente> clientes = clienteService.findAll();
        List<ClienteDTO> clienteDTOs = new ArrayList<>();

        for (Cliente cliente : clientes) {
            clienteDTOs.add(convertToDto(cliente));
        }

        return clienteDTOs;
    }

    @GetMapping("/{id}")
    public ClienteDTO one(@PathVariable Integer id) {
        Cliente cliente = clienteService.findById(id);
        return convertToDto(cliente);
    }

    @PostMapping
    public Cliente newEntity(@RequestBody Cliente cliente) {
        return this.clienteService.save(cliente);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Integer id, @RequestBody Cliente clienteAmodificar) {
        Cliente cliente = clienteService.findById(id);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        modificarCliente(cliente, clienteAmodificar);

        Cliente clienteModificado = clienteService.save(cliente);

        return ResponseEntity.ok(convertToDto(clienteModificado));
    }

    private Cliente modificarCliente(Cliente cliente, Cliente clienteAmodificar) {
        cliente.setApellido(clienteAmodificar.getApellido());
        cliente.setNombre(clienteAmodificar.getNombre());
        cliente.setDni(clienteAmodificar.getDni());
        cliente.setDomicilio(clienteAmodificar.getDomicilio());
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
}

