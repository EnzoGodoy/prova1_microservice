package br.com.espm.playyourlist.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.espm.playyourlist.musica.MusicaEntity;

@FeignClient(name = "musicas-service", url = "${app.self-url}")
public interface MusicaProxy {

    @GetMapping("/musicas/{id}")
    MusicaEntity obterPorId(@PathVariable("id") Integer id);
}
