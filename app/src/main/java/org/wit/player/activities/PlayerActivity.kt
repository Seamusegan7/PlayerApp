package org.wit.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class PlayerActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        info("Player Main Activity started..")


        btnAdd.setOnClickListener() {
            val placemarkTitle = placemarkTitle.text.toString()
            if (placemarkTitle.isNotEmpty()) {
                info("add Button Pressed: $placemarkTitle")
            }
            else {
                toast ("Please Enter a title")
            }
        }
    }
}