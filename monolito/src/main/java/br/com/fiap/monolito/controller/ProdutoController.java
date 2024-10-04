package br.com.fiap.monolito.controller;

import br.com.fiap.monolito.model.Produto;
import br.com.fiap.monolito.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v2/api/produtos")
public class ProdutoController {
  @Autowired
  private ProdutoService produtoService;

  @GetMapping
  public ResponseEntity<List<Produto>> listarProduto() {
    return ResponseEntity.ok(produtoService.listarProduto());
  }

  @PostMapping
  public ResponseEntity<Produto> adicionarProduto(@RequestBody Produto produto) {
    Produto produtoSalvo = produtoService.salvarProduto(produto);
    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(produtoSalvo.getId())
        .toUri();

    return ResponseEntity.created(uri).body(produtoSalvo);
   }

   @GetMapping("/{id}")
  public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
    Produto produtoBuscado = produtoService.buscarProdutoPorId(id);

    if(produtoBuscado == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(produtoService.buscarProdutoPorId(id));
   }

   @PutMapping("/{id}")
  public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
      return ResponseEntity.ok(produtoService.atualizarProduto(id, produto));
   }

   @DeleteMapping("/{id}")
  public ResponseEntity<Void> removerProduto(@PathVariable Long id) {
    produtoService.excluirProduto(id);
    return ResponseEntity.noContent().build();
   }

  @PutMapping("/atualizar-estoque/{produtoId}/{quantidade}")
  public ResponseEntity<Produto> atualizarEstoque(@PathVariable Long produtoId, @PathVariable int quantidade) {
    return ResponseEntity.ok(produtoService.atualizarEstoque(produtoId, quantidade));

  }
}
