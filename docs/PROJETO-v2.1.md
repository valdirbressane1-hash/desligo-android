# Desligo — Documento do Projeto v2.1

**"Liga o que importa. Desliga o resto."**

*Atualizado em 2026-09-06*
*Incorporando análise técnica de riscos e melhorias*

---

## 1. Visão Geral

**Desligo** é um aplicativo Android de bem-estar digital que permite ao usuário bloquear seletivamente o acesso a apps, chamadas e notificações com base em horários, perfis e contexto — usando VPN local (sem root).

---

## 2. Roadmap

### v1.0 — MVP (Meses 1-4)

- [ ] VPN local para filtrar tráfego por app (baseado no padrão NetGuard)
- [ ] Perfis pré-definidos (Estudo, Sono, Trabalho, Livre)
- [ ] Bloqueio de apps por perfil
- [ ] Timer / Pomodoro integrado
- [ ] Interface simples (1 toque para ativar perfil)
- [ ] Guia de configuração por fabricante (Xiaomi, Samsung, Huawei, OnePlus, Motorola)
- [ ] Recuperação de PIN via verificação Google Sign-In (sem armazenar dados de conta)
- [ ] Publicação: GitHub Releases + F-Droid (build sem dependências proprietárias) + APKPure
- [ ] Firebase Analytics + Crashlytics (apenas na build de distribuição alternativa)
- [ ] Beta aberto para feedback
- [ ] Ajustes baseados no feedback do beta

### v2.0 — Premium (Meses 5-6)

- [ ] Modo Gaming (1 toque)
- [ ] Reativar Agendamento como recurso premium
- [ ] Reativar Bloqueio de chamadas como recurso premium
- [ ] Tema escuro
- [ ] Exportar/Importar configurações
- [ ] Modo Foco completo
- [ ] Perfis ilimitados (criar perfis customizados)
- [ ] Integração com Google Play Billing
- [ ] Play Store (quando houver retorno)

### v3.0 — Crescimento (Meses 7-9)

- [ ] Widget / Atalho de tela
- [ ] Modo automático por app aberto
- [ ] Bloqueio por localização (premium)

---

## 3. Stack Técnico

- **Linguagem:** Kotlin
- **UI:** Jetpack Compose
- **Filtragem de tráfego:** VpnService (local, sem root)
- **Bloqueio de chamadas:** CallScreeningService
- **Persistência:** Room (SQLite)
- **Analytics:** Firebase Analytics + Crashlytics
- **Monetização:** Google Play Billing (Play Store) / gateway alternativo (Pix/Stripe) para builds fora da Play Store
- **Build:** Gradle, Android Studio

---

## 4. Análise de Pontos Fortes

### 4.1 Escolha técnica correta e comprovada
Usar `VpnService` local para filtrar tráfego por app é exatamente a abordagem do NetGuard (open source, maduro, sem root) — copiando o padrão que já funciona, não reinventando algo arriscado.

### 4.2 Modelo de trial bem desenhado
Dar 15 dias com *tudo* liberado (incluindo bloqueio de chamadas e agendamento) e depois retrair é psicologicamente mais forte que o freemium tradicional "sempre limitado" — a pessoa sente a perda de algo que já tinha, não apenas a ausência de algo que nunca teve. Converte melhor.

### 4.3 Atenção aos fabricantes chineses/agressivos com background
A tabela de guias por fabricante (Xiaomi, Samsung, Huawei, OnePlus, Motorola) ataca o principal motivo pelo qual apps desse tipo recebem reviews 1 estrela ("parou de funcionar sozinho"). Poucos concorrentes fazem isso de forma proativa e automática.

### 4.4 Postura de privacidade limpa
"VPN local, zero coleta de dados pessoais, zero envio a servidores externos" — bom para o usuário e facilita a justificativa da permissão sensível de VPN no formulário de segurança do Google Play.

### 4.5 Documento excepcionalmente completo
Cobre riscos técnicos, contingência, testes, analytics, manutenção trimestral. Raro em planos de app solo — vai poupar decisões no meio do desenvolvimento.

---

## 5. Riscos e Mitigações (Revisado)

