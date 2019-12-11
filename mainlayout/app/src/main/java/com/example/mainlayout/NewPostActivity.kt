package com.example.mainlayout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class NewPostActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST:Int = 1
    lateinit var insertedImageView:ImageView
    lateinit var insertedMapView:ImageView
    lateinit var muserInfo: UserInfo
    private var userID:String = "User01"
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    private val mstorage = FirebaseStorage.getInstance()
    private val requestMapCode: Int = 1001
    private var mlatitude: String? =null
    private var mlongitude: String? =null

    var filePath:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val idPreference = getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        val insertImageBtn = findViewById<ImageView>(R.id.new_post_gallery)
        val mapsBtn = findViewById<ImageView>(R.id.new_post_insertmap)
        val arrowBtn = findViewById<ImageView>(R.id.new_post_arrow)
        val actionBar = supportActionBar
        actionBar!!.title = " "
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        muserInfo = intent.getSerializableExtra("userInfo") as UserInfo




        insertImageBtn.setOnClickListener {
            intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        arrowBtn.setOnClickListener {
            if(::insertedImageView.isInitialized || ::insertedMapView.isInitialized) {
                if(::insertedImageView.isInitialized) {
                    if (insertedImageView.visibility == View.GONE && it.rotation == 0f) {
                        insertedImageView.visibility = View.VISIBLE
                        it.rotation = 180f
                    } else {
                        insertedImageView.visibility = View.GONE
                        it.rotation = 0f
                    }
                }
                if(::insertedMapView.isInitialized) {
                    if (insertedMapView.visibility == View.GONE && it.rotation == 0f) {
                        insertedMapView.visibility = View.VISIBLE
                        it.rotation = 180f
                    } else {
                        insertedMapView.visibility = View.GONE
                        it.rotation = 0f
                    }
                }

            }
            else
                Toast.makeText(this, "첨부된 이미지가 없습니다", Toast.LENGTH_LONG).show()
        }

        mapsBtn.setOnClickListener {
            val intent =Intent(this, MapsActivity::class.java)
            intent.putExtra("RequestCode", requestMapCode)
            startActivityForResult(intent,requestMapCode)
        }


    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> {
                finish()
            }

            R.id.write-> {
                val editTextView = findViewById<EditText>(R.id.new_post_edittext)
                val postText = editTextView.text.toString()
                var postImage:String?

                if(postText != "") {
                    if (filePath != null) {
                        val fileName: String = userID + "_" + System.currentTimeMillis()
                        val storageRef =
                            mstorage.getReferenceFromUrl("gs://mobilesw8-d30db.appspot.com/MobileProject8")
                                .child(muserInfo.userType + "/" + muserInfo.userName + "/" + fileName)
                        storageRef.putFile(filePath!!).addOnSuccessListener(OnSuccessListener {
                            Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            storageRef.downloadUrl.addOnCompleteListener {
                                Toast.makeText(this, "Image Uri Successfully", Toast.LENGTH_SHORT).show();
                                postImage = it.result.toString()
                                uploadPost(postText,postImage)
                            }

                        })
                    }
                    else
                        uploadPost(postText, null)

                }
                else
                    finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun uploadPost(postText:String, postImage:String?){
        val userPostDB = databaseReference.child("Users/" + userID + "/Post")
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val yearMonthFormat = SimpleDateFormat("yyyyMMdd")
        val timeFormat = SimpleDateFormat("hhmmss")
        val postDate = yearMonthFormat.format(date)
        val postTime = timeFormat.format(date)
        val post = Post(postText, postDate, postTime, postImage, mlatitude, mlongitude, muserInfo)
        userPostDB.push().setValue(post)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_newpost_menu,menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            insertedImageView = findViewById<ImageView>(R.id.new_post_insertedImage)
            filePath = data.data
            try{
                val input: InputStream? = contentResolver.openInputStream(filePath!!)
                val img : Bitmap = BitmapFactory.decodeStream(input)
                input!!.close()
                insertedImageView.setImageBitmap(img)
            }catch(e: Exception ){

            }
        }
        else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "취소되었습니다", Toast.LENGTH_LONG).show()
        }
        else if(requestCode == requestMapCode && resultCode == RESULT_OK && data != null){
            mlatitude = data.extras!!.getDouble("latitude").toString()
            mlongitude = data.extras!!.getDouble("longitude").toString()
            insertedMapView = findViewById(R.id.new_post_insertedmap)
            val mapUrl :String = "https://maps.google.com/maps/api/staticmap?center=" + mlatitude + "," + mlongitude + "&zoom=15&size=200x200&sensor=false&key=AIzaSyCsEHUViLllof4Fx-GvhXkdJuO4lxw6dUA"
            Log.d("a","This is URL : " +mapUrl)
            Glide.with(this).load(mapUrl).into(insertedMapView)

        }
    }




}
