package com.joaobapt.todo.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaobapt.todo.R

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        val recyclerView = view.findViewById<RecyclerView>(R.id.task_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = TaskListAdapter()
        adapter.currentList = taskList
        recyclerView.adapter = adapter
    }
    
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    
    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TaskListFragment.
         */
        @JvmStatic
        fun newInstance() = TaskListFragment()
    }
}