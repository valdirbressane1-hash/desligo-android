# Desligo — Plano de Trabalho v2.1

**Data:** 2026-09-06
**Escopo:** MVP (v1.0) + Premium (v2.0)
**Estimativa total:** ~16 semanas (4 meses)

---

## 0. Pré-requisitos (antes de codar)

### Ação do Valdir — Semana 0
- [ ] Criar conta GitHub (se não tiver)
- [ ] Criar repositório `desligo-android` (público, para F-Droid)
- [ ] Criar conta Firebase (console.firebase.google.com)
- [ ] Baixar Android Studio instalado e funcionando
- [ ] Ter pelo menos 1 dispositivo Android físico para testes
- [ ] Confirmar dados da conta Pix (titular, banco)

### Ação minha — Semana 0
- [ ] Gerar estrutura completa do projeto Android
- [ ] Configurar Gradle com flavors (`foss` / `full`)
- [ ] Configurar `.gitignore` (credentials.md, build/, .idea/, etc.)
- [ ] Gerar ícones e splash screen (mipmap densities)
- [ ] Escrever README.md inicial

---

## 1. Arquitetura do Projeto

```
desligo-android/
├── app/
│   ├── src/
│   │   ├── main/                    # Código compartilhado
│   │   │   ├── java/com/desligo/
│   │   │   │   ├── App.kt                       # Application class
│   │   │   │   ├── MainActivity.kt               # Entry point
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/                    # Material 3 theme
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── HomeScreen.kt         # Tela principal (perfis)
│   │   │   │   │   │   ├── ProfileDetailScreen.kt
│   │   │   │   │   │   ├── TimerScreen.kt        # Pomodoro
│   │   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   │   ├── OnboardingScreen.kt   # Primeiro uso
│   │   │   │   │   │   └── PremiumScreen.kt      # Paywall
│   │   │   │   │   └── components/               # UI reutilizável
│   │   │   │   ├── vpn/
│   │   │   │   │   ├── DesligoVpnService.kt      # VpnService principal
│   │   │   │   │   ├── TrafficFilter.kt          # Lógica de filtragem
│   │   │   │   │   └── VpnManager.kt             # Controle start/stop
│   │   │   │   ├── calls/
│   │   │   │   │   └── CallBlockerService.kt     # CallScreeningService
│   │   │   │   ├── profiles/
│   │   │   │   │   ├── Profile.kt                # Data class
│   │   │   │   │   ├── ProfileDao.kt             # Room DAO
│   │   │   │   │   └── ProfileRepository.kt
│   │   │   │   ├── apps/
│   │   │   │   │   ├── AppInfo.kt                # Modelo de app instalado
│   │   │   │   │   └── AppListManager.kt         # Lista de apps instalados
│   │   │   │   ├── timer/
│   │   │   │   │   ├── PomodoroTimer.kt          # Lógica do timer
│   │   │   │   │   └── TimerNotification.kt
│   │   │   │   ├── billing/
│   │   │   │   │   ├── BillingManager.kt         # Play Billing (full only)
│   │   │   │   │   └── PixActivationManager.kt   # Ativação via código Pix
│   │   │   │   ├── data/
│   │   │   │   │   ├── AppDatabase.kt            # Room database
│   │   │   │   │   ├── PreferencesManager.kt     # DataStore
│   │   │   │   │   └── PremiumState.kt           # Estado premium
│   │   │   │   ├── manufacturer/
│   │   │   │   │   ├── ManufacturerDetector.kt   # Detectar fabricante
│   │   │   │   │   └── BatteryGuide.kt           # Guia por fabricante
│   │   │   │   └── util/
│   │   │   │       ├── Constants.kt
│   │   │   │       └── Extensions.kt
│   │   │   └── res/
│   │   │       ├── values/strings.xml
│   │   │       ├── drawable/                     # Ícones, assets
│   │   │       └── xml/                          # VPN config, network security
│   │   ├── foss/                     # Flavor F-Droid (sem Firebase/Billing)
│   │   │   └── java/com/desligo/
│   │   │       └── analytics/
│   │   │           └── AnalyticsManager.kt       # Stub / Matomo
│   │   └── full/                     # Flavor Play Store
│   │       └── java/com/desligo/
│   │           └── analytics/
│   │               └── AnalyticsManager.kt       # Firebase Analytics
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── docs/
│   ├── PROJETO-v2.1.md
│   ├── credentials.md                # ⚠️ NÃO commitar
│   ├── guias-fabricantes/
│   │   ├── xiaomi.md
│   │   ├── samsung.md
│   │   ├── huawei.md
│   │   ├── oneplus.md
│   │   └── motorola.md
│   └── politica-privacidade.md
├── build.gradle.kts                  # Root build
├── settings.gradle.kts
├── gradle.properties
├── .gitignore
└── README.md
```

