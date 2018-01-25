package generator.data

import generator.State.Companion.calendars
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class MyCalendar(val calendar: Calendar)
//: Insertable
{
    init {
        calendars.add(this)
    }

    //override fun toInsert() = "$id;${dataFormat.format(date)};$year"
    val dataFormat: DateFormat = SimpleDateFormat("yyy-MM-dd")

    val id: Int = lastId.getAndIncrement()
    val date: Date = calendar.time
    val year: Int = calendar.get(Calendar.YEAR)

    companion object {
        private val lastId = AtomicInteger(1)
//        override val primaryKey: String = "ID"
//        override val tableName: String = "MyCalendar"
//        override val schema: String = "CREATE TABLE $tableName\n" +
//                "(\n" +
//                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
//                "    Date DATE NOT NULL,\n" +
//                "    Year INTEGER NOT NULL,\n" +
//                ")"
    }
}
