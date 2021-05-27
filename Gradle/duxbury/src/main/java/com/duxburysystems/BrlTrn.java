// BrlTrn.java - Java wrapper for Duxbury's brltrn library
// Copyright 2014 Duxbury Systems, Inc.  All Rights Reserved.
// This module may not be redistributed in source form without written permission from Duxbury Systems, Inc.

package com.duxburysystems;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class BrlTrn {
    private Context context;
    private static int maxTableSize = 1000000;
    private long handle;
    private boolean ptb;
    private String prefix;
    //private String btbFile;
    //private String sctFile;
    //private String licenseFile;

    public BrlTrn(Context context) {
        this.context = context;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            destroy(); // Yes, we ignore the result.
        } finally {
            super.finalize();
        }
    }
    // Native code methods in libbrltrn.so
    public native static short version(int [] majorMinor);
    private native short create(byte [] btb, int actualSizeBtb, byte [] sct, int actualSizeSct);
    //public native short setMap(String [] lpMap);
    //public native short enableBitmapVariable();
    public native String translate(String input);
    public native String translateWithPos(String input, short inputPos, short [] outputPos);
    public native String translateWithPositionMappings(String input, int [] outputPos);
    public native short inputPosFromOutputPos(String lpInput, short outputPos);
    private native short addScrub(byte [] sbt, int actualSizeSbt, int stage);
    private native static boolean isChiTabLoaded();
    private native static boolean loadChiTab(byte [] chiTab, int actualSize);
    private native static char chdiu(short input);
    private native static short chudi(char input);
    private native short destroy();
                            
    // Load library
    static {
        System.loadLibrary("brltrn");
    }

    private int readTable(String table, byte[] buffer) {
        try {
            InputStream stream = context.getAssets().open(AssetUtilities.toPath(table));
            if (stream != null) {
                return stream.read(buffer);
            }
        }
        catch (IOException e) {
            // Ignore
        }
        return 0;
    }

    public boolean loadChiTab() {
        byte[] chiTab = new byte[maxTableSize];
        return loadChiTab(chiTab, readTable("chitab.txt", chiTab));
    }

    // Use this preferred form to load table file from applications assets/tables directory
    public short create(String btbFile, String sctFile, boolean ptb) {
    	this.ptb = ptb;
  
        if ( ! isChiTabLoaded() && ! loadChiTab()) {
            return 11; // a-priori, a chitab failure
        }
        byte[] btb = new byte[maxTableSize];
        int btbLength = readTable(btbFile, btb);
        byte[] sct = new byte[maxTableSize];
        int sctLength = readTable(sctFile, sct);
        return create(btb, btbLength, sct, sctLength);
    }

    public short setPrefix(String prefix) {
        this.prefix = prefix;
        return 0;
    }
    
    public short addScrub(String sbtFile, int stage) {
        byte[] sbt = new byte[maxTableSize];
        int sbtLength = readTable(sbtFile, sbt);
        return addScrub(sbt, sbtLength, stage);
    }
}
