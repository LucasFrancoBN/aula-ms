package br.com.fiap.monolito.repository;

import br.com.fiap.monolito.model.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
}
