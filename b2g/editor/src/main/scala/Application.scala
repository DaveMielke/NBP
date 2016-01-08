package org.nbp.editor

//import com.aspose.words._
import org.acra._
import annotation._

@ReportsCrashes(
  formKey = "",
  mailTo = "deane@blazie.net",
  customReportContent = Array(ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT)
)
class Application extends android.app.Application {
  override def onCreate() {
    super.onCreate()
    ACRA.init(this)
    //val aspose = new AsposeWordsApplication()
    //aspose.loadLibs(this)
  }
}
