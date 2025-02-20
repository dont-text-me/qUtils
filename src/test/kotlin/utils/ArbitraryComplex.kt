package utils

import complex.ComplexNumber
import complex.ComplexNumber.Companion.i
import complex.ComplexNumberCart
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

private val shrinker =
    Shrinker<ComplexNumber> { num ->
        listOf(num + 0.5, num - 0.5, num + 0.5.i, num - 0.5.i)
    }

val arbitraryComplex =
    arbitrary(shrinker) {
        val real = Arb.double().bind()
        val im = Arb.double().bind()
        ComplexNumberCart(real, im)
    }
