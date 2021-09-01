package com.kriptal.numeric

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.numericDoubles
import io.kotest.property.checkAll
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.abs

class DoubleDecimalNumberTest : StringSpec({
    "equality, hashcode, and compareTo are based on value" {
        forAll(
                row(DoubleDecimalNumber.valueOf(100), BigDecimal(100)),
                row(DoubleDecimalNumber.valueOf(100.12412), BigDecimal("100.12412000")),
                row(DoubleDecimalNumber.valueOf(0.00012412), BigDecimal("0.00012412000")),
        ) { wrapped, raw ->
            wrapped shouldBe DoubleDecimalNumber.valueOf(raw)
            wrapped.hashCode() shouldBe DoubleDecimalNumber.valueOf(raw).hashCode()
            wrapped.compareTo(DoubleDecimalNumber.valueOf(raw)) shouldBe 0
        }
    }

    "rounding mode same as with whole number division" {
        val value = DoubleDecimalNumber.valueOf(99)
        val other = DoubleDecimalNumber.valueOf(88)

        (value / other).toInt() shouldBe (99 / 88)
    }


    "add returns an instance of DoubleDecimalNumber" {
        val value = DoubleDecimalNumber.valueOf(100)
        val other = DoubleDecimalNumber.valueOf("2.123")
        value.add(other)::class.java shouldBe DoubleDecimalNumber::class.java
    }

    "subtract returns an instance of DoubleDecimalNumber" {
        val value = DoubleDecimalNumber.valueOf(100)
        val other = DoubleDecimalNumber.valueOf("2.123")
        value.subtract(other)::class.java shouldBe DoubleDecimalNumber::class.java
    }

    "multiply returns an instance of DoubleDecimalNumber" {
        val value = DoubleDecimalNumber.valueOf(100)
        val other = DoubleDecimalNumber.valueOf("2.123")
        value.multiply(other)::class.java shouldBe DoubleDecimalNumber::class.java
    }

    "divide returns an instance of DoubleDecimalNumber" {
        val value = DoubleDecimalNumber.valueOf(100)
        val other = DoubleDecimalNumber.valueOf("2.123")
        value.divide(other, RoundingMode.HALF_DOWN)::class.java shouldBe DoubleDecimalNumber::class.java
    }

    "pow returns an instance of DoubleDecimalNumber" {
        val value = DoubleDecimalNumber.valueOf(100)
        value.pow(2)::class.java shouldBe DoubleDecimalNumber::class.java
    }

    "remainder returns an instance of DoubleDecimalNumber" {
        val value = DoubleDecimalNumber.valueOf(100)
        val other = DoubleDecimalNumber.valueOf("2.123")
        value.remainder(other)::class.java shouldBe DoubleDecimalNumber::class.java
    }

    "negate returns opposite signum number" {
        val value = DoubleDecimalNumber.valueOf(100)
        value.negate() shouldBe DoubleDecimalNumber.valueOf(-100)
        value.negate().negate() shouldBe DoubleDecimalNumber.valueOf(100)
    }

    "abs returns absolute value" {
        val value = DoubleDecimalNumber.valueOf(100)
        value.abs() shouldBe value
        value.negate().abs() shouldBe value
    }

    "signum returns correct value" {
        DoubleDecimalNumber.valueOf(0).signum() shouldBe 0
        DoubleDecimalNumber.valueOf(-100).signum() shouldBe -1
        DoubleDecimalNumber.valueOf(100).signum() shouldBe 1
    }

    "division by zero fails" {
        val value = DoubleDecimalNumber.valueOf(10.0)
        shouldThrow<ArithmeticException> { value.divide(DoubleDecimalNumber.ZERO, RoundingMode.DOWN) }
        shouldThrow<ArithmeticException> { value / 0.0 }
        shouldThrow<ArithmeticException> { value / 0 }
        shouldThrow<ArithmeticException> { value / 0L }
        shouldThrow<ArithmeticException> { value / "0" }
        shouldThrow<ArithmeticException> { value / BigDecimal.ZERO }
        shouldThrow<ArithmeticException> { value / BigInteger.ZERO }
    }

    "remainder by zero fails" {
        val value = DoubleDecimalNumber.valueOf(10.0)
        shouldThrow<ArithmeticException> { value.remainder(DoubleDecimalNumber.ZERO) }
        shouldThrow<ArithmeticException> { value % 0.0 }
        shouldThrow<ArithmeticException> { value % 0 }
        shouldThrow<ArithmeticException> { value % 0L }
        shouldThrow<ArithmeticException> { value % "0" }
        shouldThrow<ArithmeticException> { value % BigDecimal.ZERO }
        shouldThrow<ArithmeticException> { value % BigInteger.ZERO }
    }

    "compareTo with non-finite number fails" {
        val value = DoubleDecimalNumber.valueOf(10.0)
        shouldThrow<ArithmeticException> { value > Double.NaN }
        shouldThrow<ArithmeticException> { value > Double.POSITIVE_INFINITY }
        shouldThrow<ArithmeticException> { value > Double.NEGATIVE_INFINITY }
        shouldThrow<ArithmeticException> { value > "NaN" }
        shouldThrow<ArithmeticException> { value > "Infinity" }
        shouldThrow<ArithmeticException> { value > "-Infinity" }
    }

    "valueOf with non-finite number fails" {
        shouldThrow<ArithmeticException> { DoubleDecimalNumber.valueOf(Double.NaN) }
        shouldThrow<ArithmeticException> { DoubleDecimalNumber.valueOf(Double.POSITIVE_INFINITY) }
        shouldThrow<ArithmeticException> { DoubleDecimalNumber.valueOf(Double.NEGATIVE_INFINITY) }

        shouldThrow<ArithmeticException> { DoubleDecimalNumber.valueOf("NaN") }
        shouldThrow<ArithmeticException> { DoubleDecimalNumber.valueOf("Infinity") }
        shouldThrow<ArithmeticException> { DoubleDecimalNumber.valueOf("-Infinity") }
    }

    //due to precision loss we can get non-finite values (e.g. infinity) - limit zero-based values
    val numberGeneratorFactory: () -> Arb<Double> = {
        Arb.numericDoubles(from = -12414.0, to = 121414.0).filter { abs(it) > 0.0000000005 }
    }
    val nonZeroNumberGeneratorFactory: () -> Arb<Double> = { numberGeneratorFactory().filter { it != 0.0 } }
    "covariant max" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val receiverParam = DoubleDecimalNumber.valueOf(param)
            val result = receiver.max(receiverParam)

            receiverParam.max(receiver) shouldBe result

            receiver.max(param) shouldBe result
            receiver.max(param.toString()) shouldBe result
            receiver.max(param.toBigDecimal()) shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                receiver.max(param.toLong()) shouldBe result
                receiver.max(param.toInt()) shouldBe result
                receiver.max(param.toBigDecimal().toBigInteger()) shouldBe result
            }
        }
    }

    "covariant min" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val receiverParam = DoubleDecimalNumber.valueOf(param)
            val result = receiver.min(receiverParam)

            receiverParam.min(receiver) shouldBe result

            receiver.min(param) shouldBe result
            receiver.min(param.toString()) shouldBe result
            receiver.min(param.toBigDecimal()) shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                receiver.min(param.toLong()) shouldBe result
                receiver.min(param.toInt()) shouldBe result
                receiver.min(param.toBigDecimal().toBigInteger()) shouldBe result
            }
        }
    }
    "operator: plus" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val result = receiver + DoubleDecimalNumber.valueOf(param)

            receiver + param shouldBe result
            receiver + param.toString() shouldBe result
            receiver + param.toBigDecimal() shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                receiver + param.toLong() shouldBe result
                receiver + param.toInt() shouldBe result
                receiver + param.toBigDecimal().toBigInteger() shouldBe result
            }
        }
    }
    "operator: minus" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val result = receiver - DoubleDecimalNumber.valueOf(param)

            receiver - param shouldBe result
            receiver - param.toString() shouldBe result
            receiver - param.toBigDecimal() shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                receiver - param.toLong() shouldBe result
                receiver - param.toInt() shouldBe result
                receiver - param.toBigDecimal().toBigInteger() shouldBe result
            }
        }
    }

    "operator: multiply" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val result = receiver * DoubleDecimalNumber.valueOf(param)

            receiver * param shouldBe result
            receiver * param.toString() shouldBe result
            receiver * param.toBigDecimal() shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                receiver * param.toLong() shouldBe result
                receiver * param.toInt() shouldBe result
                receiver * param.toBigDecimal().toBigInteger() shouldBe result
            }
        }
    }

    "operator: divide" {
        checkAll(numberGeneratorFactory(), nonZeroNumberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val result = receiver / DoubleDecimalNumber.valueOf(param)

            receiver / param shouldBe result
            receiver / param.toString() shouldBe result
            receiver / param.toBigDecimal() shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                receiver / param.toLong() shouldBe result
                receiver / param.toInt() shouldBe result
                receiver / param.toBigDecimal().toBigInteger() shouldBe result
            }
        }
    }

    "operator: remainder" {
        checkAll(numberGeneratorFactory(), nonZeroNumberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val result = receiver % DoubleDecimalNumber.valueOf(param)

            receiver % param shouldBe result
            receiver % param.toString() shouldBe result
            receiver % param.toBigDecimal() shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                receiver % param.toLong() shouldBe result
                receiver % param.toInt() shouldBe result
                receiver % param.toBigDecimal().toBigInteger() shouldBe result
            }
        }
    }

    "operator: compareTo" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = DoubleDecimalNumber.valueOf(initial)
            val result = receiver > DoubleDecimalNumber.valueOf(param)

            (receiver > param) shouldBe result
            (receiver > param.toString()) shouldBe result
            (receiver > param.toBigDecimal()) shouldBe result
            val wholeNumber = try {
                param.toInt().toDouble() == param
            } catch (ignored: NumberFormatException) {
                false
            }

            if (wholeNumber) {
                (receiver > param.toLong()) shouldBe result
                (receiver > param.toInt()) shouldBe result
                (receiver > param.toBigDecimal().toBigInteger()) shouldBe result
            }
        }
    }
})
