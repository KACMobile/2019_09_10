package com.example.mainlayout

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class GroupFragment : Fragment() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    var userInfos = arrayListOf<UserInfo>()
    private val userID:String = "User01"


    companion object {
        fun newInstance() = GroupFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.group_fragment, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.group_fragment_recyclerView)
        recyclerView.adapter = GroupRecyclerAdapter(userInfos)

        databaseReference.child("Users" + userID + "Follow").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        }
        )

        }


        return rootView

        //val listView = view?.findViewById<ListView>(R.id.main_listview)
        //listView?.adapter = MyCustomAdapter(this)

    }
    private lateinit var viewModel: GroupViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)

    }





}