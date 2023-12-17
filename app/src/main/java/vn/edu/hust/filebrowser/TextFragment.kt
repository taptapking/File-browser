package vn.edu.hust.filebrowser

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.File

class TextFragment: Fragment() {


    val root = Environment.getExternalStorageDirectory()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val text = File(root.toString()+"/"+arguments!!.getString("text"))
        Log.v("TAG",text.toString())
        val content = text.inputStream().reader().readText()
        text.inputStream().close()
        view.findViewById<TextView>(R.id.text_content).text = content
        view.findViewById<TextView>(R.id.text_name).text = arguments!!.getString("text")



    }
}