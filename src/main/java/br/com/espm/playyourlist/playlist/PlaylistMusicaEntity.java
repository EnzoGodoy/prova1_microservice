package br.com.espm.playyourlist.playlist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "playlist_musicas")
public class PlaylistMusicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "playlistid", nullable = false)
    private Integer playlistId;

    @Column(name = "musicaid", nullable = false)
    private Integer musicaId;

    public PlaylistMusicaEntity() {
    }

    public PlaylistMusicaEntity(Integer playlistId, Integer musicaId) {
        this.playlistId = playlistId;
        this.musicaId = musicaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public Integer getMusicaId() {
        return musicaId;
    }

    public void setMusicaId(Integer musicaId) {
        this.musicaId = musicaId;
    }
}
