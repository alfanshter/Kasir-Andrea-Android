package com.kasirandrea.kasirandrea.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import kotlin.math.ln
import kotlin.math.pow

class ProdukAdapter(
    private val notesList: MutableList<ProdukModel>
    ) : RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, produkModel: ProdukModel)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var nama: TextView
        internal var harga: TextView
        internal var deskripsi: TextView
        internal var foto: ImageView


        init {
            nama = view.findViewById(R.id.txtnama)
            harga = view.findViewById(R.id.txtharga)
            deskripsi = view.findViewById(R.id.txtdeskripsi)
            foto = view.findViewById(R.id.imgfoto)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_produk, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        Picasso.get().load(Constant.folder_foto+note.foto).centerCrop().fit().into(holder.foto)
        holder.nama.text = note.nama.toString()

        //Subsquence batasan tampilan nama
        if (note.nama!!.length >24){
            holder.nama.text = "${note.nama.subSequence(0,24)}.."
        }else{
            holder.nama.text = note.nama
        }

        if (note.deskripsi!!.length >24){
            holder.deskripsi.text  = "${note.deskripsi.subSequence(0,24)}.."
        }else{
            holder.deskripsi.text = note.deskripsi

        }


        if (Math.abs(note.harga!! / 1000000) > 1) {
            holder.harga.text = "${(note.harga / 1000000).toString().toString() + "m"}"
        } else if (Math.abs(note.harga / 1000) > 1) {
            holder.harga.text = (note.harga / 1000).toString().toString() + "k"
        } else {
            holder.harga.text = note.harga.toString()
        }



        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition,note)
            }
        }
    }



}
