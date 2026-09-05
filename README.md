# Desligo — Liga o que importa. Desliga o resto.

Aplicativo Android de bem-estar digital que permite bloquear seletivamente apps, chamadas e notificações com base em perfis e horários.

## Funcionalidades (MVP)

- **VPN Local** — filtra tráfego por app, sem root
- **4 Perfis** — Estudo, Sono, Trabalho, Livre
- **Timer / Pomodoro** — sessões focadas
- **Bloqueio de chamadas** — opcional
- **Guia por fabricante** — Xiaomi, Samsung, Huawei, OnePlus, Motorola
- **Privacidade** — zero coleta de dados, zero servidores externos

## Stack

- Kotlin + Jetpack Compose
- VpnService (local)
- Room (SQLite)
- Material 3
- Min SDK 26 (Android 8.0)

## Build

```bash
# Flavor foss (F-Droid)
./gradlew assembleFossDebug

# Flavor full (Play Store)
./gradlew assembleFullDebug
```

## Licença

[Definir]

---

*"Liga o que importa. Desliga o resto."*
