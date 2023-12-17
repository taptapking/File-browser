package vn.edu.hust.filebrowser

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.File

class FolderFragment: Fragment() {

    var items = arrayListOf<ItemModel>()
    val root = Environment.getExternalStorageDirectory()
    var listFiles = root.listFiles()
    var adapter = ItemAdapter(items, object: ItemAdapter.ItemClickListener{
        override fun ItemClick(position: Int) {
            // ("Not yet implemented")
        }
    }, object: ItemAdapter.ItemLongClickListener{
        override fun ItemLongClick(position: Int) {
            // ("Not yet implemented")
        }
    })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        items.clear()
        super.onViewCreated(view, savedInstanceState)

        val folder1 = File(root.toString()+"/"+arguments!!.getString("folder"))

        listFiles = folder1.listFiles()
        if (listFiles != null)
            for (item in listFiles){
                if (item.isDirectory){
                    items.add(ItemModel(item.name, "DIR"))
                    Log.v("TAG", item.name + " - directory")
                } else if (item.isFile){
                    items.add(ItemModel(item.nameWithoutExtension,item.extension))
                    Log.v("TAG", item.name + " - file")
                }

            }

        val recyclerView2 = view.findViewById(R.id.folder_view) as RecyclerView
        val folderName = view.findViewById<TextView>(R.id.folder_name)
        folderName.text=arguments!!.getString("folder")
        recyclerView2.layoutManager = LinearLayoutManager(context)
        recyclerView2.adapter = adapter

    }
}