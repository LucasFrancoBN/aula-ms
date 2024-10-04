package br.com.fiap.monolito.service;

import br.com.fiap.monolito.model.Produto;
import br.com.fiap.monolito.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
  @Autowired
  private ProdutoRepository produtoRepository;

  public List<Produto> listarProduto() {
    return produtoRepository.findAll();
  }

  public Produto salvarProduto(Produto produto) {
    return produtoRepository.save(produto);
  }

  public Produto buscarProdutoPorId(Long id) {
    return produtoRepository.findById(id).orElse(null);
  }

  public Produto atualizarProduto(Long id, Produto produto) {
    Produto produtoBuscado = buscarProdutoPorId(id);

    if(produtoBuscado == null) {
      throw new RuntimeException("Produto não encontrado");
    }

    produtoBuscado.setNome(produto.getNome());
    produtoBuscado.setDescricao(produto.getDescricao());
    produtoBuscado.setPreco(produto.getPreco());
    produtoBuscado.setPreco(produto.getPreco());
    produtoBuscado.setQuantidadeEstoque(produto.getQuantidadeEstoque());

    return produtoRepository.save(produtoBuscado);
  }

  public void excluirProduto(Long id) {
    Produto produtoBuscado = buscarProdutoPorId(id);

    if(produtoBuscado == null) {
      throw new RuntimeException("Produto não encontrado");
    }

    produtoRepository.deleteById(id);
  }

  public Produto atualizarEstoque(Long id, int quantidade) {
    Produto produtoBuscado = buscarProdutoPorId(id);

    if(produtoBuscado == null) {
      throw new IllegalArgumentException("Produto não encontrado");
    }

    produtoBuscado.setQuantidadeEstoque(produtoBuscado.getQuantidadeEstoque() - quantidade);

    return produtoRepository.save(produtoBuscado);
  }
}
