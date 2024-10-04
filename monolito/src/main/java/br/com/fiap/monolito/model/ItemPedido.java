package br.com.fiap.monolito.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemPedido {
  private Integer idProduto;
  private int quantidade;
}
