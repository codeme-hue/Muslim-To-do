package id.kardihaekal.muslimto_do

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import id.kardihaekal.muslimto_do.adapter.Adapter
import id.kardihaekal.muslimto_do.model.ToDoModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), UpdateAndDelete {

    lateinit var database: DatabaseReference
    var toDoList: MutableList<ToDoModel>? = null
    lateinit var adapter: Adapter
    private var listViewItem: ListView? = null
    var hariIni: String? = null
    var tanggal: String? = null


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().reference
        listViewItem = findViewById(R.id.item_listView) as ListView

        val dateNow = Calendar.getInstance().time
        hariIni = DateFormat.format("EEEE", dateNow) as String
        tanggal = DateFormat.format("d MMMM yyyy", dateNow) as String
        tvCurrentTime.text = "$hariIni, $tanggal"


        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
            val textEditText = EditText(this)
            textEditText.setBackgroundColor(R.color.colorPrimaryLight)
            alertDialog.setMessage("Add Item")
            alertDialog.setPositiveButton("ADD") { dialog, i ->

                val todoItemData = ToDoModel.create()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false

                val newItemData = database.child("todo").push()
                todoItemData.UID = newItemData.key

                newItemData.setValue(todoItemData)

                dialog.dismiss()
                Toast.makeText(
                    this@MainActivity,
                    "Item saved", Toast.LENGTH_SHORT
                ).show()


            }
            alertDialog.setView(textEditText)
            alertDialog.show()

        }

        toDoList = mutableListOf<ToDoModel>()
        adapter = Adapter(this, toDoList!!)
        listViewItem!!.adapter = adapter
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@MainActivity, "No Item Add",
                    Toast.LENGTH_SHORT).show()

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                toDoList!!.clear()
                addItemToList(snapshot)

            }

        })

    }

    private fun addItemToList(snapshot: DataSnapshot) {

        val items = snapshot.children.iterator()
        if (items.hasNext()) {

            val toDoIndexValue = items.next()
            val itemsIterator = toDoIndexValue.children.iterator()

            while (itemsIterator.hasNext()) {

                val currentItem = itemsIterator.next()
                val toDoItemData = ToDoModel.create()
                val map = currentItem.getValue() as HashMap<String, Any>

                toDoItemData.UID = currentItem.key
                toDoItemData.itemDataText = map.get("itemDataText") as String?
                toDoItemData.done = map.get("done") as Boolean?
                toDoList!!.add(toDoItemData)

            }

        }


        adapter.notifyDataSetChanged()
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {

        val itemReference = database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)

    }

    override fun onItemDelete(itemUID: String) {

        val itemReference = database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()

    }

}
