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

class BigDecimalNumberTest : StringSpec({
    "equality, hashcode, and compareTo are based on value" {
        forAll(
                row(BigDecimalNumber.valueOf(100), BigDecimal(100)),
                row(BigDecimalNumber.valueOf(100.12412), BigDecimal("100.12412000")),
                row(BigDecimalNumber.valueOf(0.00012412), BigDecimal("0.00012412000")),
        ) { wrapped, raw ->
            wrapped shouldBe BigDecimalNumber.valueOf(raw)
            wrapped.hashCode() shouldBe BigDecimalNumber.valueOf(raw).hashCode()
            wrapped.compareTo(BigDecimalNumber.valueOf(raw)) shouldBe 0
        }
    }

    "rounding mode same as with whole number division" {
        val value = BigDecimalNumber.valueOf(99)
        val other = BigDecimalNumber.valueOf(88)

        (value / other).toInt() shouldBe (99 / 88)
    }

    "add returns an instance of BigDecimalNumber" {
        val value = BigDecimalNumber.valueOf(100)
        val other = BigDecimalNumber.valueOf("2.123")
        value.add(other)::class.java shouldBe BigDecimalNumber::class.java
    }

    "subtract returns an instance of BigDecimalNumber" {
        val value = BigDecimalNumber.valueOf(100)
        val other = BigDecimalNumber.valueOf("2.123")
        value.subtract(other)::class.java shouldBe BigDecimalNumber::class.java
    }

    "multiply returns an instance of BigDecimalNumber" {
        val value = BigDecimalNumber.valueOf(100)
        val other = BigDecimalNumber.valueOf("2.123")
        value.multiply(other)::class.java shouldBe BigDecimalNumber::class.java
    }

    "divide returns an instance of BigDecimalNumber" {
        val value = BigDecimalNumber.valueOf(100)
        val other = BigDecimalNumber.valueOf("2.123")
        value.divide(other, RoundingMode.HALF_DOWN)::class.java shouldBe BigDecimalNumber::class.java
    }

    "pow returns an instance of BigDecimalNumber" {
        val value = BigDecimalNumber.valueOf(100)
        value.pow(2)::class.java shouldBe BigDecimalNumber::class.java
    }

    "remainder returns an instance of BigDecimalNumber" {
        val value = BigDecimalNumber.valueOf(100)
        val other = BigDecimalNumber.valueOf("2.123")
        value.remainder(other)::class.java shouldBe BigDecimalNumber::class.java
    }

    "negate returns opposite signum number" {
        val value = BigDecimalNumber.valueOf(100)
        value.negate() shouldBe BigDecimalNumber.valueOf(-100)
        value.negate().negate() shouldBe BigDecimalNumber.valueOf(100)
    }

    "abs returns absolute value" {
        val value = BigDecimalNumber.valueOf(100)
        value.abs() shouldBe value
        value.negate().abs() shouldBe value
    }

    "signum returns correct value" {
        BigDecimalNumber.valueOf(0).signum() shouldBe 0
        BigDecimalNumber.valueOf(-100).signum() shouldBe -1
        BigDecimalNumber.valueOf(100).signum() shouldBe 1
    }

    "division by zero fails" {
        val value = BigDecimalNumber.valueOf(10.0)
        shouldThrow<ArithmeticException> { value.divide(BigDecimalNumber.ZERO, RoundingMode.DOWN) }
        shouldThrow<ArithmeticException> { value / 0.0 }
        shouldThrow<ArithmeticException> { value / 0 }
        shouldThrow<ArithmeticException> { value / 0L }
        shouldThrow<ArithmeticException> { value / "0" }
        shouldThrow<ArithmeticException> { value / BigDecimal.ZERO }
        shouldThrow<ArithmeticException> { value / BigInteger.ZERO }
    }

    "remainder by zero fails" {
        val value = BigDecimalNumber.valueOf(10.0)
        shouldThrow<ArithmeticException> { value.remainder(BigDecimalNumber.ZERO) }
        shouldThrow<ArithmeticException> { value % 0.0 }
        shouldThrow<ArithmeticException> { value % 0 }
        shouldThrow<ArithmeticException> { value % 0L }
        shouldThrow<ArithmeticException> { value % "0" }
        shouldThrow<ArithmeticException> { value % BigDecimal.ZERO }
        shouldThrow<ArithmeticException> { value % BigInteger.ZERO }
    }

    "compareTo with non-finite number fails" {
        val value = BigDecimalNumber.valueOf(10.0)
        shouldThrow<NumberFormatException> { value > Double.NaN }
        shouldThrow<NumberFormatException> { value > Double.POSITIVE_INFINITY }
        shouldThrow<NumberFormatException> { value > Double.NEGATIVE_INFINITY }
        shouldThrow<NumberFormatException> { value > "NaN" }
        shouldThrow<NumberFormatException> { value > "Infinity" }
        shouldThrow<NumberFormatException> { value > "-Infinity" }
    }

    "valueOf with non-finite number fails" {
        shouldThrow<NumberFormatException> { BigDecimalNumber.valueOf(Double.NaN) }
        shouldThrow<NumberFormatException> { BigDecimalNumber.valueOf(Double.POSITIVE_INFINITY) }
        shouldThrow<NumberFormatException> { BigDecimalNumber.valueOf(Double.NEGATIVE_INFINITY) }

        shouldThrow<NumberFormatException> { BigDecimalNumber.valueOf("NaN") }
        shouldThrow<NumberFormatException> { BigDecimalNumber.valueOf("Infinity") }
        shouldThrow<NumberFormatException> { BigDecimalNumber.valueOf("-Infinity") }
    }

    val numberGeneratorFactory: () -> Arb<Double> = { Arb.numericDoubles(from = -12414.0, to = 121414.0) }
    val nonZeroNumberGeneratorFactory: () -> Arb<Double> = { numberGeneratorFactory().filter { it != 0.0 } }
    "covariant max" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = BigDecimalNumber.valueOf(initial)
            val receiverParam = BigDecimalNumber.valueOf(param)
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
            val receiver = BigDecimalNumber.valueOf(initial)
            val receiverParam = BigDecimalNumber.valueOf(param)
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

    "operator: minus" {
        checkAll(numberGeneratorFactory(), numberGeneratorFactory()) { initial, param ->
            val receiver = BigDecimalNumber.valueOf(initial)
            val result = receiver - BigDecimalNumber.valueOf(param)

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
            val receiver = BigDecimalNumber.valueOf(initial)
            val result = receiver * BigDecimalNumber.valueOf(param)

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
            val receiver = BigDecimalNumber.valueOf(initial)
            val result = receiver / BigDecimalNumber.valueOf(param)

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
        checkAll(nonZeroNumberGeneratorFactory(), nonZeroNumberGeneratorFactory()) { initial, param ->
            val receiver = BigDecimalNumber.valueOf(initial)
            val result = receiver % BigDecimalNumber.valueOf(param)

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
            val receiver = BigDecimalNumber.valueOf(initial)
            val result = receiver > BigDecimalNumber.valueOf(param)

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