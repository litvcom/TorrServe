package ru.yourok.torrserve.ui.main


import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import ru.yourok.torrserve.R
import ru.yourok.torrserve.server.models.torrent.Torrent
import ru.yourok.torrserve.utils.ByteFmt
import java.text.SimpleDateFormat
import java.util.*


class TorrentsAdapter(private val activity: Activity) : BaseAdapter() {
    private val list = mutableListOf<Torrent>()

    fun update(list: List<Torrent>) {
        if (this.list.size != list.size) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        } else {
            var changed = false
            for (i in list.indices) {
                if (this.list[i] != list[i]) {
                    this.list[i] = list[i]
                    changed = true
                }
            }
            if (changed)
                notifyDataSetChanged()
        }
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val vi: View = view ?: (activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.torrent_item, null)

        val title = list[position].title
        val poster = list[position].poster
        val hash = list[position].hash
        val size = list[position].torrent_size
        val addTime = list[position].timestamp

        var addStr = ""

        if (addTime > 0) {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            sdf.setTimeZone(TimeZone.getDefault())
            addStr = sdf.format(Date(addTime * 1000))
        }

        vi.findViewById<ImageView>(R.id.ivPoster)?.visibility = View.GONE

        if (poster.isNotEmpty())
            vi.findViewById<ImageView>(R.id.ivPoster)?.let {
                it.visibility = View.VISIBLE
                Glide.with(view?.context ?: return@let)
                    .asBitmap()
                    .load(poster)
                    .fitCenter()
                    .placeholder(ColorDrawable(0x3c000000))
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(it)
            }

        vi.findViewById<TextView>(R.id.tvTorrName)?.text = title
        vi.findViewById<TextView>(R.id.tvTorrHash)?.text = hash.toUpperCase()
        if (addStr.isNotEmpty()) {
            vi.findViewById<TextView>(R.id.tvTorrDate)?.text = addStr
            vi.findViewById<TextView>(R.id.tvTorrDate)?.visibility = View.VISIBLE
        } else
            vi.findViewById<TextView>(R.id.tvTorrDate)?.visibility = View.GONE
        vi.findViewById<TextView>(R.id.tvTorrSize)?.text = ByteFmt.byteFmt(size)

        return vi
    }

    override fun getItem(p0: Int): Any? {
        if (p0 < 0 || p0 >= list.size)
            return null
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int = list.size
}