package br.com.fiap.mspedido.controller;

import br.com.fiap.mspedido.model.Pedido;
import br.com.fiap.mspedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
  @Autowired
  private PedidoService pedidoService;

  @PostMapping
  public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
    Pedido pedidoSalvo = pedidoService.criarPedido(pedido);
    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(pedidoSalvo.getId())
        .toUri();
    return ResponseEntity.created(uri).body(pedidoSalvo);
  }
}
