# Play Your List — MS-AV-01

Implementação da atividade **MS-AV-01-Endpoints**: um app de montagem de playlists
para o sistema HPWM, dividido conceitualmente em 4 microsserviços — `musicas`,
`playlists`, `reproducao` e `api` — todos dentro do **mesmo projeto Spring Boot**
(conforme pedido no enunciado, "para facilitar").

## Estrutura do projeto

Cada "microsserviço" vive em seu próprio pacote Java, com sua entidade,
repositório JPA e controller:

```
src/main/java/br/com/espm/playyourlist/
├── PlayYourListApplication.java   -> classe principal (@EnableFeignClients)
├── musica/                        -> microsserviço "musicas" (CRUD)
├── playlist/                      -> microsserviço "playlists"
├── reproducao/                    -> microsserviço "reproducao"
├── api/                           -> microsserviço "api" (orquestrador via OpenFeign)
└── exception/                     -> tratamento global de erros (404 / 400)
```

### Por que o `api` usa OpenFeign se está tudo no mesmo projeto?

O enunciado pede explicitamente que o orquestrador valide os recursos e
registre a execução **via OpenFeign**. Como todos os módulos rodam na mesma
aplicação/porta (`8080`), os `@FeignClient` do pacote `api` apontam para
`http://localhost:8080` (propriedade `app.self-url`) e chamam os endpoints dos
outros pacotes como se fossem serviços remotos — preservando a prática pedida
mesmo sem múltiplos deploys.

### Observação sobre o enunciado

O enunciado do `PUT /api/executar/{playlistId}` menciona registrar a execução
"pelo endpoint `POST /statistic`" — não existe esse endpoint na especificação;
o microsserviço de estatísticas de execução é o `reproducao` (`POST
/reproducao`). O `ReproducaoProxy` foi implementado apontando para
`POST /reproducao`, que é o endpoint real definido para esse recurso. Se a
intenção era outro nome, é só renomear o `@PostMapping` em
`ReproducaoProxy.java`.

## Como rodar

1. Abra a pasta no VS Code com a extensão **Spring Boot Extension Pack**
   instalada (mesma usada em aula).
2. Deixe o Maven baixar as dependências (precisa de internet — não incluídas
   aqui) e rode `PlayYourListApplication` pelo botão **Run** ou:
   ```bash
   ./mvnw spring-boot:run
   ```
   (ou `mvn spring-boot:run` se tiver o Maven instalado)
3. A aplicação sobe em `http://localhost:8080`.
4. Console do H2 (opcional, para ver os dados): `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:playyourlist`
   - Usuário: `sa` / Senha: `password`

Os dados de exemplo do enunciado (`musicas`, `playlists`, `playlist_musicas`,
`reproducoes`) já são carregados automaticamente pelo `data.sql`.

## Endpoints implementados

### musicas
| Método | Rota | Descrição |
|---|---|---|
| POST | `/musicas` | Cadastra música (valida título, artista, álbum, duração, gênero) |
| GET | `/musicas` | Lista todas |
| GET | `/musicas/{id}` | Busca por id (404 se não existir) |
| PUT | `/musicas/{id}` | Atualiza (corpo completo, com validação) |
| DELETE | `/musicas/{id}` | Exclui |

Exemplo de corpo (POST/PUT):
```json
{
  "titulo": "Hotel California",
  "artista": "Eagles",
  "album": "Hotel California",
  "duracao": 391,
  "genero": "Rock"
}
```

### playlists
| Método | Rota | Descrição |
|---|---|---|
| POST | `/playlists` | Cria playlist |
| GET | `/playlists` | Lista todas |
| GET | `/playlists/{playlistid}` | Busca por id |
| PUT | `/playlists/{playlistid}` | Atualiza nome/descrição |
| DELETE | `/playlists/{playlistid}` | Exclui a playlist e as músicas associadas |
| POST | `/playlists/{playlistid}/musicas/{musicaId}` | Associa música à playlist |
| DELETE | `/playlists/{playlistid}/musicas/{musicaId}` | Remove música da playlist |
| GET | `/playlists/{playlistid}/musicas` | Lista apenas os **ids** das músicas da playlist |

Exemplo de corpo (POST/PUT):
```json
{
  "nome": "Rock Anos 80",
  "descricao": "Sucessos do rock dos anos 80"
}
```

### reproducao
| Método | Rota | Descrição |
|---|---|---|
| POST | `/reproducao` | Cria registro de execução de uma playlist |
| GET | `/reproducao/{playlistid}` | Lista as execuções da playlist |
| GET | `/reproducao/total/{playlistid}` | Total de execuções da playlist |

Exemplo de corpo (POST):
```json
{ "playlistId": 1 }
```

### api (orquestrador — usa OpenFeign para chamar os módulos acima)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/adicionar/{playlistId}/musicas/{musicaId}` | Valida playlist e música (Feign), depois associa. Retorna a mensagem `Musica X adicionada com sucesso a playlist Y` |
| PUT | `/api/executar/{playlistId}` | Valida a playlist (Feign) e registra a execução via `reproducao` (Feign) |

## Validações aplicadas

- **Música**: título e artista obrigatórios (não vazios/só espaços), álbum
  opcional (máx. 150 caracteres), duração obrigatória e maior que zero,
  gênero opcional (máx. 50 caracteres).
- **Playlist**: nome obrigatório (não vazio/só espaços), descrição opcional
  (máx. 255 caracteres).
- Erros de validação retornam **400** com um mapa `{campo: mensagem}`.
- Recurso não encontrado retorna **404** com `{"erro": "..."}`.

## Testando com Thunder Client / curl

Exemplo rápido pelo curl:
```bash
curl -X POST http://localhost:8080/musicas \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Hotel California","artista":"Eagles","duracao":391,"genero":"Rock"}'

curl http://localhost:8080/playlists/1/musicas

curl -X POST http://localhost:8080/api/adicionar/1/musicas/2

curl -X PUT http://localhost:8080/api/executar/1
```

No Thunder Client (VS Code), crie uma requisição nova para cada linha da
tabela de endpoints acima, usando os corpos de exemplo indicados.

## Publicando no repositório git

O projeto já está pronto para ser versionado. No terminal, dentro da pasta:

```bash
git init
git add .
git commit -m "MS-AV-01 - Play Your List"
git branch -M main
git remote add origin <URL_DO_SEU_REPOSITORIO_NO_GITHUB>
git push -u origin main
```

Depois é só entregar o link do repositório conforme pedido no enunciado.
