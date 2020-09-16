package id.kardihaekal.muslimto_do

interface UpdateAndDelete {

    fun modifyItem(itemUID: String, isDone: Boolean)
    fun onItemDelete(itemUID: String)
}