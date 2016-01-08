package org.nbp.editor
package ui

import java.io._

import collection.JavaConversions._
import util._
import matching._

import android.app._
import android.content._
import android.content.res._
import android.content.pm._
import android.graphics._
import android.net.Uri
import android.os._
import android.text._
import android.text.method._
import android.util._
import android.view._
import android.webkit.MimeTypeMap
import android.widget._

//import com.aspose.words._
import org.acra._

/**
 * Defines a custom, more accessible, '''MovementMethod'''.
 *
 * Android's stock '''ArrowKeyMovementMethod''' has crucial accessibility issues.
 * Key among them is that it does not arrow by actual line, instead moving by screen line.
 * Further, the accessibility framework does not speak the newly-reached line in its entirety, even though it does initially, and the feedback is inconsistent.
 *
 * As a fix, we override a few key methods to move up and down physical lines, thus providing consistent feedback and a more pleasant experience.
*/

class MyMovementMethod extends ArrowKeyMovementMethod {

  override protected def down(widget:TextView, buffer:Spannable) = {
    val cap = (MetaKeyKeyListener.getMetaState(buffer, KeyEvent.META_SHIFT_ON) == 1) || (MetaKeyKeyListener.getMetaState(buffer, 0x800) != 0)
    val alt = MetaKeyKeyListener.getMetaState(buffer, KeyEvent.META_ALT_ON) == 1
    val length = buffer.length
    val cursor = Selection.getSelectionStart(buffer)
    val candidate = buffer.subSequence(cursor, length)
    val nextLineBeg = cursor+(TextUtils.indexOf(candidate, '\n') match {
      case -1 => candidate.length
      case v => v+1
    })
    if(cap) {
      if(alt) {
        Selection.extendSelection(buffer, length)
        true
      } else{
        Selection.extendSelection(buffer, nextLineBeg)
        true
      }
    } else {
      if(alt) {
        Selection.setSelection(buffer, length)
        true
      } else {
        Selection.setSelection(buffer, nextLineBeg)
        true
      }
    }
  }

  override protected def up(widget:TextView, buffer:Spannable) = {
    val cap = (MetaKeyKeyListener.getMetaState(buffer, KeyEvent.META_SHIFT_ON) == 1) || (MetaKeyKeyListener.getMetaState(buffer, 0x800) != 0)
    val alt = MetaKeyKeyListener.getMetaState(buffer, KeyEvent.META_ALT_ON) == 1
    val cursor = Selection.getSelectionStart(buffer)
    val candidate = buffer.subSequence(0, cursor)
    val prevLineBeg = TextUtils.lastIndexOf(candidate, '\n') match {
      case -1 => 0
      case v => TextUtils.lastIndexOf(candidate.subSequence(0, v), '\n') match {
        case -1 => 0
        case v => v+1
      }
    }
    if(cap) {
      if(alt) {
        Selection.extendSelection(buffer, 0)
        true
      } else{
        Selection.extendSelection(buffer, prevLineBeg)
        true
      }
    } else {
      if(alt) {
        Selection.setSelection(buffer, 0)
        true
      } else {
        Selection.setSelection(buffer, prevLineBeg)
        true
      }
    }
  }

}

/**
 * An '''EditText''' widget with a variety of accessibility improvements and simple rich-text formatting capabilities.
*/

class MyEditText(context:Context, attributes:AttributeSet, defStyleAttr:Int) extends EditText(context, attributes, defStyleAttr) {

  setMovementMethod(new MyMovementMethod())

  addTextChangedListener(new TextFormatter)

  def this(context:Context, attributes:AttributeSet) =
    this(context, attributes, 0)

  def this(context:Context) =
    this(context, null, 0)

  /**
   * Convenience method for selecting spans of a specified type.
  */

  private def spans[T](start:Int, end:Int, cls:Class[T]):collection.immutable.List[T] =
    getText.getSpans(start, end, cls).toList

  /**
   * Convenience method for retrieving a list of '''style.StyleSpan''' spans for a given region.
  */

  private def styleSpans(start:Int, end:Int) =
    spans[style.StyleSpan](start, end, classOf[style.StyleSpan])

  /**
   * Provides spoken feedback when arrowing into a formatted region.
  */

