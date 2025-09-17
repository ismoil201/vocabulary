package com.ismoil.vocabulary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismoil.vocabulary.databinding.ActivityMainBinding
import com.ismoil.vocabulary.room.AppDatabase
import com.ismoil.vocabulary.room.entities.Word

class MainActivity : AppCompatActivity(), WordAdapter.itemOnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private var selectedWord: Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()


        binding.btnAdd.setOnClickListener {

            Intent(this, AddActivity::class.java).let {
                updateAddWordResult.launch(it)
            }
        }

        binding.deleteBtn.setOnClickListener {
            delete()
        }

        binding.editBtn.setOnClickListener {
            edit()
        }
    }

    private fun edit() {
        if (selectedWord == null) return
        val intent = Intent(this, AddActivity::class.java)
            .putExtra("originWord", selectedWord)
        updateEditWordResult.launch(intent)

    }


    private fun delete() {
        if (selectedWord == null) return

        Thread {

            selectedWord?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.delete(word)

                runOnUiThread {
                    wordAdapter.list.remove(word)
                    wordAdapter.notifyDataSetChanged()
                    binding.textTextView.text = ""
                    binding.meanTextView.text = ""
                    selectedWord = null
                    Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
                }
            }

        }.start()
    }


    private val updateAddWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false

        if (result.resultCode == RESULT_OK && isUpdated) {

            updateAddWord()
        }
    }

    private val updateEditWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val editWord = result.data?.getParcelableExtra<Word>("editWord")
        if (result.resultCode == RESULT_OK && editWord != null) {
            updateEditWord(editWord)
        }
    }

    private fun updateEditWord(word: Word) {

        var index = wordAdapter.list.indexOfFirst({ it.id == word.id })

        wordAdapter.list[index] = word
        runOnUiThread {
            selectedWord = word
            wordAdapter.notifyItemChanged(index)
            binding.textTextView.text = word.text
            binding.meanTextView.text = word.mean

        }
    }



    private fun initRecyclerView() {

        wordAdapter = WordAdapter(mutableListOf(), this)

        binding.recyclerview.adapter = wordAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoraation(this, LinearLayoutManager.VERTICAL)
        binding.recyclerview.addItemDecoration(dividerItemDecoration)


        Thread {
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            wordAdapter.list.addAll(list)
            runOnUiThread {

                wordAdapter.notifyDataSetChanged()
            }
        }.start()


    }


    override fun onClick(word: Word) {
        selectedWord = word
        binding.textTextView.text = word.text
        binding.meanTextView.text = word.mean

    }

    private fun updateAddWord() {
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let { word ->
                wordAdapter.list.add(0, word)

                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }

            }

        }.start()
    }
}