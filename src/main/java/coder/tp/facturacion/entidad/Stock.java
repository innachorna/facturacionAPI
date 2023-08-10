package coder.tp.facturacion.entidad;


import jakarta.persistence.*;

@Entity
@Table(name="STOCK")
public class Stock {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_stock;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(name = "cantidad")
    private Integer cantidad;

    public Stock(Integer cantidad) {
        super();
        this.cantidad = cantidad;
    }

    public Stock() {
        super();
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getId_stock() {
        return id_stock;
    }

    public void setId_stock(Integer id_stock) {
        this.id_stock = id_stock;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id_stock=" + id_stock +
                ", stock_cantidad=" + cantidad +
                '}';
    }
}
