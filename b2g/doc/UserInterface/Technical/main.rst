Technical Information
=====================

* Weight: 575g [20.3oz]

* Dimensions:

  + Width: 20cm [8in]
  + Depth: 11cm [4.3in]
  + Height: 3cm [1.2in] (including feet and keys)

* Braille Display: Metec, 20 cells, 8 dots per cell

* Headset Jack: 3.5mm OMTP (on left side)

* USB Ports:

  + Standard A (on left side)
  + Micro B (on back)

* Battery: Lithium Polymer, 4 Volt, 5.6 Amp Hours, not user accessible

* Camera: 5MP, built-in flash

* Wi-Fi: IEEE 802.11 b/g, Hotspot, Wi-Fi Direct

* Bluetooth: 2.1+EDR, Power Class 1.5

  + Profiles: OPP, A2DP, AVRCP, HID

* GPS Receiver: NAVSTAR L1C/A Band, 48 channels

* Accelerometer: 4 axis 

* Compass

* Internal Memory: 8.5GB eMMC, 500MB DRAM

.. comment

  * Micro-SD Card Slot: 32GB max, internal, user accessible (beneath cover on bottom)

* SD Card Slot: 32GB max, external (on back)

* Micro-SIM Card Slot: internal, user accessible (beneath cover on bottom)

* Cellular Wireless Module:

  + Quad-band GSM / GPRS / EDGE: 850MHz, 900MHz, 1800MHz, 1900MHz
  + Quad-band UMTS WCDMA FDD: 800MHz, 850MHz, 1900MHz, 2100MHz
  + Dual-band CDMA EVDO Rev A: 800MHz, 1900MHz
  + GPS: 1575.42MHz

The Serial Port
---------------

The serial port is a 10-pin male connector under `The Removable Cover`_
in the corner nearest to the Power switch. In other words, with the bottom
toward you and the side with the Power switch up, the port is in the
upper-right corner. In this orientation, its pins are arranged in two
five-pin columns. From top to bottom:

* The pins in the left column are numbered: 1, 3, 5, 7, 9
* The pins in the right column are numbered: 2, 4, 6, 8, 10

Like this::

  1 o o 2
  3 o o 4
  5 o o 6
  7 o o 8
  9 o o 10

The pins are used as follows:

===  =============
Pin  Function
---  -------------
2    inbound data
3    outbound data
5    ground
===  =============

The port is configured as follows:

============  =====================
Property      Setting
------------  ---------------------
Baud          115200
Data Bits     8
Stop Bits     1
Parity        none
Flow Control  software (X-ON/X-OFF)
============  =====================