  override protected def onSelectionChanged(start:Int, end:Int) {
    super.onSelectionChanged(start, end)
    // TODO: Only 0-length selection (I.e. cursor movement) is handled for now.
    if(start == end) {
      def within[T](spans:collection.immutable.List[T]) = spans.filter { span =>
        val spanStart = getText.getSpanStart(span)
        val spanEnd = getText.getSpanEnd(span)
        spanStart <= start && spanEnd != start
      }
      within[style.StyleSpan](styleSpans(start, end))
      .foreach { span =>
        span.getStyle match {
          case Typeface.BOLD_ITALIC => announceForAccessibility(getContext.getString(R.string.bold)+" "+getContext.getString(R.string.italic))
          case Typeface.BOLD => announceForAccessibility(getContext.getString(R.string.bold))
          case Typeface.ITALIC => announceForAccessibility(getContext.getString(R.string.italic))
          case _ =>
        }
      }
      within[style.UnderlineSpan](spans[style.UnderlineSpan](start, end, classOf[style.UnderlineSpan]))
      .foreach { span =>
        announceForAccessibility(getContext.getString(R.string.underline))
      }
    }
  }

  /**
   * Speak a region around the cursor's position bounded by the specified character.
   * Currently dispatches to Spiel for speech.
  */

  private def speakRegion(bounds:Regex) = {
    val text = getText.toString
    val cursor = getSelectionStart
    val before = text.take(cursor)
    val after = text.drop(cursor)
    val txt = before.reverse.takeWhile(v => bounds.findFirstIn(v.toString) == None).reverse+
    after.takeWhile(v => bounds.findFirstIn(v.toString) == None)
    announceForAccessibility(txt)
  }

  private def speakLine() {
    speakRegion("\n".r)
  }

  private def speakWord() = {
    speakRegion("""\W""".r)
  }

  private def speakChar() = {
    val text = getText.toString
    val cursor = getSelectionStart
    val ch = text(if(cursor > text.length-1) cursor-1 else cursor)
    announceForAccessibility(ch.toString)
  }

  private var bold = false
  private var italic = false
  private var underline = false

  override def onKeyUp(keyCode:Int, event:KeyEvent):Boolean = {
    Log.d("editorcheck", "Up "+keyCode+", "+event)
    keyCode match {
      case KeyEvent.KEYCODE_B if(event.isCtrlPressed) =>
        if(bold) {
          announceForAccessibility(getContext.getString(R.string.bold)+" "+getContext.getString(R.string.off))
          bold = false
        } else {
          announceForAccessibility(getContext.getString(R.string.bold)+" "+getContext.getString(R.string.on))
          bold = true
        }
        true
      case KeyEvent.KEYCODE_I if event.isCtrlPressed =>
        if(italic) {
          announceForAccessibility(getContext.getString(R.string.italic)+" "+getContext.getString(R.string.off))
          italic = false
        } else {
          announceForAccessibility(getContext.getString(R.string.italic)+" "+getContext.getString(R.string.on))
          italic = true
        }
        true
      case KeyEvent.KEYCODE_U if event.isCtrlPressed =>
        if(underline) {
          announceForAccessibility(getContext.getString(R.string.underline)+" "+getContext.getString(R.string.off))
          underline = false
        } else {
          announceForAccessibility(getContext.getString(R.string.underline)+" "+getContext.getString(R.string.on))
          underline = true
        }
        true
      case KeyEvent.KEYCODE_L if event.isCtrlPressed =>
        speakLine()
        true
      case KeyEvent.KEYCODE_W if event.isCtrlPressed =>
        speakWord()
        true
      case KeyEvent.KEYCODE_C if event.isCtrlPressed =>
        speakChar()
        true
      case 536 =>
        Log.d("editorcheck", "Should speak line")
        speakLine()
        true
      case 548 =>
        Log.d("editorcheck", "Should speak word")
        speakWord()
        true
      case 578 =>
        Log.d("editorcheck", "Should speak character")
        speakChar()
        true
      case _ => super.onKeyUp(keyCode, event)
    }
  }

  /**
   * Formats text as per the state of various effects (bold, italic, etc.)
   * TODO: Only triggers on text insertion, won't handle selection-based toggling.
  */

