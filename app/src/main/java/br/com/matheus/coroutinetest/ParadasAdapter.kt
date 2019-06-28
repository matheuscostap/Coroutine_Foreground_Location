package br.com.matheus.coroutinetest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.matheus.coroutinetest.model.Parada
import kotlinx.android.synthetic.main.linha_parada.view.*

class ParadasAdapter(val ctx: Context, var paradas: List<Parada>,val onItemClick: (parada: Parada) -> Unit): RecyclerView.Adapter<ParadaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParadaViewHolder {
        return ParadaViewHolder(LayoutInflater.from(ctx).inflate(R.layout.linha_parada,parent,false))
    }

    override fun getItemCount(): Int {
        return paradas.size
    }

    override fun onBindViewHolder(holder: ParadaViewHolder, position: Int) {
        val parada = paradas[position]
        holder.tvCodigo.text = parada.codigo

        var linhas = ""
        parada.linhas.forEach {
            linhas += "${it.codigoLinha} - "
        }

        holder.tvLinhas.text = linhas

        holder.itemView.setOnClickListener {
            onItemClick(parada)
        }
    }

}

class ParadaViewHolder(view: View): RecyclerView.ViewHolder(view){
    val tvLinhas = view.tvLinhas
    val tvCodigo = view.tvCodigo
}