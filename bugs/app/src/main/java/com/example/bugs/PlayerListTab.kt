package com.example.bugs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bugs.com.example.bugs.PlayerListAdapter
import com.example.bugs.managers.PlayerManager
import com.example.bugs.models.Player

class PlayerListTab : Fragment() {

    private lateinit var listViewPlayers: ListView
    private lateinit var buttonPlay: Button
    private lateinit var textViewEmptyList: TextView

    private var selectedPlayer: Player? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player_list, container, false)

        listViewPlayers = view.findViewById(R.id.listViewPlayers)
        buttonPlay = view.findViewById(R.id.buttonPlay)
        textViewEmptyList = view.findViewById(R.id.textViewEmptyList)

        setupListeners()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список каждый раз, когда фрагмент становится видимым
        updatePlayerList()
    }

    private fun updatePlayerList() {
        val players = PlayerManager.getPlayers()

        if (players.isEmpty()) {
            // Если список пуст, показываем сообщение и скрываем ListView
            textViewEmptyList.visibility = View.VISIBLE
            listViewPlayers.visibility = View.GONE
        } else {
            // Если игроки есть, показываем ListView и скрываем сообщение
            textViewEmptyList.visibility = View.GONE
            listViewPlayers.visibility = View.VISIBLE

            // Создаем и устанавливаем адаптер
            val adapter = PlayerListAdapter(requireContext(), players)
            listViewPlayers.adapter = adapter
        }

        // Сбрасываем выбор и скрываем кнопку
        selectedPlayer = null
        buttonPlay.visibility = View.GONE
        // Сбрасываем подсветку элемента списка, если он был выбран
        listViewPlayers.clearChoices()
    }



    private fun setupListeners() {
        // Обработчик нажатия на элемент списка
        listViewPlayers.setOnItemClickListener { parent, view, position, id ->
            // Получаем выбранного игрока
            selectedPlayer = parent.getItemAtPosition(position) as Player
            // Показываем кнопку "Играть"
            buttonPlay.visibility = View.VISIBLE
        }

        // Обработчик нажатия на кнопку "Играть"
        buttonPlay.setOnClickListener {
            // Проверяем, что игрок выбран
            selectedPlayer?.let { player ->
                // Логика при нажатии на кнопку "Играть"
                // Пока что просто выводим сообщение
                Toast.makeText(
                    requireContext(),
                    "Начинаем игру с ${player.name}!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}