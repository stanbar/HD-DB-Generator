package generator.data

import generator.RandomDataGenerator

data class Teacher(val name: String,
        val surname: String,
        val title: String) {
    val pesel: String = RandomDataGenerator.randomPesel()
}

