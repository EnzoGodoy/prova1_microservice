package br.com.espm.playyourlist.reproducao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReproducaoRepository extends JpaRepository<ReproducaoEntity, Integer> {

    List<ReproducaoEntity> findByPlaylistId(Integer playlistId);

    long countByPlaylistId(Integer playlistId);
}
