package ru.yourok.torrserve.ui.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yourok.torrserve.R
import ru.yourok.torrserve.ext.popBackStackFragment
import ru.yourok.torrserve.server.api.Api
import ru.yourok.torrserve.ui.fragments.TSFragment

class AddFragment : TSFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val vi = inflater.inflate(R.layout.add_fragment, container, false)
        return vi
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.apply {
            findViewById<Button>(R.id.btnOK)?.setOnClickListener {
                val link = view?.findViewById<EditText>(R.id.etMagnet)?.text?.toString() ?: ""
                val title = view?.findViewById<EditText>(R.id.etTitle)?.text?.toString() ?: ""
                val poster = view?.findViewById<EditText>(R.id.etPoster)?.text?.toString() ?: ""

                if (!link.isNullOrBlank())
                    lifecycleScope.launch(Dispatchers.IO) {
                        Api.addTorrent(link, title, poster, true)
                    }
                popBackStackFragment()
            }
            findViewById<Button>(R.id.btnCancel)?.setOnClickListener {
                popBackStackFragment()
            }
        }
    }
}