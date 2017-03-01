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

@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package jlelse.newscatchr.backend.apis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import co.metalab.asyncawait.async
import com.afollestad.materialdialogs.MaterialDialog
import com.cloudrail.si.interfaces.Social
import com.cloudrail.si.services.Facebook
import com.cloudrail.si.services.Twitter
import jlelse.newscatchr.extensions.resStr
import jlelse.newscatchr.ui.views.ProgressDialog
import jlelse.readit.R

class SharingApi(val context: Activity, network: SocialNetwork) {
	private var social: Social? = when (network) {
		SocialNetwork.Twitter -> Twitter(context, TwitterClientID, TwitterClientSecret)
		SocialNetwork.Facebook -> Facebook(context, FacebookClientID, FacebookClientSecret)
		else -> null
	}
	private var progressDialog: ProgressDialog = ProgressDialog(context).apply { show() }

	fun share(title: String, text: String): SharingApi {
		var errorMsg = ""
		if (social != null) {
			context.async {
				val success = await {
					try {
						social?.postUpdate(text)
						true
					} catch (e: Exception) {
						e.printStackTrace()
						errorMsg += e.message
						false
					}
				}
				progressDialog.dismiss()
				Snackbar.make(context.findViewById(R.id.mainactivity_container), if (success) R.string.suc_share.resStr() + "" else (R.string.share_failed.resStr() + ": $errorMsg"), Snackbar.LENGTH_SHORT).show()
			}
		} else {
			context.startActivity(Intent.createChooser(Intent().apply {
				action = Intent.ACTION_SEND
				type = "text/plain"
				putExtra(Intent.EXTRA_SUBJECT, title)
				putExtra(Intent.EXTRA_TEXT, text)
			}, "${R.string.share.resStr()} $title"))
			context.runOnUiThread { progressDialog.dismiss() }
		}
		return this
	}

	enum class SocialNetwork {
		Twitter, Facebook, Native
	}

}

fun askForSharingService(context: Context, network: (SharingApi.SocialNetwork) -> Unit) {
	MaterialDialog.Builder(context)
			.items(R.string.twitter.resStr(), R.string.facebook.resStr(), R.string.more.resStr())
			.itemsCallback { _, _, which, _ ->
				when (which) {
					0 -> network(SharingApi.SocialNetwork.Twitter)
					1 -> network(SharingApi.SocialNetwork.Facebook)
					2 -> network(SharingApi.SocialNetwork.Native)
				}
			}
			.negativeText(android.R.string.cancel)
			.show()
}