package org.nbp

import language.implicitConversions

import android.content.DialogInterface

package object editor {

  implicit def f2DialogInterfaceOnClickListener(f:(DialogInterface, Int) => Unit) = new DialogInterface.OnClickListener {
    def onClick(i:DialogInterface, w:Int) = f(i, w)
  }

  implicit def f2DialogInterfaceOnClickListener(f: () => Unit) = new DialogInterface.OnClickListener {
    def onClick(i:DialogInterface, w:Int) = f()
  }

  implicit def f2Runnable(f: () => Any) = new Runnable {
    def run() {
      f()
    }
  }

}