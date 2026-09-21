package br.com.espm.playyourlist.reproducao;

import jakarta.validation.constraints.NotNull;

/**
 * Corpo esperado no POST /reproducao.
 * Exemplo: { "playlistId": 1 }
 */
public class ReproducaoRequest {

    @NotNull(message = "playlistId e obrigatorio")
    private Integer playlistId;

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }
}
