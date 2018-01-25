package generator.data

interface Schematable{
    val schema : String
    val tableName : String
    val primaryKey : String
}