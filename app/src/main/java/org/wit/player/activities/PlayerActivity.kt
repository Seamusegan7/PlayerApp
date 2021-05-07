package org.wit.player.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.*
import org.wit.player.R
import org.wit.player.helpers.readImage
import org.wit.player.helpers.readImageFromPath
import org.wit.player.helpers.showImagePicker
import org.wit.player.main.MainApp
import org.wit.player.models.Location
import org.wit.player.models.PlayerModel


class PlayerActivity : AppCompatActivity(), AnkoLogger {

    var player = PlayerModel()
    lateinit var app : MainApp
    var edit = false
    val IMAGE_REQUEST = 1 //ID I created for image
    val LOCATION_REQUEST = 2
    //var location = Location(52.245696, -7.139102, 15f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        app = application as MainApp
        //edit = true



        val spinner: Spinner = findViewById(R.id.players_spinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.playerPositions_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                player.playerPosition= parent.getItemAtPosition(position) as String
                info("Item Selected"+ parent.getItemAtPosition(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }




        //reading the player from the intent and placing its fields into view controls
        if (intent.hasExtra("player_edit")) {
            edit = true
            player = intent.extras?.getParcelable<PlayerModel>("player_edit")!!
            playerName.setText(player.playerName)
            playerClub.setText(player.playerClub)
            spinner.setSelection(resources.getStringArray(R.array.playerPositions_array).indexOf(player.playerPosition)) //getting index number of the player position in the array and showing it in playerActivity

            ratingBar.setRating(player.playerRating)
            playerImage.setImageBitmap(readImageFromPath(this, player.image))
            if (player.image != null) {
                chooseImage.setText(R.string.change_player_image)
            }
            btnAdd.setText(R.string.save_player)
        }

        playerLocation.setOnClickListener {
//listener for changing marker location in maps view
        val location = Location(52.245696, -7.139102, 15f)
        //if statement for if the zoom is not 0.0 we are going to use the default location that we just set in the line above
        if (player.zoom != 0f){
            location.lat =  player.lat
            location.lng = player.lng
            location.zoom = player.zoom
        }

           startActivityForResult (intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }




        btnAdd.setOnClickListener() {
            player.playerName = playerName.text.toString()
            player.playerClub = playerClub.text.toString()
            player.playerRating = ratingBar.rating
            if (player.playerName.isEmpty()) {
                toast(R.string.enter_player_title)
            } else {
                if (edit) {
                    app.players.update(player.copy())
                } else {
                    app.players.create(player.copy())
                }
            }
                info("add Button Pressed: ${playerName}")
                setResult(AppCompatActivity.RESULT_OK)
                finish() //updates the list view and finsishes the activity
            }



        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)



        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

    }

    //inflating the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_player, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    //handling the event for the cancel button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.item_delete -> {
                app.players.delete(player)
                finish()
            }

            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    player.image = data.getData().toString()
                    playerImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_player_image)
                }
            }
            //saving the location once the maps activity has finished
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    player.lat = location.lat
                    player.lng = location.lng
                    player.zoom = location.zoom
                }
            }

        }
    }

}

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}