package coder.tp.facturacion.dto;

import coder.tp.facturacion.entidad.Comprobante;

import java.util.List;

public class ClienteDTO {
    private Integer idCliente;
    private String nombre;
    private String apellido;
    private Integer dni;
    private String domicilio;

    private List<ComprobanteDTO> comprobantes;

    public ClienteDTO(Integer idCliente, String nombre, String apellido, Integer dni, String domicilio, List<ComprobanteDTO> comprobantes) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.domicilio = domicilio;
        this.comprobantes = comprobantes;
    }
    public ClienteDTO(Integer idCliente, String nombre, String apellido, Integer dni, String domicilio) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.domicilio = domicilio;
    }
    public ClienteDTO() {
        super();
    }

    // Getters y setters
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public List<ComprobanteDTO> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(List<ComprobanteDTO> comprobantes) {
        this.comprobantes = comprobantes;
    }
}
