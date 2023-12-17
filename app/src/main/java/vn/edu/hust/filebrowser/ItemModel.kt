package vn.edu.hust.filebrowser

import java.io.Serializable

data class ItemModel(var name: String = "default", var extension: String = "default"): Serializable{
    override fun toString(): String {
        return "$name.$extension"
    }
}
