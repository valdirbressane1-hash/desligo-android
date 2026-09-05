package com.desligo.manufacturer

import android.os.Build

data class ManufacturerGuide(
    val manufacturer: String,
    val icon: String,
    val steps: List<String>,
    val notes: String = ""
)

object ManufacturerDetector {

    fun detect(): String {
        return Build.MANUFACTURER.lowercase()
    }

    fun getGuide(): ManufacturerGuide {
        val manufacturer = detect()
        return when {
            manufacturer.contains("xiaomi") -> xiaomiGuide
            manufacturer.contains("samsung") -> samsungGuide
            manufacturer.contains("huawei") || manufacturer.contains("honor") -> huaweiGuide
            manufacturer.contains("oneplus") -> oneplusGuide
            manufacturer.contains("motorola") || manufacturer.contains("moto") -> motorolaGuide
            else -> genericGuide
        }
    }

    private val xiaomiGuide = ManufacturerGuide(
        manufacturer = "Xiaomi / MIUI / HyperOS",
        icon = "📱",
        steps = listOf(
            "Abra Configurações → Apps → Gerenciar apps",
            "Encontre 'Desligo' na lista",
            "Toque em 'Autostart' e ative",
            "Volte e toque em 'Economia de bateria'",
            "Selecione 'Sem restrições'",
            "Em Configurações → Bateria → otimização de bateria",
            "Encontre Desligo e selecione 'Não otimizar'",
            "Mantenha o Desligo aberto na lista de apps recentes (não feche pelo multitasking)"
        ),
        notes = "MIUI é agressivo com apps em background. Se o Desligo parar de funcionar, verifique se não foi fechado pelo sistema."
    )

    private val samsungGuide = ManufacturerGuide(
        manufacturer = "Samsung / One UI",
        icon = "📱",
        steps = listOf(
            "Abra Configurações → Apps → Desligo",
            "Toque em 'Bateria'",
            "Selecione 'Sem restrições'",
            "Volte e verifique se 'Permitir em background' está ativo",
            "Em Configurações → Manutenção do dispositivo → Bateria",
            "Desative 'Otimização automática' ou adicione Desligo às exceções"
        ),
        notes = "Samsung One UI geralmente é menos agressivo, mas pode matar apps após atualizações."
    )

    private val huaweiGuide = ManufacturerGuide(
        manufacturer = "Huawei / EMUI / HarmonyOS",
        icon = "📱",
        steps = listOf(
            "Abra Configurações → Bateria → Inicialização de apps",
            "Encontre Desligo e ative 'Gerenciar manualmente'",
            "Ative 'Iniciar automaticamente', 'Execução em segundo plano' e 'Iniciar automaticamente'",
            "Em Configurações → Apps → Apps → Desligo → Bateria",
            "Selecione 'Sem restrições'",
            "Proteja o app no multitasking (trave com cadeado)"
        ),
        notes = "Huawei é o mais agressivo. Se necessário, desative a 'Proteção de bateria' nas configurações avançadas."
    )

    private val oneplusGuide = ManufacturerGuide(
        manufacturer = "OnePlus / OxygenOS",
        icon = "📱",
        steps = listOf(
            "Abra Configurações → Bateria → Otimização de bateria",
            "Encontre Desligo e selecione 'Não otimizar'",
            "Em Configurações → Apps → Desligo",
            "Ative 'Executar em background'",
            "No multitasking, deslize Desligo para baixo para travar"
        ),
        notes = "OxygenOS é relativamente amigável, mas pode ter problemas após atualizações maiores."
    )

    private val motorolaGuide = ManufacturerGuide(
        manufacturer = "Motorola",
        icon = "📱",
        steps = listOf(
            "Abra Configurações → Bateria → Otimização de bateria",
            "Encontre Desligo e selecione 'Não otimizar'",
            "Em Configurações → Apps → Desligo → Bateria",
            "Selecione 'Sem restrições'",
            "Mantenha Desligo aberto no multitasking"
        ),
        notes = "Motorola geralmente é menos restritivo, mas a Moto Actions pode interferir."
    )

    private val genericGuide = ManufacturerGuide(
        manufacturer = "Genérico / Android Stock",
        icon = "📱",
        steps = listOf(
            "Abra Configurações → Apps → Desligo → Bateria",
            "Selecione 'Sem restrições' ou 'Não otimizar'",
            "Em Configurações → Bateria → Otimização",
            "Encontre Desligo e desative a otimização",
            "Mantenha Desligo aberto no multitasking"
        ),
        notes = "Android Stock (Pixel, Android One) geralmente não tem problemas com apps em background."
    )
}
