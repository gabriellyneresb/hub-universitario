# Documentação: Issue 1 - Busca de Atividades

**Issue relacionada:** Busca de atividades não é implementada
**Branch:** `feat/busca-atividades`

---

## O problema

O campo de busca existia na tela, mas não funcionava: o termo digitado nunca chegava a filtrar de verdade a lista de atividades, nem no frontend nem no backend. O `PROJECT.md` pede que a busca considere título e descrição, sem diferenciar maiúsculas de minúsculas, e que seja feita pela API, não só filtrando o que já está na tela.

---

## Arquivos e funções alterados

| Arquivo                       | Função alterada                   | Por quê                                                                                                                                                                                  |
| ----------------------------- | --------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `ActivityRepository.java`     | Nova função de busca (adicionada) | Era necessário um jeito de consultar o banco já filtrando por título ou descrição, em vez de trazer tudo e filtrar depois.                                                               |
| `ActivityService.java`        | `list`                            | Essa é a função que monta a listagem de atividades. Ela recebia o termo de busca, mas o ignorava. Precisava passar a usar a nova busca do repositório quando havia um termo digitado.    |
| `ActivityControllerTest.java` | 4 testes novos (adicionados)      | Para comprovar que a busca funciona nos cenários esperados (por título, por descrição, sem resultado, sem termo) e evitar que ela quebre no futuro sem ninguém perceber.                 |
| `activityService.ts`          | `getActivities`                   | É a função do frontend que chama a API para trazer as atividades. Ela recebia o termo digitado, mas nunca o enviava na chamada. Precisava passar a mandar esse termo de verdade.         |
| `useActivities.ts`            | `useActivities`                   | É o hook que a tela usa para buscar e guardar em cache os resultados. Sem incluir o termo de busca nessa lógica de cache, buscas diferentes acabariam se confundindo umas com as outras. |

---

## Backend

### O que foi implementado

A busca de atividades por título e descrição, direto no banco de dados.

### Como

Foi criada uma nova forma de consultar as atividades no banco, que já filtra pelo termo digitado, em vez de trazer tudo e filtrar depois em Java. O serviço que monta a listagem de atividades passou a decidir, na hora, se veio um termo de busca: se sim, usa essa nova consulta; se não veio nada (ou só espaço em branco), continua trazendo a lista completa como já fazia antes.

### Por quê

- **Fazer a busca no banco, e não em memória**, atende diretamente ao que o `PROJECT.md` pede, e continua funcionando bem mesmo se o catálogo de atividades crescer muito.
- **Tratar "campo em branco" como "sem busca"** evita um comportamento estranho: se alguém digitar só espaços e clicar em buscar, o esperado é ver tudo, não uma lista vazia.
- **Manter o comportamento antigo quando não há busca** garante que nada que já funcionava foi quebrado.

---

## Frontend

### O que foi implementado

O envio do termo digitado para a API, e o correto reaproveitamento dos resultados na tela.

### Como

A função que busca as atividades na API passou a de fato mandar o termo digitado junto com a requisição, antes esse termo era recebido mas descartado. Além disso, o mecanismo que guarda os resultados em cache (para a tela não recarregar tudo à toa) passou a considerar o termo de busca como parte do que identifica cada resultado guardado. Ou seja, uma busca por "workshop" e uma busca por "curso" agora são tratadas como coisas diferentes, e não uma sobrescrevendo a outra.

### Por quê

- **Enviar o termo para a API** é o que faltava para a busca funcionar de ponta a ponta. Sem isso, mesmo com o backend pronto, a busca continuaria não fazendo nada na prática.
- **Diferenciar os resultados por termo no cache** segue o mesmo padrão que o resto do projeto já usa (por exemplo, os detalhes de cada atividade também são guardados separadamente por id). Isso mantém a busca consistente com o resto da aplicação e evita comportamento inesperado ao alternar entre buscas.

---

## Testes

### O que foi implementado

Quatro novos testes cobrindo os cenários principais da busca.

### Como

Foram adicionados testes que simulam chamadas reais à API de busca, cobrindo: buscar por um termo que está no título, buscar por um termo que está na descrição, buscar um termo que não existe em nenhuma atividade, e buscar com o campo em branco.

### Por quê

Cada teste corresponde diretamente a um comportamento esperado da funcionalidade, então servem como prova de que a busca funciona como deveria, e também protegem contra alguém quebrar essa funcionalidade sem perceber em uma mudança futura.

---

## Resumo

| Onde                           | O que mudou                                          |
| ------------------------------ | ---------------------------------------------------- |
| Backend (consulta ao banco)    | Nova forma de buscar atividades por título/descrição |
| Backend (regra de negócio)     | Passou a usar essa busca quando há termo digitado    |
| Backend (testes)               | 4 testes novos cobrindo a busca                      |
| Frontend (chamada à API)       | Passou a enviar o termo digitado                     |
| Frontend (cache de resultados) | Passou a diferenciar resultados por termo buscado    |