### 5.1 ⚠️ CallScreeningService — Exclusividade do sistema
**Problema:** O Android só permite **um único app** como "app de triagem de chamadas padrão" por vez. Se o usuário já usa Google Telefone (proteção contra spam nativa) ou Truecaller, ativar o Desligo como bloqueador de chamadas exige trocar esse padrão — fricção de UX grande e potencial confusão.

**Mitigações:**
- Tela de onboarding clara explicando a troca, com botão para reverter facilmente
- Documentar no guia que o usuário pode voltar ao app original a qualquer momento
- Considerar tornar o bloqueio de chamadas **opcional** (não ativado por padrão)
- Testar fluxo de troca em 5+ dispositivos reais

**Classificação:** Risco ALTO (antes: médio)

### 5.2 ⚠️ Inconsistência distribuição × monetização
**Problema:** Google Play Billing só funciona dentro da Play Store. Na Fase 1 (GitHub + F-Droid + APKPure), não existe mecanismo de pagamento para o Premium.

**Mitigações:**
- **Opção A:** Implementar gateway alternativo (Pix, Stripe) para builds fora da Play Store
- **Opção B:** Na Fase 1, liberar tudo como trial de 15 dias sem cobrança real — focar em distribuição e feedback
- **Opção C:** Usar chave de licença manual (código enviado por email após pagamento Pix)
- Definir qual opção antes de começar a implementar monetização

**Classificação:** Risco ALTO (antes: não endereçado)

### 5.3 ⚠️ F-Droid pode rejeitar dependências proprietárias
**Problema:** F-Droid exige código 100% aberto e builds reprodutíveis, sem SDKs proprietários. Firebase Analytics/Crashlytics e Google Play Billing são bibliotecas proprietárias do Google.

**Mitigações:**
- **Criar duas variantes de build:** uma "F-Droid" sem dependências proprietárias, outra "Play Store" com tudo
- Usar `build flavors` do Gradle para isolar dependências
- Na variante F-Droid: usar analytics alternativos (Matomo self-hosted, ou nenhum)
- Antes de subir ao F-Droid, rodar `fdroid lint` local para verificar conformidade

**Classificação:** Risco MÉDIO (antes: não endereçado)

### 5.4 ⚠️ Bateria — Meta <5%/dia é aspiracional
**Problema:** VPN sempre ativo interceptando todo tráfego tem overhead real de CPU/rede. NetGuard e similares geram reclamações de bateria mesmo bem otimizados.

**Mitigações:**
- Tratar <5%/dia como **meta aspiracional**, não como critério de aprovação do MVP
- Usar `VpnService.Builder` com `setBlocking(false)` quando possível
- Minimizar wake locks; usar JobScheduler para tarefas periódicas
- Monitorar consumo real em beta e publicar métricas transparentes
- Critério de aprovação realista: <10%/dia em uso moderado

**Classificação:** Risco MÉDIO (antes: otimista demais)

### 5.5 ⚠️ Recuperação de PIN × "conta Google"
**Problema:** A política de privacidade diz que não há coleta de dados/contas, mas o mecanismo de recuperação de PIN fala em "confirmação via conta Google". Contradição potencial.

**Mitigações:**
- Definir claramente: é verificação via Google Sign-In sem armazenar dado nenhum? Ou é inconsistência?
- Se for Google Sign-In: documentar que o token não é armazenado, usado apenas para verificação pontual
- Se não for viável: implementar recuperação via pergunta de segurança local (sem rede)
- Atualizar política de privacidade antes do lançamento

**Classificação:** Risco MÉDIO

### 5.6 Custo de tempo subestimado
**Problema:** "Desenvolvimento: R$0 (código gerado por IA)" subestima o esforço real. Código gerado por IA ainda precisa de testes em dispositivo físico, depuração de bugs específicos de fabricante, ajustes de UI.

**Mitigações:**
- Reconhecer que o custo financeiro é ~R$0 mas o custo de **tempo** é real
- Planejar pelo menos 2-4h/dia de dedicação nos meses 1-4
- Budget para imprevistos: possível aquisição de dispositivo de teste secundário

**Classificação:** Informativo

### 5.7 Encoding corrompido no documento
**Problema:** Na tabela de dispositivos de teste (seção original 9), "market share" aparece como caracteres chineses (市场份额) — problema de encoding.

