package complex

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class ComplexNumberPropTest: DescribeSpec({
    describe("Addition property tests"){
        it("Adding two cartesian numbers modifies the real and imaginary fields"){
            checkAll <ComplexNumberCart, ComplexNumberCart> {a, b ->
                (a + b).real shouldBeEqual  ((a.real) + (b.real))
                (a + b).imaginary shouldBeEqual ((a.imaginary) + (b.imaginary))
            }
        }

        it("Addition is commutative"){
            checkAll <ComplexNumber, ComplexNumber> {a, b ->
                a + b shouldBeEqual  b + a
            }
        }
    }
})