  class TextFormatter extends TextWatcher {

    def beforeTextChanged(c:CharSequence, start:Int, count:Int, afterLength:Int) { }

    var changeStart:Int = _
    var changeEnd:Int = _

    /**
     * Records the start and end of a given change so '''afterTextChanged''' knows where to work.
    */

    def onTextChanged(c:CharSequence, start:Int, beforeLength:Int, count:Int) {
      changeStart = start
      changeEnd = changeStart+count
    }

    val logger = new LogPrinter(Log.DEBUG, "editorcheck")

    /**
     * Tries to generate an optimal set of spans based on '''spans''' of the desired type, based on the truth of '''check''', using '''newSpan''' to generate new spans.
    */

    private def doFormat(spans:collection.immutable.List[_], check: () => Boolean, newSpan:() => Any) {
      if(check()) {
        if(spans == Nil) {
          // No adjacent spans, so just insert a new one.
          getText.setSpan(newSpan(), changeStart, changeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
          // If there's an identical span just before, find it, remove it, then generate a new span with the original start and ending after the new change.
          spans.find(s => getText.getSpanEnd(s) == changeStart).foreach { s =>
            val spanStart = getText.getSpanStart(s)
            getText.removeSpan(s)
            getText.setSpan(newSpan(), spanStart, changeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
          }
          // If there's a span after us, delete it and generate a new one starting just before the change and ending where the original following span ended.
          spans.find(s => getText.getSpanStart(s) == changeEnd).foreach { s =>
            val spanEnd = getText.getSpanEnd(s)
            getText.removeSpan(s)
            getText.setSpan(newSpan(), changeStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
          }
        }
      } else {
        // If we're entering text not of the desired type, but are within a span of the desired type, then split the old span into two. One ends before the change, and the second ends immediately after.
        spans.filter { span =>
          getText.getSpanStart(span) <= changeStart && getText.getSpanEnd(span) >= changeEnd
        }.headOption.foreach { span =>
          val spanStart = getText.getSpanStart(span)
          val spanEnd = getText.getSpanEnd(span)
          getText.removeSpan(span)
          getText.setSpan(newSpan(), spanStart, changeStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
          getText.setSpan(newSpan(), changeEnd, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
      }
      //TextUtils.dumpSpans(getText, logger, "")
    }

    /**
     * Format newly-entered text, optimizing spans where possible.
    */

    def afterTextChanged(e:Editable) {
      // Use a slightly larger start and end value so we can catch adjacent spans.
      val start = if(changeStart == 0) 0 else changeStart-1
      val end = if(changeEnd == getText.length) getText.length else changeEnd+1
      doFormat(styleSpans(start, end).filter(_.getStyle == Typeface.BOLD_ITALIC), () => bold && italic, () => new style.StyleSpan(Typeface.BOLD_ITALIC))
      doFormat(styleSpans(start, end).filter(_.getStyle == Typeface.BOLD), () => bold && !italic, () => new style.StyleSpan(Typeface.BOLD))
      doFormat(styleSpans(start, end).filter(_.getStyle == Typeface.ITALIC), () => italic && !bold, () => new style.StyleSpan(Typeface.ITALIC))
      doFormat(spans[style.UnderlineSpan](start, end, classOf[style.UnderlineSpan]), () => underline, () => new style.UnderlineSpan())
    }

  }

}

/**
 * Main '''Activity'''.
*/

class Editor extends Activity {

  private var editor:Option[MyEditText] = None

  private var initialText = ""

  private def editedText = editor.map(_.getText)

  private val documents = new File(Environment.getExternalStorageDirectory, "Documents")

  override def onCreate(bundle:Bundle) {
    // Uncomment to enable StrictMode.
    //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build())
    //StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build())
    super.onCreate(bundle)
    Preferences(this)
    val e = new MyEditText(this)
    e.setSingleLine(false)
    e.setPadding(50, 0, 0, 0)
    editor = Some(e)
    setContentView(e)
    val intent = getIntent
    // Spin this off in its own thread as it seems to slow things down significantly.
    new Thread({ () =>
      //val asposeLicense = new License()
      //asposeLicense.setLicense(getAssets.open("Aspose.Words.lic"))
      if(!documents.exists)
        documents.mkdir()
      if(intent.getData != null)
        load(intent.getData.getPath)
      else if(Preferences.lastFile != "" && new File(Preferences.lastFile).exists)
        load(Preferences.lastFile)
      else
        Preferences.lastFile = ""
      runOnUiThread { () => initialText = editor.map(_.getText.toString).getOrElse("") }
      if(!getPackageManager.getInstalledPackages(0).map(_.packageName).contains("org.openintents.filemanager"))
        runOnUiThread { () => installFileManager() }
    }).start()
  }

  override def onDestroy() {
    super.onDestroy()
    if(Preferences.lastFile != "")
      save()
  }

  private var loadingDialog:AlertDialog = _

  private def load(filename:String) {
    // Probably a better/safer way to handle this but we'll silently ignore exceptions here for now.
    Try { Looper.prepare() }
    runOnUiThread { () => loadingDialog = new AlertDialog.Builder(this).setMessage(R.string.loading).show() }
    new Thread({ () =>
      try {
        val file = new File(filename)
        if(filename.endsWith(".doc") || filename.endsWith(".docx")) {
        /* aspose
          val doc = new Document(new FileInputStream(file))
          val sb = new SpannableStringBuilder()
          doc.getFirstSection.getBody.getChildNodes.toArray.foreach { n =>
            n match {
              case p:Paragraph =>
                p.getChildNodes.toArray.foreach { n =>
                  n match {
                    case r:Run =>
                      val start = sb.length
                      val str = r.getText
                      val end = start+str.length
                      sb.append(str)
                      val f = r.getFont
                      if(f.getBold && f.getItalic)
                        sb.setSpan(new style.StyleSpan(Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                      else if(f.getBold)
                        sb.setSpan(new style.StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                      else if(f.getItalic)
                        sb.setSpan(new style.StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                      else if(f.getUnderline != Underline.NONE)
                        sb.setSpan(new style.UnderlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    case _ =>
                  }
                }
                sb.append("\n")
              case _ =>
            }
          }
          runOnUiThread(() => editor.foreach(_.setText(sb)))
        */
        } else {
          val reader = new FileReader(filename)
          val bytes = new Array[Char](file.length.toInt)
          reader.read(bytes, 0, bytes.length)
          runOnUiThread { () => editor.foreach(_.setText(new String(bytes))) }
          reader.close()
        }
        Preferences.lastFile = filename
        initialText = editedText.map(_.toString).getOrElse("")
        runOnUiThread { () => resetTitle() }
      } catch {
        case t:Throwable =>
        Log.e("editor", "Error loading document", t)
          runOnUiThread { () =>
            new AlertDialog.Builder(this)
              .setMessage(t.getMessage)
              .setPositiveButton(android.R.string.ok, () => ACRA.getErrorReporter.handleException(t))
              .show()
          }
      } finally {
        runOnUiThread { () =>
          loadingDialog.hide()
          Toast.makeText(this, R.string.loaded, Toast.LENGTH_SHORT).show()
        }
      }
    }).start()
  }

  /**
   * Prompt to save document, running '''afterSave''' regardless of whether "OK" or "Cancel" is selected.
  */

  private def promptToSave(afterSave: () => Unit) {
    new AlertDialog.Builder(this)
      .setMessage(getString(R.string.saveChanges))
      .setPositiveButton(getString(android.R.string.yes), () => save(afterSave))
      .setNegativeButton(getString(android.R.string.no), () => afterSave())
      .show()
  }

  /**
   * Indicates what type of operation is being performed when the file manager opens.
  */

  object FileOp extends Enumeration {
    val Open = Value
    val Save = Value
  }

  /**
   * Always prompt for a filename regardless of whether one has already been set.
  */

  def saveAs(afterSave: () => Unit = {() => }) {
    startFileManager(FileOp.Save)
    afterSave()
  }

  /**
   * If this file has not yet been saved, prompt for a filename. Otherwise, save it again under its existing name.
   *
   * '''afterSave''' runs when the save operation is complete.
  */

  private def save(afterSave: () => Unit = { () => }) {
    if(Preferences.lastFile == "")
      saveAs(afterSave)
    else {
      new Thread({ () =>
        persist()
        afterSave()
      }).start()
    }
  }

  /**
   * Persists the content of the main '''MyEditText''' widget as a Microsoft Word or text document.
  */

  private def persist() {
    try {
      val file = new File(Preferences.lastFile)
      if(Preferences.lastFile.endsWith(".doc") || Preferences.lastFile.endsWith(".docx")) {
      /* aspose
        val doc = new DocumentBuilder()
        editedText.foreach { text =>
          var start = 0
          var end = 0
          var length = text.length
          while(start < length) {
            end = text.nextSpanTransition(start, length, null)
            val spans = text.getSpans(start, end, classOf[Object]).toList
            val font = doc.getFont
            font.clearFormatting()
            spans.foreach { span =>
              span match {
                case style:style.StyleSpan => style.getStyle match {
                  case Typeface.BOLD_ITALIC =>
                    font.setBold(true)
                    font.setItalic(true)
                  case Typeface.BOLD => font.setBold(true)
                  case Typeface.ITALIC => font.setItalic(true)
                  case _ =>
                }
                case u:style.UnderlineSpan => font.setUnderline(Underline.DASH)
                case _ =>
              }
            }
            val out = text.subSequence(start, end).toString
            doc.write(out)
            start = end
          }
        }
        doc.getDocument.save(file.getAbsolutePath)
      */
      } else {
        editedText.map(_.toString).foreach { text =>
          val writer = new FileWriter(file)
          writer.write(text)
          writer.close()
        }
      }
      initialText = editedText.map(_.toString).getOrElse("")
      runOnUiThread { () =>
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show()
        resetTitle()
      }
    } catch {
      case t:Throwable =>
        Log.e("editor", "Error persisting document", t)
        runOnUiThread { () =>
          new AlertDialog.Builder(this)
            .setMessage(t.getMessage)
            .setPositiveButton(android.R.string.ok, () => ACRA.getErrorReporter.handleException(t))
            .show()
        }
    }
  }

  /**
   * Start the file manager running the specified operation.
  */

  private def startFileManager(op:FileOp.Value) {
    val intent = new Intent(Intent.ACTION_GET_CONTENT)
    if(Preferences.lastFile != "") {
      val f = new File(Preferences.lastFile)
      val path = new File(f.getPath)
      if(path.exists)
        intent.setData(Uri.fromFile(path))
    } else
      intent.setData(Uri.fromFile(documents))
    op match {
      case FileOp.Open =>
        intent.putExtra("org.openintents.extra.TITLE", getString(R.string.openDocument))
        intent.putExtra("org.openintents.extra.BUTTON_TEXT", getString(R.string.open))
      case FileOp.Save =>
        intent.putExtra("org.openintents.extra.TITLE", getString(R.string.saveDocument))
        intent.putExtra("org.openintents.extra.BUTTON_TEXT", getString(R.string.save))
    }
    try {
      startActivityForResult(intent, op.id)
    } catch {
      case e:ActivityNotFoundException => installFileManager()
    }
  }

  /**
   * Perform the operation indicated by the file manager.
  */

  override protected def onActivityResult(requestCode:Int, resultCode:Int, result:Intent) {
    if(resultCode == Activity.RESULT_OK && result != null) {

      val data = result.getData

      val op = FileOp(requestCode)

      op match {
        case FileOp.Open => 
          val f = new File(data.getPath)
          if(!f.exists) {
            new AlertDialog.Builder(this)
              .setMessage(getString(R.string.fileNotFound))
              .setPositiveButton(getString(android.R.string.ok), null)
              .show()
            return
          }
          load(data.getPath)
        case FileOp.Save =>
          new Thread({ () =>
            val f = new File(data.getPath)
            if(f.exists) {
              runOnUiThread { () =>
                new AlertDialog.Builder(this)
                  .setMessage(getString(R.string.confirmOverwrite))
                  .setPositiveButton(getString(android.R.string.yes), { () =>
                    Preferences.lastFile = f.getPath
                    save()
                  })
                  .setNegativeButton(getString(android.R.string.no), () => startFileManager(FileOp.Save))
                  .show()
              }
            } else {
              Preferences.lastFile = f.getPath
              save()
            }
          }).start()
      }
    }
  }

  /**
   * If the file is unsaved, prompt to save before exiting. If it has already been saved, resave it.
   *
   * While it may be good form to expect the user to explicitly save if desired, notetakers typically aggressively persist documents when their editors are exited. As such, this editor honors that expectation.
  */

  override def finish() {
    Log.d("editorcheck", "Edited: "+editedText+", Initial: "+initialText)
    if(editedText.getOrElse("") != initialText) {
      if(Preferences.lastFile != "")
        save(afterSave = { () => super.finish() })
      else
        promptToSave({ () => super.finish() })
    } else
      super.finish()
  }

  private var menu:Option[Menu] = None

  override def onCreateOptionsMenu(m:Menu):Boolean = {
    menu = Some(m)
    new MenuInflater(this).inflate(R.menu.main, menu.get)
    super.onCreateOptionsMenu(m)
  }

  override def onOptionsItemSelected(item:MenuItem) = {
    item.getItemId match {
      case R.id.newDocument => newDocument()
      case R.id.open => openDocument()
      case R.id.save => save()
      case R.id.saveAs => saveAs()
      case R.id.send =>
        save(afterSave = { () =>
          val i = new Intent(Intent.ACTION_SEND)
          val url = "file://"+Preferences.lastFile
          val uri = Uri.parse(url)
          val mimeType = Option(MimeTypeMap.getFileExtensionFromUrl(url)).flatMap { extension =>
            Option(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension))
          }.getOrElse("text/plain")
          i.setType(mimeType)
          i.putExtra(Intent.EXTRA_STREAM, uri)
          startActivity(Intent.createChooser(i, getString(R.string.send)))
        })
    }
    true
  }

  /**
   * Reset the title based on the currently loaded file.
  */

  private def resetTitle() {
    if(Preferences.lastFile == "")
      setTitle(getString(android.R.string.untitled))
    else {
      val file = new File(Preferences.lastFile)
      setTitle(Uri.fromFile(file).getLastPathSegment)
    }
  }

  /**
   * Reset the editor state to that of an empty document.
  */

  private def resetEditorState() {
    Log.d("editorcheck", "Resetting")
    editor.foreach(_.setText(""))
    initialText = ""
    Preferences.lastFile = ""
    resetTitle()
  }

  private def newDocument() {
    if(initialText == editedText.map(_.toString).getOrElse(""))
      resetEditorState()
    else
      promptToSave(() => resetEditorState())
  }

  private def openDocument() {
    for(
      txt <- editedText
    ) {
      if(initialText == txt.toString) // TODO take formatting info into account
        return startFileManager(FileOp.Open)
      else {
        if(Preferences.lastFile == "")
          return promptToSave({ () => startFileManager(FileOp.Open) } )
        else
          return save(afterSave = { () => startFileManager(FileOp.Open) } )
      }
    }
    startFileManager(FileOp.Open)
  }

  /**
   * Install the needed file manager, either by linking to Play where supported, or opening a URI in the browser where not.
  */

  private def installFileManager() {
    new AlertDialog.Builder(this)
      .setMessage(getString(R.string.noFileManager))
      .setPositiveButton(getString(android.R.string.yes), { () =>
        val intent = new Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("market://details?id=org.openintents.filemanager"))
        try {
          startActivity(intent)
        } catch {
          case e:ActivityNotFoundException => 
            intent.setData(Uri.parse("http://www.apk4fun.com/apk/1005/"))
            startActivity(intent)
        }
      })
      .setNegativeButton(getString(android.R.string.no), null)
      .show()
  }

  override def onKeyUp(keyCode:Int, event:KeyEvent):Boolean = {
    keyCode match {
      case KeyEvent.KEYCODE_N if(event.isCtrlPressed) =>
        newDocument()
        return true
      case KeyEvent.KEYCODE_O if(event.isCtrlPressed) =>
        openDocument()
        return true
      case KeyEvent.KEYCODE_S if(event.isCtrlPressed && event.isShiftPressed) =>
        saveAs()
        return true
      case KeyEvent.KEYCODE_S if(event.isCtrlPressed) =>
        save()
        return true
      case _ =>
    }
    super.onKeyUp(keyCode, event)
  }

}
