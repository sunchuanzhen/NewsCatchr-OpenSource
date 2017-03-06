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

package jlelse.newscatchr.ui.recycleritems

import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.flexbox.FlexboxLayout
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.utils.ViewHolderFactory
import jlelse.newscatchr.ui.fragments.BaseFragment
import jlelse.newscatchr.ui.views.addTagView
import jlelse.readit.R
import org.jetbrains.anko.find

class TagsRecyclerItem(val tags: Array<String>? = null, val fragment: BaseFragment? = null) : AbstractItem<TagsRecyclerItem, TagsRecyclerItem.ViewHolder>() {
	private val FACTORY = ItemFactory()

	override fun getType(): Int {
		return R.id.tags_item_id
	}

	override fun getLayoutRes(): Int {
		return R.layout.recyclertagsitem
	}

	override fun bindView(viewHolder: ViewHolder, payloads: MutableList<Any?>?) {
		super.bindView(viewHolder, payloads)
		viewHolder.tagsBox.removeAllViews()
		if (fragment != null) tags?.forEach {
			viewHolder.tagsBox.addTagView(fragment, it)
		}
	}

	override fun getFactory(): ViewHolderFactory<out ViewHolder> = FACTORY

	class ItemFactory : ViewHolderFactory<ViewHolder> {
		override fun create(v: View): ViewHolder {
			return ViewHolder(v)
		}
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		var tagsBox: FlexboxLayout = view.find<FlexboxLayout>(R.id.tagsBox)
	}
}