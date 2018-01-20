package generator

import java.util.*
import java.util.concurrent.ThreadLocalRandom


fun Array<String>.random() = this[ThreadLocalRandom.current().nextInt(size)].capitalize()
fun Date.random(from: Date, to: Date) = Date(ThreadLocalRandom.current().nextLong(from.time, to.time))

object RandomDataGenerator {

    val names = arrayOf("Ada", "Adela", "Adelajda", "Adrianna", "Agata", "Agnieszka", "Aldona", "Aleksandra",
            "Alicja", "Alina", "Amanda", "Amelia", "Anastazja", "Andżelika", "Aneta", "Anita", "Anna", "Antonina",
            "Barbara", "Beata", "Berenika", "Bernadeta", "Blanka", "Bogusława", "Bożena", "Cecylia", "Celina", "Czesława",
            "Dagmara", "Danuta", "Daria", "Diana", "Dominika", "Dorota", "Edyta", "Eliza", "Elwira", "Elżbieta", "Emilia",
            "Eugenia", "Ewa", "Ewelina", "Felicja", "Franciszka", "Gabriela", "Grażyna", "Halina", "Hanna", "Helena", "Iga",
            "Ilona", "Irena", "Irmina", "Iwona", "Izabela", "Jadwiga", "Janina", "Joanna", "Jolanta", "Jowita", "Judyta",
            "Julia", "Julita", "Justyna", "Kamila", "Karina", "Karolina", "Katarzyna", "Kazimiera", "Kinga", "Klaudia",
            "Kleopatra", "Kornelia", "Krystyna", "Laura", "Lena", "Leokadia", "Lidia", "Liliana", "Lucyna", "Ludmiła",
            "Luiza", "Łucja", "Magdalena", "Maja", "Malwina", "Małgorzata", "Marcelina", "Maria", "Marianna", "Mariola",
            "Marlena", "Marta", "Martyna", "Marzanna", "Marzena", "Matylda", "Melania", "Michalina", "Milena", "Mirosława",
            "Monika", "Nadia", "Natalia", "Natasza", "Nikola", "Nina", "Olga", "Oliwia", "Otylia", "Pamela", "Patrycja",
            "Paula", "Paulina", "Regina", "Renata", "Roksana", "Róża", "Rozalia", "Sabina", "Sandra", "Sara", "Sonia", "Stanisława",
            "Stefania", "Stella", "Sylwia", "Tamara", "Tatiana", "Teresa", "Urszula", "Weronika", "Wiesława", "Wiktoria", "Wioletta",
            "Zofia", "Zuzanna", "Zyta", "Żaneta", "Adam", "Adolf", "Adrian", "Albert", "Aleksander", "Aleksy", "Alfred", "Amadeusz",
            "Andrzej", "Antoni", "Arkadiusz", "Arnold", "Artur", "Bartłomiej", "Bartosz", "Benedykt", "Beniamin",
            "Bernard", "Błażej", "Bogdan", "Bogumił", "Bogusław", "Bolesław", "Borys", "Bronisław", "Cezary", "Cyprian",
            "Cyryl", "Czesław", "Damian", "Daniel", "Dariusz", "Dawid", "Dionizy", "Dominik", "Donald", "Edward", "Emanuel",
            "Emil", "Eryk", "Eugeniusz", "Fabian", "Feliks", "Ferdynand", "Filip", "Franciszek", "Fryderyk", "Gabriel", "Gerard",
            "Grzegorz", "Gustaw", "Henryk", "Herbert", "Hilary", "Hubert", "Ignacy", "Igor", "Ireneusz", "Jacek", "Jakub", "Jan",
            "Janusz", "Jarosław", "Jerzy", "Joachim", "Józef", "Julian", "Juliusz", "Kacper", "Kajetan", "Kamil", "Karol", "Kazimierz",
            "Klaudiusz", "Konrad", "Krystian", "Krzysztof", "Lech", "Leon", "Leszek", "Lucjan", "Ludwik", "Łukasz", "Maciej",
            "Maksymilian", "Marceli", "Marcin", "Marek", "Marian", "Mariusz", "Mateusz", "Michał", "Mieczysław", "Mikołaj",
            "Miłosz", "Mirosław", "Nikodem", "Norbert", "Olaf", "Olgierd", "Oskar", "Patryk", "Paweł", "Piotr", "Przemysław", "Radosław",
            "Rafał", "Remigiusz", "Robert", "Roman", "Rudolf", "Ryszard", "Sebastian", "Seweryn", "Sławomir", "Stanisław",
            "Stefan", "Sylwester", "Szymon", "Tadeusz", "Teodor", "Tomasz", "Wacław", "Waldemar", "Wiesław", "Wiktor",
            "Witold", "Władysław", "Włodzimierz", "Wojciech", "Zbigniew", "Zdzisław", "Zenon", "Zygmunt")

    val surnames = arrayOf("nowak", "kowalska", "wiśniewska", "wójcik", "kowalczyk", "kamińska", "lewandowska",
            "dąbrowska", "zielińska", "szymańska", "woźniak", "kozłowska", "jankowska", "wojciechowska", "kwiatkowska", "mazur",
            "krawczyk", "piotrowska", "kaczmarek", "grabowska", "pawłowska", "michalska", "zając", "król", "nowakowska",
            "wieczorek", "jabłońska", "majewska", "adamczyk", "wróbel", "nowicka", "dudek", "olszewska", "jaworska",
            "malinowska", "stępień", "górska", "pawlak", "witkowska", "walczak", "rutkowska", "sikora", "nowak",
            "kowalski", "wiśniewski", "wójcik", "kowalczyk", "kamiński",
            "lewandowski", "dąbrowski", "zieliński", "szymański", "woźniak", "kozłowski", "jankowski", "mazur")


    var titles = arrayOf("Technik", "Licencjat", "Inrzynier", "Magister", "Doktor", "Profesor")



    fun randomPesel(): String {
        val sb = StringBuilder()
        val age = ThreadLocalRandom.current().nextInt(18, 81) // 18-80
        val year = 117 - age // 2017 - age
        sb.append(year)
        val month = ThreadLocalRandom.current().nextInt(1, 13)
        if (month < 10)
            sb.append(0)
        sb.append(month)
        val day = ThreadLocalRandom.current().nextInt(1, 31)
        if (day < 10)
            sb.append(0)
        sb.append(day)

        for (i in 0..4)
            sb.append(ThreadLocalRandom.current().nextInt(10))

        return sb.toString()
    }

    fun randomGrade() = ThreadLocalRandom.current().nextDouble(2.0, 6.0)

    fun nextTitle(title: String) = titles.getOrNull(titles.indexOf(title) + 1) ?: title

    fun randomCalendar(): Calendar {
        val randomYear = ThreadLocalRandom.current().nextInt(1990, 2012)
        val randomMonth = ThreadLocalRandom.current().nextInt(1, 12)
        val randomDay = ThreadLocalRandom.current().nextInt(1, 28)

        val cal :java.util.Calendar = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.YEAR, randomYear)
        cal.set(java.util.Calendar.MONDAY, randomMonth)
        cal.set(java.util.Calendar.DAY_OF_MONTH,randomDay)
        return cal
    }


}