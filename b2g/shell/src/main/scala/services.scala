package org.nbp.shell

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.{BroadcastReceiver, Context, Intent, IntentFilter}
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.{Binder, IBinder}
import android.provider.Settings.{Secure, System}
import android.speech.tts.TextToSpeech
import TextToSpeech._
import android.telephony.{PhoneStateListener, SignalStrength, TelephonyManager}
import android.util.Log

/**
 * Service for speaking various device statistics.
*/

class StatisticsService extends Service with OnInitListener {

  class LocalBinder extends Binder {
    def getService = StatisticsService.this
  }

  private val binder = new LocalBinder

  override def onBind(intent:Intent) = binder

  private var tts:TextToSpeech = null

  private var signalStrengths:Option[SignalStrength] = None

  class LocalPhoneStateListener extends PhoneStateListener {
    override def onSignalStrengthsChanged(s:SignalStrength) {
      signalStrengths = Some(s)
    }
  }

  private var telephonyManager:TelephonyManager = null

  override def onCreate() {
    super.onCreate()
    tts = new TextToSpeech(this, this)
    telephonyManager = getSystemService(Context.TELEPHONY_SERVICE).asInstanceOf[TelephonyManager]
    telephonyManager.listen(new LocalPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
  }

  def onInit(status:Int) {
    // TODO: Should probably check for presence of required language
    tts.setLanguage(java.util.Locale.getDefault)
  }

  override def onDestroy() {
    super.onDestroy()
    tts.shutdown()
  }

  private def say(str:String) {
    tts.speak(str, QUEUE_FLUSH, null)
  }

  import android.os.BatteryManager

  def sayBatteryLevel() {
    // Yoinked and modified from Eyes-free Shell--thanks guys!
    val receiver:BroadcastReceiver = new BroadcastReceiver {
      override def onReceive(context:Context, intent:Intent) {
        context.unregisterReceiver(this)
        val rawLevel = intent.getIntExtra("level", -1)
        val scale = intent.getIntExtra("scale", -1)
        val status = intent.getIntExtra("status", -1)
        var msg = ""
        if(rawLevel >= 0 && scale > 0) {
          val batteryLevel = (rawLevel*100)/scale
          msg = batteryLevel+" %"
        }
        if(status == BatteryManager.BATTERY_STATUS_CHARGING)
          msg += getString(R.string.charging)
        say(msg)
      }
    }
    registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED))
  }

  import java.text.DateFormat
  import java.util.Calendar

  def sayDate() {
    val formatter = DateFormat.getDateInstance()
    val time = Calendar.getInstance.getTime
    say(formatter.format(time))
  }

  def sayTime() {
    val formatter = DateFormat.getTimeInstance()
    val time = Calendar.getInstance.getTime
    say(formatter.format(time))
  }

  def saySignalStrengths() {
    var voiceOperatorName = telephonyManager.getNetworkOperatorName
    var msg = if(voiceOperatorName != "") voiceOperatorName+". " else ""
    signalStrengths.map {s =>
      val strength = s.getGsmSignalStrength
      val bars = strength match {
        case 0 | 99 => 0
        case b if(b >= 16) => 4
        case b if(b >= 8) => 3
        case b if(b >= 4) => 2
        case _ => 1
      }
      msg += bars+" "+getString(if(bars == 1) R.string.bar else R.string.bars)+". "
    }
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE).asInstanceOf[ConnectivityManager]
    val networkInfo = Option(connectivityManager.getActiveNetworkInfo)
    networkInfo.foreach { ni =>
      msg += (ni.getType match {
        case ConnectivityManager.TYPE_MOBILE => telephonyManager.getNetworkType match {
          case TelephonyManager.NETWORK_TYPE_UMTS => getString(R.string.data_3g)
          case TelephonyManager.NETWORK_TYPE_EDGE => getString(R.string.data_edge)
          case _ => getString(R.string.data_mobile)
        } case ConnectivityManager.TYPE_WIFI =>
          val wifiManager = getSystemService(Context.WIFI_SERVICE).asInstanceOf[WifiManager]
          val wifiInfo = wifiManager.getConnectionInfo
          val signalStrength = WifiManager.calculateSignalLevel(wifiInfo.getRssi, 4)
          val barOrBars = if(signalStrength == 1) getString(R.string.bar) else getString(R.string.bars)
          getString(R.string.data_wifi)+" "+wifiInfo.getSSID+" "+signalStrength+" "+barOrBars
      })
      msg += " "
    }
    val bluetoothOn = Option(BluetoothAdapter.getDefaultAdapter).map(_.isEnabled).getOrElse(false)
    if(bluetoothOn)
      msg += getString(R.string.bluetooth)+" "
    val locationProviders = System.getString(getContentResolver, Secure.LOCATION_PROVIDERS_ALLOWED)
    if(locationProviders.contains("gps"))
      msg += getString(R.string.gps)
    say(msg)
  }

}
