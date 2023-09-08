package coder.tp.facturacion.controller;

import java.util.List;
import coder.tp.facturacion.dto.ClienteDTO;
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
        List<ClienteDTO> clientesDTO = clienteService.findAll();

        if (clientesDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clientesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> one(@PathVariable Integer id) {
        ClienteDTO clienteDTO = clienteService.one(id);

        if (clienteDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clienteDTO);
    }

    @PostMapping
    public ResponseEntity<?> newEntity(@RequestBody ClienteDTO clienteDTO) {
        ClienteDTO clienteGuardadoDTO = clienteService.newEntity(clienteDTO);

        if (clienteGuardadoDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardadoDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Integer id, @RequestBody ClienteDTO clienteAmodificarDTO) {
        ClienteDTO clienteDTO = clienteService.actualizarCliente(clienteAmodificarDTO, id);

        if (clienteDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clienteDTO);
    }
}

