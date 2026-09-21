package br.com.espm.playyourlist.reproducao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reproducao")
public class ReproducaoController {

    private final ReproducaoRepository reproducaoRepository;

    public ReproducaoController(ReproducaoRepository reproducaoRepository) {
        this.reproducaoRepository = reproducaoRepository;
    }

    @PostMapping
    public ResponseEntity<ReproducaoEntity> registrar(@Valid @RequestBody ReproducaoRequest request) {
        ReproducaoEntity reproducao = new ReproducaoEntity();
        reproducao.setPlaylistId(request.getPlaylistId());
        reproducao.setDataHora(LocalDateTime.now());

        ReproducaoEntity salva = reproducaoRepository.save(reproducao);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }

    @GetMapping("/{playlistid}")
    public ResponseEntity<List<ReproducaoEntity>> listarPorPlaylist(@PathVariable Integer playlistid) {
        return new ResponseEntity<>(reproducaoRepository.findByPlaylistId(playlistid), HttpStatus.OK);
    }

    @GetMapping("/total/{playlistid}")
    public ResponseEntity<Map<String, Object>> totalPorPlaylist(@PathVariable Integer playlistid) {
        long total = reproducaoRepository.countByPlaylistId(playlistid);
        return new ResponseEntity<>(Map.of("playlistId", playlistid, "totalReproducoes", total), HttpStatus.OK);
    }
}