---

## 2. Fase 1 — MVP (Semanas 1-8)

### Sprint 1: Fundação (Semanas 1-2)

**Objetivo:** Projeto compila, roda, tem estrutura básica.

| # | Tarefa | Detalhes | Dependência |
|---|--------|----------|-------------|
| 1.1 | Criar projeto Android Studio | Min SDK 26 (Android 8.0), Target SDK 34 | — |
| 1.2 | Configurar Gradle flavors | `foss` e `full`, cada um com seu `AnalyticsManager` | 1.1 |
| 1.3 | Configurar Material 3 + tema | Cores, tipografia, dark mode base | 1.1 |
| 1.4 | Implementar `AppDatabase` (Room) | Tabelas: profiles, blocked_apps, settings | 1.1 |
| 1.5 | Criar `PreferencesManager` (DataStore) | PIN, trial state, premium state, config | 1.1 |
| 1.6 | Implementar `ManufacturerDetector` | Detectar Xiaomi/Samsung/Huawei/OnePlus/Motorola | 1.1 |
| 1.7 | Criar tela de Onboarding | 3-4 slides: conceito, permissões, VPN, pronto | 1.3 |
| 1.8 | Configurar `.gitignore` | credentials.md, build/, .idea/, *.apk | 1.1 |
| 1.9 | Gerar assets | Ícone (mipmap), splash screen, notification icon | — |

**Entregável:** App roda em emulador, mostra onboarding, tem banco de dados funcionando.

---

### Sprint 2: VPN Core (Semanas 3-4)

**Objetivo:** VPN filtra tráfego por app — a funcionalidade central.

| # | Tarefa | Detalhes | Dependência |
|---|--------|----------|-------------|
| 2.1 | Implementar `DesligoVpnService` | Estender `VpnService`, configurar interface tun | 1.4 |
| 2.2 | Implementar `TrafficFilter` | Lógica de allow/deny por pacote de app | 2.1 |
| 2.3 | Implementar `VpnManager` | Start/stop, notificação persistente, auto-restart | 2.1 |
| 2.4 | Implementar `AppListManager` | Listar apps instalados com ícone e nome | 1.4 |
| 2.5 | Tela de seleção de apps por perfil | Lista com toggle por app, filtro, busca | 2.4, 1.3 |
| 2.6 | Integrar VPN com perfis | Ao ativar perfil → inicia VPN com lista de apps bloqueados | 2.2, 1.4 |
| 2.7 | Tratar permissão de VPN | Dialog explicativo → `VpnService.prepare()` → onActivityResult | 2.1 |
| 2.8 | Testes em dispositivo real | Verificar filtragem funciona em Samsung/Xiaomi | 2.6 |

**Entregável:** Usuário ativa perfil → VPN bloqueia apps selecionados. Base do produto funciona.

---

### Sprint 3: Perfis + UI Principal (Semanas 5-6)

**Objetivo:** Interface completa, perfis funcionais, timer.

| # | Tarefa | Detalhes | Dependência |
|---|--------|----------|-------------|
| 3.1 | Implementar `HomeScreen` | Cards de perfis (Estudo, Sono, Trabalho, Livre), botão ativo | 2.6 |
| 3.2 | Implementar `ProfileDetailScreen` | Editar apps bloqueados, ativar/desativar perfil | 2.5 |
| 3.3 | Implementar `PomodoroTimer` | 25/5 min, notificação com contagem regressiva | — |
| 3.4 | Implementar `TimerScreen` | UI do Pomodoro, histórico de sessões | 3.3 |
| 3.5 | Implementar `SettingsScreen` | PIN, tema, sobre, guia do fabricante, premium | — |
| 3.6 | Sistema de PIN | Proteger configurações com PIN de 4 dígitos (local) | 1.5 |
| 3.7 | Implementar `BatteryGuide` | Tela com guia específico por fabricante detectado | 1.6 |
| 3.8 | Notificação de perfil ativo | Persistent notification com ação para desativar | 2.3 |

