package coder.tp.facturacion.controller;

import coder.tp.facturacion.dto.ClienteDTO;
import coder.tp.facturacion.dto.ComprobanteDTO;
import coder.tp.facturacion.dto.ItemDTO;
import coder.tp.facturacion.entidad.Cliente;
import coder.tp.facturacion.entidad.Comprobante;
import coder.tp.facturacion.entidad.Item;
import coder.tp.facturacion.service.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ComprobanteDTO> findAll() {
        List<Comprobante> comprobantes = comprobanteService.findAll();
        List<ComprobanteDTO> comprobanteDTOs = new ArrayList<>();

        for (Comprobante comprobante : comprobantes) {
            comprobanteDTOs.add(convertToDTO(comprobante));
        }

        return comprobanteDTOs;
    }

    @GetMapping("/{id}")
    public ComprobanteDTO one(@PathVariable Integer id) {
        Comprobante comprobante = comprobanteService.findById(id);
        return convertToDTO(comprobante);
    }

    @PostMapping
    public ComprobanteDTO newEntity(@RequestBody Comprobante comprobante) {
        Comprobante nuevoComprobante = comprobanteService.save(comprobante);
        ComprobanteDTO comprobanteDTO = convertToDTO(nuevoComprobante);

        return comprobanteDTO;

/**
        Comprobante nuevoComprobante = comprobanteService.save(comprobante);
        return ResponseEntity.ok(nuevoComprobante);
*/
    }

    private ComprobanteDTO convertToDTO(Comprobante comprobante) {
        if (comprobante == null) {
            return null;
        }

        ComprobanteDTO dto = new ComprobanteDTO();
        dto.setId_comprobante(comprobante.getId_comprobante());
        dto.setFecha(comprobante.getFecha());
        dto.setPrecio_total(comprobante.getPrecio_total());

        ClienteDTO clienteDTO = new ClienteDTO();
        Cliente cliente = comprobante.getCliente();

        dto.setCliente(new ClienteDTO(cliente.getId_cliente(), cliente.getNombre(), cliente.getApellido(), cliente.getDni(), cliente.getDomicilio()));

        List<ItemDTO> itemDTOs = new ArrayList<>();
        for (Item item : comprobante.getItems()) {
            itemDTOs.add(new ItemDTO(item.getId_item(), item.getCantidad(), item.getPrecio_total()));
        }
        dto.setItems(itemDTOs);

        return dto;
    }

}
