package vn.edu.hust.filebrowser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val items = arrayListOf<ItemModel>()
    val imageExtensions = arrayListOf("jpg","png","gif","jpeg","tiff","svg")
    val root = Environment.getExternalStorageDirectory()
    var listFiles = root.listFiles()

    val mFragment = FolderFragment()
    val nFragment = ImageFragment()
    val oFragment = TextFragment()

    var adapter = ItemAdapter(items, object: ItemAdapter.ItemClickListener{
        override fun ItemClick(position: Int) {
            TODO("Not yet implemented")
        }
    }, object: ItemAdapter.ItemLongClickListener{
        override fun ItemLongClick(position: Int) {
            TODO("Not yet implemented")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if ((Build.VERSION.SDK_INT < 30) && (Build.VERSION.SDK_INT>22)) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Log.v("TAG", "Permission Denied => Request permission")
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1234)
            } else {
                Log.v("TAG", "Permission Granted")
            }
        } else if (Build.VERSION.SDK_INT>=30) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }

        refreshList()

        val recyclerView1 = findViewById(R.id.recycler_view) as RecyclerView



        adapter.listener2 = object: ItemAdapter.ItemLongClickListener{
            override fun ItemLongClick(position: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                val inflater = layoutInflater
                if (items[position].extension=="DIR")
                    builder.setTitle(items[position].name)
                else
                    builder.setTitle(items[position].toString())
                val dialogLayout = inflater.inflate(R.layout.alert_folder, null)
                val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
                val checkBox = dialogLayout.findViewById<CheckBox>(R.id.delete_confirm)
                editText.hint = "Rename"
                builder.setView(dialogLayout)
                builder.setPositiveButton("Rename") { dialogInterface, i ->
                    var sourceDir: File
                    var targetDir: File
                    if (items[position].extension=="DIR") {
                        sourceDir = File(root.toString() + "/" + items[position].name)
                        targetDir = File(root.toString()+"/"+editText.text.toString())
                    }else {
                        sourceDir = File(root.toString() + "/" + items[position].toString())
                        targetDir = File(root.toString()+"/"+editText.text.toString()+"."+items[position].extension)
                    }
                    Log.v("TAG","$sourceDir")
                    if (sourceDir.renameTo(targetDir)){
                        Toast.makeText(applicationContext, "Renamed "+items[position].name+" to "+editText.text.toString(), Toast.LENGTH_SHORT).show()
                        refreshList()
                    } else {
                        Toast.makeText(applicationContext, "Could not rename "+items[position].name, Toast.LENGTH_SHORT).show()
                    }
                }
                builder.setNeutralButton("Delete") {dialogInterface, i ->
                    var sourceDir: File
                    if (items[position].extension=="DIR") {
                        sourceDir = File(root.toString() + "/" + items[position].name)
                    }else {
                        sourceDir = File(root.toString() + "/" + items[position].toString())
                    }
                    if (checkBox.isChecked){
                        try {
                            sourceDir.delete()
                            Toast.makeText(applicationContext, "Deleted "+items[position].name, Toast.LENGTH_SHORT).show()
                            refreshList()
                        } catch (e: IOException){
                            e.printStackTrace()
                            Toast.makeText(applicationContext, "Could not delete "+items[position].name, Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(applicationContext, "Deletion of "+items[position].name+" is not confirmed", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.setNegativeButton("Cancel") {dialogInterface, i ->}
                builder.show()
            }
        }

        adapter.listener = object: ItemAdapter.ItemClickListener{
            override fun ItemClick(position: Int) {
                if (items[position].extension=="DIR"){
                    val mBundle= Bundle()
                    mBundle.putString("folder",items[position].name)
                    supportFragmentManager.beginTransaction().apply{
                        mFragment.arguments = mBundle
                        replace(R.id.frame_layout, mFragment)
                        commit()
                        addToBackStack(null)
                    }
                } else if (imageExtensions.contains(items[position].extension)){
                    val mBundle= Bundle()
                    mBundle.putString("image",items[position].toString())

                    supportFragmentManager.beginTransaction().apply {
                        nFragment.arguments = mBundle
                        replace(R.id.frame_layout, nFragment)
                        commit()
                        addToBackStack(null)
                    }
                } else if (items[position].extension=="txt"){
                    val mBundle= Bundle()
                    mBundle.putString("text",items[position].toString())

                    supportFragmentManager.beginTransaction().apply {
                        oFragment.arguments = mBundle
                        replace(R.id.frame_layout, oFragment)
                        commit()
                        addToBackStack(null)
                    }
                }

            }
        }


        recyclerView1.layoutManager = LinearLayoutManager(this)
        recyclerView1.adapter = adapter

    }
    fun refreshList(){
        adapter.notifyDataSetChanged()
        items.clear()
        listFiles = root.listFiles()
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
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.actionbarhomepage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("New Folder")
        val dialogLayout = inflater.inflate(R.layout.alert_new_folder, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.newFolder)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Create") {dialogInterface, i ->
            val newDir = File(root, editText.text.toString())
            if (!newDir.exists()){
                if (newDir.mkdir()){
                    Toast.makeText(applicationContext, "Successfully created folder "+editText.text.toString(), Toast.LENGTH_SHORT).show()
                    refreshList()
                } else{
                    Toast.makeText(applicationContext, "Failed to create the folder ", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Folder already existed ", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") {dialogInterface, i ->}
        builder.show()
        return super.onOptionsItemSelected(item)
    }
}