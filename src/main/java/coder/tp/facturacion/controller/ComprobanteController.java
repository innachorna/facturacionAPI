package coder.tp.facturacion.controller;

import coder.tp.facturacion.dto.ComprobanteDTO;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comprobante")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    @GetMapping
    public ResponseEntity<List<ComprobanteDTO>> findAll() {
        List<ComprobanteDTO> comprobantesDTO = comprobanteService.findAll();

        if (comprobantesDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comprobantesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobanteDTO> one(@PathVariable Integer id) {
        ComprobanteDTO comprobanteDTO = comprobanteService.one(id);

        if (comprobanteDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comprobanteDTO);
    }

    @PostMapping
    public ResponseEntity<?> newEntity(@RequestBody ComprobanteDTO comprobanteDTO) {
        Map<String, Object> response = comprobanteService.newEntity(comprobanteDTO);

        if (response.get("comprobanteDTO") == null) {
            String mensaje = (String) response.get("mensaje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
        }

        ComprobanteDTO nuevoComprobanteDTO = (ComprobanteDTO) response.get("comprobanteDTO");
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComprobanteDTO);
    }
}
