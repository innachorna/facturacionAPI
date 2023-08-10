package coder.tp.facturacion.repository;

import coder.tp.facturacion.entidad.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteRepository  extends JpaRepository<Comprobante, Integer> {
}
