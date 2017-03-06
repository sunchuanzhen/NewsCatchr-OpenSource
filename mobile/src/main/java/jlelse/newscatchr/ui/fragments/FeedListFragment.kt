/*
 * NewsCatchr
 * Copyright © 2017 Jan-Lukas Else
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jlelse.newscatchr.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import jlelse.newscatchr.backend.Feed
import jlelse.newscatchr.extensions.notNullAndEmpty
import jlelse.newscatchr.ui.layout.BasicRecyclerUI
import jlelse.newscatchr.ui.recycleritems.FeedListRecyclerItem
import jlelse.newscatchr.ui.recycleritems.NCAdapter
import jlelse.newscatchr.ui.recycleritems.TagsRecyclerItem
import jlelse.newscatchr.ui.views.StatefulRecyclerView
import jlelse.readit.R
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find

class FeedListFragment : BaseFragment() {
	private var fragmentView: View? = null
	private var feeds: Array<Feed>? = null
	private var tags: Array<String>? = null
	private val recyclerOne: StatefulRecyclerView? by lazy { fragmentView?.find<StatefulRecyclerView>(R.id.basicrecyclerview_recycler) }
	private var fastAdapter = FastItemAdapter<FeedListRecyclerItem>()
	private var tagsAdapter = NCAdapter<TagsRecyclerItem>(order = 100)

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		fragmentView = fragmentView ?: BasicRecyclerUI().createView(AnkoContext.create(context, this))
		if (recyclerOne?.adapter == null) {
			tagsAdapter.wrap(fastAdapter)
			recyclerOne?.adapter = tagsAdapter
		}
		feeds = getAddedObject("feeds")
		fastAdapter.clear()
		if (feeds.notNullAndEmpty()) {
			fastAdapter.add(mutableListOf<FeedListRecyclerItem>().apply {
				feeds?.forEachIndexed { i, feed ->
					add(FeedListRecyclerItem(feed = feed, isLast = i == feeds?.lastIndex, fragment = this@FeedListFragment, adapter = fastAdapter))
				}
			})
		}
		tags = getAddedObject("tags")
		tagsAdapter.clear()
		if (tags.notNullAndEmpty()) {
			tagsAdapter.add(TagsRecyclerItem(fragment = this, tags = tags))
		}
		recyclerOne?.restorePosition()
		return fragmentView
	}

}
