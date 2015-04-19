package org.nbp.shell
package models

import collection.JavaConversions._

import android.app.Activity
import android.content.{BroadcastReceiver, Context, Intent}
import android.content.pm.{PackageManager, ResolveInfo}
import android.database.sqlite.SQLiteDatabase;
import android.util.Log

import com.j256.ormlite.android.apptools._
import com.j256.ormlite.dao._
import com.j256.ormlite.field._
import com.j256.ormlite.support._
import com.j256.ormlite.table._

class DatabaseHelper(context:Context) extends OrmLiteSqliteOpenHelper(context, "shell.db", null, 1) {

  val launchableDao:Dao[Launchable, Int] = DaoManager.createDao(getConnectionSource, classOf[Launchable])
  Launchable.dao = launchableDao

  val screenDao:Dao[Screen, Int] = DaoManager.createDao(getConnectionSource, classOf[Screen])
  Screen.dao = screenDao

  override def onCreate(db:SQLiteDatabase, connectionSource:ConnectionSource) {
    Log.i("shell", "onCreate")
    TableUtils.createTable(connectionSource, classOf[Launchable])
    val pm = context.getPackageManager
    val mainIntent = new Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    pm.queryIntentActivities(mainIntent, 0).foreach { a =>
      launchableDao.create(new App(pm, a))
    }
    TableUtils.createTable(connectionSource, classOf[Screen])
  }

  override def onUpgrade(db:SQLiteDatabase, connectionSource:ConnectionSource, oldVersion:Int, newVersion:Int) {
    TableUtils.dropTable(connectionSource, classOf[Launchable], true)
    TableUtils.dropTable(connectionSource, classOf[Screen], true)
    onCreate(db)
  }

}

@DatabaseTable(tableName="launchables")
class Launchable {

  @DatabaseField(generatedId = true)
  var id = 0

  @DatabaseField(canBeNull = false)
  var launchableType = ""

  @DatabaseField(canBeNull = false)
  var title = ""

  @DatabaseField(canBeNull = false)
  var lastLaunched = 0l

  @DatabaseField(canBeNull = false)
  var value1 = ""

  @DatabaseField
  var value2 = ""

  lazy val intent = {
    val intent = new Intent("android.intent.action.MAIN")
    .addCategory("android.intent.category.LAUNCHER")
    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK+Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    .setClassName(value1, value2)
    intent
  }

  def launch() {
    lastLaunched = System.currentTimeMillis
  }

  def screens:List[Screen] = Screen.dao.queryForAll.filter { s =>
    s.launchableIDList.contains(id)
  }.toList

  override def toString() = title

}

object Launchable {
  private[models] var dao:Dao[Launchable, Int] = null
}

class App extends Launchable {

  launchableType = "app"

  def this(pm:PackageManager, info:ResolveInfo) = {
    this()
    title = {
      val label = info.loadLabel(pm).toString()
      if(label == "")
        info.activityInfo.name.toString()
      else label
    }
    value1 = info.activityInfo.packageName
    value2 = info.activityInfo.name
  }

}

class PackageUpdater extends BroadcastReceiver {
  override def onReceive(c:Context, i:Intent) {
    val helper = new DatabaseHelper(c)
    val pkg = i.getData.getSchemeSpecificPart
    val dao = helper.launchableDao
    val pm = c.getPackageManager
    i.getAction match {
      case Intent.ACTION_PACKAGE_ADDED if(!i.getBooleanExtra(Intent.EXTRA_REPLACING, false)) =>
        val mainIntent = new Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        mainIntent.setPackage(pkg)
        pm.queryIntentActivities(mainIntent, 0).foreach { a =>
          dao.create(new App(pm, a))
        }
      case Intent.ACTION_PACKAGE_REMOVED if(!i.getBooleanExtra(Intent.EXTRA_REPLACING, false)) =>
        val query = dao.deleteBuilder()
        query.where().eq("value1", pkg)
        dao.delete(query.prepare())
      case _ => // Avoid MatchError
    }
    ui.LaunchableList.instance.foreach(_.recreateTabs())
  }
}

@DatabaseTable(tableName="screens")
class Screen {

  @DatabaseField(generatedId = true)
  var id = 0

  @DatabaseField(canBeNull=false)
  var title = ""

  @DatabaseField(canBeNull=false)
  var launchableType = "all"

  @DatabaseField(canBeNull=false)
  var sortOrder = "alpha"

  @DatabaseField(canBeNull=false)
  private[models] var launchableIDs:String = ""

  def launchableIDList = launchables.map(_.id)

  def launchables:List[Launchable] = {
    val list = launchableType match {
      case "all" => Launchable.dao.queryForAll().toList
      case "custom" => launchableIDs.split(" ").filterNot(_ == "").map { l =>
        Launchable.dao.queryForId(l.toInt)
      }.toList
    }
    sortOrder match {
      case "alpha" => list.sortWith((v1, v2) => v1.title < v2.title)
      case "recent" => list.sortWith((v1, v2) => v1.lastLaunched > v2.lastLaunched)
      case _ => list
    }
  }

  def launchables_=(l:List[Launchable]) = {
    launchableIDs = l.map(_.id).mkString(" ")
  }

  @DatabaseField(canBeNull=false)
  private var previousID = 0
  def previous_=(s:Option[Screen]) = previousID = s.map(_.id).getOrElse(0)
  def previous = Option(Screen.dao.queryForId(previousID))

  @DatabaseField(canBeNull=false)
  private var nextID = 0
  def next_=(s:Option[Screen]) = nextID = s.map(_.id).getOrElse(0)
  def next = Option(Screen.dao.queryForId(nextID))

  def swapLeft() = previous.foreach { p =>
    val pp = p.previous
    val n = next
    previous = pp
    next = Some(p)
    p.previous = Some(this)
    p.next = n
    Screen.dao.update(this)
    Screen.dao.update(p)
  }

  def swapRight() = next.foreach { n =>
    val nn = n.next
    val p = previous
    next = nn
    previous = Some(n)
    n.previous = p
    n.next = Some(this)
    Screen.dao.update(this)
    Screen.dao.update(n)
  }

  def update() = Screen.dao.update(this)

  def delete() {
    val dao = Screen.dao
    dao.delete(this)
    previous.foreach { s =>
      s.next = next
      dao.update(s)
      dao.update(s)
    }
    next.foreach { s =>
      s.previous = previous
      dao.update(s)
    }
  }

  override val toString = title

}

object Screen {

  private[models] var dao:Dao[Screen, Int] = null

  def first:Option[Screen] = {
    dao.queryForAll().toList.headOption.map { s =>
      var rv:Screen = s
      var tv:Option[Screen] = Some(s)
      while(tv != None) {
        rv = tv.get
        tv = rv.previous
      }
      rv
    }.orElse(None)
  }

  def all = dao.queryForAll()

  private def queryWhereEq(field:String, value:String) = {
    val query = dao.queryBuilder()
    query.where.eq(field, value)
    dao.query(query.prepare())
  }

  def custom = queryWhereEq("launchableType", "custom")

  def byTitle(t:String) = queryWhereEq("title", t)

}
