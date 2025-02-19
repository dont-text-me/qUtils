import complex.ComplexNumber.Companion.ofR
import complex.ComplexNumber.Companion.ofReal
import complex.ComplexNumber.Companion.withImaginary
import complex.ComplexNumber.Companion.withTheta
import kotlin.math.PI

fun main() {
    val cartesian = ofReal(5) withImaginary 4
    val polar = ofR(3) withTheta (PI / 3)

    println(cartesian.inverse())
    println(polar.conjugate())

    println(cartesian + polar)
}
