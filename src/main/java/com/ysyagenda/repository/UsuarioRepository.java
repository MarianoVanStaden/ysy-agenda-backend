package com.ysyagenda.repository;

import com.ysyagenda.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndClinicaId(String email, Long clinicaId);
    List<Usuario> findByTipoUsuarioAndClinicaId(Usuario.TipoUsuario tipo, Long clinicaId);
    List<Usuario> findByClinicaId(Long clinicaId);
    Optional<Usuario> findByDniAndClinicaId(String dni, Long clinicaId);
}
