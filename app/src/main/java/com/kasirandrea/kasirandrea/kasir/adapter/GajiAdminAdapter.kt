package com.kasirandrea.kasirandrea.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.model.gaji.RiwayatGajiModel
import java.sql.Date
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class GajiAdminAdapter(
    private val notesList: MutableList<RiwayatGajiModel>
    ) : RecyclerView.Adapter<GajiAdminAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, RiwayatGajiModel: RiwayatGajiModel)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var bulan: TextView
        internal var gaji: TextView
        internal var bonus: TextView
        internal var total: TextView


        init {
            bulan = view.findViewById(R.id.txtbulan)
            gaji = view.findViewById(R.id.txtgaji)
            bonus = view.findViewById(R.id.txtbonus)
            total = view.findViewById(R.id.txttotal)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_riwayatgaji, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]



        val formatter: NumberFormat = DecimalFormat("#,###")

        val sdf = SimpleDateFormat("MMM y")
        val currentDate = sdf.format(note.createdAt)
        holder.bulan.text = "Gaji $currentDate"

        holder.gaji.text = formatter.format(note.gajiPokok)
        holder.bonus.text = "${note.jumlahPenjualan} X ${formatter.format(note.bonus)}"
        holder.total.text = "${formatter.format(note.totalPenghasilan)}"

        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition,note)
            }
        }
    }




}
