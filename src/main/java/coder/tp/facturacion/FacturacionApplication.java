package coder.tp.facturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FacturacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacturacionApplication.class, args);
		System.out.println("Aplicacion levantada");
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}




	/** dentro del run, debajo del main:

	@Autowired
	private DaoFactory daoFactory;

	public static void main(String[] args) {
		SpringApplication.run(FacturacionApplication.class, args);
		System.out.println("Aplicacion levantada");
	}

	@Override
	public void run(String... args) throws Exception {
		try {

			//##########################################################################
			//Se crea un Cliente
			Cliente cliente = new Cliente("Inna","Chorna", 12345678, "Av.Cabildo 210");

			//Se crean 2 productos
			Producto producto1 = new Producto( "Celular Motorola G3",78000);
			Producto producto2 = new Producto( "Funda celular Moto G3",5000);


			//##########################################################################
			//Se da de alte el cliente en la BD
			daoFactory.create(cliente);
			imprimirCliente(cliente, "Cliente Guardado: ");
			//Se dan de alta los productos en la BD
			daoFactory.create(producto1);
			daoFactory.create(producto2);
			//##########################################################################

			//Se crea stock para los productos 1 y 2
			Stock stock1 = new Stock(2000);
			Stock stock2 = new Stock(5000);
			stock1.setProducto(producto1);
			stock2.setProducto(producto2);

			//##########################################################################
			//Se da de alta el stock de productos
			daoFactory.create(stock1);
			daoFactory.create(stock2);
			//##########################################################################

			//##########################################################################
			//COMPRA: Se genera una compra por parte del cliente que consiste de 2 items, celulares y fundas.
			//##########################################################################
			//Se crea un Comprobante
			Comprobante comprobante = new Comprobante("2023-07-26");

			List<Comprobante> comprobantes = new ArrayList<Comprobante>();
			comprobantes.add(comprobante);
			cliente.setComprobantes(comprobantes);

			//Se crea el 1er Item
			Item item = new Item(producto1, 2);
			//Se crea el 2do Item
			Item item2 = new Item(producto2, 5);

			List<Item> items = new ArrayList<Item>();
			items.add(item);
			items.add(item2);
			comprobante.setItems(items);
			comprobante.setPrecio_total(item.getPrecio_total() + item2.getPrecio_total());
			comprobante.setCliente(cliente);

			item.setComprobante(comprobante);
			item2.setComprobante(comprobante);

			//##########################################################################
			//Se da de alta el comprobante junto con sus items en la BD
			daoFactory.create(comprobante);
			//##########################################################################


			//Consulto el cliente guardado con su comprobante
			Cliente clienteGuardado = daoFactory.getCliente(cliente);
			imprimirCliente(clienteGuardado, "Cliente con su comprobante: ");
			//##########################################################################

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	private void imprimirCliente(Cliente clienteGuardado, String s) {
		System.out.println("----------------------------");
		System.out.println(s + clienteGuardado.toString());
	}

	*/
}
