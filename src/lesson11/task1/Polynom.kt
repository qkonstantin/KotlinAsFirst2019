@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- сложная.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {

    private fun isEmptyList(list: List<Double>) = if (list.isNotEmpty()) list else listOf(0.0)
    private val coefficients = isEmptyList(coeffs.toList().dropWhile { it == 0.0 })

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = coefficients[i]

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double = coefficients.fold(0.0) { prev, coeff -> prev * x + coeff }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int = coefficients.size - 1

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        var first = this.coefficients
        var second = other.coefficients
        val difference = abs(first.size - second.size)
        val result = mutableListOf<Double>()
        if (first.size != max(first.size, second.size)) {
            first = other.coefficients
            second = this.coefficients
        }
        for (i in first.indices) {
            if (i >= difference) result.add(first[i] + second[i - difference])
            else result.add(first[i])
        }
        return Polynom(*result.toDoubleArray())
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        val res = mutableListOf<Double>()
        for (i in coefficients.indices)
            res += -coefficients[i]
        return Polynom(*res.toDoubleArray())
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = plus(-other)

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val map = mutableMapOf<Int, Double>()
        val list = mutableListOf<Double>()
        for (i in coefficients.indices) {
            for (j in other.coefficients.indices)
                map[i + j] = map.getOrDefault(i + j, 0.0) + coefficients[i] * other.coefficients[j]
        }
            for (i in map.toList().indices)
                list.add(map.getOrDefault(i, 0.0))
        return Polynom(*list.toDoubleArray())
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom = TODO()

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Polynom && coefficients == other.coefficients

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = coefficients.hashCode()
}
