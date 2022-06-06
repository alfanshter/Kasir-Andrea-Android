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

class ListPesananAdapter(
    private val notesList: MutableList<ProdukModel>
    ) : RecyclerView.Adapter<ListPesananAdapter.ViewHolder>() {

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
        internal var txtnomorpesanan: TextView
        internal var txtkalender: TextView
        internal var txtharga: TextView
        internal var txtstatus: TextView


        init {
            txtnomorpesanan = view.findViewById(R.id.txtnomorpesanan)
            txtkalender = view.findViewById(R.id.txtkalender)
            txtharga = view.findViewById(R.id.txtharga)
            txtstatus = view.findViewById(R.id.txtstatus)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_transaksi, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]

        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition,note)
            }
        }
    }



}