**Entregável:** App usável com 4 perfis, Pomodoro, PIN, guia de fabricante.

---

### Sprint 4: Trial + Monetização + Polimento (Semanas 7-8)

**Objetivo:** Sistema de trial, ativação Pix, preparação para beta.

| # | Tarefa | Detalhes | Dependência |
|---|--------|----------|-------------|
| 4.1 | Implementar `PremiumState` | Controlar trial (15 dias), premium ativo/expirado | 1.5 |
| 4.2 | Implementar `PixActivationManager` | Gerar código de ativação, validar código inserido | 4.1 |
| 4.3 | Implementar `PremiumScreen` | Paywall: mostrar recursos bloqueados, botão "Ativar com Pix" | 4.2 |
| 4.4 | Bloquear recursos premium | Desativar agendamento, bloqueio de chamadas, perfis extras após trial | 4.1 |
| 4.5 | Implementar fluxo Pix no app | Mostrar chave Pix → copiar → campo para inserir código | 4.2 |
| 4.6 | Implementar `BillingManager` (full flavor) | Play Billing para Play Store (preparar, ativar depois) | 1.2 |
| 4.7 | Recuperação de PIN | Via pergunta de segurança local (sem rede) | 3.6 |
| 4.8 | Polimento UI/UX | Animações, transições, empty states, error states | 3.1-3.5 |
| 4.9 | Testes em 3+ dispositivos | Samsung, Xiaomi, Motorola (ou disponíveis) | 4.8 |
| 4.10 | Gerar APKs de teste | Build foss (F-Droid) e full (APKPure) | 4.9 |

**Entregável:** MVP completo, trial funcionando, APK pronto para beta.

---

## 3. Fase 2 — Beta + Premium (Semanas 9-14)

### Sprint 5: Beta Aberto (Semanas 9-10)

| # | Tarefa | Detalhes | Responsável |
|---|--------|----------|-------------|
| 5.1 | Publicar APK no GitHub Releases | Release notes, instruções de instalação | Valdir |
| 5.2 | Submeter ao F-Droid | Build foss, MR no fdroiddata | Valdir (com suporte) |
| 5.3 | Publicar no APKPure | APK full | Valdir |
| 5.4 | Recrutar testers | Comunidades Android, Reddit, grupos Telegram | Valdir |
| 5.5 | Coletar feedback | Formulário Google Forms ou GitHub Issues | Valdir |
| 5.6 | Monitorar Crashlytics | Corrigir crashes críticos rapidamente | Ambos |
| 5.7 | Ajustar com base no feedback | Bugs, UX, funcionalidades faltando | IA |

---

### Sprint 6: Premium Features (Semanas 11-14)

| # | Tarefa | Detalhes | Dependência |
|---|--------|----------|-------------|
| 6.1 | Modo Gaming (1 toque) | Ativa perfil que libera todos os apps de jogos, desativa notificações | 3.1 |
| 6.2 | Agendamento de perfis | Ativar perfil em horário/dia específico | 3.2 |
| 6.3 | Bloqueio de chamadas | CallScreeningService, com tela de configuração | — |
| 6.4 | Tema escuro completo | Material 3 dynamic color + dark theme toggle | 1.3 |
| 6.5 | Exportar/Importar config | JSON com perfis, apps bloqueados, settings | 1.4 |
| 6.6 | Modo Foco | Timer + bloqueio total de apps não essenciais + mensagem personalizada | 3.4 |
| 6.7 | Perfis customizados | Criar/editar/deletar perfis além dos 4 padrão | 3.2 |
| 6.8 | Integração Play Billing | Ativar na Play Store quando disponível | 4.6 |

**Entregável:** Versão premium completa, pronta para Play Store.

---

## 4. Fase 3 — Crescimento (Semanas 15+)

