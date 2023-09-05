package coder.tp.facturacion.dto;

public class ItemDTO {
    private Integer id_item;
    private Integer cantidad;
    private float precioTotal;
    private ProductoDTO producto;

    public ItemDTO(Integer id_item, Integer cantidad, float precioTotal, ProductoDTO producto) {
        this.id_item = id_item;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.producto = producto;
    }

    public ItemDTO() {
        super();
    }

    public Integer getId_item() {
        return id_item;
    }

    public void setId_item(Integer id_item) {
        this.id_item = id_item;
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

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }
}