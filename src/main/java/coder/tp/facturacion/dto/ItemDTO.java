package coder.tp.facturacion.dto;

public class ItemDTO {
    private Integer idItem;
    private Integer cantidad;
    private float precioTotal;
    private Integer idProducto;

    public ItemDTO(Integer idItem, Integer cantidad, float precioTotal) {
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }

    public ItemDTO(Integer idItem, Integer cantidad, float precioTotal, Integer idProducto) {
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.idProducto = idProducto;
    }

    // Getters y setters
    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
}