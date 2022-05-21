package coroutine

import kotlinx.coroutines.*

fun main() {
    print("Hello")

    GlobalScope.launch {
        println("Another task started")
        delay(5000L)
        println("The other task finished")
    }

    println("ooooooooo")

    runBlocking {
        println(this.coroutineContext.toString())
        delay(5000L)
    }

    println("World!")
}
