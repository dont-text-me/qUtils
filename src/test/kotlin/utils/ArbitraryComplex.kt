package utils

import complex.ComplexNumberCart
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.numericDouble


val arbitraryComplex = arbitrary {
    val real = Arb.double().bind()
    val im = Arb.double().bind()
    ComplexNumberCart(real, im)
}