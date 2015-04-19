package org.nbp.shell
package ui

import collection.JavaConversions._

import android.app.{Activity, AlertDialog, Dialog, DialogFragment, ListActivity}
import android.content.{BroadcastReceiver, ComponentName, Context, DialogInterface, Intent, IntentFilter, ServiceConnection}
import android.os.{Bundle, IBinder}
import android.util.Log
import android.view.{ContextMenu, Menu, MenuInflater, MenuItem, View, ViewGroup}
import android.widget.{AdapterView, ArrayAdapter, Button, EditText, LinearLayout, ListView, Spinner, TabHost, TextView}

import android.view.{KeyCharacterMap, KeyEvent}

/**
 * Provides default keyboard commands for speaking device statistics.
*/

/*trait StatisticsKeyHandler extends Activity {
  this: Activity =>

  private var StatisticsService:Option[StatisticsService] = None

  override def onCreate(bundle:Bundle) {
    super.onCreate(bundle)
    startService(new Intent(this, classOf[StatisticsService]))
    bindService(new Intent(this, classOf[StatisticsService]), onService, Context.BIND_AUTO_CREATE)
  }

  override def onDestroy() {
    super.onDestroy()
    unbindService(onService)
  }

  private val onService = new ServiceConnection {
    def onServiceConnected(className:ComponentName, rawBinder:IBinder) {
      StatisticsService = Some(rawBinder.asInstanceOf[StatisticsService#LocalBinder].getService)
    }
    def onServiceDisconnected(className:ComponentName) {
      StatisticsService = None
    }
  }

  override def onKeyDown(keycode:Int, event:KeyEvent) =  keycode match {
    case KeyEvent.KEYCODE_B =>
      StatisticsService.map(_.sayBatteryLevel())
      true
    case KeyEvent.KEYCODE_D =>
      StatisticsService.map(_.sayDate())
      true
    case KeyEvent.KEYCODE_S =>
      StatisticsService.map(_.saySignalStrengths())
      true
    case KeyEvent.KEYCODE_T =>
      StatisticsService.map(_.sayTime())
      true
    case _ => super.onKeyDown(keycode, event)
  }

}*/

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity
import com.j256.ormlite.android.apptools.OpenHelperManager

import models._

trait LaunchableList extends OrmLiteBaseActivity[DatabaseHelper] with AdapterView.OnItemClickListener with TypedActivity {
  this: Activity =>

  private var all:ListView = null

  private var tabHost:TabHost = null

  override def onCreate(bundle:Bundle) {
    super.onCreate(bundle)
    LaunchableList._instance = Some(this)
    getHelper
    setContentView(R.layout.main)
    tabHost = findView(TR.main)
    tabHost.setup()
    recreateTabs(true)
  }

  private var lastSelectedItem = -1

  def recreateTabs(initial:Boolean = false, focusOnContent:Boolean = true) {
    var currentTab:String = null
    if(!initial) {
      currentTab = tabHost.getCurrentTabTag
      setContentView(R.layout.main)
      tabHost = findView(TR.main)
      tabHost.setup()
    }
    all = findView(TR.launchables)
    all.setOnItemClickListener(this)
    registerForContextMenu(all)
    tabHost.addTab(tabHost.newTabSpec("all")
      .setIndicator(getString(R.string.all))
      .setContent(R.id.tab_all)
    )
    Screen.all.foreach { s =>
      val view = new ListView(this)
      view.setAdapter(
        new ArrayAdapter[Launchable](this,  android.R.layout.simple_list_item_1, s.launchables)
      )
      registerForContextMenu(view)
      tabHost.addTab(tabHost.newTabSpec(s.title)
        .setIndicator(s.title)
        .setContent(new TabHost.TabContentFactory {
          def createTabContent(tag:String) = view
        })
      )
    }
    for(i <- 1.to(tabHost.getTabWidget.getTabCount-1)) {
      registerForContextMenu(tabHost.getTabWidget.getChildTabViewAt(i))
    }
    val dao = getHelper.launchableDao
    val query = dao.queryBuilder()
    .orderBy("title", true)
    val launchables = dao.query(query.prepare())
    all.setAdapter(
      new ArrayAdapter[Launchable](this, android.R.layout.simple_list_item_1, launchables)
    )
    Option(currentTab).map { ct =>
      tabHost.setCurrentTabByTag(ct)
      if(focusOnContent) {
        if(lastSelectedItem != -1) {
          val list = tabHost.getCurrentView.asInstanceOf[ListView]
          list.setSelection(lastSelectedItem)
          list.requestFocus()
          lastSelectedItem = -1
        } else tabHost.getTabContentView.requestFocus()
      } else tabHost.getCurrentTabView.requestFocus()
    }.getOrElse {
      tabHost.setCurrentTab(0)
      all.requestFocus()
    }
  }

