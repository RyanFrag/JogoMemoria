package com.example.jogomemoria

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import kotlin.random.Random
import android.os.Handler



class MainActivity : AppCompatActivity() {
    private lateinit var listBotoes: List<ImageView>
    private lateinit var cartas: List<Memoria>
    private var cartaSelecionada: Int? = null
    private lateinit var botao1: ImageView
    private lateinit var botao2: ImageView
    private lateinit var botao3: ImageView
    private lateinit var botao4: ImageView
    private lateinit var botao5: ImageView
    private lateinit var botao6: ImageView
    private lateinit var botao7: ImageView
    private lateinit var botao8: ImageView
    private lateinit var restart: Button
    private lateinit var animationView: LottieAnimationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        botao1 = findViewById(R.id.botao1)
        botao2 = findViewById(R.id.botao2)
        botao3 = findViewById(R.id.botao3)
        botao4 = findViewById(R.id.botao4)
        botao5 = findViewById(R.id.botao5)
        botao6 = findViewById(R.id.botao6)
        botao7 = findViewById(R.id.botao7)
        botao8 = findViewById(R.id.botao8)
        restart = findViewById(R.id.restart)
        val dataBase = mutableListOf(
            R.drawable.bigode,
            R.drawable.bruxo,
            R.drawable.calvo,
            R.drawable.canabis,
            R.drawable.cria,
            R.drawable.karate,
            R.drawable.laele,
            R.drawable.locutor,
            R.drawable.manuel,
            R.drawable.mentiroso,
            R.drawable.pensando,
            R.drawable.sono,
            R.drawable.superhomem
            )

        val listaJogo = dataBase.shuffled(Random).take(4).flatMap { listOf(it, it) }.shuffled()



        listBotoes = listOf(botao1, botao2, botao3, botao4, botao5, botao6, botao7, botao8)

        cartas = listBotoes.indices.map { index ->
            Memoria(listaJogo[index])
        }
        
        listBotoes.forEachIndexed { index, botao ->
            botao.setOnClickListener {
                verificaCarta(index)
                viraCarta()
                fimJogo()
            }
        }
        restart.setOnClickListener {
            for(carta in cartas){
                animationView.visibility = View.GONE
                carta.virada = false
                carta.igual = false
                for (botao in listBotoes){
                    botao.setImageResource(R.drawable.carta)
                    botao.alpha = 1.0f

                }
            }
            val novaLista = dataBase.shuffled(Random).take(4).flatMap { listOf(it, it) }.shuffled()
            cartas = listBotoes.indices.map { index ->
                Memoria(novaLista[index])
            }

        }

    }

    private fun fimJogo() {
        if (cartas.all { it.virada && it.igual }) {
            animationView = findViewById(R.id.animacao)
            animationView.setAnimation(R.raw.congratulation)
            animationView.repeatCount = 0
            animationView.visibility = View.VISIBLE
            Toast.makeText(this, "PARABÉNS, FIM DE JOGO", Toast.LENGTH_SHORT).show()
            animationView.playAnimation()
            val handler = Handler()
            handler.postDelayed({
                animationView.visibility = View.GONE
            }, 3000L)
        }
    }

    private fun verificaCarta(index: Int) {
        val carta = cartas[index]
        if(carta.virada){
            Toast.makeText(this, "Movimento inválido", Toast.LENGTH_SHORT).show()
            return
        }
        if(cartaSelecionada == null){
            restauraPosicao()
            cartaSelecionada = index
        }else{
            checkIgual(cartaSelecionada!!, index)
            cartaSelecionada = null
        }
        carta.virada = !carta.virada
    }

    private fun restauraPosicao() {
        for(carta in cartas){
            if(!carta.igual){
                carta.virada = false
            }
        }
    }

    private fun checkIgual(carta1: Int, carta2: Int) {
        if (cartas[carta1].id == cartas[carta2].id ){
            Toast.makeText(this, "SÃO IGUAIS!!", Toast.LENGTH_SHORT).show()
            cartas[carta1].igual = true
            cartas[carta2].igual = true
        }
    }

    private fun viraCarta() {
        cartas.forEachIndexed { index, carta ->
            val botao = listBotoes[index]
            if(carta.igual){
                botao.alpha = 0.1f
            }
            botao.setImageResource(if (carta.virada) carta.id else R.drawable.carta)
        }

    }
}