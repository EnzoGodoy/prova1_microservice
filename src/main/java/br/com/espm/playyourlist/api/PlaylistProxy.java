package br.com.espm.playyourlist.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.espm.playyourlist.playlist.PlaylistEntity;

@FeignClient(name = "playlists-service", url = "${app.self-url}")
public interface PlaylistProxy {

    @GetMapping("/playlists/{playlistid}")
    PlaylistEntity obterPorId(@PathVariable("playlistid") Integer playlistid);

    @PostMapping("/playlists/{playlistid}/musicas/{musicaId}")
    void adicionarMusica(@PathVariable("playlistid") Integer playlistid, @PathVariable("musicaId") Integer musicaId);
}
