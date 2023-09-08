package coder.tp.facturacion.controller;

import coder.tp.facturacion.dto.ProductoDTO;
import coder.tp.facturacion.dto.StockDTO;
import coder.tp.facturacion.entidad.Producto;
import coder.tp.facturacion.entidad.Stock;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockDTO>> findAll() {
        List<Stock> stocks = stockService.findAll();
        List<StockDTO> stocksDTO = new ArrayList<>();

        for (Stock stock : stocks) {
            stocksDTO.add(convertToDTO(stock));
        }

        if (stocksDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(stocksDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> one(@PathVariable Integer id) {
        Stock stock = stockService.findById(id);

        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToDTO(stock));
    }

    @PostMapping
    public ResponseEntity<StockDTO> newEntity(@RequestBody StockDTO stockDTO) {
        Stock stock = convertToEntity(stockDTO);

        Stock nuevoStock = stockService.save(stock);
        StockDTO nuevoStockDTO = convertToDTO(nuevoStock);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoStockDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Integer id, @RequestBody StockDTO nuevoStockDTO) {

        Stock nuevoStock = convertToEntity(nuevoStockDTO);
        Stock stockActualizado = stockService.actualizarStock(nuevoStock, id);
        StockDTO stockActualizadoDTO = convertToDTO(stockActualizado);

        if (stockActualizadoDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(stockActualizadoDTO);
        }
    }

    private StockDTO convertToDTO(Stock stock) {
        if (stock == null) {
            return null;
        }

        Producto producto = stock.getProducto();
        ProductoDTO productoDTO = new ProductoDTO(producto.getId_producto(), producto.getDescripcion(), producto.getPrecio());

        StockDTO stockDTO = new StockDTO();
        stockDTO.setId_stock(stock.getId_stock());
        stockDTO.setCantidad(stock.getCantidad());
        stockDTO.setProducto(productoDTO);

        return stockDTO;
    }

    private Stock convertToEntity(StockDTO stockDTO) {
        if (stockDTO == null) {
            return null;
        }

        Stock stock = new Stock();
        stock.setId_stock(stockDTO.getId_stock());
        stock.setCantidad(stockDTO.getCantidad());

        ProductoDTO productoDTO = stockDTO.getProducto();

        if (productoDTO != null) {
            Producto producto = new Producto();
            producto.setId_producto(productoDTO.getId_producto());
            producto.setDescripcion(productoDTO.getDescripcion());
            producto.setPrecio(productoDTO.getPrecio());

            stock.setProducto(producto);
        }
        return stock;
    }
}