  override def onDestroy() {
    super.onDestroy()
    LaunchableList._instance = None
  }

  def onItemClick(parent:AdapterView[_], view:View, position:Int, id:Long) {
    val list = parent.asInstanceOf[ListView]
    val launchable = all.getAdapter.getItem(position).asInstanceOf[Launchable]
    launchable.launch()
    getHelper.launchableDao.update(launchable)
    startActivity(launchable.intent)
  }

  private def showScreenDialog() {
    (new ScreenDialog).show(getFragmentManager, "screen")
  }

  private class ScreenDialog extends DialogFragment {
    override def onCreateDialog(b:Bundle) = {
      val dialog = new Dialog(getActivity)
      dialog.setContentView(R.layout.new_screen_dialog)
      val title = dialog.findViewById(R.id.screen_title).asInstanceOf[EditText]
      val launchableType = dialog.findViewById(R.id.screen_launchable_type).asInstanceOf[Spinner]
      val typeNames = getString(R.string.all) :: getString(R.string.custom) :: Nil
      val types:ArrayAdapter[String] = new ArrayAdapter(getActivity, android.R.layout.simple_spinner_item, typeNames.toArray)
      types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      launchableType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener {
        def onItemSelected(parent:AdapterView[_],
        view:View, pos:Int, id:Long) {
          val choice = parent.getItemAtPosition(pos).toString
          screen.launchableType = if(choice == LaunchableList.this.getString(R.string.all)) "all" else "custom"
        }
        def onNothingSelected(parent:AdapterView[_]) { }
      })
      launchableType.setAdapter(types)
      val sortOrder = dialog.findViewById(R.id.screen_sort_order).asInstanceOf[Spinner]
      val orderNames = getString(R.string.alphabetically) :: getString(R.string.recently_used) :: getString(R.string.custom) :: Nil
      val orders = new ArrayAdapter[String](getActivity, android.R.layout.simple_spinner_item, orderNames.toArray)
      orders.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener {
        def onItemSelected(parent:AdapterView[_],
        view:View, pos:Int, id:Long) {
          val choice = parent.getItemAtPosition(pos).toString
          screen.sortOrder = if(choice == LaunchableList.this.getString(R.string.alphabetically)) "alpha"
            else if(choice == LaunchableList.this.getString(R.string.recently_used)) "recent"
            else "custom"
        }
        def onNothingSelected(parent:AdapterView[_]) { }
      })
      sortOrder.setAdapter(orders)
      dialog.findViewById(R.id.screen_ok).asInstanceOf[Button].setOnClickListener(new View.OnClickListener {
        def onClick(v:View) {
          val t = title.getText.toString
          if(t != "") {
            dialog.dismiss()
            screen.title = t
            val dao = getHelper.screenDao
            dao.create(screen)
            val first = Screen.first
            first.foreach { f =>
              Log.d("shellcheck", "here")
              if(f.id != screen.id) {
                Log.d("shellcheck", "here2")
                screen.next = first
                f.previous = Some(screen)
                dao.update(f)
                dao.update(screen)
              }
            }
            title.setText("")
            LaunchableList.this.recreateTabs()
          } else {
            new AlertDialog.Builder(LaunchableList.this)
            .setMessage(getString(R.string.title_missing))
            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener {
              def onClick(i:DialogInterface, what:Int) {
                showScreenDialog()
              }
            })
            .show()
            showScreenDialog()
          }
        }
      })
      dialog.findViewById(R.id.screen_cancel).asInstanceOf[Button].setOnClickListener(new View.OnClickListener {
        def onClick(v:View) {
          title.setText("")
          dialog.dismiss()
        }
      })
      dialog
    }
  }

  private var screen:Screen = null

  private var launchable:Launchable = null

  override def onCreateContextMenu(menu:ContextMenu, v:View, info:ContextMenu.ContextMenuInfo) {
    val menuID = if(v.isInstanceOf[ListView]) R.menu.launchable_context else R.menu.screen_context
    new MenuInflater(this).inflate(menuID, menu)
    if(menuID == R.menu.screen_context) {
      val results = Screen.byTitle(v.asInstanceOf[ViewGroup].getChildAt(1).asInstanceOf[TextView].getText.toString)
      screen = results.head
      screen.previous.getOrElse {
        menu.removeItem(R.id.move_left)
      }
      screen.next.getOrElse {
        menu.removeItem(R.id.move_right)
      }
    } else {
      lastSelectedItem = info.asInstanceOf[AdapterView.AdapterContextMenuInfo].position
      launchable = v.asInstanceOf[AdapterView[ArrayAdapter[Launchable]]].getSelectedItem.asInstanceOf[Launchable]
      def removeMoveUp() = menu.removeItem(R.id.move_up)
      def removeMoveDown() = menu.removeItem(R.id.move_down)
      def removeMoveItems() {
        removeMoveUp()
        removeMoveDown()
      }
      if(Screen.custom.isEmpty) menu.removeItem(R.id.include_on_screens)
      if(tabHost.getCurrentTab != 0) {
        val results = Screen.byTitle(tabHost.getCurrentTabTag)
        screen = results.head
        if(screen.launchableType != "custom" || screen.sortOrder != "custom") removeMoveItems()
        else {
          val ids = screen.launchableIDList
          val index = ids.indexOf(launchable.id)
          if(index == 0) removeMoveUp()
          if(index == ids.length-1) removeMoveDown()
        }
      } else removeMoveItems()
    }
  }

  override def onContextItemSelected(item:MenuItem) = {
    item.getItemId match {
      case R.id.move_left =>
        screen.swapLeft()
        recreateTabs(focusOnContent = false)
      case R.id.move_right =>
        screen.swapRight()
        recreateTabs(focusOnContent = false)
      case R.id.edit => showScreenDialog()
      case R.id.delete =>
        new AlertDialog.Builder(LaunchableList.this)
        .setMessage(getString(R.string.screen_delete_confirmation, screen.title))
        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener {
          def onClick(i:DialogInterface, what:Int) {
            screen.delete()
            screen = null
            tabHost.setCurrentTab(tabHost.getCurrentTab-1)
            recreateTabs()
          }
        })
        .setNegativeButton(getString(R.string.no), null)
        .show()
      case R.id.include_on_screens =>
        val custom = Screen.custom
        new AlertDialog.Builder(this)
        .setMultiChoiceItems(
          custom.map(_.title.asInstanceOf[CharSequence]).toArray,
          custom.map { v =>
            if(launchable.screens.map(_.id)contains(v.id)) true else false
          }.toArray,
          new DialogInterface.OnMultiChoiceClickListener {
            def onClick(dialog:DialogInterface, item:Int, checked:Boolean) {
              screen = Screen.custom(item)
              if(checked) screen.launchables ::= launchable
              else screen.launchables = screen.launchables.filterNot(_.id == launchable.id)
              screen.update()
              recreateTabs()
            }
          }
        ).setPositiveButton(R.string.ok, null)
        .show()
      case R.id.move_up =>
        val launchables = screen.launchables.toBuffer
        val index = launchables.map(_.id).indexOf(launchable.id)
        val target = launchables(index-1)
        launchables(index-1) = launchables(index)
        launchables(index) = target
        screen.launchables = launchables.toList
        screen.update()
        lastSelectedItem -= 1
        recreateTabs()
      case R.id.move_down =>
        val launchables = screen.launchables.toBuffer
        val index = launchables.map(_.id).indexOf(launchable.id)
        val target = launchables(index+1)
        launchables(index+1) = launchables(index)
        launchables(index) = target
        screen.launchables = launchables.toList
        screen.update()
        lastSelectedItem += 1
        recreateTabs()
    }
    true
  }

  private var menu:Option[Menu] = None

  override def onCreateOptionsMenu(m:Menu):Boolean = {
    menu = Some(m)
    new MenuInflater(this).inflate(R.menu.main, menu.get)
    super.onCreateOptionsMenu(m)
  }

  override def onOptionsItemSelected(item:MenuItem) = {
    item.getItemId match {
      case R.id.new_screen =>
        screen = new Screen
        showScreenDialog()
    }
    true
  }

}

object LaunchableList {
  private var _instance:Option[LaunchableList] = None
  def instance = _instance
}

class Home extends LaunchableList {

  override def onCreate(bundle:Bundle) {
    super.onCreate(bundle)
    setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_GLOBAL)
  }

  override def onKeyDown(keycode:Int, event:KeyEvent) =  keycode match {
    case KeyEvent.KEYCODE_BACK => true
    case _ => super.onKeyDown(keycode, event)
  }


}
