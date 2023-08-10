package coder.tp.facturacion.entidad;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="COMPROBANTE")
public class Comprobante {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Integer id_comprobante;

    @Column(name = "fecha")
    private String fecha;

    @Column(name = "precio_total")
    private float precio_total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy="comprobante", cascade = CascadeType.ALL)
    private List<Item> items;

    public Comprobante(String fecha) {
        super();
        this.fecha = fecha;
    }

    public Comprobante(Integer id_comprobante, String fecha, float precio_total, Cliente cliente, List<Item> items) {
        this.id_comprobante = id_comprobante;
        this.fecha = fecha;
        this.precio_total = precio_total;
        this.cliente = cliente;
        this.items = items;
    }

    public Comprobante() {
        super();
    }

    public Integer getId_comprobante() {
        return id_comprobante;
    }

    public void setId_comprobante(Integer id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(float precio_total) {
        this.precio_total = precio_total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Comprobante{" +
                "id_comprobante=" + id_comprobante +
                ", fecha='" + fecha + '\'' +
                ", precio_total=" + precio_total +
                ", items=" + items +
                '}';
    }
}
