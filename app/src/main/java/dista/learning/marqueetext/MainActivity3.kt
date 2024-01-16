package dista.learning.marqueetext

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class MainActivity3 : AppCompatActivity() {

    private val dataList = listOf("Activity 1", "Activity 2", "Activity 3", "Activity 4", "Activity 5")
    private  lateinit  var recycler: RecyclerView
    private lateinit var myAdapter:MyAdapter
    private val autoScrollHandler = Handler()
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
         val layoutManager = recycler.layoutManager as LinearLayoutManager
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val nextPosition = currentPosition +1

            if (nextPosition < myAdapter.itemCount){
                recycler.smoothScrollToPosition(nextPosition)
            }
            else {
                recycler.smoothScrollToPosition(0)
            }
            autoScrollHandler.postDelayed(this, 3000L)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        supportActionBar?.title = "Activity 3"
      recycler  = findViewById(R.id.rvRecyclerview)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.layoutManager = layoutManager
       val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recycler)
         myAdapter = MyAdapter(this, dataList as MutableList<String>)
        recycler.adapter = myAdapter
        startAutoScroll()

    }

    private fun startAutoScroll(){
            autoScrollHandler.postDelayed(autoScrollRunnable, 3000L)
    }

    override fun onDestroy() {
        super.onDestroy()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }

}

class MyAdapter(private val context:Context, private val dataList:MutableList<String>):RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
     val marqueeText :TextView = itemView.findViewById(R.id.titleTextView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         val view = LayoutInflater.from(context).inflate(R.layout.item_marquee,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.marqueeText.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // To add the item dynamically
fun addItem(item:String){
    dataList.add(item)
    notifyItemInserted(dataList.size-1)
}
}