**Ação:** Corrigir encoding no documento final antes de compartilhar com terceiros.

---

## 6. Monetização (Revisada)

### Modelo Freemium com Trial de 15 dias

| Recurso | Gratuito (após trial) | Premium |
|---------|----------------------|---------|
| Perfis pré-definidos | ✅ (3 perfis) | ✅ Ilimitados |
| Timer / Pomodoro | ✅ | ✅ |
| Modo Gaming | ❌ | ✅ |
| Agendamento | ❌ | ✅ |
| Bloqueio de chamadas | ❌ | ✅ |
| Tema escuro | ❌ | ✅ |
| Exportar/Importar | ❌ | ✅ |
| Modo Foco | ❌ | ✅ |

### Canais de pagamento

| Canal | Método | Status |
|-------|--------|--------|
| Google Play Store | Play Billing (assinatura) | v2.0 |
| Fora da Play Store | **Pix (chave CPF)** | ✅ Definido |

**Pix configurado:**
- Tipo de chave: CPF
- Chave: `{PIX_KEY_CPF}` (armazenada separadamente em arquivo seguro, não expor no documento público)
- Fluxo: usuário copia chave Pix → faz pagamento → app valida comprovante ou código de ativação manual

---

## 7. Distribuição (Revisada)

### Fase 1 — Lançamento (MVP)
1. **GitHub Releases** — APK direto, comunidade tech
2. **F-Droid** — build sem dependências proprietárias (variant "foss")
3. **APKPure / Aptoide** — alcance alternativo

### Fase 2 — Crescimento
4. **Google Play Store** — quando houver retorno/tração suficiente

### Variantes de build (Gradle flavors)

| Flavor | Dependências | Destino |
|--------|-------------|---------|
| `foss` | Sem Firebase, sem Play Billing | F-Droid |
| `full` | Com Firebase + Play Billing | Play Store, APKPure |

---

## 8. Custos (Revisado)

| Item | Custo |
|------|-------|
| Desenvolvimento | R$0 financeiro (código IA) — **tempo real: 2-4h/dia × 4 meses** |
| Conta GitHub | R$0 |
| Conta F-Droid | R$0 |
| Conta APKPure/Aptoide | R$0 |
| Conta Firebase | R$0 (plano gratuito) |
| Google Play Developer | R$130 (US$25, quando necessário) |
| Domínio (opcional) | ~R$40/ano |
| Dispositivo de teste extra (possível) | ~R$300-500 |
| Conta Pix | R$0 (já existente) |
| **Total financeiro fase 1** | **~R$0 a R$500** |

---

## 9. Divisão de Trabalho

### IA (eu)
- Escrever todo o código (Kotlin, Jetpack Compose)
- Gerar documentação técnica
- Criar guias de configuração por fabricante
- Montar assets (ícones, splash screen) se necessário
- Orientar publicação nas lojas
- Configurar Firebase Analytics + Crashlytics
- Redigir política de privacidade
- Criar variantes de build (foss/full)

### Você (Valdir)
- Criar contas nas lojas (GitHub, F-Droid, APKPure)
- Criar conta Firebase
- Publicar os APKs (com meu suporte)
- Recrutar testers para beta
- Responder reviews e feedback dos usuários
- Testar em dispositivos físicos reais
- Manutenção contínua após lançamento
- Decisões de negócio (preço, funcionalidades, prioridades)
- Monitorar analytics semanalmente
- Implementar fluxo de pagamento Pix (chave CPF)

---

## 10. Checklist Pré-Lançamento

- [ ] Política de privacidade publicada e linkada no app
- [ ] Mecanismo de recuperação de PIN definido e consistente com política
- [ ] Variantes de build (foss/full) funcionando
- [ ] Encoding do documentação corrigido (sem caracteres chineses acidentais)
- [ ] Guia de configuração por fabricante testado em dispositivos reais
- [ ] Fluxo de troca de CallScreeningService documentado e testado
- [ ] Meta de bateria revisada com dados reais do beta
- [ ] Formulário de segurança do Google Play preenchido (justificativa VPN)

---

*Documento gerado em 2026-09-05*
*Atualizado em 2026-09-06 — v2.1 (incorporando análise técnica de riscos)*
*Projeto: Desligo — "Liga o que importa. Desliga o resto."*
