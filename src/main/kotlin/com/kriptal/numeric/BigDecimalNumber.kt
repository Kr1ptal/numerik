package com.kriptal.numeric

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

private val DEFAULT_SCALE = System.getProperty("bigdecimal.scale", "12").toInt()

/**
 * Default rounding mode for division is: "round to nearest, ties to even". This rounding mode is in line
 * with IEEE 754 floating number division standard and is used to minimize rounding errors after many
 * divisions/multiplication.
 *
 * Due to rounding mode, this class will behave as a floating-point number, except with unlimited precision. This
 * also means that integer division as decimal number and rounding back to integer will work as expected.
 * For example, result of int division, as floating-point number:
 * - Within double precision:  2.999999999999999 -> 2
 * - Outside double precision: 2.9999999999999999 -> 3
 *
 * IEEE 754 Rounding: https://en.wikipedia.org/wiki/Floating-point_arithmetic#Rounding_modes
 *
 * **NOTE**: Integer types will be converted into decimal representation. Even though we use same rounding mode as
 * integer number division, consumers should make sure to round down to whole number manually in this case before each
 * multiplication/division, so the fractional part is ignored. This class is **NOT** suitable as a replacement
 * for integer types.
 * */
class BigDecimalNumber private constructor(private val value: BigDecimal) : DecimalNumber() {
    override fun add(other: DecimalNumber): BigDecimalNumber {
        return BigDecimalNumber(value + other.toBigDecimal())
    }

    override fun subtract(other: DecimalNumber): BigDecimalNumber {
        return BigDecimalNumber(value - other.toBigDecimal())
    }

    //multiplication needs rounding too otherwise we can get increasingly larger scales into infinity, significantly
    //slowing down calculations: 1.13 * 1.33 = 1.5029
    override fun multiply(other: DecimalNumber): BigDecimalNumber {
        return BigDecimalNumber(roundedOperation { value * other.toBigDecimal() })
    }

    override fun divide(other: DecimalNumber, roundingMode: RoundingMode): BigDecimalNumber {
        return BigDecimalNumber(value.divide(other.toBigDecimal(), DEFAULT_SCALE, roundingMode))
    }

    override fun pow(n: Int): BigDecimalNumber {
        return BigDecimalNumber(value.pow(n))
    }

    override fun scaleByPowerOfTen(n: Int): BigDecimalNumber {
        return BigDecimalNumber(value.scaleByPowerOfTen(n))
    }

    override fun remainder(other: DecimalNumber): BigDecimalNumber {
        return BigDecimalNumber(value.remainder(other.toBigDecimal()))
    }

    override fun negate(): BigDecimalNumber {
        return BigDecimalNumber(value.negate())
    }

    override fun abs(): BigDecimalNumber {
        if (value.signum() >= 0) {
            return this
        }
        return BigDecimalNumber(value.negate())
    }

    override fun signum(): Int {
        return value.signum()
    }

    override fun toByte(): Byte {
        return value.toByte()
    }

    override fun toChar(): Char {
        return value.toChar()
    }

    override fun toDouble(): Double {
        return value.toDouble()
    }

    override fun toFloat(): Float {
        return value.toFloat()
    }

    override fun toInt(): Int {
        return value.toInt()
    }

    override fun toLong(): Long {
        return value.toLong()
    }

    override fun toShort(): Short {
        return value.toShort()
    }

    override fun toBigDecimal(): BigDecimal {
        return value
    }

    override fun toBigInteger(): BigInteger {
        return value.toBigInteger()
    }

    override fun compareTo(other: DecimalNumber): Int {
        return value.compareTo(other.toBigDecimal())
    }

    override fun toString(): String {
        return value.toPlainString()
    }

