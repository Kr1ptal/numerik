package com.kriptal.numeric

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow

class DoubleDecimalNumber private constructor(private val value: Double) : DecimalNumber() {
    override fun add(other: DecimalNumber): DoubleDecimalNumber {
        return DoubleDecimalNumber(value + other.toDouble())
    }

    override fun subtract(other: DecimalNumber): DoubleDecimalNumber {
        return DoubleDecimalNumber(value - other.toDouble())
    }

    override fun multiply(other: DecimalNumber): DoubleDecimalNumber {
        return DoubleDecimalNumber(value * other.toDouble())
    }

    // use valueOf for additional validation
    override fun divide(other: DecimalNumber, roundingMode: RoundingMode): DoubleDecimalNumber {
        return valueOf(value / getValidatedNonZeroNumber(other.toDouble()))
    }

    override fun pow(n: Int): DoubleDecimalNumber {
        return DoubleDecimalNumber(value.pow(n))
    }

    override fun scaleByPowerOfTen(n: Int): DoubleDecimalNumber {
        return DoubleDecimalNumber(value * (10.0.pow(n.toDouble())))
    }

    override fun remainder(other: DecimalNumber): DoubleDecimalNumber {
        return DoubleDecimalNumber(value % getValidatedNonZeroNumber(other.toDouble()))
    }

    override fun negate(): DoubleDecimalNumber {
        return DoubleDecimalNumber(-value)
    }

    override fun abs(): DoubleDecimalNumber {
        if (signum() >= 0) {
            return this
        }
        return DoubleDecimalNumber(-value)
    }

    override fun signum(): Int {
        return value.compareTo(0.0)
    }

    override fun toByte(): Byte {
        // Double#toByte() is deprecated - this is the recommended way
        return value.toInt().toByte()
    }

    override fun toChar(): Char {
        return value.toInt().toChar()
    }

    override fun toDouble(): Double {
        return value
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
        // Double#toShort() is deprecated - this is the recommended way
        return value.toInt().toShort()
    }

    override fun toBigDecimal(): BigDecimal {
        return BigDecimal.valueOf(value)
    }

    override fun toBigInteger(): BigInteger {
        return BigInteger.valueOf(value.toLong())
    }

    override fun compareTo(other: DecimalNumber): Int {
        // seems a bit faster than using Double.compare method
        val o = other.toDouble()
        if (value < o) return -1
        if (value > o) return 1
        return 0
    }

    override fun toString(): String {
        return value.toString()
    }

