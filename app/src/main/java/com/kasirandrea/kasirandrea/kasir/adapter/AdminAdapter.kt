package com.kasirandrea.kasirandrea.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminModel
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import kotlin.math.ln
import kotlin.math.pow

class AdminAdapter(
    private val notesList: MutableList<AdminModel>
    ) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, AdminModel: AdminModel)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var nama: TextView
        internal var inisial: TextView


        init {
            nama = view.findViewById(R.id.txtnama)
            inisial = view.findViewById(R.id.txtinisial_nama)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        holder.nama.text = note.nama.toString()
        holder.inisial.text = note.nama!!.subSequence(0,1)
        //Subsquence batasan tampilan nama
        if (note.nama!!.length >24){
            holder.nama.text = "${note.nama.subSequence(0,24)}.."
        }else{
            holder.nama.text = note.nama
        }


        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition,note)
            }
        }
    }



}
