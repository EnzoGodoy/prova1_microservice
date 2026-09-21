package br.com.espm.playyourlist.musica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "musicas")
public class MusicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Titulo e obrigatorio")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Artista e obrigatorio")
    @Column(nullable = false)
    private String artista;

    @Size(max = 150, message = "Album deve ter no maximo 150 caracteres")
    private String album;

    @NotNull(message = "Duracao e obrigatoria")
    @Positive(message = "Duracao deve ser maior que zero")
    @Column(nullable = false)
    private Integer duracao;

    @Size(max = 50, message = "Genero deve ter no maximo 50 caracteres")
    private String genero;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
