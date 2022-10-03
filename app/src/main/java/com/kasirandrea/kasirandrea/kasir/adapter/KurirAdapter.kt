package com.kasirandrea.kasirandrea.kasir.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.admin.ongkir.OngkirActivity
import com.kasirandrea.kasirandrea.kasir.model.rajaongkir.CostsItem
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.ln
import kotlin.math.pow

class KurirAdapter(
    private val notesList: MutableList<CostsItem>,  private val context : Context
    ) : RecyclerView.Adapter<KurirAdapter.ViewHolder>(),AnkoLogger {

    //database
    private var dialog: Dialog? = null

    var  checkitem = -1
    interface Dialog {
        fun onClick(position: Int, CostsItem: CostsItem)
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
        internal var pengiriman: TextView
        internal var pos: ImageView
        internal var tiki: ImageView
        internal var jne: ImageView
        internal var ly_voucher : MaterialCardView

        init {
            nama = view.findViewById(R.id.txtnama)
            harga = view.findViewById(R.id.txtongkir)
            deskripsi = view.findViewById(R.id.txtdeskripsi)
            pengiriman = view.findViewById(R.id.txtpengiriman)
            pos = view.findViewById(R.id.img_pos)
            tiki = view.findViewById(R.id.img_tiki)
            jne = view.findViewById(R.id.img_jne)
            ly_voucher = view.findViewById(R.id.ly_voucher)

            ly_voucher.setOnClickListener {
                setSingleSelection(adapterPosition)
            }
        }
    }


    private fun setSingleSelection(adapterPosition: Int) {
        val note = notesList[adapterPosition]

        if (adapterPosition == RecyclerView.NO_POSITION) return
        checkitem = adapterPosition
        notifyItemChanged(checkitem)
        if (dialog!=null){
            dialog!!.onClick(adapterPosition,note)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_kurir, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]

        if (checkitem == position){
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.biru))
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }


        if (OngkirActivity.kurir =="jne"){
            holder.tiki.visibility = View.GONE
            holder.pos.visibility = View.GONE
            holder.jne.visibility = View.VISIBLE
        }else if (OngkirActivity.kurir =="tiki"){
            holder.tiki.visibility = View.VISIBLE
            holder.pos.visibility = View.GONE
            holder.jne.visibility = View.GONE
        }else if (OngkirActivity.kurir =="pos"){
            holder.tiki.visibility = View.GONE
            holder.pos.visibility = View.VISIBLE
            holder.jne.visibility = View.GONE
        }
        holder.nama.text = note.service
        holder.deskripsi.text = note.description
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = note.cost!![0]!!.value
        val harga: String = formatter.format(myNumber)
        holder.harga.text = "Rp. $harga"


        holder.pengiriman.text = "${note.cost[0]!!.etd} hari"

        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition,note)
            }
        }
    }



}
