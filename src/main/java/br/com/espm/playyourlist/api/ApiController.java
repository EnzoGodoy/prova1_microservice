package br.com.espm.playyourlist.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espm.playyourlist.musica.MusicaEntity;
import br.com.espm.playyourlist.playlist.PlaylistEntity;
import br.com.espm.playyourlist.reproducao.ReproducaoRequest;
import feign.FeignException;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final MusicaProxy musicaProxy;
    private final PlaylistProxy playlistProxy;
    private final ReproducaoProxy reproducaoProxy;

    public ApiController(MusicaProxy musicaProxy, PlaylistProxy playlistProxy, ReproducaoProxy reproducaoProxy) {
        this.musicaProxy = musicaProxy;
        this.playlistProxy = playlistProxy;
        this.reproducaoProxy = reproducaoProxy;
    }

    @PostMapping("/adicionar/{playlistId}/musicas/{musicaId}")
    public ResponseEntity<String> adicionar(@PathVariable Integer playlistId, @PathVariable Integer musicaId) {

        PlaylistEntity playlist;
        MusicaEntity musica;

        // 1) valida se a playlist existe (via OpenFeign -> microservico playlists)
        try {
            playlist = playlistProxy.obterPorId(playlistId);
        } catch (FeignException.NotFound e) {
            return new ResponseEntity<>("Playlist nao encontrada: " + playlistId, HttpStatus.NOT_FOUND);
        }

        // 2) valida se a musica existe (via OpenFeign -> microservico musicas)
        try {
            musica = musicaProxy.obterPorId(musicaId);
        } catch (FeignException.NotFound e) {
            return new ResponseEntity<>("Musica nao encontrada: " + musicaId, HttpStatus.NOT_FOUND);
        }

        // 3) associa a musica a playlist (via OpenFeign -> microservico playlists)
        playlistProxy.adicionarMusica(playlistId, musicaId);

        String mensagem = "Musica " + musica.getTitulo() + " adicionada com sucesso a playlist " + playlist.getNome();
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    @PutMapping("/executar/{playlistId}")
    public ResponseEntity<String> executar(@PathVariable Integer playlistId) {

        PlaylistEntity playlist;

        // valida se a playlist existe antes de registrar a execucao
        try {
            playlist = playlistProxy.obterPorId(playlistId);
        } catch (FeignException.NotFound e) {
            return new ResponseEntity<>("Playlist nao encontrada: " + playlistId, HttpStatus.NOT_FOUND);
        }

        // gera o registro de execucao (via OpenFeign -> microservico reproducao)
        ReproducaoRequest request = new ReproducaoRequest();
        request.setPlaylistId(playlistId);
        reproducaoProxy.registrar(request);

        String mensagem = "Playlist " + playlist.getNome() + " executada com sucesso";
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }
}
