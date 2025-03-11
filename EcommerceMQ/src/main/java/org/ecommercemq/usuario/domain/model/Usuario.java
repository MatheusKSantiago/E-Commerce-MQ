package org.ecommercemq.usuario.domain.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String nome;
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;
    @Enumerated(value = EnumType.STRING)
    private TiposUsuario tipoUsuario;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, TiposUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        switch (tipoUsuario) {
            case CLIENTE:
                authorities.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));
                break;
            case GERENTE:
                authorities.add(new SimpleGrantedAuthority("ROLE_GERENTE"));
                break;
            case VENDEDOR:
                authorities.add(new SimpleGrantedAuthority("ROLE_VENDEDOR"));
                break;
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }



    public void setTipoUsuario(TiposUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public UUID getId() {
        return id;
    }

    public TiposUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}
