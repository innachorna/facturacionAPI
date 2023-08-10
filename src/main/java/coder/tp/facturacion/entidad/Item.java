package coder.tp.facturacion.entidad;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="ITEM")
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer id_item;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_total")
    private float precio_total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_comprobante")
    //@JsonIgnore           // Evita que se serialice la relación (solución al loop)
    private Comprobante comprobante;

    @ManyToOne()
    @JoinColumn(name="id_producto")
    //@JsonIgnore
    private Producto producto;

    public Item(Integer id_item, Integer cantidad, float precio_total) {
        this.id_item = id_item;
        this.cantidad = cantidad;
        this.precio_total = precio_total;
    }

    public Item(Producto producto, Integer cantidad) {
        super();
        this.producto = producto;
        this.cantidad = cantidad;
        //this.precio_total = cantidad * producto.getPrecio();
    }

    public Item() {
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

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public float getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(float precio_total) {
        this.precio_total = precio_total;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id_item=" + id_item +
                ", cantidad=" + cantidad +
                ", precio_total=" + precio_total +
                //", comprobante=" + comprobante +
                ", producto=" + producto +
                '}';
    }
}