### Sprint 7+: Crescimento

| # | Tarefa | Detalhes |
|---|--------|----------|
| 7.1 | Widget de tela | Toggle rápido de perfil ativo |
| 7.2 | Modo automático por app | Detectar app aberto → aplicar regra |
| 7.3 | Bloqueio por localização | Geofence → ativar perfil (premium) |
| 7.4 | Play Store listing | Screenshots, descrição ASO, formulário VPN |
| 7.5 | Guia de fabricantes expandido | Adicionar mais fabricantes conforme feedback |

---

## 5. Decisões Técnicas Críticas

### 5.1 VPN: Padrão NetGuard
- Usar `VpnService` com interface `tun0`
- Filtrar por UID do pacote (cada app Android tem UID único)
- Pacotes de apps bloqueados → descartar
- Pacotes de apps permitidos → passar through
- Não interceptar conteúdo (só permitir/bloquear por app)

### 5.2 PIN: Armazenamento local
- Hash SHA-256 do PIN + salt aleatório
- Armazenado em `EncryptedSharedPreferences`
- Sem backup em nuvem (privacidade)
- Recuperação: pergunta de segurança (também local)

### 5.3 Trial: Controle local
- Data de instalação em `DataStore`
- 15 dias corridos (não úso ativo)
- Após expiração: bloquear recursos premium, manter básicos
- Sem servidor — controle 100% local (simples, sem custo)

### 5.4 Ativação Pix: Códigos
- Código de ativação: UUID v4 gerado offline
- Validação: hash do código + device ID (anti-pirataria básica)
- Fluxo: Valdir gera código após receber Pix → envia por email/WhatsApp → usuário insere no app
- Futuro: API de validação automatizada (se volume justificar)

### 5.5 Build Flavors
```
productFlavors {
    foss {
        dimension = "distribution"
        // Sem Firebase, sem Play Billing
        // Analytics: stub ou Matomo
    }
    full {
        dimension = "distribution"
        // Firebase Analytics + Crashlytics
        // Google Play Billing
    }
}
```

---

## 6. Riscos do Plano de Trabalho

| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| VPN não funciona em Android 14+ | ALTO | Testar cedo (Sprint 2), adaptar se necessário |
| Bateria >10%/dia | MÉDIO | Otimizar wake locks, testar no beta |
| F-Droid rejeita build | MÉDIO | Rodar `fdroid lint` antes de submter |
| CallScreeningService conflita | MÉDIO | Tornar opcional, bom onboarding |
| Tester insuficiente para beta | MÉDIO | Postar em múltiplas comunidades |
| Bug de fabricante específico | MÉDIO | Testar em 3+ dispositivos reais |

---

## 7. Cronograma Resumido

```
Semana 0  ──── Preparação (contas, repositório, Android Studio)
Semana 1-2 ─── Sprint 1: Fundação (projeto, DB, onboarding)
Semana 3-4 ─── Sprint 2: VPN Core (filtragem por app)
Semana 5-6 ─── Sprint 3: Perfis + UI + Timer
Semana 7-8 ─── Sprint 4: Trial + Pix + Polimento → MVP PRONTO
Semana 9-10 ── Sprint 5: Beta aberto (publicar, coletar feedback)
Semana 11-14 ─ Sprint 6: Premium features
Semana 15+ ─── Sprint 7+: Crescimento (widget, automático, Play Store)
```

**Marco principal:** Semana 8 = MVP pronto para beta público.

---

## 8. O que eu entrego agora vs. depois

### Posso gerar agora (nesta sessão):
- Estrutura completa do projeto Android (todos os arquivos)
- Gradle configurado com flavors
- Código de todas as Sprints 1-4
- Assets básicos (ícone, splash)
- Documentação (README, guias, política de privacidade)

### Precisa do Valdir primeiro:
- ✅ Conta GitHub + repositório criado (para eu fazer push)
- ✅ Dispositivo Android para testes
- ✅ Confirmar titular da conta Pix

### Depende de testes reais:
- Otimização de bateria por fabricante
- Ajustes finos de UX
- Compatibilidade com Android 14+

---

*Plano gerado em 2026-09-06*
*Projeto: Desligo — "Liga o que importa. Desliga o resto."*
