package coder.tp.facturacion.service;


import java.util.List;

import coder.tp.facturacion.entidad.Cliente;
import coder.tp.facturacion.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return this.clienteRepository.findAll();
    }

    public Cliente save(Cliente cliente) {
        return this.clienteRepository.save(cliente);
    }

    public Cliente findById(Integer id) {

        var opCliente =  this.clienteRepository.findById(id);
        if (opCliente.isPresent()) {
            System.out.println("Cliente Existe");
            return opCliente.get();
        } else {
            System.out.println("Cliente no existe");
            return null;
        }
    }
}
