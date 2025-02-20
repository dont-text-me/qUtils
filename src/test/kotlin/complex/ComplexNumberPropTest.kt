package complex

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.property.checkAll
import kotlin.math.sqrt

class ComplexNumberPropTest : DescribeSpec({
    describe("Addition property tests") {
        it("Adding two cartesian numbers modifies the real and imaginary fields") {
            checkAll<ComplexNumberCart, ComplexNumberCart> { a, b ->
                (a + b).real shouldBeEqual ((a.real) + (b.real))
                (a + b).imaginary shouldBeEqual ((a.imaginary) + (b.imaginary))
            }
        }

        it("Addition is commutative") {
            checkAll<ComplexNumber, ComplexNumber> { a, b ->
                a + b shouldBeEqual b + a
            }
        }

        it("Adding a real number only modifies the real part") {
            checkAll<ComplexNumber, Double> { a, b ->
                (a + b).imaginary shouldBeEqual a.toCartesian().imaginary
                (a + b).real shouldBeEqual (a.toCartesian().real + b.toDouble())
            }
        }
    }

    describe("Multiplication property tests") {
        it("Multiplying two cartesian numbers modifies the real and imaginary fields") {
            checkAll<ComplexNumberCart, ComplexNumberCart> { a, b ->
                (a * b).real shouldBeEqual (a.real * b.real) - (a.imaginary * b.imaginary)
                (a * b).imaginary shouldBeEqual (a.real * b.imaginary) + (b.real * a.imaginary)
            }
        }
        it("Multiplication is commutative") {
            checkAll<ComplexNumber, ComplexNumber> { a, b ->
                a * b shouldBeEqual b * a
            }
        }
        it("Multiplying by a real number modifies both parts") {
            checkAll<ComplexNumber, Double> { a, b ->
                (a * b).real shouldBeEqual a.toCartesian().real * b
                (a * b).imaginary shouldBeEqual a.toCartesian().imaginary * b
            }
        }
    }

    describe("Complex conjugate tests") {
        it("Complex conjugate of cartesian number negates the imaginary part") {
            checkAll<ComplexNumberCart> {
                it.conjugate().toCartesian().imaginary shouldBeEqual -it.imaginary
                it.conjugate().toCartesian().real shouldBeEqual it.real
            }
        }
        it("Complex conjugate of polar number negates theta") {
            checkAll<ComplexNumberPolar> {
                it.conjugate().toPolar().theta shouldBeEqual -it.theta
            }
        }
    }

    describe("Mod squared tests") {
        it("Mod squared of cartesian number should be equal to square root of the sum of the squares of its parts") {
            checkAll<ComplexNumberCart> {
                it.modSquared() shouldBeEqual sqrt(it.imaginary * it.imaginary + it.real * it.real)
            }
        }

        it("Mod squared of polar number should be equal to its length (r)") {
            checkAll<ComplexNumberPolar> {
                it.modSquared() shouldBeEqual it.r
            }
        }
    }

    describe("Negation tests") {
        it("Negating a cartesian number negates both its parts") {
            checkAll<ComplexNumberCart> {
                (-it).imaginary shouldBeEqual -(it.imaginary)
                (-it).real shouldBeEqual -(it.real)
            }
        }
    }

    describe("Inverse tests") {
        it("Inverse of cartesian number is equivalent to dividing each part by its mod squared value") {
            checkAll<ComplexNumberCart> {
                it.inverse().real shouldBeEqual it.real / sqrt(it.imaginary * it.imaginary + it.real * it.real)
                it.inverse().imaginary shouldBeEqual it.imaginary / sqrt(it.imaginary * it.imaginary + it.real * it.real)
            }
        }
    }
})
