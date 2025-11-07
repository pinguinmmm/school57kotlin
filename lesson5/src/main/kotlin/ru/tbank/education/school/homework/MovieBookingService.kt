package ru.tbank.education.school.homework

package ru.tbank.education.school.homework


class NoAvailableSeatException(message: String) : Exception(message)
class SeatAlreadyBookedException(message: String) : Exception(message)

class MovieBookingService(private val maxQuantityOfSeats: Int) {

    private val bookings = mutableMapOf<String, MutableList<Int>>()

    init {
        require(maxQuantityOfSeats > 0) {
            "Максимальное количество мест должно быть больше нуля."
        }
    }

    fun bookSeat(movieId: String, seat: Int) {
        if (seat !in 1..maxQuantityOfSeats) {
            throw IllegalArgumentException("Некорректный номер места: $seat. Допустимый диапазон: 1..$maxQuantityOfSeats")
        }

        val movieSeats = bookings.getOrPut(movieId) { mutableListOf() }
        if (movieSeats.contains(seat)) {
            throw SeatAlreadyBookedException("Место $seat для фильма '$movieId' уже забронировано.")
        }

        if (movieSeats.size >= maxQuantityOfSeats) {
            throw NoAvailableSeatException("Все места для фильма '$movieId' уже заняты.")
        }

        movieSeats.add(seat)
        println("Место $seat успешно забронировано для фильма '$movieId'.")
    }

    fun cancelBooking(movieId: String, seat: Int) {
        val movieSeats = bookings[movieId]
            ?: throw NoSuchElementException("Для фильма '$movieId' нет активных броней.")

        if (!movieSeats.remove(seat)) {
            throw NoSuchElementException("Место $seat для фильма '$movieId' не было забронировано.")
        }

        println("Бронирование места $seat для фильма '$movieId' отменено.")
    }

    fun isSeatBooked(movieId: String, seat: Int): Boolean {
        return bookings[movieId]?.contains(seat) ?: false
    }
}
fun main() {
    val service = MovieBookingService(5)

    val movieId = "Inception"

    service.bookSeat(movieId, 1)
    service.bookSeat(movieId, 2)

    println(service.isSeatBooked(movieId, 1)) // true
    println(service.isSeatBooked(movieId, 3)) // false

    service.cancelBooking(movieId, 1)
    println(service.isSeatBooked(movieId, 1)) // false

    try {
        service.bookSeat(movieId, 0)
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }

    try {
        service.cancelBooking(movieId, 10)
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }
}
