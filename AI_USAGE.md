# AI_USAGE.md

Este arquivo documenta, de forma transparente, o uso de ferramentas de inteligência artificial
durante o desenvolvimento deste desafio, conforme solicitado pelo `CHALLENGE.md`.

A inteligência artificial foi utilizada como ferramenta de apoio técnico e otimização de tempo,
principalmente para identificar possíveis problemas, explicar o funcionamento do código,
orientar as correções e auxiliar na estruturação da documentação. Todas as alterações foram 
analisadas, testadas e revisadas pelos desenvolvedores antes de serem consideradas concluídas.

---

## Ferramentas e Modelos Utilizados

- **Claude (Anthropic) / Claude Sonnet:** Utilizado via web chat principalmente na investigação 
  do repositório, diagnósticos, geração de trechos de código (backend, frontend e testes), revisão 
  de correções e auxílio na criação de documentações (Issues, Pull Requests e este arquivo).
- **ChatGPT (OpenAI):** Utilizado como apoio complementar durante a compreensão/correção dos 
  problemas e para auxiliar na organização e redação final das documentações.

---

## Como a IA foi Utilizada (Fluxo Geral de Trabalho)

O fluxo de trabalho adotado pela equipe seguiu as seguintes etapas:

1. **Investigação:** A IA foi utilizada para analisar o código existente, confrontá-lo com o 
   `PROJECT.md` e identificar a causa raiz de comportamentos incorretos e bugs.
2. **Reprodução Local:** Os problemas apontados foram testados e reproduzidos localmente para 
   confirmar as falhas antes de qualquer alteração.
3. **Desenvolvimento e Sugestão:** Após a confirmação, os desenvolvedores analisaram as 
   sugestões de implementação, trechos de código e explicações fornecidas pela IA.
4. **Revisão Humana:** Todo o código sugerido foi lido e analisado linha a linha. Lógicas, nomes, 
   mensagens de erro e formatos de arquivo foram adaptados conforme as regras do projeto.
5. **Validação:** As correções foram testadas manualmente (executando o projeto no frontend e no 
   backend) e validadas através da suíte de testes automatizados e linters localmente.
6. **Documentação:** A IA auxiliou na estruturação e redação dos arquivos de suporte, relatos de 
   issues e textos de Pull Requests.

---

## Detalhamento por Issue / Contribuição

### Issue/PR #1 — Busca de atividades não funcional (maria-brito15 / Duda)

- **Investigação:** Utilização do Claude para diagnosticar a causa do não funcionamento da busca de 
  atividades no frontend e backend, validando o código contra o `PROJECT.md`.
- **Geração e Correção:** Criada nova consulta no `ActivityRepository` e ajustado o `ActivityService.list` 
  para busca case-insensitive por título e descrição. No frontend, corrigido o `activityService.ts` 
  para enviar o termo de busca via parâmetro do Axios e ajustado o `useActivities.ts` para incluir o 
  termo na chave do cache.
- **Documentação:** Geração e lapidação da documentação da issue (`docs/issue#1.md`).
- **Validação e Revisão:** A solução foi aceita após verificar que o tratamento de termos em branco 
  preservava a listagem completa e que o `.trim()` era tratado adequadamente. Testado manualmente e 
  via testes unitários (4 novos testes no `ActivityControllerTest.java`).

### Issue/PR #2 — Erro 404 sendo retornado como 500 (Rafael Torres Lemos)

- **Investigação:** Identificado via Claude que identificadores inexistentes disparavam `IllegalArgumentException`, 
  resultando em `500 Internal Server Error` em vez de `404 Not Found`.
- **Geração e Correção:** Criação da exceção customizada `ActivityNotFoundException`, novo handler no 
  `GlobalExceptionHandler` mapeando para `404`, e atualização do `requireActivity` no `ActivityService`. 
  Como achado correlato, o handler de `IllegalArgumentException` foi ajustado de `500` para `400`.
- **Documentação:** Elaboração da documentação (`issue2.md`), textos para abertura/fechamento da Issue no 
  GitHub e explicações sobre o fluxo de tratamento de erros.
- **Validação e Revisão:** O código foi revisado com atenção à propagação nos endpoints de inscrições. 
  Foram feitas rodadas de ajuste de tom nos textos para torná-los mais diretos. Suíte de testes, lint 
  e build foram validados localmente.

### Issue/PR #3 — API aceita inscrições além da capacidade da atividade (Gaby)

- **Investigação e Correção:** O comportamento foi identificado com auxílio do Claude e reproduzido no projeto. 
  A IA auxiliou no entendimento de onde a validação deveria ocorrer e na estruturação da resposta para 
  atividades lotadas ou encerradas.
- **Validação e Revisão:** As alterações foram revisadas e validadas via testes automatizados executados 
  localmente com Java 21 (`mvnw.cmd test`), obtendo `BUILD SUCCESS`.

### Issue/PR #11 — API permite múltiplas inscrições com o mesmo e-mail (Gaby)

- **Investigação e Correção:** Identificado e confirmado por testes. A IA atuou como apoio no desenho da 
  solução para impedir inscrições duplicadas do mesmo e-mail na mesma atividade, sem afetar pessoas 
  diferentes com nomes idênticos.
- **Validação e Revisão:** Implementação e testes revisados e validados localmente antes do commit.

### Contribuições e Investigações Gerais (Lucas)

- **Investigação:** Leitura cruzada do repositório com o `PROJECT.md` e identificação de falhas pontuais 
  a partir dos testes existentes.
- **Geração e Correção:** Auxílio em sugestões de código, correções e elaboração de testes para os arquivos:
  - `RegistrationService.java`
  - `GlobalExceptionHandler.java`
  - `ActivityService.java`
  - `ActivityRepository.java`
  - `activityService.ts`
  - `useActivities.ts`
- **Validação e Revisão:** Leitura e revisão da lógica sugerida pela IA, adaptação de nomes de variáveis 
  e mensagens de erro, seguidas da execução completa da suíte de testes.

---

## Principais Arquivos Influenciados

- **Backend:** `ActivityRepository.java`, `ActivityService.java`, `RegistrationService.java`, 
  `GlobalExceptionHandler.java`, `ActivityNotFoundException.java` e classes de teste (`ActivityControllerTest.java`).
- **Frontend:** `activityService.ts` e `useActivities.ts`.
- **Documentação:** `AI_USAGE.md`, `docs/issue#1.md`, `issue2.md` e descrições de Issues/PRs no GitHub.

---

## Declaração Final

O uso de ferramentas de inteligência artificial neste projeto teve o propósito exclusivo de atuar como 
suporte para investigação, aprendizado, resolução de bugs e otimização do fluxo de desenvolvimento.

A identificação definitiva dos problemas, a reprodução dos erros, as decisões finais de arquitetura 
e implementação, o refinamento do código, a execução dos testes e os commits foram inteiramente 
realizados e validados pelos desenvolvedores do projeto.