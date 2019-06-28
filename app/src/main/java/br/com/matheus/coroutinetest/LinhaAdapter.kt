package br.com.matheus.coroutinetest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.matheus.coroutinetest.model.Linha
import kotlinx.android.synthetic.main.linha_linha.view.*

class LinhaAdapter(val ctx: Context, val linhas: List<Linha>): RecyclerView.Adapter<LinhaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinhaViewHolder {
        return LinhaViewHolder(LayoutInflater.from(ctx).inflate(R.layout.linha_linha,parent,false))
    }

    override fun getItemCount(): Int {
        return linhas.size
    }

    override fun onBindViewHolder(holder: LinhaViewHolder, position: Int) {
        val linha = linhas[position]

        holder.tvCodigoLinha.text = linha.codigoLinha
        holder.tvNomeLinha.text = linha.nomeLinha
    }

}


class LinhaViewHolder(view: View): RecyclerView.ViewHolder(view){
    val tvNomeLinha = view.tvNomeLinha
    val tvCodigoLinha = view.tvCodigoLinha
}