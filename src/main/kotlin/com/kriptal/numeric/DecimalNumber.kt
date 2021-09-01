package com.kriptal.numeric

import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.*

private const val DEFAULT_IMPLEMENTATION_PROPERTY = "decimalnumber.type"

abstract class DecimalNumber : Number(), Comparable<DecimalNumber> {

    abstract fun add(other: DecimalNumber): DecimalNumber

    abstract fun subtract(other: DecimalNumber): DecimalNumber

    abstract fun multiply(other: DecimalNumber): DecimalNumber

    abstract fun divide(other: DecimalNumber, roundingMode: RoundingMode): DecimalNumber

    abstract fun pow(n: Int): DecimalNumber

    abstract fun scaleByPowerOfTen(n: Int): DecimalNumber

    abstract fun remainder(other: DecimalNumber): DecimalNumber

    abstract fun negate(): DecimalNumber

    abstract fun abs(): DecimalNumber

    abstract fun signum(): Int

    // need to manually override from Number class. Kotlin compiler bug, see: https://youtrack.jetbrains.com/issue/KT-22972
    abstract override fun toDouble(): Double
    abstract override fun toFloat(): Float
    abstract override fun toLong(): Long
    abstract override fun toInt(): Int
    abstract override fun toChar(): Char
    abstract override fun toShort(): Short
    abstract override fun toByte(): Byte
    abstract fun toBigDecimal(): BigDecimal
    abstract fun toBigInteger(): BigInteger

    abstract fun max(other: DecimalNumber): DecimalNumber
    abstract fun max(other: Int): DecimalNumber
    abstract fun max(other: Long): DecimalNumber
    abstract fun max(other: String): DecimalNumber
    abstract fun max(other: Double): DecimalNumber
    abstract fun max(other: BigDecimal): DecimalNumber
    abstract fun max(other: BigInteger): DecimalNumber

    abstract fun min(other: DecimalNumber): DecimalNumber
    abstract fun min(other: Int): DecimalNumber
    abstract fun min(other: Long): DecimalNumber
    abstract fun min(other: String): DecimalNumber
    abstract fun min(other: Double): DecimalNumber
    abstract fun min(other: BigDecimal): DecimalNumber
    abstract fun min(other: BigInteger): DecimalNumber

    abstract override fun toString(): String

    // convert to double - hash-code needs to be consistent with "equals"
    override fun hashCode(): Int {
        return toDouble().hashCode()
    }

