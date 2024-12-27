package com.ysyagenda.repository;

import com.ysyagenda.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipo);
}