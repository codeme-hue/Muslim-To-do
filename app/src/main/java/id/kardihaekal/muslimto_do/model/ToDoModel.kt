package id.kardihaekal.muslimto_do.model

class ToDoModel {
    companion object Factory {
        fun create(): ToDoModel =
            ToDoModel()
    }
    var UID: String? = null
    var itemDataText: String? = null
    var done: Boolean? = null
}