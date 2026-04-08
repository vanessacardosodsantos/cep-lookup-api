# CEP Lookup API

API REST para consulta de endereços por CEP com cache local usando Caffeine.

## Tecnologias

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Cache** — abstração de cache do Spring
- **Caffeine** — implementação de cache local em memória
- **RestClient** — cliente HTTP nativo do Spring para consumir a API ViaCEP
- **Lombok** 
- **Gradle**

## O que é Cache e por que usamos aqui

CEPs raramente mudam de endereço. Sem cache, cada requisição para `/cep/{cep}` faria
uma chamada HTTP para a API externa do ViaCEP — adicionando latência e dependência de
disponibilidade externa em toda requisição.

Com Caffeine, o resultado da primeira consulta é armazenado em memória. As consultas
seguintes para o mesmo CEP são respondidas localmente em menos de 1ms, sem nenhuma
chamada à ViaCEP.

```
Primeira requisição:
  GET /cep/01310100 → ViaCEP (chamada HTTP real ~200ms)

Requisições seguintes:
  GET /cep/01310100 → Cache local (~1ms)
```

## Configuração do Cache

```
Implementação : Caffeine (cache local em memória)
Tamanho máximo: 500 CEPs simultâneos em memória
TTL           : 24 horas após escrita
Métricas      : habilitadas via recordStats()
```

Quando o cache atinge 500 entradas, o Caffeine remove automaticamente
os CEPs menos acessados (política LRU) para abrir espaço para novos.

Quando o TTL de 24 horas expira, o dado é descartado. Na próxima requisição
para aquele CEP, uma nova chamada à ViaCEP é feita e o cache é renovado.

## Endpoints

### Consultar endereço por CEP

```
GET /cep/{cep}
```

Aceita CEP com ou sem formatação: `01310-100` e `01310100` são equivalentes.

**Resposta de sucesso — 200 OK**
```json
{
  "cep": "01310-100",
  "logradouro": "Avenida Paulista",
  "complemento": "de 1 a 610 - lado par",
  "bairro": "Bela Vista",
  "cidade": "São Paulo",
  "estado": "SP",
  "codigoIbge": "3550308",
  "fromCache": false
}
```

O campo `fromCache` indica a origem do dado:
- `false` — dado buscado na ViaCEP (Cache Miss)
- `true` — dado servido do cache local (Cache Hit)


### Métricas do cache

```
GET /cache/stats
```

**Resposta**
```json
{
  "enderecos": {
    "hits": 42,
    "misses": 8,
    "hitRate": "84.0%",
    "tamanhoAtual": 8,
    "evictions": 0
  }
}
```

| Campo | Descrição |
|---|---|
| `hits` | Requisições respondidas pelo cache sem chamar a ViaCEP |
| `misses` | Requisições que foram à ViaCEP porque o CEP não estava no cache |
| `hitRate` | Percentual de requisições servidas do cache. `N/A` quando não houve requisições ainda |
| `tamanhoAtual` | Quantidade de CEPs armazenados em memória no momento |
| `evictions` | Entradas removidas por TTL expirado ou limite de tamanho atingido |

### Limpar cache

```
GET /cache/clear
```

Remove todas as entradas do cache. A próxima requisição para qualquer CEP
será um Cache Miss e irá à ViaCEP.

## Como executar

```bash
./gradlew bootRun
```

A aplicação sobe em `http://localhost:8080`.

## Fonte de dados

[ViaCEP](https://viacep.com.br) — API pública e gratuita de consulta de CEPs brasileiros.
