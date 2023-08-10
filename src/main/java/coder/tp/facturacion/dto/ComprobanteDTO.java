package coder.tp.facturacion.dto;

import java.util.List;

public class ComprobanteDTO {

    private Integer id_comprobante;
    private String fecha;
    private float precio_total;

    private ClienteDTO cliente;
    private List<ItemDTO> items;

    public ComprobanteDTO(Integer id_comprobante, String fecha, float precio_total) {
        this.id_comprobante = id_comprobante;
        this.fecha = fecha;
        this.precio_total = precio_total;
    }

    public ComprobanteDTO() {
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

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }
}
