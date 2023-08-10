package coder.tp.facturacion.controller;

import coder.tp.facturacion.entidad.Stock;
import coder.tp.facturacion.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Stock> findAll() {
        return this.stockService.findAll();
    }

    @GetMapping("/{id}")
    public Stock one(@PathVariable Integer id) {
        return this.stockService.findById(id);
    }

    @PostMapping
    public Stock newEntity(@RequestBody Stock stock) {
        return this.stockService.save(stock);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Integer id, @RequestBody Stock nuevoStock) {
        Stock stock = stockService.findById(id);

        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        if (nuevoStock.getCantidad() != null) {
            stock.setCantidad(nuevoStock.getCantidad());
        }

        Stock stockActualizado = stockService.save(stock);

        return ResponseEntity.ok(stockActualizado);
    }
}
