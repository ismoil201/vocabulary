package com.ismoil.vocabulary

import android.R.id.edit
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.google.android.material.chip.Chip
import com.ismoil.vocabulary.databinding.ActivityAddBinding
import com.ismoil.vocabulary.room.AppDatabase
import com.ismoil.vocabulary.room.entities.Word

class AddActivity : AppCompatActivity() {

    private var dataBase: AppDatabase? = null
    private lateinit var binding: ActivityAddBinding
    private var originWord: Word? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = AppDatabase.getInstance(this)

        initViews()

        binding.addButton.setOnClickListener {
            if (originWord == null) add() else edit()

        }

    }



    private fun initViews() {
        val types = listOf("명사", "동사", "대명사", "형용사", "부사", "감탄사", "조사", "전치사", "접속사")

        binding.typeCheapGroup.apply {
            types.forEach { text ->
                addView(createCheap(text))
            }
        }

         originWord = intent.getParcelableExtra<Word>("originWord")
        originWord?.let { word ->
            binding.textInputEditText.setText(word.text)
            binding.meanInputEditText.setText(word.mean)
            val chip =
                binding.typeCheapGroup.children.find { (it as Chip).text == word.type } as? Chip
            chip?.isChecked = true
        }

        binding.textInputEditText.addTextChangedListener(){
            it?.let { text ->
                binding.textInputEditText.error = when (text.length) {
                    0 -> "값을 입력해주세요"
                    1 -> "2자 이상을 입력해주세요"
                    else -> null
                }
            }
        }
    }

    private fun createCheap(text: String): Chip {
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }

    }

    private fun add() {

        Thread {
            val text = binding.textInputEditText.text.toString()
            val mean = binding.meanInputEditText.text.toString()
            val type = binding.typeCheapGroup.findViewById<Chip>(
                binding.typeCheapGroup.checkedChipId
            ).text.toString()

            AppDatabase.getInstance(this)?.wordDao()
                ?.insert(Word(text = text, mean = mean, type = type))

            runOnUiThread {
                Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()

            }

            val intent = Intent().putExtra("isUpdated", true)
            setResult(RESULT_OK, intent)
            finish()
        }.start()
    }

    private fun edit() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = binding.typeCheapGroup.findViewById<Chip>(
            binding.typeCheapGroup.checkedChipId
        ).text.toString()

        val editWord = originWord?.copy(text = text, mean = mean, type = type)
        Thread {
            editWord?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.update(word)
                runOnUiThread {
                    Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show()
                    val intent = Intent().putExtra("editWord", word)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }.start()

    }
}


