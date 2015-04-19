package org.nbp.shell
import _root_.android.app.{Activity, Dialog}
import _root_.android.view.View

case class TypedResource[T](id: Int)
case class TypedLayout(id: Int)

object TR {
  val screen_launchable_type = TypedResource[android.widget.Spinner](R.id.screen_launchable_type)
  val launchables = TypedResource[android.widget.ListView](R.id.launchables)
  val main = TypedResource[android.widget.TabHost](R.id.main)
  val screen_sort_order = TypedResource[android.widget.Spinner](R.id.screen_sort_order)
  val tab_all = TypedResource[android.widget.LinearLayout](R.id.tab_all)
  val screen_cancel = TypedResource[android.widget.Button](R.id.screen_cancel)
  val screen_title = TypedResource[android.widget.EditText](R.id.screen_title)
  val screen_ok = TypedResource[android.widget.Button](R.id.screen_ok)
 object layout {
  val new_screen_dialog = TypedLayout(R.layout.new_screen_dialog)
 val main = TypedLayout(R.layout.main)
 }
}
trait TypedViewHolder {
  def findViewById( id: Int ): View
  def findView[T](tr: TypedResource[T]) = findViewById(tr.id).asInstanceOf[T]
}
trait TypedView extends View with TypedViewHolder
trait TypedActivityHolder extends TypedViewHolder
trait TypedActivity extends Activity with TypedActivityHolder
trait TypedDialog extends Dialog with TypedViewHolder
object TypedResource {
  implicit def layout2int(l: TypedLayout) = l.id
  implicit def view2typed(v: View) = new TypedViewHolder { 
    def findViewById( id: Int ) = v.findViewById( id )
  }
  implicit def activity2typed(a: Activity) = new TypedViewHolder { 
    def findViewById( id: Int ) = a.findViewById( id )
  }
  implicit def dialog2typed(d: Dialog) = new TypedViewHolder { 
    def findViewById( id: Int ) = d.findViewById( id )
  }
}
