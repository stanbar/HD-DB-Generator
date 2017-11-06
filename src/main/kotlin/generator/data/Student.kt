package generator.data

import generator.RandomDataGenerator

data class Student(val class_id: Int,
                   val name: String,
                   val surname: Int){
    val pesel: String = RandomDataGenerator.randomPesel()
}
