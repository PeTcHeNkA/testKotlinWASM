package by.mrpetchenka.testkotlinwasm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform