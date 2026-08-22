# Uso de Inteligência Artificial

Este documento declara o uso de ferramentas de IA durante a investigação e
resolução da Issue #1, conforme exigido pelo `CHALLENGE.md`.

## Ferramentas e modelos utilizados

- Claude (Anthropic), via chat, para investigação de código, comparação
  entre versões e revisão de correções.

## Em quais etapas a IA foi utilizada

- **Investigação**: comparação entre uma versão anterior do repositório
  (sem o fix) e a versão corrigida, para identificar exatamente o que
  havia mudado e onde o bug ainda persistia.
- **Diagnóstico**: identificação de que a documentação de uma correção
  anterior (`doc/issue#3.md`) descrevia uma mudança no frontend
  (`activityService.ts`) que na prática não existia no código — o
  arquivo continuava idêntico à versão com bug, exceto por
  terminadores de linha (CRLF).
- **Geração de código**: sugestão da correção em `getActivities()` para
  enviar o termo de busca como query param à API.
- **Revisão e validação**: execução de `eslint`, `tsc -b` (build) e
  `vitest run` sobre o frontend após a correção, para confirmar ausência
  de regressões. Tentativa de execução dos testes de backend
  (`./mvnw test`) não foi possível no ambiente usado pela IA por falta
  de acesso à internet; a lógica das queries foi revisada manualmente.
- **Documentação**: apoio na redação deste arquivo e de mensagens de
  commit no padrão Conventional Commits.

## Resumo dos principais prompts/objetivos solicitados

- "confirme a resolução da issue #3" e "mostre as diferenças entre o
  old e o new" — comparação de código entre versões.
- Validação da correção proposta (rodar testes, lint, build).
- Ajuda para nomear commits e organizar o fluxo (issue → fix → close).

## Sugestões aceitas, adaptadas ou rejeitadas

- **Aceita**: a correção de `getActivities()` para enviar `search` como
  query param, condicionada a `search` não vazio.
- **Aceita**: manter a `queryKey` do TanStack Query em `useActivities.ts`
  incluindo o termo de busca (já presente na versão investigada,
  confirmada como correta).
- **Adaptada**: nomes de commits inicialmente sugeridos como `fix(...)`
  foram discutidos (alternativa `feat`) e mantidos como `fix`, por se
  tratar de correção de um bug relatado em Issue, não de funcionalidade
  nova.

## Arquivos ou partes da solução influenciados

- `apps/frontend/src/services/activityService.ts` (correção aplicada)
- `apps/backend/src/main/java/br/edu/hub/repository/ActivityRepository.java` (revisão)
- `apps/backend/src/main/java/br/edu/hub/service/ActivityService.java` (revisão)
- `apps/backend/src/test/java/br/edu/hub/ActivityControllerTest.java` (revisão)
- `apps/frontend/src/hooks/useActivities.ts` (revisão)
- `doc/issue#3.md` (revisão de precisão do conteúdo)

## Como o participante revisou e validou o resultado

- Comparação linha a linha (`diff`) entre a versão com bug e a versão
  corrigida, normalizando terminadores de linha (CRLF/LF) para evitar
  falsos positivos de diferença.
- Execução real de `eslint`, `tsc -b` e `vitest run` no frontend após a
  correção; único teste falho (`activity.test.ts`) confirmado como
  pré-existente e não relacionado à busca, ao rodar a mesma suíte na
  versão anterior ao fix.
- Leitura manual da query derivada do Spring Data JPA
  (`findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByDateDesc`)
  e da lógica condicional em `ActivityService.list()` para confirmar
  aderência aos critérios do `PROJECT.md` (busca por título e
  descrição, case-insensitive, feita pela API).
