package br.com.espm.playyourlist.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.espm.playyourlist.reproducao.ReproducaoEntity;
import br.com.espm.playyourlist.reproducao.ReproducaoRequest;

@FeignClient(name = "reproducao-service", url = "${app.self-url}")
public interface ReproducaoProxy {

    @PostMapping("/reproducao")
    ReproducaoEntity registrar(@RequestBody ReproducaoRequest request);
}
