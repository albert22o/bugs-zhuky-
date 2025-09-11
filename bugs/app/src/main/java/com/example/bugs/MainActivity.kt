package com.example.bugs

import android.app.Activity
import android.os.Bundle
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    private lateinit var editTextName: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var spinnerCourse: Spinner
    private lateinit var seekBarDifficulty: SeekBar
    private lateinit var calendarViewBirth: CalendarView
    private lateinit var buttonRegister: Button
    private lateinit var textViewResult: TextView
    private lateinit var imageViewZodiac: ImageView
    private lateinit var textDifficultyLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Убедитесь, что активность использует платформенный layout (activity_main.xml)
        setContentView(R.layout.activity_main)

        // Инициализация view
        editTextName = findViewById(R.id.editTextName)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        spinnerCourse = findViewById(R.id.spinnerCourse)
        seekBarDifficulty = findViewById(R.id.seekBarDifficulty)
        calendarViewBirth = findViewById(R.id.calendarViewBirth)
        buttonRegister = findViewById(R.id.buttonRegister)
        textViewResult = findViewById(R.id.textViewResult)
        imageViewZodiac = findViewById(R.id.imageViewZodiac)
        textDifficultyLabel = findViewById(R.id.textDifficultyLabel)

        // Заполнение спиннера курсами
        val courses = arrayOf("1 курс", "2 курс", "3 курс", "4 курс")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCourse.adapter = adapter

        // Переменная для даты рождения (ms)
        var birthDateMillis: Long = calendarViewBirth.date

        // Инициално показать значение сложности
        textDifficultyLabel.text = "Уровень сложности: ${seekBarDifficulty.progress + 1}"

        // Обновление метки сложности в реальном времени
        seekBarDifficulty.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textDifficultyLabel.text = "Уровень сложности: ${progress + 1}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Обработчик выбора даты — автообновление знака
        calendarViewBirth.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            birthDateMillis = cal.timeInMillis

            val zodiac = getZodiac(birthDateMillis)
            val zodiacResId = getZodiacImageRes(zodiac)
            if (zodiacResId != null) {
                imageViewZodiac.setImageResource(zodiacResId)
            } else {
                imageViewZodiac.setImageResource(android.R.color.transparent)
            }
        }

        // Показать знак для начальной даты
        val initialZodiac = getZodiac(birthDateMillis)
        getZodiacImageRes(initialZodiac)?.let { imageViewZodiac.setImageResource(it) }

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString().trim()
            if (name.isEmpty()) {
                editTextName.error = "Введите ФИО"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            val selectedGenderId = radioGroupGender.checkedRadioButtonId
            val gender = if (selectedGenderId != -1)
                findViewById<RadioButton>(selectedGenderId).text.toString()
            else "Не указан"

            val course = spinnerCourse.selectedItem.toString()
            val difficulty = seekBarDifficulty.progress + 1

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val birthDateStr = sdf.format(Date(birthDateMillis))

            val zodiac = getZodiac(birthDateMillis)

            val player = Player(name, gender, course, difficulty, birthDateStr, zodiac)

            textViewResult.text = """
                ФИО: ${player.name}
                Пол: ${player.gender}
                Курс: ${player.course}
                Уровень сложности: ${player.difficulty}
                Дата рождения: ${player.birthDate}
                Знак зодиака: ${player.zodiac}
            """.trimIndent()

            // Если есть картинка — установить
            getZodiacImageRes(zodiac)?.let { imageViewZodiac.setImageResource(it) }
        }
    }

    private fun getZodiac(birthMillis: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = birthMillis
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH) + 1

        return when {
            (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Овен"
            (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "Телец"
            (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "Близнецы"
            (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "Рак"
            (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "Лев"
            (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "Дева"
            (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "Весы"
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Скорпион"
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Стрелец"
            (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "Козерог"
            (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Водолей"
            (month == 2 && day >= 19) || (month == 3 && day <= 20) -> "Рыбы"
            else -> "Неизвестно"
        }
    }

    private fun getZodiacImageRes(zodiac: String): Int? {
        return when (zodiac) {
            "Овен" -> R.drawable.aries
            "Телец" -> R.drawable.taurus
            "Близнецы" -> R.drawable.gemini
            "Рак" -> R.drawable.cancer
            "Лев" -> R.drawable.leo
            "Дева" -> R.drawable.virgo
            "Весы" -> R.drawable.libra
            "Скорпион" -> R.drawable.scorpio
            "Стрелец" -> R.drawable.sagittarius
            "Козерог" -> R.drawable.capricorn
            "Водолей" -> R.drawable.aquarius
            "Рыбы" -> R.drawable.pisces
            else -> null
        }
    }
}