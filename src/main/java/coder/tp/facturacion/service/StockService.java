package coder.tp.facturacion.service;

import java.util.ArrayList;
import java.util.List;

import coder.tp.facturacion.dto.ProductoDTO;
import coder.tp.facturacion.dto.StockDTO;
import coder.tp.facturacion.entidad.Producto;
import coder.tp.facturacion.entidad.Stock;
import coder.tp.facturacion.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<StockDTO> findAll() {
        List<Stock> stocks = stockRepository.findAll();
        List<StockDTO> stocksDTO = new ArrayList<>();
        for (Stock stock : stocks) {
            stocksDTO.add(convertToDTO(stock));
        }
        return stocksDTO;
    }

    public Stock save(Stock stock) {
        return this.stockRepository.save(stock);
    }

    public StockDTO newEntity(StockDTO stockDTO) {
        Stock stock = convertToEntity(stockDTO);
        Stock nuevoStock = save(stock);

        return convertToDTO(nuevoStock);
    }

    public Stock findById(Integer id) {

        var opCliente = this.stockRepository.findById(id);
        if (opCliente.isPresent()) {
            return opCliente.get();
        } else {
            return null;
        }
    }

    public StockDTO one(Integer id) {
        Stock stock = findById(id);

        if (stock == null) {
            return null;
        }
        return this.convertToDTO(stock);
    }

    public StockDTO actualizarStock(StockDTO nuevoStockDTO, Integer id) {

        // Verificar si el registro existe en la table de Stock
        Boolean existeStock = existeStock(id);

        if (existeStock) {
            Stock stock = this.findById(id);
            Stock nuevoStock = convertToEntity(nuevoStockDTO);

            int cantDeStockASetear = nuevoStock.getCantidad();
            stock.setCantidad(cantDeStockASetear);

            Stock stockActualizado = save(stock);

            return convertToDTO(stockActualizado);
        }
        return null;
    }

    private Boolean existeStock(Integer id) {
        return this.stockRepository.existsById(id);
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

