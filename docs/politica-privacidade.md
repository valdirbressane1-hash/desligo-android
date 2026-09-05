# Política de Privacidade — Desligo

**Última atualização:** 06 de setembro de 2026

## Introdução

O Desligo respeita sua privacidade. Esta política explica como o app funciona e quais dados (se algum) são processados.

## Dados Coletados

**O Desligo NÃO coleta, armazena ou envia dados pessoais para servidores externos.**

Todo o processamento acontece localmente no seu dispositivo:
- Perfis e configurações ficam armazenados apenas no seu celular
- O PIN de segurança é armazenado localmente com hash criptográfico
- Nenhum dado é enviado para servidores do Desligo ou de terceiros

## Permissões

### VPN (android.net.VpnService)
- **Por quê:** Para filtrar o tráfego de rede por aplicativo
- **Como:** A VPN é 100% local — todo o tráfego permanece no seu dispositivo
- **Dados:** O Desligo apenas permite ou bloqueia o acesso por app, sem inspecionar o conteúdo

### Telefonia (READ_PHONE_STATE, READ_CALL_LOG)
- **Por quê:** Para bloquear chamadas indesejadas (recurso opcional)
- **Como:** Apenas verifica números para bloquear, não armazena histórico de chamadas

### Notificações (POST_NOTIFICATIONS)
- **Por quê:** Para mostrar quando um perfil está ativo ou o timer está rodando

## Serviços de Terceiros

### Versão F-Droid (foss)
- Nenhum serviço de terceiros

### Versão Play Store (full)
- **Firebase Analytics:** Coleta dados anônimos de uso (telas visitadas, eventos). Sem dados pessoais.
- **Firebase Crashlytics:** Coleta relatórios de crash para correção de bugs. Sem dados pessoais.

## Pagamento Premium

- O pagamento é feito via Pix diretamente para a conta do desenvolvedor
- O Desligo não armazena dados bancários ou financeiros
- O código de ativação é gerado localmente no app

## Retenção de Dados

- Todos os dados ficam no seu dispositivo
- Desinstalar o app apaga todos os dados
- Você pode apagar seus dados a qualquer momento nas Configurações do app

## Segurança

- PIN armazenado com hash SHA-256 + salt aleatório
- Dados armazenados em SharedPreferences criptografadas (Android Keystore)
- Sem transmissão de dados pela internet

## Menores de Idade

O Desligo não coleta dados de menores de 13 anos. O app é adequado para todas as idades.

## Alterações

Alterações nesta política serão publicadas junto com atualizações do app.

## Contato

Dúvidas sobre privacidade? Entre em contato:
- Email: desligo.app@gmail.com

---

*"Liga o que importa. Desliga o resto."*
