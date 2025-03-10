package org.ecommercemq;

import org.ecommercemq.usuario.domain.model.TiposUsuario;
import org.ecommercemq.usuario.domain.model.Usuario;
import org.ecommercemq.usuario.domain.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class EcommerceMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceMqApplication.class, args);
    }

    @Profile("dev")
    @Bean
    public CommandLineRunner contaDevAdmin(UsuarioService usuarioService){
        return args ->{
          usuarioService.salvarUsuario(new Usuario("admin","admin@email.com","123", TiposUsuario.GERENTE));
        };
    }
}
