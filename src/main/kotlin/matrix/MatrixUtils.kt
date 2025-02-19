package matrix

import complex.ComplexNumber
import kotlin.collections.all
import kotlin.collections.first
import kotlin.math.abs
import kotlin.math.pow

typealias Matrix<T> = Array<Array<T>>

class MatrixUtils {
    companion object {
        private fun Double.withinToleranceOf(
            other: Double,
            tol: Double = 10e-14,
        ) = abs(this - other) <= tol

        /**
         * Generate a unitary matrix that contains 1's on its main diagonal and zeros elsewhere
         * */
        fun unitary2d(size: Int): Matrix<Int> = (0..<size).map { row -> Array(row) { 0 } + 1 + Array(size - row - 1) { 0 } }.toTypedArray()

        inline fun <reified T> Matrix<T>.transpose() = Array(this.size) { idx -> this.map { it[idx] }.toTypedArray<T>() }

        fun <T> Matrix<T>.display() = this.forEach { println(it.toList()) }

        fun Matrix<ComplexNumber>.complexConjugate() = this.map { it.map { num -> num.conjugate() }.toTypedArray() }.toTypedArray()

        fun Array<ComplexNumber>.complexConjugate() = this.map { it.conjugate() }.toTypedArray()

        fun Matrix<ComplexNumber>.hermitianConjugate() = this.complexConjugate().transpose()

        fun Matrix<ComplexNumber>.isIdentityMatrix() =
            (0..<this.size).all { row ->
                this[row].count { it.toCartesian().real.equals(1.0) && it.toCartesian().imaginary.equals(0.0) } == 1 &&
                    this[row].indexOfFirst {
                        it.toCartesian().real.equals(1.0) &&
                            it.toCartesian().imaginary.equals(
                                0.0,
                            )
                    } == row
            }

        fun <T : Number> Matrix<T>.isIdentityMatrix() =
            (0..<this.size).all { row ->
                this[row].count { it.toDouble().withinToleranceOf(1.0, 10e-12) } == 1 &&
                    this[row].indexOfFirst { it.toDouble().withinToleranceOf(1.0, 10e-12) } == row
            }

        infix fun <T : Number> Array<T>.dot(other: Array<T>): Double {
            if (this.size != other.size) throw IllegalArgumentException("Mismatched array length: ${this.size}, ${other.size}")
            return this.zip(other).sumOf { (a, b) -> a.toDouble() * b.toDouble() }
        }

        infix fun Array<ComplexNumber>.dot(other: Array<ComplexNumber>): ComplexNumber {
            if (this.size != other.size) throw IllegalArgumentException("Mismatched array length: ${this.size}, ${other.size}")
            return this.zip(other, ComplexNumber::times).reduce(ComplexNumber::plus)
        }

        infix fun <T : Number> Array<ComplexNumber>.dot(other: Array<T>): ComplexNumber {
            if (this.size != other.size) throw IllegalArgumentException("Mismatched array length: ${this.size}, ${other.size}")
            return this.zip(other, ComplexNumber::times).reduce(ComplexNumber::plus)
        }

        /**
         * Throw an exception if two matrices cannot be used to calculate their dot product.
         * */
        fun <T : Any, R : Any> checkMatricesForDotProduct(
            m1: Matrix<T>,
            m2: Matrix<R>,
        ) {
            // step 1: check both matrices are well-formed i.e. all rows are of the same width
            if (!m1.all { it.size == m1[0].size }) throw IllegalArgumentException("Left matrix is not well-formed")
            if (!m2.all { it.size == m2[0].size }) throw IllegalArgumentException("Right matrix is not well-formed")

            // step 2: check that the dot product can be calculated - check that left matrix has as many columns as the right one has rows
            if (m1.first().size != m2.size) {
                throw IllegalArgumentException(
                    "Cannot multiply matrices of dimension (${m1.size}, ${m1[0].size}) and (${m2.size}, ${m2[0].size})",
                )
            }
        }

        inline infix fun <reified T : Number> Matrix<T>.dot(other: Matrix<T>): Matrix<Double> {
            checkMatricesForDotProduct(this, other)
            return this.map { row ->
                other.transpose().map { row dot it }.toTypedArray()
            }.toTypedArray()
        }

        inline infix fun <reified T : Number> Matrix<T>.dot(other: Matrix<ComplexNumber>): Matrix<ComplexNumber> {
            checkMatricesForDotProduct(this, other)
            return this.map { row ->
                other.transpose().map { it dot row }.toTypedArray()
            }.toTypedArray()
        }

        infix fun Matrix<ComplexNumber>.dot(other: Matrix<ComplexNumber>): Matrix<ComplexNumber> {
            checkMatricesForDotProduct(this, other)
            return this.map { row ->
                other.transpose().map { row dot it }.toTypedArray()
            }.toTypedArray()
        }

        inline infix fun <reified T : Number> Matrix<ComplexNumber>.dot(other: Matrix<T>): Matrix<ComplexNumber> {
            checkMatricesForDotProduct(this, other)
            return this.map { row ->
                other.transpose().map { row dot it }.toTypedArray()
            }.toTypedArray()
        }

        infix fun <T : Any> Matrix<T>.isEqualTo(other: Matrix<T>) =
            this.zip(other).all { (lRow, rRow) ->
                lRow.zip(rRow).all { (a, b) -> a == b }
            }

        fun Matrix<ComplexNumber>.isUnitary() = (this dot this.hermitianConjugate()).isIdentityMatrix()

        fun Matrix<ComplexNumber>.isHermitian() = this.hermitianConjugate() isEqualTo this

        fun <T : Number> Array<T>.vectorLength() = this.sumOf { it.toDouble().pow(2) }

        fun Array<ComplexNumber>.vectorLength() = this.zip(this.complexConjugate(), ComplexNumber::times).reduce(ComplexNumber::plus)
    }
}
