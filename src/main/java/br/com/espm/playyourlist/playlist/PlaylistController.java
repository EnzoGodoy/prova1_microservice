package br.com.espm.playyourlist.playlist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espm.playyourlist.exception.RecursoNaoEncontradoException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMusicaRepository playlistMusicaRepository;

    public PlaylistController(PlaylistRepository playlistRepository,
                               PlaylistMusicaRepository playlistMusicaRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistMusicaRepository = playlistMusicaRepository;
    }

    @PostMapping
    public ResponseEntity<PlaylistEntity> criar(@Valid @RequestBody PlaylistEntity playlist) {
        playlist.setId(null);
        PlaylistEntity salva = playlistRepository.save(playlist);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlaylistEntity>> listar() {
        return new ResponseEntity<>(playlistRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{playlistid}")
    public ResponseEntity<PlaylistEntity> buscarPorId(@PathVariable Integer playlistid) {
        PlaylistEntity playlist = playlistRepository.findById(playlistid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist nao encontrada: " + playlistid));
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @PutMapping("/{playlistid}")
    public ResponseEntity<PlaylistEntity> atualizar(@PathVariable Integer playlistid,
                                                      @Valid @RequestBody PlaylistEntity playlist) {
        PlaylistEntity existente = playlistRepository.findById(playlistid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist nao encontrada: " + playlistid));

        existente.setNome(playlist.getNome());
        existente.setDescricao(playlist.getDescricao());

        return new ResponseEntity<>(playlistRepository.save(existente), HttpStatus.OK);
    }

    @DeleteMapping("/{playlistid}")
    public ResponseEntity<Void> excluir(@PathVariable Integer playlistid) {
        PlaylistEntity existente = playlistRepository.findById(playlistid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist nao encontrada: " + playlistid));

        // remove primeiro as musicas associadas para nao violar a FK
        playlistMusicaRepository.deleteByPlaylistId(playlistid);
        playlistRepository.delete(existente);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{playlistid}/musicas/{musicaId}")
    public ResponseEntity<Void> adicionarMusica(@PathVariable Integer playlistid, @PathVariable Integer musicaId) {
        // garante que a playlist existe
        playlistRepository.findById(playlistid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist nao encontrada: " + playlistid));

        // evita duplicar a mesma musica na playlist
        if (playlistMusicaRepository.findByPlaylistIdAndMusicaId(playlistid, musicaId).isEmpty()) {
            playlistMusicaRepository.save(new PlaylistMusicaEntity(playlistid, musicaId));
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{playlistid}/musicas/{musicaId}")
    public ResponseEntity<Void> removerMusica(@PathVariable Integer playlistid, @PathVariable Integer musicaId) {
        playlistRepository.findById(playlistid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist nao encontrada: " + playlistid));

        playlistMusicaRepository.deleteByPlaylistIdAndMusicaId(playlistid, musicaId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{playlistid}/musicas")
    public ResponseEntity<List<Integer>> listarMusicas(@PathVariable Integer playlistid) {
        playlistRepository.findById(playlistid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist nao encontrada: " + playlistid));

        List<Integer> ids = playlistMusicaRepository.findByPlaylistId(playlistid).stream()
                .map(PlaylistMusicaEntity::getMusicaId)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }
}