    // minimize instantiations of DecimalNumber wrapper classes
    fun max(other: BigDecimalNumber): BigDecimalNumber = if (this >= other) this else other
    override fun max(other: DecimalNumber): BigDecimalNumber = if (this >= other) this else valueOf(other.toBigDecimal())
    override fun max(other: Int): BigDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: Long): BigDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: String): BigDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: Double): BigDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: BigDecimal): BigDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: BigInteger): BigDecimalNumber = if (this >= other) this else valueOf(other)

    // minimize instantiations of DecimalNumber wrapper classes
    fun min(other: BigDecimalNumber): BigDecimalNumber = if (this <= other) this else other
    override fun min(other: DecimalNumber): BigDecimalNumber = if (this <= other) this else valueOf(other.toBigDecimal())
    override fun min(other: Int): BigDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: Long): BigDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: String): BigDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: Double): BigDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: BigDecimal): BigDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: BigInteger): BigDecimalNumber = if (this <= other) this else valueOf(other)

    override operator fun compareTo(other: Int): Int = value.compareTo(other.toBigDecimal())
    override operator fun compareTo(other: Long): Int = value.compareTo(other.toBigDecimal())
    override operator fun compareTo(other: String): Int = value.compareTo(other.toBigDecimal())
    override operator fun compareTo(other: Double): Int = value.compareTo(other.toBigDecimal())
    override operator fun compareTo(other: BigDecimal): Int = value.compareTo(other)
    override operator fun compareTo(other: BigInteger): Int = value.compareTo(other.toBigDecimal())

    override operator fun plus(other: DecimalNumber): BigDecimalNumber = valueOf(value.add(other.toBigDecimal()))
    override operator fun plus(other: Int): BigDecimalNumber = valueOf(value.add(BigDecimal(other)))
    override operator fun plus(other: Long): BigDecimalNumber = valueOf(value.add(BigDecimal.valueOf(other)))
    override operator fun plus(other: String): BigDecimalNumber = valueOf(value.add(BigDecimal(other)))
    override operator fun plus(other: Double): BigDecimalNumber = valueOf(value.add(BigDecimal.valueOf(other)))
    override operator fun plus(other: BigDecimal): BigDecimalNumber = valueOf(value.add(other))
    override operator fun plus(other: BigInteger): BigDecimalNumber = valueOf(value.add(other.toBigDecimal()))

    override operator fun minus(other: DecimalNumber): BigDecimalNumber = valueOf(value.subtract(other.toBigDecimal()))
    override operator fun minus(other: Int): BigDecimalNumber = valueOf(value.subtract(BigDecimal(other)))
    override operator fun minus(other: Long): BigDecimalNumber = valueOf(value.subtract(BigDecimal.valueOf(other)))
    override operator fun minus(other: String): BigDecimalNumber = valueOf(value.subtract(BigDecimal(other)))
    override operator fun minus(other: Double): BigDecimalNumber = valueOf(value.subtract(BigDecimal.valueOf(other)))
    override operator fun minus(other: BigDecimal): BigDecimalNumber = valueOf(value.subtract(other))
    override operator fun minus(other: BigInteger): BigDecimalNumber = valueOf(value.subtract(other.toBigDecimal()))

    override operator fun times(other: DecimalNumber): BigDecimalNumber = valueOf(roundedOperation { value.multiply(other.toBigDecimal()) })
    override operator fun times(other: Int): BigDecimalNumber = valueOf(roundedOperation { value.multiply(BigDecimal(other)) })
    override operator fun times(other: Long): BigDecimalNumber = valueOf(roundedOperation { value.multiply(BigDecimal.valueOf(other)) })
    override operator fun times(other: String): BigDecimalNumber = valueOf(roundedOperation { value.multiply(BigDecimal(other)) })
    override operator fun times(other: Double): BigDecimalNumber = valueOf(roundedOperation { value.multiply(BigDecimal.valueOf(other)) })
    override operator fun times(other: BigDecimal): BigDecimalNumber = valueOf(roundedOperation { value.multiply(other) })
    override operator fun times(other: BigInteger): BigDecimalNumber = valueOf(roundedOperation { value.multiply(other.toBigDecimal()) })

    override operator fun div(other: DecimalNumber): BigDecimalNumber = valueOf(value.divide(other.toBigDecimal(), DEFAULT_SCALE, RoundingMode.HALF_EVEN))
    override operator fun div(other: Int): BigDecimalNumber = valueOf(value.divide(BigDecimal(other), DEFAULT_SCALE, RoundingMode.HALF_EVEN))
    override operator fun div(other: Long): BigDecimalNumber = valueOf(value.divide(BigDecimal.valueOf(other), DEFAULT_SCALE, RoundingMode.HALF_EVEN))
    override operator fun div(other: String): BigDecimalNumber = valueOf(value.divide(BigDecimal(other), DEFAULT_SCALE, RoundingMode.HALF_EVEN))
    override operator fun div(other: Double): BigDecimalNumber = valueOf(value.divide(BigDecimal.valueOf(other), DEFAULT_SCALE, RoundingMode.HALF_EVEN))
    override operator fun div(other: BigDecimal): BigDecimalNumber = valueOf(value.divide(other, DEFAULT_SCALE, RoundingMode.HALF_EVEN))
    override operator fun div(other: BigInteger): BigDecimalNumber = valueOf(value.divide(other.toBigDecimal(), DEFAULT_SCALE, RoundingMode.HALF_EVEN))

    override operator fun rem(other: DecimalNumber): BigDecimalNumber = valueOf(value.remainder(other.toBigDecimal()))
    override operator fun rem(other: Int): BigDecimalNumber = valueOf(value.remainder(BigDecimal(other)))
    override operator fun rem(other: Long): BigDecimalNumber = valueOf(value.remainder(BigDecimal.valueOf(other)))
    override operator fun rem(other: String): BigDecimalNumber = valueOf(value.remainder(BigDecimal(other)))
    override operator fun rem(other: Double): BigDecimalNumber = valueOf(value.remainder(BigDecimal.valueOf(other)))
    override operator fun rem(other: BigDecimal): BigDecimalNumber = valueOf(value.remainder(other))
    override operator fun rem(other: BigInteger): BigDecimalNumber = valueOf(value.remainder(other.toBigDecimal()))

    override operator fun unaryMinus(): BigDecimalNumber = this.negate()
    override fun inc(): BigDecimalNumber = valueOf(value.add(BigDecimal.ONE))
    override fun dec(): BigDecimalNumber = valueOf(value.subtract(BigDecimal.ONE))

    // Uses static method shadowing so calling BigDecimalNumber.valueOf calls these methods - even though its superclass
    // has the same static methods.
    companion object : DecimalNumberFactory<BigDecimalNumber> {
        // commonly used constants
        @JvmField val ZERO: BigDecimalNumber = valueOf(0)
        @JvmField val ONE: BigDecimalNumber = valueOf(1)
        @JvmField val TWO: BigDecimalNumber = valueOf(2)
        @JvmField val THREE: BigDecimalNumber = valueOf(3)
        @JvmField val FOUR: BigDecimalNumber = valueOf(4)
        @JvmField val FIVE: BigDecimalNumber = valueOf(5)
        @JvmField val SIX: BigDecimalNumber = valueOf(6)
        @JvmField val SEVEN: BigDecimalNumber = valueOf(7)
        @JvmField val EIGHT: BigDecimalNumber = valueOf(8)
        @JvmField val NINE: BigDecimalNumber = valueOf(9)
        @JvmField val TEN: BigDecimalNumber = valueOf(10)
        @JvmField val HUNDRED: BigDecimalNumber = valueOf(100)

        @JvmStatic
        override fun valueOf(value: BigDecimal): BigDecimalNumber = BigDecimalNumber(value)

        @JvmStatic
        override fun valueOf(value: BigInteger): BigDecimalNumber = BigDecimalNumber(value.toBigDecimal())

        @JvmStatic
        override fun valueOf(value: Long): BigDecimalNumber = BigDecimalNumber(BigDecimal.valueOf(value))

        @JvmStatic
        override fun valueOf(value: Int): BigDecimalNumber = BigDecimalNumber(BigDecimal(value))

        @JvmStatic
        override fun valueOf(value: Double): BigDecimalNumber = BigDecimalNumber(BigDecimal.valueOf(value))

        @JvmStatic
        override fun valueOf(value: String): BigDecimalNumber = BigDecimalNumber(BigDecimal(value))

        private inline fun roundedOperation(operation: () -> BigDecimal): BigDecimal {
            val result = operation()
            // optimization - no need to scale up
            if (result.scale() <= DEFAULT_SCALE) {
                return result
            }
            return result.setScale(DEFAULT_SCALE, RoundingMode.HALF_EVEN)
        }
    }
}

fun BigDecimal.toBigDecimalNumber(): BigDecimalNumber = BigDecimalNumber.valueOf(this)
fun BigInteger.toBigDecimalNumber(): BigDecimalNumber = BigDecimalNumber.valueOf(this)
fun Long.toBigDecimalNumber(): BigDecimalNumber = BigDecimalNumber.valueOf(this)
fun Int.toBigDecimalNumber(): BigDecimalNumber = BigDecimalNumber.valueOf(this)
fun String.toBigDecimalNumber(): BigDecimalNumber = BigDecimalNumber.valueOf(this)
fun Double.toBigDecimalNumber(): BigDecimalNumber = BigDecimalNumber.valueOf(this)
