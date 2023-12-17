package vn.edu.hust.filebrowser

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.io.File

class ImageFragment: Fragment() {

    val root = Environment.getExternalStorageDirectory()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val image = File(root.toString()+"/"+arguments!!.getString("image"))
        Log.v("TAG",image.toString())
        val bitmap = BitmapFactory.decodeFile(image.absolutePath)

        view.findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
        view.findViewById<TextView>(R.id.image_name).text = arguments!!.getString("image")



    }
}