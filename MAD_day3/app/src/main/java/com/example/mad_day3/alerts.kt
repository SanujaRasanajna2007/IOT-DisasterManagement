package com.example.mad_day3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_day3.Controller.getCityName
import com.example.mad_day3.Controller.landSlideAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.awaitAll

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [alerts.newInstance] factory method to
 * create an instance of this fragment.
 */
class alerts : Fragment() {
    val db = Firebase.firestore
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityNameObj = getCityName()
        var test: String = ""
//        val categories = ArrayList<String>()
//        Toast.makeText(requireContext(), cityNameObj.getCityName(requireContext()), Toast.LENGTH_SHORT).show()
        db.collection("locationInfo")
            .whereEqualTo("name",cityNameObj.getCityName(requireContext()))
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    test = document.getString("category") ?: ""
//                    categories.add(document.getString("category") ?: "")
//                    Log.v("TEST OUT", document.getString("category") ?: "NOTHING");
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to retrieve categories", Toast.LENGTH_LONG).show()
            }
//        categories.forEach { item ->
//            Toast.makeText(requireContext(), item, Toast.LENGTH_SHORT).show()
//            if(item == "Landslide"){
//                Toast.makeText(requireContext(), "PASSED", Toast.LENGTH_SHORT).show()
//                //show cards
//                val landSlideRecycleView = view.findViewById<RecyclerView>(R.id.recycleview)
//                landSlideRecycleView.layoutManager = LinearLayoutManager(requireContext())
//                landSlideRecycleView.adapter = landSlideAdapter(10.6,"Yes", "Yes")
//            }else if(item == "Rainfall"){
//                //show cards
//            }
//        }
        Toast.makeText(requireContext(), test, Toast.LENGTH_SHORT).show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment alerts.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            alerts().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}