package com.kasirandrea.kasirandrea.kasir.adapter.listpesanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananModel
import java.sql.Date
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ListPesananAdapter(
    private val notesList: MutableList<PesananModel>
    ) : RecyclerView.Adapter<ListPesananAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, PesananModel: PesananModel)
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

        val formatter: NumberFormat = DecimalFormat("#,###")

        holder.txtnomorpesanan.text = note.nomorpesanan
        holder.txtharga.text = "Rp. ${formatter.format(note.harga)}"
        if (note.is_status == 0){
            holder.txtstatus.text = "Dalam Proses"
        }else if (note.is_status == 1){
            holder.txtstatus.text = "Sukses"
        }else if (note.is_status == 2){
            holder.txtstatus.text = "Gagal"
        }
        val sdf = SimpleDateFormat("dd MMM, hh:mm a")
        val currentDate = sdf.format(note.createdAt)
        holder.txtkalender.text = currentDate
        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition,note)
            }
        }
    }




}
