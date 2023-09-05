package coder.tp.facturacion.controller;

import java.util.ArrayList;
import java.util.List;

import coder.tp.facturacion.dto.ProductoDTO;
import coder.tp.facturacion.entidad.Producto;
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
        List<Producto> productos = productoService.findAll();
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            ProductoDTO productoDTO = convertToDto(producto);
            productosDTO.add(productoDTO);
        }

        if (productosDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(productosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> one(@PathVariable Integer id) {
        Producto producto = productoService.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToDto(producto));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> newEntity(@RequestBody ProductoDTO productoDTO) {
        Producto producto = convertToEntity(productoDTO);

        Producto productoGuardado = productoService.save(producto);

        ProductoDTO productoGuardadoDTO = convertToDto(productoGuardado);

        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardadoDTO);
    }


    private ProductoDTO convertToDto(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId_producto(producto.getId_producto());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setPrecio(producto.getPrecio());
        return productoDTO;
    }

    private Producto convertToEntity(ProductoDTO productoDTO) {
        if (productoDTO == null) {
            return null;
        }
        Producto producto = new Producto();
        producto.setId_producto(productoDTO.getId_producto());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        return producto;
    }
}
