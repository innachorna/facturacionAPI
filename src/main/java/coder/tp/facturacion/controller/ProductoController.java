package coder.tp.facturacion.controller;

import java.util.List;
import coder.tp.facturacion.dto.ProductoDTO;
import coder.tp.facturacion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAll() {
        List<ProductoDTO> productosDTO = productoService.findAll();

        if (productosDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> one(@PathVariable Integer id) {
        ProductoDTO productoDTO = productoService.one(id);

        if (productoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productoDTO);
    }

    @PostMapping
    public ResponseEntity<?> newEntity(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoGuardadoDTO = productoService.newEntity(productoDTO);
        if (productoGuardadoDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Producto existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardadoDTO);
    }
}
