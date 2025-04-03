import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

suspend fun producer(channel: Channel<Product>) {
    for (i in 1..10) {
        val product = Product(i)
        println("Producent produkuje: ${product.id}")
        channel.send(product)
        delay(100)
    }
}

suspend fun przetwarzacz(id: Int, inputChannel: Channel<Product>, outputChannel: Channel<Product>?) {
    for (product in inputChannel) {
        println("Przetwarzacz $id przetwarza produkt: ${product.id}")
        delay(50)
        outputChannel?.send(product)
    }
}

suspend fun consumer(channel: Channel<Product>) {
    for (product in channel) {
        println("Konsument konsumuje produkt: ${product.id}")
    }
}

fun main() = runBlocking {
    val N = 3
    val channels = List(N + 1) { Channel<Product>(1) }  // Kanały dla przetwórców i konsumenta

    launch { producer(channels[0]) }

    val processors = List(N) { index ->
        launch {
            przetwarzacz(index, channels[index], channels.getOrNull(index + 1))
        }
    }

    launch { consumer(channels[N]) }

    joinAll(*processors.toTypedArray())
    return@runBlocking
}
