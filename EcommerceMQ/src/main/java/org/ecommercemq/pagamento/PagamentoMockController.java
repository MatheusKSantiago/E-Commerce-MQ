package org.ecommercemq.pagamento;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/pagamento")
@Tag(name = "Pagamento Mock API", description = "serve para simular um pagamento")
public class PagamentoMockController {

    @GetMapping()
    public boolean pagar() throws InterruptedException {
        Thread.sleep(3000);
        return ThreadLocalRandom.current().nextDouble() <= 0.6;
    }
}
