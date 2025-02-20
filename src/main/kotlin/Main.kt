import complex.ComplexNumber.Companion.i
import complex.ComplexNumber.Companion.plus
import complex.ComplexNumber.Companion.ofR
import complex.ComplexNumber.Companion.withTheta

fun main() {
    val cartesian = 3 + 3.i
    val polar = ofR(3) withTheta 5
    println(cartesian.inverse())
    println(polar.conjugate())

    println(cartesian + polar)
}
