# Desligo — Guia de Início Rápido

## Passo 1: Abrir no Android Studio

1. Abra o **Android Studio**
2. Clique em **File → Open**
3. Navegue até a pasta `desligo` que eu criei
4. Clique em **OK**
5. Aguarde o Gradle sync (pode demorar 2-5 min na primeira vez)

### Se pedir para instalar SDK:
- Clique em **Install** quando aparecer o popup
- Aceite as licenças
- Aguarde download (~500MB)

---

## Passo 2: Configurar Emulador (se não tiver dispositivo)

1. **Tools → Device Manager**
2. Clique em **Create Virtual Device**
3. Escolha **Pixel 6** (ou qualquer um)
4. Selecione **API 34** (Android 14) — baixe se necessário
5. Clique em **Finish**
6. Clique no ▶️ para iniciar o emulador

---

## Passo 3: Rodar o App

1. No Android Studio, selecione o emulador/device no dropdown superior
2. Clique no botão ▶️ **Run** (ou Shift+F10)
3. Aguarde o build (primeira vez: 3-5 min)
4. O app vai abrir no emulador!

### Se der erro no build:
- **Erro de Java version:** File → Settings → Build → Gradle → Defina JDK 17
- **Erro de SDK:** Tools → SDK Manager → Instale API 34
- **Erro de Gradle:** File → Sync Project with Gradle Files

---

## Passo 4: Testar o App

### O que testar:
1. **Onboarding** — deslize pelas 4 telas, clique "Começar"
2. **Home** — veja os 4 perfis (Estudo, Sono, Trabalho, Livre)
3. **Timer** — clique em "Pomodoro Timer", inicie uma sessão
4. **Configurações** — clique no ícone ⚙️
5. **Guia do fabricante** — veja as instruções de bateria
6. **PIN** — configure um PIN de 4 dígitos

### Para testar a VPN:
1. Na tela inicial, clique em um perfil (ex: "Estudo")
2. Clique em "Gerenciar apps bloqueados"
3. Selecione alguns apps para bloquear
4. Volte e clique "Ativar Perfil"
5. Aceite a permissão de VPN quando aparecer
6. Tente abrir o app bloqueado — deve ser impedido!

---

## Passo 5: Criar Conta GitHub (se não tiver)

1. Acesse **github.com**
2. Clique em **Sign Up**
3. Siga o cadastro (email, senha, username)
4. Verifique seu email

### Criar o repositório:
1. No GitHub, clique no **+** (canto superior direito) → **New repository**
2. Nome: `desligo-android`
3. Descrição: "App Android de bem-estar digital"
4. Deixe **Private** (por enquanto)
5. NÃO marque "Add README" (já temos um)
6. Clique em **Create repository**

---

## Passo 6: Fazer Push para o GitHub

### No terminal (dentro do Android Studio):
```bash
cd desligo
git init
git add .
git commit -m "Initial commit - Desligo v1.0 MVP"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/desligo-android.git
git push -u origin main
```

### Substituir SEU_USUARIO pelo seu username do GitHub.

---

## Passo 7: Criar Conta Firebase (para analytics — opcional agora)

1. Acesse **console.firebase.google.com**
2. Clique em **Create a project**
3. Nome: `desligo-app`
4. Desative Google Analytics (opcional)
5. Clique em **Create Project**

### Adicionar o app Android:
1. Clique no ícone Android
2. Package name: `com.desligo`
3. App nickname: `Desligo`
4. Clique em **Register app**
5. Baixe o `google-services.json`
6. Coloque na pasta `app/` do projeto

---

## Estrutura do Projeto (Resumo)

```
desligo/
├── app/
│   ├── src/main/java/com/desligo/
│   │   ├── vpn/          ← VPN local (core do app)
│   │   ├── profiles/     ← Perfis (Room database)
│   │   ├── timer/        ← Pomodoro
│   │   ├── billing/      ← Pagamento Pix
│   │   ├── ui/screens/   ← Todas as telas
│   │   └── data/         ← Configurações e estado
│   └── src/main/res/     ← Ícones, strings, temas
├── docs/                 ← Documentação
├── build.gradle.kts      ← Configuração do build
└── README.md
```

---

## Problemas Comuns

| Problema | Solução |
|----------|---------|
| "SDK not found" | Tools → SDK Manager → Instale API 34 |
| "JDK not found" | File → Settings → Build → Gradle → JDK 17 |
| "Gradle sync failed" | File → Sync Project with Gradle Files |
| "App not installing" | Limpe o emulador: Wipe Data no Device Manager |
| "VPN not working" | Testar em dispositivo real (emulador tem limitações) |

---

## Próximos Passos Após Rodar

1. ✅ Testar todas as telas
2. ✅ Testar VPN em dispositivo real
3. ✅ Ajustar cores/ícones se quiser
4. ✅ Publicar beta no GitHub Releases
5. ✅ Recrutar testers

---

*Guia gerado em 2026-09-06*
