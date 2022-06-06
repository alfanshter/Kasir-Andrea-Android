package com.kasirandrea.kasirandrea.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangModel
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class KeranjangSelesaiAdapter(
    private val notesList: MutableList<KeranjangModel>
    ) : RecyclerView.Adapter<KeranjangSelesaiAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, KeranjangModel: KeranjangModel)
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
        internal var foto: ImageView

        init {
            nama = view.findViewById(R.id.txtnama)
            harga = view.findViewById(R.id.txtharga)
            foto = view.findViewById(R.id.imgfoto)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_keranjang_pesanan, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        Picasso.get().load(Constant.folder_foto+note.produk!!.foto).centerCrop().fit().into(holder.foto)
        holder.nama.text = note.produk.nama.toString()

        //Subsquence batasan tampilan nama
        if (note.produk.nama!!.length >13){
            holder.nama.text = "${note.produk.nama.subSequence(0,13)}.."
        }else{
            holder.nama.text = note.produk.nama
        }


        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = note.harga
        val harga: String = formatter.format(myNumber)
        holder.harga.text = "Rp. $harga"


        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition,note)
            }
        }


    }



}
