import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.selectUnbiased

data class Product(val id: Int)

suspend fun producer1(channels: List<Channel<Product>>) {
    for (i in 1..10) {
        delay(100)
        val product = Product(i)
        println("Producent produkuje: ${product.id}")
        selectUnbiased<Unit> {
            channels.forEach { channel ->
                channel.onSend(product) {
                    println("Produkt ${product.id} wysłany do pośrednika ${channels.indexOf(channel)}")
                }
            }
        }
        delay(100)
    }
}

suspend fun pośrednik1(id: Int, channelIn: Channel<Product>, channelOut: Channel<Product>) {
    for (product in channelIn) {
        println("Pośrednik $id przekazuje produkt: ${product.id}")
        delay(500)
        channelOut.send(product)
    }
}

suspend fun consumer1(channel: Channel<Product>) {
    for (product in channel) {
        println("Konsument konsumuje produkt: ${product.id}")
    }
}

fun main() = runBlocking {
    val N = 3
    val producerChannel = List(N) { Channel<Product>(1) }
    val consumerChannel = Channel<Product>(1)

    launch { producer1(producerChannel) }
    val intermediaries = List(N) { index ->
        launch {
            pośrednik1(index, producerChannel[index], consumerChannel)
        }
    }
    launch { consumer(consumerChannel) }

    joinAll(*intermediaries.toTypedArray())
    return@runBlocking
}
