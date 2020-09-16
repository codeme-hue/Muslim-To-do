package id.kardihaekal.muslimto_do.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import id.kardihaekal.muslimto_do.R
import id.kardihaekal.muslimto_do.UpdateAndDelete
import id.kardihaekal.muslimto_do.model.ToDoModel

class Adapter(context: Context, toDoList: MutableList<ToDoModel>) : BaseAdapter() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = toDoList
    private var updateAndDelete: UpdateAndDelete = context as UpdateAndDelete



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val UID: String = itemList.get(position).UID as String
        val itemDataText = itemList.get(position).itemDataText as String
        val done: Boolean = itemList.get(position).done as Boolean
        val  view: View
        val  viewHolder: ListViewHolderr

        if(convertView==null)
        {
            view = inflater.inflate(
                R.layout.row_itemslayout,
                parent, false)
            viewHolder = ListViewHolderr(view)
            view.tag = viewHolder
        }


        else
        {
            view = convertView
            viewHolder = view.tag as ListViewHolderr
        }



        viewHolder.textLabel.text = itemDataText
        viewHolder.isDone.isChecked = done
        viewHolder.isDone.setOnClickListener {
            updateAndDelete.modifyItem(UID, !done)
        }

        viewHolder.isDeleted.setOnClickListener {
            updateAndDelete.onItemDelete(UID)
        }


        return  view


    }

    private class ListViewHolderr(row: View?) {
        val textLabel: TextView = row!!.findViewById(R.id.item_textView) as TextView
        val isDone: CheckBox = row!!.findViewById(R.id.cbox) as CheckBox
        val isDeleted: ImageButton = row!!.findViewById(R.id.close) as ImageButton
    }

    override fun getItem(position: Int): Any {

        return itemList.get(position)

    }

    override fun getItemId(position: Int): Long {

        return position.toLong()


    }

    override fun getCount(): Int {

        return itemList.size


    }
}
