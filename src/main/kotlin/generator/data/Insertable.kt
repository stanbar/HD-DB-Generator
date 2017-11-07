package generator.data

interface Insertable {
    fun toInsert() : String
}