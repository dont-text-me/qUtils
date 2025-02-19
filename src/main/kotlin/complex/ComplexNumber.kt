package complex

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

sealed class ComplexNumber {
    companion object {
        @JvmInline
        value class ComplexNumberCartBuilder(val real: Number)

        @JvmInline
        value class ComplexNumberPolarBuilder(val r: Number)

        fun ofReal(real: Number) = ComplexNumberCartBuilder(real)

        fun ofR(r: Number) = ComplexNumberPolarBuilder(r)

        infix fun ComplexNumberCartBuilder.withImaginary(imaginary: Number) = ComplexNumberCart(this.real, imaginary)

        infix fun ComplexNumberPolarBuilder.withTheta(theta: Number) = ComplexNumberPolar(this.r, theta)
    }

    fun toPolar(): ComplexNumberPolar =
        when (this) {
            is ComplexNumberPolar -> this
            is ComplexNumberCart -> {
                val r = sqrt(this.real.pow(2) + this.imaginary.pow(2))
                val theta =
                    acos(
                        this.real / r,
                    )
                ComplexNumberPolar(r, theta)
            }
        }

    fun toCartesian(): ComplexNumberCart =
        when (this) {
            is ComplexNumberCart -> this
            is ComplexNumberPolar -> {
                val real = cos(this.theta) * this.r
                val imaginary = sin(this.theta) * this.r
                ComplexNumberCart(real, imaginary)
            }
        }

    operator fun plus(other: ComplexNumber) =
        this.toCartesian().let {
            ComplexNumberCart(it.real + other.toCartesian().real, it.imaginary + other.toCartesian().imaginary)
        }

    operator fun plus(other: Number) = this.toCartesian().let { ComplexNumberCart(it.real + other.toDouble(), it.imaginary) }

    override fun equals(other: Any?) =
        when (other) {
            is ComplexNumber ->
                this.toCartesian().real.equals(
                    other.toCartesian().real,
                ) && this.toCartesian().imaginary.equals(other.toCartesian().imaginary)
            is Number -> this.toCartesian().real == other && this.toCartesian().imaginary == 0.0
            else -> false
        }

    operator fun unaryMinus() = this.toCartesian().let { ComplexNumberCart(-it.real, -it.imaginary) }

    operator fun minus(other: ComplexNumber) = this + (-other)

    operator fun minus(other: Number) = this + (-(other.toDouble()))

    operator fun times(other: Number) =
        this.toCartesian().let { ComplexNumberCart(it.real * (other.toDouble()), it.imaginary * other.toDouble()) }

    operator fun times(other: ComplexNumber) =
        this.toCartesian().let {
            val otherCart = other.toCartesian()
            ComplexNumberCart(
                (it.real * otherCart.real) - (it.imaginary * otherCart.imaginary),
                (it.real * otherCart.imaginary) + (otherCart.real * it.imaginary),
            )
        }

    fun modSquared() = this.toPolar().r

    fun conjugate() =
        when (this) {
            is ComplexNumberCart -> ComplexNumberCart(this.real, -this.imaginary)
            is ComplexNumberPolar -> ComplexNumberPolar(this.r, -this.theta)
        }

    fun inverse() =
        this.conjugate().toCartesian().let {
            ComplexNumberCart(it.real / it.modSquared(), -it.imaginary / it.modSquared())
        }

    operator fun div(other: ComplexNumber) = this * other.inverse()

    operator fun div(other: Number) =
        this.toCartesian().let {
            ComplexNumberCart(it.real / other.toDouble(), it.imaginary / other.toDouble())
        }

    override fun hashCode() = javaClass.hashCode()
}

data class ComplexNumberCart(
    val real: Double,
    val imaginary: Double,
) : ComplexNumber() {
    constructor(real: Number, imaginary: Number) : this(real.toDouble(), imaginary.toDouble())

    override fun toString() =
        when {
            this.imaginary > 0 -> "$real + ${imaginary}i"
            this.imaginary <= 0 -> "$real - ${-imaginary}i"
            this.imaginary.equals(0.0) -> "$real"
            else -> throw IllegalArgumentException("Could not process complex numbers with components $real and $imaginary")
        }
}

data class ComplexNumberPolar(
    val r: Double,
    val theta: Double,
) : ComplexNumber() {
    constructor(r: Number, theta: Number) : this(r.toDouble(), theta.toDouble())

    override fun toString() = "$r * e ^ (i * $theta)"
}
