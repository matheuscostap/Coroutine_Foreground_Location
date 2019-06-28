package br.com.matheus.coroutinetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.matheus.coroutinetest.model.Parada
import br.com.matheus.coroutinetest.network.TransporteRepositoryImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val repository = TransporteRepositoryImpl()
    private var paradas = emptyList<Parada>()
    private lateinit var adapter: ParadasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress.visibility = View.VISIBLE

        val adapter = ParadasAdapter(this@MainActivity,paradas){parada ->
            val intent = Intent(this@MainActivity, ParadaActivity::class.java)
            intent.putExtra("parada",parada)
            startActivity(intent)
        }

        rvParadas.layoutManager = LinearLayoutManager(this@MainActivity)
        rvParadas.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try{
                paradas = repository.retornarParadas()

                withContext(Dispatchers.Main){
                    progress.visibility = View.GONE
                    adapter.paradas = paradas
                    adapter.notifyDataSetChanged()
                }

            }catch (e: Throwable){
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity,"Erro!",Toast.LENGTH_LONG).show()
                    progress.visibility = View.GONE
                }
            }
        }

        btnFiltrar.setOnClickListener {
            if (!etFiltro.text.toString().isNullOrEmpty()){
                paradas = filtrarPorLinha(etFiltro.text.toString())
                adapter.paradas = paradas
                adapter.notifyDataSetChanged()
            }
        }

    }


    private fun filtrarPorLinha(linhaS: String): List<Parada>{
        var filtrado = emptyList<Parada>().toMutableList()

        paradas.forEach {parada ->
            parada.linhas.forEach {linha ->
                if (linha.codigoLinha.contains(linhaS,true)){
                    filtrado.add(parada)
                }
            }
        }

        return filtrado
    }

}

