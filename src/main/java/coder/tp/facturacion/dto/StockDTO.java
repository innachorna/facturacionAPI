package coder.tp.facturacion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StockDTO {

    private Integer id_stock;
    private Integer cantidad;
    private ProductoDTO producto;

    public StockDTO(Integer id_stock, Integer cantidad, ProductoDTO producto) {
        this.id_stock = id_stock;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public StockDTO() {
    }

    public Integer getId_stock() {
        return id_stock;
    }

    public void setId_stock(Integer id_stock) {
        this.id_stock = id_stock;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }
}