import complex.ComplexNumberCart
import complex.ComplexNumberPolar
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.sqrt

class ComplexNumberTest {
    @Nested
    inner class ConversionTests {
        @Test
        fun `cartesian to polar`() {
            val cartesian = ComplexNumberCart(3.0, -4.0)

            expectThat(cartesian.toPolar()) {
                get { this.r }.isEqualTo(5.0)
                get { this.theta }.isEqualTo(acos(3.0 / 5.0))
            }
        }

        @Test
        fun `polar to cartesian`() {
            val polar = ComplexNumberPolar(5.0, PI / 3.0)

            expectThat(polar.toCartesian()) {
                get { real }.isEqualTo(2.5, tolerance = 10e-14)
                get { imaginary }.isEqualTo((5 * sqrt(3.0)) / 2.0)
            }
        }
    }

    @Nested
    inner class AdditionTests {
        @Test
        fun `adding two cartesian numbers`() {
            val left = ComplexNumberCart(4, -3)
            val right = ComplexNumberCart(5, 2)

            val result = left + right

            expectThat(result) {
                get { real }.isEqualTo(9.0)
                get { imaginary }.isEqualTo(-1.0)
            }
        }

        @Test
        fun `adding is commutative`() {
            val left = ComplexNumberCart(4, -3)
            val right = ComplexNumberCart(5, 2)

            val lr = left + right
            val rl = right + left
            expect {
                that(lr.real).isEqualTo(rl.real)
                that(lr.imaginary).isEqualTo(rl.imaginary)
            }
        }

        @Test
        fun `adding real and complex`() {
            val complex = ComplexNumberCart(4, -3)

            expectThat(complex + 3) {
                get { real }.isEqualTo(7.0)
                get { imaginary }.isEqualTo(-3.0)
            }
        }
    }

    @Nested
    inner class MultiplicationTests {
        @Test
        fun `multiplying two cartesian numbers`() {
            val left = ComplexNumberCart(3, -4) // 3 - 4i
            val right = ComplexNumberCart(5, 7) // 5 + 7i
            // (3 - 4i) (5 + 7i) = 15 + 21i - 20i + 28 = 43 + 1i
            expectThat(left * right) {
                get { real }.isEqualTo(43.0)
                get { imaginary }.isEqualTo(1.0)
            }
        }

        @Test
        fun `multiplying is commutative`() {
            val left = ComplexNumberCart(3, -4)
            val right = ComplexNumberCart(5, 7)
            val lr = left * right
            val rl = right * left
            expect {
                that(lr.real).isEqualTo(rl.real)
                that(lr.imaginary).isEqualTo(rl.imaginary)
            }
        }

        @Test
        fun `multiplying cartesian and polar`() {
            val left = ComplexNumberCart(5, 7) // 5 + 7i
            val right = ComplexNumberPolar(5, -acos(3.0 / 5.0)) // 3 - 4i (equivalent)
            expectThat(left * right) {
                get { real }.isEqualTo(43.0)
                get { imaginary }.isEqualTo(1.0)
            }
        }

        @Test
        fun `multiplying real and complex`() {
            val left = ComplexNumberCart(5, 7)

            expectThat(left * 5) {
                get { real }.isEqualTo(25.0)
                get { imaginary }.isEqualTo(35.0)
            }
        }
    }
}