    // minimize instantiations of DecimalNumber wrapper classes
    fun max(other: DoubleDecimalNumber): DoubleDecimalNumber = if (this >= other) this else other
    override fun max(other: DecimalNumber): DoubleDecimalNumber = if (this >= other) this else valueOf(other.toDouble())
    override fun max(other: Int): DoubleDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: Long): DoubleDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: String): DoubleDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: Double): DoubleDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: BigDecimal): DoubleDecimalNumber = if (this >= other) this else valueOf(other)
    override fun max(other: BigInteger): DoubleDecimalNumber = if (this >= other) this else valueOf(other)

    // minimize instantiations of DecimalNumber wrapper classes
    fun min(other: DoubleDecimalNumber): DoubleDecimalNumber = if (this <= other) this else other
    override fun min(other: DecimalNumber): DoubleDecimalNumber = if (this <= other) this else valueOf(other.toDouble())
    override fun min(other: Int): DoubleDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: Long): DoubleDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: String): DoubleDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: Double): DoubleDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: BigDecimal): DoubleDecimalNumber = if (this <= other) this else valueOf(other)
    override fun min(other: BigInteger): DoubleDecimalNumber = if (this <= other) this else valueOf(other)

    override operator fun compareTo(other: Int): Int = value.compareTo(other)
    override operator fun compareTo(other: Long): Int = value.compareTo(other)
    override operator fun compareTo(other: String): Int = value.compareTo(getValidatedFiniteNumber(other.toDouble()))
    override operator fun compareTo(other: Double): Int = value.compareTo(getValidatedFiniteNumber(other))
    override operator fun compareTo(other: BigDecimal): Int = value.compareTo(other.toDouble())
    override operator fun compareTo(other: BigInteger): Int = value.compareTo(other.toDouble())

    override operator fun plus(other: DecimalNumber): DoubleDecimalNumber = valueOf(value + other.toDouble())
    override operator fun plus(other: Int): DoubleDecimalNumber = valueOf(value + other)
    override operator fun plus(other: Long): DoubleDecimalNumber = valueOf(value + other)
    override operator fun plus(other: String): DoubleDecimalNumber = valueOf(value + other.toDouble())
    override operator fun plus(other: Double): DoubleDecimalNumber = valueOf(value + other)
    override operator fun plus(other: BigDecimal): DoubleDecimalNumber = valueOf(value + other.toDouble())
    override operator fun plus(other: BigInteger): DoubleDecimalNumber = valueOf(value + other.toDouble())

    override operator fun minus(other: DecimalNumber): DoubleDecimalNumber = valueOf(value - other.toDouble())
    override operator fun minus(other: Int): DoubleDecimalNumber = valueOf(value - other)
    override operator fun minus(other: Long): DoubleDecimalNumber = valueOf(value - other)
    override operator fun minus(other: String): DoubleDecimalNumber = valueOf(value - other.toDouble())
    override operator fun minus(other: Double): DoubleDecimalNumber = valueOf(value - other)
    override operator fun minus(other: BigDecimal): DoubleDecimalNumber = valueOf(value - other.toDouble())
    override operator fun minus(other: BigInteger): DoubleDecimalNumber = valueOf(value - other.toDouble())

    override operator fun times(other: DecimalNumber): DoubleDecimalNumber = valueOf(value * other.toDouble())
    override operator fun times(other: Int): DoubleDecimalNumber = valueOf(value * other)
    override operator fun times(other: Long): DoubleDecimalNumber = valueOf(value * other)
    override operator fun times(other: String): DoubleDecimalNumber = valueOf(value * other.toDouble())
    override operator fun times(other: Double): DoubleDecimalNumber = valueOf(value * other)
    override operator fun times(other: BigDecimal): DoubleDecimalNumber = valueOf(value * other.toDouble())
    override operator fun times(other: BigInteger): DoubleDecimalNumber = valueOf(value * other.toDouble())

    override operator fun div(other: DecimalNumber): DoubleDecimalNumber = valueOf(value / getValidatedNonZeroNumber(other.toDouble()))
    override operator fun div(other: Int): DoubleDecimalNumber = valueOf(value / getValidatedNonZeroNumber(other.toDouble()))
    override operator fun div(other: Long): DoubleDecimalNumber = valueOf(value / getValidatedNonZeroNumber(other.toDouble()))
    override operator fun div(other: String): DoubleDecimalNumber = valueOf(value / getValidatedNonZeroNumber(other.toDouble()))
    override operator fun div(other: Double): DoubleDecimalNumber = valueOf(value / getValidatedNonZeroNumber(other))
    override operator fun div(other: BigDecimal): DoubleDecimalNumber = valueOf(value / getValidatedNonZeroNumber(other.toDouble()))
    override operator fun div(other: BigInteger): DoubleDecimalNumber = valueOf(value / getValidatedNonZeroNumber(other.toDouble()))

    override operator fun rem(other: DecimalNumber): DoubleDecimalNumber = valueOf(value % getValidatedNonZeroNumber(other.toDouble()))
    override operator fun rem(other: Int): DoubleDecimalNumber = valueOf(value % getValidatedNonZeroNumber(other.toDouble()))
    override operator fun rem(other: Long): DoubleDecimalNumber = valueOf(value % getValidatedNonZeroNumber(other.toDouble()))
    override operator fun rem(other: String): DoubleDecimalNumber = valueOf(value % getValidatedNonZeroNumber(other.toDouble()))
    override operator fun rem(other: Double): DoubleDecimalNumber = valueOf(value % getValidatedNonZeroNumber(other))
    override operator fun rem(other: BigDecimal): DoubleDecimalNumber = valueOf(value % getValidatedNonZeroNumber(other.toDouble()))
    override operator fun rem(other: BigInteger): DoubleDecimalNumber = valueOf(value % getValidatedNonZeroNumber(other.toDouble()))

    override operator fun unaryMinus(): DoubleDecimalNumber = this.negate()
    override fun inc(): DoubleDecimalNumber = valueOf(value + 1.0)
    override fun dec(): DoubleDecimalNumber = valueOf(value - 1.0)

    // Uses static method shadowing so calling DoubleDecimalNumber.valueOf calls these methods - even though its superclass
    // has the same static methods.
    @Suppress("NOTHING_TO_INLINE")
    companion object : DecimalNumberFactory<DoubleDecimalNumber> {
        // commonly used constants
        @JvmField val ZERO: DoubleDecimalNumber = valueOf(0)
        @JvmField val ONE: DoubleDecimalNumber = valueOf(1)
        @JvmField val TWO: DoubleDecimalNumber = valueOf(2)
        @JvmField val THREE: DoubleDecimalNumber = valueOf(3)
        @JvmField val FOUR: DoubleDecimalNumber = valueOf(4)
        @JvmField val FIVE: DoubleDecimalNumber = valueOf(5)
        @JvmField val SIX: DoubleDecimalNumber = valueOf(6)
        @JvmField val SEVEN: DoubleDecimalNumber = valueOf(7)
        @JvmField val EIGHT: DoubleDecimalNumber = valueOf(8)
        @JvmField val NINE: DoubleDecimalNumber = valueOf(9)
        @JvmField val TEN: DoubleDecimalNumber = valueOf(10)
        @JvmField val HUNDRED: DoubleDecimalNumber = valueOf(100)

        @JvmStatic
        override fun valueOf(value: BigDecimal): DoubleDecimalNumber = DoubleDecimalNumber(value.toDouble())

        @JvmStatic
        override fun valueOf(value: BigInteger): DoubleDecimalNumber = DoubleDecimalNumber(value.toDouble())

        @JvmStatic
        override fun valueOf(value: Long): DoubleDecimalNumber = DoubleDecimalNumber(value.toDouble())

        @JvmStatic
        override fun valueOf(value: Int): DoubleDecimalNumber = DoubleDecimalNumber(value.toDouble())

        @JvmStatic
        override fun valueOf(value: Double): DoubleDecimalNumber = DoubleDecimalNumber(getValidatedFiniteNumber(value))

        @JvmStatic
        override fun valueOf(value: String): DoubleDecimalNumber = DoubleDecimalNumber(getValidatedFiniteNumber(value.toDouble()))

        /**
         * Validates that provided number is not one of: [Double.NaN], [Double.POSITIVE_INFINITY], [Double.NEGATIVE_INFINITY].
         *
         * NOTE: Validation needs to be present only when wrapping [String] or [Double] (or [Float] if we decide to support it).
         * No other type can have non-finite value.
         *
         * @return provided number - if validation succeeds.
         * */
        private inline fun getValidatedFiniteNumber(number: Double): Double {
            if (!number.isFinite()) {
                throw ArithmeticException("Non-finite numbers are not supported: $number")
            }
            return number
        }

        /**
         * Validates that provided number is not 0.0 (ZERO). Necessary when using divide and remainder functions.
         *
         * NOTE: In theory, this could be omitted because division/remainder by ZERO produces [Double.NaN], which
         * would get validated by [getValidatedFiniteNumber]. We're keeping this to get a clear message that one tries to
         * divide by ZERO.
         *
         * @return provided number - if validation succeeds.
         * */
        private inline fun getValidatedNonZeroNumber(number: Double): Double {
            if (number == 0.0) {
                throw ArithmeticException("Cannot divide by 0.")
            }
            return number
        }
    }
}

fun BigDecimal.toDoubleDecimalNumber(): DoubleDecimalNumber = DoubleDecimalNumber.valueOf(this)
fun BigInteger.toDoubleDecimalNumber(): DoubleDecimalNumber = DoubleDecimalNumber.valueOf(this)
fun Long.toDoubleDecimalNumber(): DoubleDecimalNumber = DoubleDecimalNumber.valueOf(this)
fun Int.toDoubleDecimalNumber(): DoubleDecimalNumber = DoubleDecimalNumber.valueOf(this)
fun String.toDoubleDecimalNumber(): DoubleDecimalNumber = DoubleDecimalNumber.valueOf(this)
fun Double.toDoubleDecimalNumber(): DoubleDecimalNumber = DoubleDecimalNumber.valueOf(this)
