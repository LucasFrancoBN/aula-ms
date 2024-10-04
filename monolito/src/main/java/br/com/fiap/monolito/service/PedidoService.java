package br.com.fiap.monolito.service;

import br.com.fiap.monolito.model.ItemPedido;
import br.com.fiap.monolito.model.Pedido;
import br.com.fiap.monolito.repository.PedidoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {
  @Autowired
  private PedidoRepository pedidoRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper mapper = new ObjectMapper();

  public Pedido criarPedido(Pedido pedido) {
    boolean produtoDisponivel = verificarDisponibilidadeProdutos(pedido.getItensPedido());

    if(!produtoDisponivel) {
      throw new NoSuchElementException("Um ou mais produtos não estão disponivel");
    }

    double valorTotal = calcularValorTotal(pedido.getItensPedido());
    pedido.setValorTotal(valorTotal);

    atualizarEstoqueProdutos(pedido.getItensPedido());

    return pedidoRepository.save(pedido);
  }

  private void atualizarEstoqueProdutos(List<ItemPedido> itensPedido) {
    for(ItemPedido itemPedido : itensPedido) {
      Integer idProduto = itemPedido.getIdProduto();
      int quantidadeEstoque = itemPedido.getQuantidade();

      restTemplate.put(
          "http://localhost:8080/v2/api/produtos/atualizar-estoque/{produtoId}/{quantidade}",
          null,
          idProduto,
          quantidadeEstoque
      );
    }
  }

  private double calcularValorTotal(List<ItemPedido> itemPedido) {
    double valorTotal = 0.0;

    for (ItemPedido item : itemPedido) {
      Integer idProduto = item.getIdProduto();
      int quantidade = item.getQuantidade();

      ResponseEntity<String> response = restTemplate.getForEntity(
          "http://localhost:8080/v2/api/produtos/{produtoId}",
          String.class,
          idProduto
      );

      if(response.getStatusCode() == HttpStatus.OK) {
        try {

          JsonNode produtoJson = mapper.readTree(response.getBody());
          double preco = produtoJson.get("preco").asDouble();

          valorTotal += preco * quantidade;
        } catch (IOException e) {
          // TODO: fazer o tratamento depois
        }
      }
    }

    return valorTotal;
  }

  private boolean verificarDisponibilidadeProdutos(List<ItemPedido> itensPedido) {
    for (ItemPedido itemPedido : itensPedido) {
      Integer idProduto = itemPedido.getIdProduto();
      int quantidade = itemPedido.getQuantidade();

      ResponseEntity<String> response = restTemplate.getForEntity(
          "http://localhost:8080/v2/api/produtos/{produtoId}",
          String.class,
          idProduto
      );

      if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new RuntimeException("Produto não encontrado");
      }

      try {
        JsonNode produtoJson = mapper.readTree(response.getBody());

        int quantidadeProduto = produtoJson.get("quantidadeEstoque").asInt();

        if(quantidadeProduto < quantidade) {
          return false;
        }
      } catch (IOException e) {
        // TODO: tratar depois
      }
    }

    return true;
  }

}
