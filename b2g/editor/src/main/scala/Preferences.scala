package org.nbp.editor

import android.content.{Context, SharedPreferences}
import android.preference.PreferenceManager
import android.util.Log

/**
 * Convenience class for globally accessing shared preferences.
*/

object Preferences {

  private var prefs:SharedPreferences = null

  def apply(context:Context) {
    prefs = PreferenceManager.getDefaultSharedPreferences(context)
  }

  /**
   * The filename of the last loaded or saved file.
  */

  def lastFile = prefs.getString("lastFile", "")

  def lastFile_=(f:String) {
    val editor = prefs.edit()
    editor.putString("lastFile", f)
    editor.commit()
  }

}