    // covariant equality methods cannot be provided - "==" only works on classes with type intersection
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DecimalNumber) return false

        return this.compareTo(other) == 0
    }

    abstract operator fun compareTo(other: Int): Int
    abstract operator fun compareTo(other: Long): Int
    abstract operator fun compareTo(other: String): Int
    abstract operator fun compareTo(other: Double): Int
    abstract operator fun compareTo(other: BigDecimal): Int
    abstract operator fun compareTo(other: BigInteger): Int

    /** Operator functions - specific types are abstract so optimized version can be implemented in each specific implementation*/

    abstract operator fun plus(other: DecimalNumber): DecimalNumber
    abstract operator fun plus(other: Int): DecimalNumber
    abstract operator fun plus(other: Long): DecimalNumber
    abstract operator fun plus(other: String): DecimalNumber
    abstract operator fun plus(other: Double): DecimalNumber
    abstract operator fun plus(other: BigDecimal): DecimalNumber
    abstract operator fun plus(other: BigInteger): DecimalNumber

    abstract operator fun minus(other: DecimalNumber): DecimalNumber
    abstract operator fun minus(other: Int): DecimalNumber
    abstract operator fun minus(other: Long): DecimalNumber
    abstract operator fun minus(other: String): DecimalNumber
    abstract operator fun minus(other: Double): DecimalNumber
    abstract operator fun minus(other: BigDecimal): DecimalNumber
    abstract operator fun minus(other: BigInteger): DecimalNumber

    abstract operator fun times(other: DecimalNumber): DecimalNumber
    abstract operator fun times(other: Int): DecimalNumber
    abstract operator fun times(other: Long): DecimalNumber
    abstract operator fun times(other: String): DecimalNumber
    abstract operator fun times(other: Double): DecimalNumber
    abstract operator fun times(other: BigDecimal): DecimalNumber
    abstract operator fun times(other: BigInteger): DecimalNumber

    abstract operator fun div(other: DecimalNumber): DecimalNumber
    abstract operator fun div(other: Int): DecimalNumber
    abstract operator fun div(other: Long): DecimalNumber
    abstract operator fun div(other: String): DecimalNumber
    abstract operator fun div(other: Double): DecimalNumber
    abstract operator fun div(other: BigDecimal): DecimalNumber
    abstract operator fun div(other: BigInteger): DecimalNumber

    abstract operator fun rem(other: DecimalNumber): DecimalNumber
    abstract operator fun rem(other: Int): DecimalNumber
    abstract operator fun rem(other: Long): DecimalNumber
    abstract operator fun rem(other: String): DecimalNumber
    abstract operator fun rem(other: Double): DecimalNumber
    abstract operator fun rem(other: BigDecimal): DecimalNumber
    abstract operator fun rem(other: BigInteger): DecimalNumber

    abstract operator fun unaryMinus(): DecimalNumber
    abstract operator fun inc(): DecimalNumber
    abstract operator fun dec(): DecimalNumber

    companion object : DecimalNumberFactory<DecimalNumber> {
        // Must be instantiated via reflection - if instantiated in "init" and a subclass is loaded before this one, its DecimalNumberFactory is not yet initialized,
        // causing this variable to be initialized to null.
        private val DEFAULT_TYPE_FACTORY: DecimalNumberFactory<*> = run {
            val defaultType = ImplementationType.valueOf(System.getProperty(DEFAULT_IMPLEMENTATION_PROPERTY, ImplementationType.BIGDECIMAL.toString()).uppercase(Locale.getDefault()))

            val c = DecimalNumber::class.java.classLoader.loadClass(defaultType.clazz.name).getDeclaredConstructor().also { it.trySetAccessible() }
            val factory = c.newInstance() as DecimalNumberFactory<*>
            LoggerFactory.getLogger(DecimalNumber::class.java).info("Default DecimalNumber implementation: {}", defaultType)
            return@run factory
        }

        // commonly used constants
        @JvmField val ZERO: DecimalNumber = valueOf(0)
        @JvmField val ONE: DecimalNumber = valueOf(1)
        @JvmField val TWO: DecimalNumber = valueOf(2)
        @JvmField val THREE: DecimalNumber = valueOf(3)
        @JvmField val FOUR: DecimalNumber = valueOf(4)
        @JvmField val FIVE: DecimalNumber = valueOf(5)
        @JvmField val SIX: DecimalNumber = valueOf(6)
        @JvmField val SEVEN: DecimalNumber = valueOf(7)
        @JvmField val EIGHT: DecimalNumber = valueOf(8)
        @JvmField val NINE: DecimalNumber = valueOf(9)
        @JvmField val TEN: DecimalNumber = valueOf(10)
        @JvmField val HUNDRED: DecimalNumber = valueOf(100)

        @JvmStatic
        override fun valueOf(value: BigDecimal): DecimalNumber = DEFAULT_TYPE_FACTORY.valueOf(value)

        @JvmStatic
        override fun valueOf(value: BigInteger): DecimalNumber = DEFAULT_TYPE_FACTORY.valueOf(value)

        @JvmStatic
        override fun valueOf(value: Long): DecimalNumber = DEFAULT_TYPE_FACTORY.valueOf(value) //long can fit into double - but with slight loss of precision for large numbers

        @JvmStatic
        override fun valueOf(value: Int): DecimalNumber = DEFAULT_TYPE_FACTORY.valueOf(value)

        @JvmStatic
        override fun valueOf(value: Double): DecimalNumber = DEFAULT_TYPE_FACTORY.valueOf(value)

        @JvmStatic
        override fun valueOf(value: String): DecimalNumber = DEFAULT_TYPE_FACTORY.valueOf(value)
    }
}

/**
 * [DecimalNumber] factory with all supported constructor types. Concrete implementations' companion object should
 * implement this interface and annotate all overridden methods with [JvmStatic] annotation for Java interop.
 * */

interface DecimalNumberFactory<out T : DecimalNumber> {
    fun valueOf(value: BigDecimal): T

    fun valueOf(value: BigInteger): T

    fun valueOf(value: Long): T

    fun valueOf(value: Int): T

    fun valueOf(value: Double): T

    fun valueOf(value: String): T
}

private enum class ImplementationType(val clazz: Class<out DecimalNumberFactory<*>>) {
    BIGDECIMAL(BigDecimalNumber.Companion::class.java),
    DOUBLE(DoubleDecimalNumber.Companion::class.java),
    ;
}

fun BigDecimal.toDecimalNumber(): DecimalNumber = DecimalNumber.valueOf(this)
fun BigInteger.toDecimalNumber(): DecimalNumber = DecimalNumber.valueOf(this)
fun Long.toDecimalNumber(): DecimalNumber = DecimalNumber.valueOf(this)
fun Int.toDecimalNumber(): DecimalNumber = DecimalNumber.valueOf(this)
fun String.toDecimalNumber(): DecimalNumber = DecimalNumber.valueOf(this)
fun Double.toDecimalNumber(): DoubleDecimalNumber = DoubleDecimalNumber.valueOf(this)
