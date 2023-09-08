package coder.tp.facturacion.service;

import java.util.List;
import coder.tp.facturacion.entidad.Stock;
import coder.tp.facturacion.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> findAll() {
        return this.stockRepository.findAll();
    }

    public Stock save(Stock stock) {
        return this.stockRepository.save(stock);
    }

    public Stock findById(Integer id) {

        var opCliente = this.stockRepository.findById(id);
        if (opCliente.isPresent()) {
            return opCliente.get();
        } else {
            return null;
        }
    }

    public Stock actualizarStock(Stock nuevoStock, Integer id) {

        // Verificar si el registro existe en la table de Stock
        Boolean existeStock = existeStock(id);

        if (existeStock) {
            Stock stock = this.findById(id);

            int cantDeStockASetear = nuevoStock.getCantidad();
            stock.setCantidad(cantDeStockASetear);

            Stock stockActualizado = save(stock);
            //StockDTO stockActualizadoDTO = convertToDTO(stockActualizado);

            return stockActualizado;
        } else {
            return null;
        }
    }

    private Boolean existeStock(Integer id) {
        return this.stockRepository.existsById(id);
    }
}

