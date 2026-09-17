package com.cleuperumes.CleuPerfumes.Model.Service;

import com.cleuperumes.CleuPerfumes.Model.Entity.Compra;
import com.cleuperumes.CleuPerfumes.Model.Entity.Pagamento;
import com.cleuperumes.CleuPerfumes.Repository.CompraRepository;
import com.cleuperumes.CleuPerfumes.Repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private CompraRepository compraRepository;

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));
    }

    public Pagamento criarPagamento(Pagamento novoPagamento) {
        Compra compra = compraRepository.findById(novoPagamento.getCompra().getId())
                .orElseThrow(() -> new RuntimeException("Compra não encontrada para este pagamento."));

        // 1. Verificar o limite máximo de parcelas definido na compra (ex: se parcelas foi definido como 3 no cadastro)
        int limiteMaximoParcelas = (compra.getParcelas() != null) ? compra.getParcelas().intValue() : 1;
        int quantidadeAtualParcelas = compra.getPagamentos().size();

        // Se estivermos editando uma parcela existente, não contamos ela como nova
        if (novoPagamento.getId() != null) {
            quantidadeAtualParcelas -= 1; 
        }

        if (quantidadeAtualParcelas >= limiteMaximoParcelas) {
            throw new RuntimeException("Limite máximo de parcelas atingido! Esta compra permite no máximo " + limiteMaximoParcelas + " parcela(s).");
        }

        // 2. Soma os pagamentos já existentes na compra para validar o valor total
        BigDecimal totalJaPagoOuParcelado = compra.getPagamentos().stream()
                .map(Pagamento::getValorParcela)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (novoPagamento.getId() != null) {
            Pagamento pagamentoAntigo = buscarPorId(novoPagamento.getId());
            totalJaPagoOuParcelado = totalJaPagoOuParcelado.subtract(pagamentoAntigo.getValorParcela());
        }

        BigDecimal valorRestante = compra.getValorTotal().subtract(totalJaPagoOuParcelado);

        if (novoPagamento.getValorParcela().compareTo(valorRestante) > 0) {
            throw new RuntimeException("O valor da parcela ultrapassa o saldo devedor restante da compra (R$ " + valorRestante + ").");
        }

        if (novoPagamento.getNumeroParcela() == null) {
            int proximoNumero = compra.getPagamentos().size() + 1;
            novoPagamento.setNumeroParcela(proximoNumero);
        }

        novoPagamento.setCompra(compra);
        return pagamentoRepository.save(novoPagamento);
    }
    
    public Pagamento salvar(Pagamento pagamento) {
    return pagamentoRepository.save(pagamento);
    }
}