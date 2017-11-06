package generator

import generator.data.Klass
import generator.data.Teacher

const val insertDate = 2015
const val updateOneDate = 2016
const val updateTwoDate = 2017

fun main(args: Array<String>) {

    generateInserts()
    generateUpdates()
    generateExcel()

}


private fun generateInserts() {
    for (sign in CharRange('A', 'C'))
        generateClass(sign)

}

fun generateClass(sign: Char) {
    val tutor = Teacher(name = RandomDataGenerator.names.random(),
            surname = RandomDataGenerator.surnames.random(),
            title = RandomDataGenerator.titles.random())
    val klass = Klass(sign, tutor.pesel, 2017)
}

private fun generateUpdates() {

}

private fun generateExcel() {

}