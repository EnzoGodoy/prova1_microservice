package br.com.espm.playyourlist.playlist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PlaylistMusicaRepository extends JpaRepository<PlaylistMusicaEntity, Integer> {

    List<PlaylistMusicaEntity> findByPlaylistId(Integer playlistId);

    Optional<PlaylistMusicaEntity> findByPlaylistIdAndMusicaId(Integer playlistId, Integer musicaId);

    @Transactional
    void deleteByPlaylistId(Integer playlistId);

    @Transactional
    void deleteByPlaylistIdAndMusicaId(Integer playlistId, Integer musicaId);
}
