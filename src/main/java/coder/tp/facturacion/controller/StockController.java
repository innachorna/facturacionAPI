package coder.tp.facturacion.controller;

import coder.tp.facturacion.dto.StockDTO;
import coder.tp.facturacion.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockDTO>> findAll() {
        List<StockDTO> stocksDTO = stockService.findAll();
        if (stocksDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stocksDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> one(@PathVariable Integer id) {
        StockDTO stockDTO = stockService.one(id);
        if (stockDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stockDTO);
    }

    @PostMapping
    public ResponseEntity<?> newEntity(@RequestBody StockDTO stockDTO) {
        StockDTO nuevoStockDTO = stockService.newEntity(stockDTO);
        if (nuevoStockDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoStockDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Integer id, @RequestBody StockDTO nuevoStockDTO) {

        StockDTO stockActualizadoDTO = stockService.actualizarStock(nuevoStockDTO, id);

        if (stockActualizadoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stockActualizadoDTO);
    }
}
